package fi.tamk.anpro;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.DisplayMetrics;
import android.util.Log;

/**
 * Huolehtii ajanotosta ja tekoälyjen ja sijaintien päivittämisestä.
 */
class GameThread extends Thread
{
    /* Säikeen tila */
    public boolean isRunning = false; // Onko säie käynnissä?
    public boolean allLoaded = false; // Onko kaikki tarvittava ladattu?
    
    /* Pelin tila */
    public static final int GAMESTATE_LOADING_RESOURCES = 0;
    public static final int GAMESTATE_STORY             = 1;
    public static final int GAMESTATE_GAME              = 2;
    
    public  byte gameState      = GAMESTATE_LOADING_RESOURCES;
    private long gameStateTimer;
    
    /* Tarvittavat luokat */
    private Wrapper       wrapper;
    private GameMode      gameMode;
    @SuppressWarnings("unused")
	private TouchManager  touchManager;
    private Hud           hud;
    private WeaponManager weaponManager;
    private GLSurfaceView surfaceView;
    private BackgroundManager backgroundManager;
    
    /* Ajastuksen muuttujat */
    private long waveStartTime;
    private long lastMovementUpdate;
    private long lastAiUpdateStateOne;
    private long lastAiUpdateStateTwo;
    private long lastAiUpdateStateThree;
    private long lastCooldownUpdate;
    private long lastGameModeUpdate;
    private long lastCollisionUpdate;
    private long lastArmorUpdate;
    private long lastBoundCheck;
    private long lastGuideArrowUpdate;
    private long lastRadarUpdate;
    private long lastMessageUpdate;
    
    private long currentTime;

    /* Esittelytutoriaalin näyttämisen tarkastus */
    private boolean tutorialHasShown;
    
    /* Muille luokille välitettävät muuttujat (tallennetaan väliaikaisesti, sillä muut
       luokat voidaan luoda vasta kun renderöijä on ladannut kaikki grafiikat) */
    private DisplayMetrics dm;
    private Context        context;
    private GameActivity   gameActivity;
    private GLRenderer     renderer;

    /**
     * Alustaa luokan muuttujat ja luo pelitilan.
     * 
     * @param DisplayMetrics Näytön tiedot
     * @param Context		 Ohjelman konteksti
     * @param GLSurfaceView  OpenGL-pinta
     * @param GameActivity   Pelin aloittava aktiviteetti
     */
    public GameThread(DisplayMetrics _dm, Context _context, GameActivity _gameActivity,
    				  GLRenderer _renderer, GLSurfaceView _surfaceView)
    {
        wrapper       = Wrapper.getInstance();
        
        dm      	  = _dm;
        context 	  = _context;
        gameActivity  = _gameActivity;
        renderer      = _renderer;
        surfaceView   = _surfaceView;
        
        tutorialHasShown = false;
    }

    /* =======================================================
     * Uudet funktiot
     * ======================================================= */
    /**
     * Määrittää säikeen päälle tai pois.
     * 
     * @param boolean Päälle/pois
     */
    public void setRunning(boolean _run)
    {
    	isRunning = _run;
    }
    
    /**
     * Ladataan tarvittavat tiedot ja luodaan oliot.
     */
    public void initialize()
    {
    	// Luodaan manager-luokat
        EffectManager.getInstance();
        MessageManager.getInstance();
        CameraManager.getInstance();

        // Luodaan pelitilan eri osat
        backgroundManager = new BackgroundManager(wrapper);
    	weaponManager     = new WeaponManager();
        hud               = new Hud(context, weaponManager);
        touchManager      = new TouchManager(dm, surfaceView, context, hud, weaponManager);
        gameMode          = new GameMode(gameActivity, this, dm, context, hud, weaponManager);
        
        // Järjestellään Wrapperin listat uudelleen
        wrapper.sortDrawables();
        wrapper.generateAiGroups();
        
        // Merkitään kaikki ladatuiksi
        allLoaded = true;
    }

    /**
     * Suorittää säikeen. Android kutsuu tätä automaattisesti kun GameThread
     * on käynnistetty thread-funktiolla (sisältyy Thread-luokkaan).
     */
    @Override
    public void run()
    {
    	while (isRunning) {
    		
    		if (gameState == GAMESTATE_LOADING_RESOURCES) {
	    		if (renderer.allLoaded) {
	    			initialize();
	    			gameStateTimer = android.os.SystemClock.uptimeMillis();
	    			gameState = GAMESTATE_STORY;
	    		}
	    	}
	    	else if (gameState == GAMESTATE_STORY) {
	    		currentTime = android.os.SystemClock.uptimeMillis();
	    		if (currentTime - gameStateTimer >= 0) {
	    			gameState = GAMESTATE_GAME;
	    			gameStateTimer = currentTime;
	    		}
	    	}
		    
	    	while (gameState == GAMESTATE_GAME) {
		        /* Haetaan päivityksille aloitusajat */
		        waveStartTime		   = android.os.SystemClock.uptimeMillis();
		        lastMovementUpdate     = waveStartTime;
		        lastAiUpdateStateOne   = waveStartTime;
		        lastAiUpdateStateTwo   = waveStartTime;
		        lastAiUpdateStateThree = waveStartTime;
		        lastCooldownUpdate     = waveStartTime;
		        lastGameModeUpdate	   = waveStartTime;
		        lastCollisionUpdate    = waveStartTime;
		        lastArmorUpdate		   = waveStartTime;
		        lastBoundCheck         = waveStartTime;
		        lastGuideArrowUpdate   = waveStartTime;
		        lastRadarUpdate        = waveStartTime;
		        lastMessageUpdate	   = waveStartTime;
		        
		        /* Suoritetaan säiettä kunnes se määritetään pysäytettäväksi */
		        while (isRunning) {
		        	
		            // Haetaan tämänhetkinen aika
		            currentTime = android.os.SystemClock.uptimeMillis();
		            
		            /* Tarkastetaan pelaajan sijainti pelikentällä */
		            if (currentTime - lastBoundCheck >= 1000) {
		            	
		            	lastBoundCheck = currentTime;
		            	
		            	gameMode.checkBounds();
		            }
		            
		            /* Päivitetään objektien sijainnit niiden liikkeen mukaan (käsitellään
		               myös tähtitaustan rajatarkistukset) */
		            if (currentTime - lastMovementUpdate >= 10) {
		                updateMovement(currentTime);
		                updateBackgroundStars();
		            }
		           
		            /* Päivitetään tekoälyt */
		            if (wrapper.player != null) {
		            	updateAi(currentTime);
		            }
		            
		            /* Tarkistetaan törmäykset */
		            checkCollisions(currentTime);
		            
		            /* Päivitetään efektit */
		            updateEffects();
		            
		            /* Päivitetään aseiden cooldownit */
		        	updateWeaponCooldowns(currentTime);
		            
		            /* Palautetaan osa pelaajan suojista */
		            recoverWeaponArmor(currentTime);
		            
		            /* Päivitetään vihollisaallot ja pelitila */
		            updateGameMode(currentTime);
		            
		            /* Päivitetään opastusnuolet */
		            updateGuideArrows(currentTime);
		            
		            /* Päivitetään tutka */
		            updateRadar(currentTime);
		
		            /* Käydään esittelytutoriaalissa */
		            if (!tutorialHasShown) {
		            	gameMode.moveToTutorial(GameMode.TUTORIAL_START);
		            	tutorialHasShown = true;
		            }

		            /* Hidastetaan säiettä pakottamalla se odottamaan 20 ms */
		            try {
		                Thread.sleep(20);
		            } catch (InterruptedException e) {
		                e.printStackTrace();
		            }
		        }
	    	}
    	}
    }

    /**
     * Päivittää objektien liikkeen (paikan vaihtaminen, kääntyminen, nopeuksien
     * muutokset jne.)
     * 
     * @param _currentTime Tämän hetkinen aika
     */
	private void updateMovement(long _currentTime)
	{
        lastMovementUpdate = _currentTime;
        
		wrapper.player.updateMovement(_currentTime);
		
		CameraManager.updateCameraPosition();
	    
	    for (int i = wrapper.enemies.size()-1; i >= 0; --i) {
	        if (wrapper.enemies.get(i).state == Wrapper.FULL_ACTIVITY ||
	            wrapper.enemies.get(i).state == Wrapper.ANIMATION_AND_MOVEMENT) {
	            
	            wrapper.enemies.get(i).updateMovement(_currentTime);
	        }
	    }
	    
	    for (int i = wrapper.projectiles.size()-1; i >= 0; --i) {
	        if (wrapper.projectiles.get(i).state == Wrapper.FULL_ACTIVITY ||
	            wrapper.projectiles.get(i).state == Wrapper.ANIMATION_AND_MOVEMENT) {
	            
	            wrapper.projectiles.get(i).updateMovement(_currentTime);
	        }
	    }
	    
	    for (int i = wrapper.obstacles.size()-1; i >= 0; --i) {
	        if (wrapper.obstacles.get(i).state == Wrapper.FULL_ACTIVITY ||
	            wrapper.obstacles.get(i).state == Wrapper.ANIMATION_AND_MOVEMENT) {
	            
	            wrapper.obstacles.get(i).updateMovement(_currentTime);
	        }
	    }
	}

    /**
     * Päivittää tähtitaustan.
     */
	private void updateBackgroundStars()
	{
	    backgroundManager.updatePositions();
	}

    /**
     * Päivittää vihollisten ja ammusten tekoälyt tasoittain (1-4). Päivittää
     * myös pelaajan "tekoälyn" samaan aikaan neljännen tason kanssa.
     * 
     * @param _currentTime Tämän hetkinen aika
     */
	private void updateAi(long _currentTime)
	{
	    // Päivitetään tila 1
		if (_currentTime - lastAiUpdateStateOne >= 350) {
	        lastAiUpdateStateOne = _currentTime;
	        
	        for (AiObject object : wrapper.aiGroupOne) {
	        	if (object.state == Wrapper.FULL_ACTIVITY && object.ai != null) {
	        		object.ai.handleAi();
	        	}
	        }
		}
		
	    // Päivitetään tila 2
	    if (_currentTime - lastAiUpdateStateTwo >= 180) {
	        lastAiUpdateStateTwo = _currentTime;
	        
	        for (AiObject object : wrapper.aiGroupTwo) {
	        	if (object.state == Wrapper.FULL_ACTIVITY && object.ai != null) {
	        		object.ai.handleAi();
	        	}
	        }
	    }
	
	    // Päivitetään tila 3
	    if (_currentTime - lastAiUpdateStateThree >= 40) {
	        lastAiUpdateStateThree = _currentTime;
	        
	        for (AiObject object : wrapper.aiGroupThree) {
	        	if (object.state == Wrapper.FULL_ACTIVITY && object.ai != null) {
	        		object.ai.handleAi();
	        	}
	        }
	    }
	}

    /**
     * Tarkistaa törmäykset.
     * 
     * @param _currentTime Tämän hetkinen aika
     */
	private void checkCollisions(long _currentTime)
	{
        if (_currentTime - lastCollisionUpdate >= 50) {
        	
			lastCollisionUpdate = _currentTime;
			
	    	for (int i = wrapper.projectiles.size()-1; i >= 0; --i) {
	    		if (wrapper.projectiles.get(i).state == Wrapper.FULL_ACTIVITY) {
	    			wrapper.projectiles.get(i).checkCollision();
	    		}
	    	}
	    	for (int i = wrapper.enemies.size()-1; i >= 0; --i) {
	    		if (wrapper.enemies.get(i).state == Wrapper.FULL_ACTIVITY) {
	    			//wrapper.enemies.get(i).checkCollision(); // TODO: ?
	    		}
	    	}
	    	for (int i = wrapper.obstacles.size()-1; i >= 0; --i) {
	    		if (wrapper.obstacles.get(i).state == Wrapper.FULL_ACTIVITY) {
	    			wrapper.obstacles.get(i).checkCollision();
	    		}
	    	}
	    	
	    	if (wrapper.player.state == Wrapper.FULL_ACTIVITY) {
	    		wrapper.player.checkCollision();
	    	}
	    	
        }
	}

    /**
     * Päivittää efektien sijainnit, sillä joidenkin efektien on seurattava
     * niille annettuja kohteita.
     */
	private void updateEffects()
	{
		EffectManager.updateEffectPositions();
	}

    /**
     * Päivittää aseiden cooldownit sekä WeaponManagerista että HUDista.
     * 
     * @param _currentTime Tämän hetkinen aika
     */
	private void updateWeaponCooldowns(long _currentTime)
	{
        if (_currentTime - lastCooldownUpdate >= 100) {
        	lastCooldownUpdate = _currentTime;
        	
            gameMode.weaponManager.updateCooldowns();
            hud.updateCooldowns();
        }
	}

    /**
     * Palauttaa pelaajalla osan aluksen suojista.
     * 
     * @param _currentTime Tämän hetkinen aika
     */
	private void recoverWeaponArmor(long _currentTime)
	{
        if (_currentTime - lastArmorUpdate >= 150) {
        	lastArmorUpdate = _currentTime;
        	
        	if (_currentTime - wrapper.player.outOfBattleTime >= 4000) {
	        	++wrapper.player.currentArmor;
	        	hud.armorBar.updateValue(wrapper.player.currentArmor);
	        	
	        	if (wrapper.player.currentArmor > wrapper.player.armor) {
	        		wrapper.player.currentArmor = wrapper.player.armor;
	        	}
        	}
        }
	}

    /**
     * Päivittää pelitilan, eli esimerkiksi asteroidien peilauksen, vihollisten
     * uudelleensyntymisen yms.
     * 
     * @param _currentTime Tämän hetkinen aika
     */
	private void updateGameMode(long _currentTime)
	{
        if (_currentTime - lastGameModeUpdate >= 1000) {
        	lastGameModeUpdate = _currentTime;
        	
            if (GameMode.enemiesLeft == 0) {
                gameMode.startWave();
            }
            
            gameMode.mirrorAsteroidPosition();
            
            if (wrapper.weaponCollectable.state == Wrapper.INACTIVE) {
            	wrapper.weaponCollectable.setActive();
            }
        }
	}

    /**
     * Päivittää opasnuolien kohteet ja niiden osoittamat suunnat.
     * 
     * @param _currentTime Tämän hetkinen aika
     */
	private void updateGuideArrows(long _currentTime)
	{
        if (_currentTime - lastGuideArrowUpdate >= 100) {
        	lastGuideArrowUpdate = _currentTime;
        	
        	hud.guideArrowToCollectable.updateArrow();
        	hud.guideArrowToWeapon.updateArrow();
        }
	}
	/**
	 * Päivittää tutkan osoittaman vihollisen ja suunnan
	 * 
	 * @param _currentTime
	 */
	private void updateRadar(long _currentTime)
	{
		if(_currentTime - lastRadarUpdate >= 100) {
			lastRadarUpdate = _currentTime;
			
			hud.radar_top.updateRadar();
			hud.radar_left.updateRadar();
			hud.radar_right.updateRadar();
			hud.radar_down.updateRadar();
		}
	}
}