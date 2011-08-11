package fi.tamk.anpro;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.DisplayMetrics;
import android.util.Log;

/**
 * Huolehtii ajanotosta ja teko�lyjen ja sijaintien p�ivitt�misest�.
 */
class GameThread extends Thread
{
    /* S�ikeen tila */
    public boolean isRunning = false; // Onko s�ie k�ynniss�?
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

    /* Esittelytutoriaalin n�ytt�misen tarkastus */
    private boolean tutorialHasShown;
    
    /* Muille luokille v�litett�v�t muuttujat (tallennetaan v�liaikaisesti, sill� muut
       luokat voidaan luoda vasta kun render�ij� on ladannut kaikki grafiikat) */
    private DisplayMetrics dm;
    private Context        context;
    private GameActivity   gameActivity;
    private GLRenderer     renderer;

    /**
     * Alustaa luokan muuttujat ja luo pelitilan.
     * 
     * @param DisplayMetrics N�yt�n tiedot
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
     * M��ritt�� s�ikeen p��lle tai pois.
     * 
     * @param boolean P��lle/pois
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
        
        // J�rjestell��n Wrapperin listat uudelleen
        wrapper.sortDrawables();
        wrapper.generateAiGroups();
        
        // Merkit��n kaikki ladatuiksi
        allLoaded = true;
    }

    /**
     * Suoritt�� s�ikeen. Android kutsuu t�t� automaattisesti kun GameThread
     * on k�ynnistetty thread-funktiolla (sis�ltyy Thread-luokkaan).
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
		        /* Haetaan p�ivityksille aloitusajat */
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
		        
		        /* Suoritetaan s�iett� kunnes se m��ritet��n pys�ytett�v�ksi */
		        while (isRunning) {
		        	
		            // Haetaan t�m�nhetkinen aika
		            currentTime = android.os.SystemClock.uptimeMillis();
		            
		            /* Tarkastetaan pelaajan sijainti pelikent�ll� */
		            if (currentTime - lastBoundCheck >= 1000) {
		            	
		            	lastBoundCheck = currentTime;
		            	
		            	gameMode.checkBounds();
		            }
		            
		            /* P�ivitet��n objektien sijainnit niiden liikkeen mukaan (k�sitell��n
		               my�s t�htitaustan rajatarkistukset) */
		            if (currentTime - lastMovementUpdate >= 10) {
		                updateMovement(currentTime);
		                updateBackgroundStars();
		            }
		           
		            /* P�ivitet��n teko�lyt */
		            if (wrapper.player != null) {
		            	updateAi(currentTime);
		            }
		            
		            /* Tarkistetaan t�rm�ykset */
		            checkCollisions(currentTime);
		            
		            /* P�ivitet��n efektit */
		            updateEffects();
		            
		            /* P�ivitet��n aseiden cooldownit */
		        	updateWeaponCooldowns(currentTime);
		            
		            /* Palautetaan osa pelaajan suojista */
		            recoverWeaponArmor(currentTime);
		            
		            /* P�ivitet��n vihollisaallot ja pelitila */
		            updateGameMode(currentTime);
		            
		            /* P�ivitet��n opastusnuolet */
		            updateGuideArrows(currentTime);
		            
		            /* P�ivitet��n tutka */
		            updateRadar(currentTime);
		
		            /* K�yd��n esittelytutoriaalissa */
		            if (!tutorialHasShown) {
		            	gameMode.moveToTutorial(GameMode.TUTORIAL_START);
		            	tutorialHasShown = true;
		            }

		            /* Hidastetaan s�iett� pakottamalla se odottamaan 20 ms */
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
     * P�ivitt�� objektien liikkeen (paikan vaihtaminen, k��ntyminen, nopeuksien
     * muutokset jne.)
     * 
     * @param _currentTime T�m�n hetkinen aika
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
     * P�ivitt�� t�htitaustan.
     */
	private void updateBackgroundStars()
	{
	    backgroundManager.updatePositions();
	}

    /**
     * P�ivitt�� vihollisten ja ammusten teko�lyt tasoittain (1-4). P�ivitt��
     * my�s pelaajan "teko�lyn" samaan aikaan nelj�nnen tason kanssa.
     * 
     * @param _currentTime T�m�n hetkinen aika
     */
	private void updateAi(long _currentTime)
	{
	    // P�ivitet��n tila 1
		if (_currentTime - lastAiUpdateStateOne >= 350) {
	        lastAiUpdateStateOne = _currentTime;
	        
	        for (AiObject object : wrapper.aiGroupOne) {
	        	if (object.state == Wrapper.FULL_ACTIVITY && object.ai != null) {
	        		object.ai.handleAi();
	        	}
	        }
		}
		
	    // P�ivitet��n tila 2
	    if (_currentTime - lastAiUpdateStateTwo >= 180) {
	        lastAiUpdateStateTwo = _currentTime;
	        
	        for (AiObject object : wrapper.aiGroupTwo) {
	        	if (object.state == Wrapper.FULL_ACTIVITY && object.ai != null) {
	        		object.ai.handleAi();
	        	}
	        }
	    }
	
	    // P�ivitet��n tila 3
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
     * Tarkistaa t�rm�ykset.
     * 
     * @param _currentTime T�m�n hetkinen aika
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
     * P�ivitt�� efektien sijainnit, sill� joidenkin efektien on seurattava
     * niille annettuja kohteita.
     */
	private void updateEffects()
	{
		EffectManager.updateEffectPositions();
	}

    /**
     * P�ivitt�� aseiden cooldownit sek� WeaponManagerista ett� HUDista.
     * 
     * @param _currentTime T�m�n hetkinen aika
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
     * @param _currentTime T�m�n hetkinen aika
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
     * P�ivitt�� pelitilan, eli esimerkiksi asteroidien peilauksen, vihollisten
     * uudelleensyntymisen yms.
     * 
     * @param _currentTime T�m�n hetkinen aika
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
     * P�ivitt�� opasnuolien kohteet ja niiden osoittamat suunnat.
     * 
     * @param _currentTime T�m�n hetkinen aika
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
	 * P�ivitt�� tutkan osoittaman vihollisen ja suunnan
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