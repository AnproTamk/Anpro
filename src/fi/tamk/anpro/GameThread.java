package fi.tamk.anpro;

import android.content.Context;
import android.util.DisplayMetrics;

/**
 * Huolehtii ajanotosta ja teko�lyjen ja sijaintien p�ivitt�misest�.
 */
class GameThread extends Thread
{
    /* S�ikeen tila */
    private boolean running   = false; // Onko s�ie k�ynniss�?
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
    
    /* Teko�lyn nopeutus vihollisaaltojen aikana (varmistaa teko�ly� nopeuttamalla,
       ett� viholliset varmasti lopulta saavuttavat pelaajan) */
    private float updateSpeedUp = 1;
    
    /* Muille luokille v�litett�v�t muuttujat (tallennetaan v�liaikaisesti, sill� muut
       luokat voidaan luoda vasta kun render�ij� on ladannut kaikki grafiikat) */
    private DisplayMetrics dm;
    private Context        context;
    private GameActivity   gameActivity;

    /**
     * Alustaa luokan muuttujat ja luo pelitilan.
     * 
     * @param DisplayMetrics N�yt�n tiedot
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
     * M��ritt�� s�ikeen p��lle tai pois.
     * 
     * @param boolean P��lle/pois
     */
    public void setRunning(boolean _run)
    {
        running = _run;
    }

    /**
     * Suoritt�� s�ikeen. Android kutsuu t�t� automaattisesti kun GameThread
     * on k�ynnistetty thread-funktiolla (sis�ltyy Thread-luokkaan).
     */
    @Override
    public void run()
    {
        // Luodaan SurvivalMode
        gameMode = new GameMode(gameActivity, dm, context, weaponManager);

        // Luodaan EffectManager
        EffectManager.getInstance();
                
        // Merkit��n kaikki ladatuiksi
        allLoaded = true;
        
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
        
        /* Suoritetaan s�iett� kunnes se m��ritet��n pys�ytett�v�ksi */
        while (running) {
            
            // Haetaan t�m�nhetkinen aika
            long currentTime = android.os.SystemClock.uptimeMillis();
            
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
            
            /* P�ivitet��n teko�lyjen p�ivitysv�lit */
            if (currentTime - waveStartTime >= 3000) {
                updateSpeedUp = 2;
                // TODO: Onko t�m� en�� tarpeen, koska kentt� on nyt isompi?
            }
           
            /* P�ivitet��n teko�lyt */
            if (wrapper.player != null) {
            	
            	updateAi(currentTime);
            }
            
            /* Tarkistetaan t�rm�ykset */
            if (currentTime - lastCollisionUpdate >= 50) {
            	
            	checkCollisions(currentTime);
            }
            
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

            /* Hidastetaan s�iett� pakottamalla se odottamaan 20 ms */
            try {
                Thread.sleep(20);
            } catch (InterruptedException e) {
                e.printStackTrace();
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
	
	    // P�ivitet��n tila 2
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
	
	    // P�ivitet��n tila 3
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
	
	    // P�ivitet��n tila 4
	    if (_currentTime - lastAiUpdateStateFour >= (40 / updateSpeedUp)) {
	        lastAiUpdateStateFour = _currentTime;
	
	    	// P�ivitet��n pelaajan teko�ly (aina tila 4)
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
     * Tarkistaa t�rm�ykset.
     * 
     * @param _currentTime T�m�n hetkinen aika
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
     * P�ivitt�� efektien sijainnit, sill� joidenkin efektien on seurattava
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
        if (_currentTime - lastArmorUpdate >= 10000) {
        	lastArmorUpdate = _currentTime;
        	
        	if (wrapper.player.currentArmor <= 0) {
        		wrapper.player.currentArmor += 25;
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
                waveStartTime = android.os.SystemClock.uptimeMillis();
                updateSpeedUp = 1;
                
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
}