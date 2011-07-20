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
    public static final int GAMESTATE_HIDE_MAINMENU     = 0;
    public static final int GAMESTATE_LOADING_RESOURCES = 1;
    public static final int GAMESTATE_STORY             = 2;
    public static final int GAMESTATE_TUTORIALS         = 3;
    public static final int GAMESTATE_STARTUP           = 4;
    public static final int GAMESTATE_GAME              = 5;
    
    public  byte gameState      = 0;
    private long gameStateTimer;
    
    /* Tarvittavat luokat */
    private Wrapper       wrapper;
    private GameMode      gameMode;
    @SuppressWarnings("unused")
	private TouchManager  touchManager;
    private Hud           hud;
    private WeaponManager weaponManager;
    private GLSurfaceView surfaceView;
    
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
    private long lastRadarUpdate;
    
    private long currentTime;
    
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
    				  WeaponManager _weaponManager, GLRenderer _renderer, GLSurfaceView _surfaceView)
    {
        wrapper       = Wrapper.getInstance();
        
        dm      	  = _dm;
        context 	  = _context;
        gameActivity  = _gameActivity;
        weaponManager = _weaponManager;
        renderer      = _renderer;
        surfaceView   = _surfaceView;
    }

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
                
        // Luodaan Hud ja TouchManager
        hud          = new Hud(context, weaponManager);
        touchManager = new TouchManager(dm, surfaceView, context, hud, weaponManager);
        
        // Luodaan SurvivalMode
        gameMode = new GameMode(gameActivity, dm, context, hud, weaponManager);
                
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
    		
    		if (gameState == GAMESTATE_HIDE_MAINMENU) {
    			gameState = GAMESTATE_LOADING_RESOURCES;
    		}
    		else if (gameState == GAMESTATE_LOADING_RESOURCES) {
	    		if (renderer.allLoaded) {
	    			gameState = GAMESTATE_STORY;
	    			gameStateTimer = android.os.SystemClock.uptimeMillis();
	    			initialize();
	    		}
	    	}
	    	else if (gameState == GAMESTATE_STORY) {
	    		currentTime = android.os.SystemClock.uptimeMillis();
	    		if (currentTime - gameStateTimer >= 0) {
	    			gameState = GAMESTATE_TUTORIALS;
	    			gameStateTimer = currentTime;
	    		}
	    	}
	    	else if (gameState == GAMESTATE_TUTORIALS) {
	    		currentTime = android.os.SystemClock.uptimeMillis();
	    		if (currentTime - gameStateTimer >= 0) {
	    			gameState = GAMESTATE_STARTUP;
	    			gameStateTimer = currentTime;
	    		}
	    	}
	    	else if (gameState == GAMESTATE_STARTUP) {
	    		currentTime = android.os.SystemClock.uptimeMillis();
	    		if (currentTime - gameStateTimer >= 0) {
	    			gameState = GAMESTATE_GAME;
	    		}
	    	}
		    
	    	while (gameState == GAMESTATE_GAME) {
	    		
		        /* Haetaan p�ivityksille aloitusajat */
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
		        lastRadarUpdate        = waveStartTime;
		        
		        /* Suoritetaan s�iett� kunnes se m��ritet��n pys�ytett�v�ksi */
		        while (isRunning) {
		        	
		            // Haetaan t�m�nhetkinen aika
		            currentTime = android.os.SystemClock.uptimeMillis();
		            
		            /* Tarkastetaan pelaajan sijainti pelikent�ll� */
		            if (currentTime - lastBoundCheck >= 1500) {
		            	
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
		            
		            /* P�ivitet��n efektien sijainnit */
		            updateEffectPositions();
		            
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
     * P�ivitt�� t�htitaustan.
     */
	private void updateBackgroundStars()
	{
	    for (int i = wrapper.backgroundStars.size()-1; i >= 0; --i) {
	    	wrapper.backgroundStars.get(i).checkPosition();
	    }
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
		if (_currentTime - lastAiUpdateStateOne >= 300) {
	        lastAiUpdateStateOne = _currentTime;
	        
	        for (int i : wrapper.priorityOneEnemies) {
	            if (wrapper.enemyStates.get(i) == Wrapper.FULL_ACTIVITY) {
	                wrapper.enemies.get(i).ai.handleAi();
	            }
	        }
	        
	        for (int i : wrapper.priorityOneAllies) {
	            if (wrapper.allyStates.get(i) == Wrapper.FULL_ACTIVITY) {
	                wrapper.allies.get(i).ai.handleAi();
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
		
	    // P�ivitet��n tila 2
	    if (_currentTime - lastAiUpdateStateTwo >= 150) {
	        lastAiUpdateStateTwo = _currentTime;
	        
	        for (int i : wrapper.priorityTwoEnemies) {
	            if (wrapper.enemyStates.get(i) == Wrapper.FULL_ACTIVITY) {
	                wrapper.enemies.get(i).ai.handleAi();
	            }
	        }
	        
	        for (int i : wrapper.priorityTwoAllies) {
	            if (wrapper.allyStates.get(i) == Wrapper.FULL_ACTIVITY) {
	                wrapper.allies.get(i).ai.handleAi();
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
	
	    // P�ivitet��n tila 3
	    if (_currentTime - lastAiUpdateStateThree >= 75) {
	        lastAiUpdateStateThree = _currentTime;
	        
	        for (int i : wrapper.priorityThreeEnemies) {
	            if (wrapper.enemyStates.get(i) == Wrapper.FULL_ACTIVITY) {
	                wrapper.enemies.get(i).ai.handleAi();
	            }
	        }
	        
	        for (int i : wrapper.priorityThreeAllies) {
	            if (wrapper.allyStates.get(i) == Wrapper.FULL_ACTIVITY) {
	                wrapper.allies.get(i).ai.handleAi();
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
	
	    // P�ivitet��n tila 4
	    if (_currentTime - lastAiUpdateStateFour >= 40) {
	        lastAiUpdateStateFour = _currentTime;
	
	    	// P�ivitet��n pelaajan teko�ly (aina tila 4)
	    	wrapper.player.ai.handleAi();

	        for (int i : wrapper.priorityFourEnemies) {
	            if (wrapper.enemyStates.get(i) == Wrapper.FULL_ACTIVITY) {
	                wrapper.enemies.get(i).ai.handleAi();
	            }
	        }

	        for (int i : wrapper.priorityFourAllies) {
	            if (wrapper.allyStates.get(i) == Wrapper.FULL_ACTIVITY) {
	                wrapper.allies.get(i).ai.handleAi();
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
     * Tarkistaa t�rm�ykset.
     * 
     * @param _currentTime T�m�n hetkinen aika
     */
	private void checkCollisions(long _currentTime)
	{
        if (_currentTime - lastCollisionUpdate >= 50) {
        	
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
	    	
	    	if (wrapper.playerState == Wrapper.FULL_ACTIVITY) {
	    		wrapper.player.checkCollision();
	    	}
	    	
        }
	}

    /**
     * P�ivitt�� efektien sijainnit, sill� joidenkin efektien on seurattava
     * niille annettuja kohteita.
     */
	private void updateEffectPositions()
	{
    	for (int i = wrapper.backEffects.size()-1; i >= 0; --i) {
    		if (wrapper.backEffectStates.get(i) == Wrapper.FULL_ACTIVITY) {
    			wrapper.backEffects.get(i).updatePosition();
    		}
    	}
    	
    	for (int i = wrapper.frontEffects.size()-1; i >= 0; --i) {
    		if (wrapper.frontEffectStates.get(i) == Wrapper.FULL_ACTIVITY) {
    			wrapper.frontEffects.get(i).updatePosition();
    		}
    	}
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
            if (GameMode.enemiesLeft == 0) {
                gameMode.startWave();
            }
            
            gameMode.mirrorAsteroidPosition();
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
        	hud.guideArrowToMothership.updateArrow();
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