package fi.tamk.anpro;

import android.content.Context;
import android.util.DisplayMetrics;

/**
 * Huolehtii ajanotosta ja tekoälyjen ja sijaintien päivittämisestä.
 */
class GameThread extends Thread
{
    /* Säikeen tila */
    private boolean running   = false; // Onko säie käynnissä?
    public  boolean allLoaded = false; // Onko kaikki tarvittava ladattu?
    
    /* Tarvittavat luokat */
    private Wrapper       wrapper;
    private GameMode      gameMode;
    @SuppressWarnings("unused")
	private TouchManager  touchManager;
    public  Hud           hud;
    private WeaponManager weaponManager;
    private CameraManager cameraManager;
    
    /* Ajastuksen muuttujat */
    private long waveStartTime;
    private long lastMovementUpdate;
    private long lastAiUpdateStateOne;
    private long lastAiUpdateStateTwo;
    private long lastAiUpdateStateThree;
    private long lastAiUpdateStateFour;
    private long lastCooldownUpdate;
    private long lastGameModeUpdate;
    private long lastCollisionUpdate;
    private long lastArmorUpdate;
    private long lastBoundCheck;
    private long lastGuideArrowUpdate;
    
    /* Tekoälyn nopeutus vihollisaaltojen aikana (varmistaa tekoälyä nopeuttamalla,
       että viholliset varmasti lopulta saavuttavat pelaajan) */
    private float updateSpeedUp = 1;
    
    /* Muille luokille välitettävät muuttujat (tallennetaan väliaikaisesti, sillä muut
       luokat voidaan luoda vasta kun renderöijä on ladannut kaikki grafiikat) */
    private DisplayMetrics dm;
    private Context        context;
    private GameActivity   gameActivity;

    /**
     * Alustaa luokan muuttujat ja luo pelitilan.
     * 
     * @param DisplayMetrics Näytön tiedot
     * @param Context		 Ohjelman konteksti
     * @param GLSurfaceView  OpenGL-pinta
     * @param GameActivity   Pelin aloittava aktiviteetti
     */
    public GameThread(DisplayMetrics _dm, Context _context, GameActivity _gameActivity,
    				  Hud _hud, TouchManager _touchManager, WeaponManager _weaponManager)
    {
        wrapper       = Wrapper.getInstance();
        cameraManager = CameraManager.getInstance();
        
        dm      	  = _dm;
        context 	  = _context;
        gameActivity  = _gameActivity;
        hud           = _hud;
        touchManager  = _touchManager;
        weaponManager = _weaponManager;
    }

    /**
     * Määrittää säikeen päälle tai pois.
     * 
     * @param boolean Päälle/pois
     */
    public void setRunning(boolean _run)
    {
        running = _run;
    }

    /**
     * Suorittää säikeen. Android kutsuu tätä automaattisesti kun GameThread
     * on käynnistetty thread-funktiolla (sisältyy Thread-luokkaan).
     */
    @Override
    public void run()
    {
        // Luodaan SurvivalMode
        gameMode = new GameMode(gameActivity, dm, context, weaponManager);

        // Luodaan EffectManager
        EffectManager.getInstance();
                
        // Merkitään kaikki ladatuiksi
        allLoaded = true;
        
        /* Haetaan päivityksille aloitusajat */
        waveStartTime		   = android.os.SystemClock.uptimeMillis();
        lastMovementUpdate     = waveStartTime;
        lastAiUpdateStateOne   = waveStartTime;
        lastAiUpdateStateTwo   = waveStartTime;
        lastAiUpdateStateThree = waveStartTime;
        lastAiUpdateStateFour  = waveStartTime;
        lastCooldownUpdate     = waveStartTime;
        lastGameModeUpdate	   = waveStartTime;
        lastCollisionUpdate    = waveStartTime;
        lastArmorUpdate		   = waveStartTime;
        lastBoundCheck         = waveStartTime;
        lastGuideArrowUpdate   = waveStartTime;
        
        /* Suoritetaan säiettä kunnes se määritetään pysäytettäväksi */
        while (running) {
            
            // Haetaan tämänhetkinen aika
            long currentTime = android.os.SystemClock.uptimeMillis();
            
            /* Tarkastetaan pelaajan sijainti pelikentällä */
            if (currentTime - lastBoundCheck >= 1500) {
            	
            	lastBoundCheck = currentTime;
            	
            	gameMode.checkBounds();            	
            }
            
            /* Päivitetään objektien sijainnit niiden liikkeen mukaan (käsitellään
               myös tähtitaustan rajatarkistukset) */
            if (currentTime - lastMovementUpdate >= 10) {
                
                updateMovement(currentTime);
                updateBackgroundStars();
            }
            
            /* Päivitetään tekoälyjen päivitysvälit */
            if (currentTime - waveStartTime >= 3000) {
                updateSpeedUp = 2;
                // TODO: Onko tämä enää tarpeen, koska kenttä on nyt isompi?
            }
           
            /* Päivitetään tekoälyt */
            if (wrapper.player != null) {
            	
            	updateAi(currentTime);
            }
            
            /* Tarkistetaan törmäykset */
            if (currentTime - lastCollisionUpdate >= 50) {
            	
            	checkCollisions(currentTime);
            }
            
            /* Päivitetään efektien sijainnit */
            updateEffectPositions();
            
            /* Päivitetään aseiden cooldownit */
        	updateWeaponCooldowns(currentTime);
            
            /* Palautetaan osa pelaajan suojista */
            recoverWeaponArmor(currentTime);
            
            /* Päivitetään vihollisaallot ja pelitila */
            updateGameMode(currentTime);
            
            /* Päivitetään opastusnuolet */
            updateGuideArrows(currentTime);

            /* Hidastetaan säiettä pakottamalla se odottamaan 20 ms */
            try {
                Thread.sleep(20);
            } catch (InterruptedException e) {
                e.printStackTrace();
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
		cameraManager.updateCameraPosition();
	    
	    for (int i = wrapper.enemies.size()-1; i >= 0; --i) {
	        if (wrapper.enemyStates.get(i) == Wrapper.FULL_ACTIVITY ||
	            wrapper.enemyStates.get(i) == Wrapper.ANIMATION_AND_MOVEMENT) {
	            
	            wrapper.enemies.get(i).updateMovement(_currentTime);
	        }
	    }
	    
	    for (int i = wrapper.projectiles.size()-1; i >= 0; --i) {
	        if (wrapper.projectileStates.get(i) == Wrapper.FULL_ACTIVITY ||
	            wrapper.projectileStates.get(i) == Wrapper.ANIMATION_AND_MOVEMENT) {
	            
	            wrapper.projectiles.get(i).updateMovement(_currentTime);
	        }
	    }
	    
	    for (int i = wrapper.obstacles.size()-1; i >= 0; --i) {
	        if (wrapper.obstacleStates.get(i) == Wrapper.FULL_ACTIVITY ||
	            wrapper.obstacleStates.get(i) == Wrapper.ANIMATION_AND_MOVEMENT) {
	            
	            wrapper.obstacles.get(i).updateMovement(_currentTime);
	        }
	    }
	}

    /**
     * Päivittää tähtitaustan.
     */
	private void updateBackgroundStars()
	{
	    for (int i = wrapper.backgroundStars.size()-1; i >= 0; --i) {
	    	wrapper.backgroundStars.get(i).checkPosition();
	    }
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
		if (_currentTime - lastAiUpdateStateOne >= (300 / updateSpeedUp)) {
	        lastAiUpdateStateOne = _currentTime;
	        
	        for (int i : wrapper.priorityOneEnemies) {
	            if (wrapper.enemyStates.get(i) == Wrapper.FULL_ACTIVITY) {
	                wrapper.enemies.get(i).ai.handleAi();
	            }
	        }
	        
	        for (int i : wrapper.priorityOneProjectiles) {
	            if (wrapper.projectileStates.get(i) == Wrapper.FULL_ACTIVITY) {
	            	if (wrapper.projectiles.get(i).ai != null) {
	                	if (wrapper.projectiles.get(i).ai.active) {
	                		wrapper.projectiles.get(i).ai.handleAi();
	                	}
	            	}
	            }
	        }
		}
	
	    // Päivitetään tila 2
	    if (_currentTime - lastAiUpdateStateTwo >= (150 / updateSpeedUp)) {
	        lastAiUpdateStateTwo = _currentTime;
	        
	        for (int i : wrapper.priorityTwoEnemies) {
	            if (wrapper.enemyStates.get(i) == Wrapper.FULL_ACTIVITY) {
	                wrapper.enemies.get(i).ai.handleAi();
	            }
	        }
	        
	        for (int i : wrapper.priorityTwoProjectiles) {
	            if (wrapper.projectileStates.get(i) == Wrapper.FULL_ACTIVITY) {
	            	if (wrapper.projectiles.get(i).ai.active) {
	            		wrapper.projectiles.get(i).ai.handleAi();
	            	}
	            }
	        }
	    }
	
	    // Päivitetään tila 3
	    if (_currentTime - lastAiUpdateStateThree >= (75 / updateSpeedUp)) {
	        lastAiUpdateStateThree = _currentTime;
	        
	        for (int i : wrapper.priorityThreeEnemies) {
	            if (wrapper.enemyStates.get(i) == Wrapper.FULL_ACTIVITY) {
	                wrapper.enemies.get(i).ai.handleAi();
	            }
	        }
	        
	        for (int i : wrapper.priorityThreeProjectiles) {
	            if (wrapper.projectileStates.get(i) == Wrapper.FULL_ACTIVITY) {
	            	if (wrapper.projectiles.get(i).ai.active) {
	            		wrapper.projectiles.get(i).ai.handleAi();
	            	}
	            }
	        }
	    }
	
	    // Päivitetään tila 4
	    if (_currentTime - lastAiUpdateStateFour >= (40 / updateSpeedUp)) {
	        lastAiUpdateStateFour = _currentTime;
	
	    	// Päivitetään pelaajan tekoäly (aina tila 4)
	    	wrapper.player.ai.handleAi();
	        
	        for (int i : wrapper.priorityFourEnemies) {
	            if (wrapper.enemyStates.get(i) == Wrapper.FULL_ACTIVITY) {
	                wrapper.enemies.get(i).ai.handleAi();
	            }
	        }
	        
	        for (int i : wrapper.priorityFourProjectiles) {
	            if (wrapper.projectileStates.get(i) == Wrapper.FULL_ACTIVITY) {
	            	if (wrapper.projectiles.get(i).ai.active) {
	            		wrapper.projectiles.get(i).ai.handleAi();
	            	}
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
		lastCollisionUpdate = _currentTime;
    	
    	for (int i = wrapper.projectiles.size()-1; i >= 0; --i) {
    		if (wrapper.projectileStates.get(i) == Wrapper.FULL_ACTIVITY) {
    			wrapper.projectiles.get(i).checkCollision();
    		}
    	}
    	for (int i = wrapper.enemies.size()-1; i >= 0; --i) {
    		if (wrapper.enemyStates.get(i) == Wrapper.FULL_ACTIVITY) {
    			//wrapper.enemies.get(i).checkCollision(); // TODO: ?
    		}
    	}
    	for (int i = wrapper.obstacles.size()-1; i >= 0; --i) {
    		if (wrapper.obstacleStates.get(i) == Wrapper.FULL_ACTIVITY) {
    			wrapper.obstacles.get(i).checkCollision();
    		}
    	}
	}

    /**
     * Päivittää efektien sijainnit, sillä joidenkin efektien on seurattava
     * niille annettuja kohteita.
     */
	private void updateEffectPositions()
	{
    	for (int i = wrapper.effects.size()-1; i >= 0; --i) {
    		if (wrapper.effectStates.get(i) == Wrapper.FULL_ACTIVITY) {
    			wrapper.effects.get(i).updatePosition();
    		}
    	}
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
        if (_currentTime - lastArmorUpdate >= 10000) {
        	lastArmorUpdate = _currentTime;
        	
        	if (wrapper.player.currentArmor <= 0) {
        		wrapper.player.currentArmor += 25;
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
            if (GameMode.enemiesLeft == 0) {
                waveStartTime = android.os.SystemClock.uptimeMillis();
                updateSpeedUp = 1;
                
                gameMode.startWave();
            }
            
            gameMode.mirrorAsteroidPosition();
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
        	hud.guideArrowToMothership.updateArrow();
        }
	}
}