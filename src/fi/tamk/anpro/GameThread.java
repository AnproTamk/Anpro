package fi.tamk.anpro;

import android.content.Context;
import android.opengl.GLSurfaceView;
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
    private Wrapper      wrapper;
    private AbstractMode gameMode;
    private TouchManager touchManager;
    public  Hud          hud;
    
    /* Ajastuksen muuttujat */
    private long waveStartTime          = 0;
    private long lastMovementUpdate     = 0;
    private long lastAiUpdateStateOne   = 0;
    private long lastAiUpdateStateTwo   = 0;
    private long lastAiUpdateStateThree = 0;
    private long lastAiUpdateStateFour  = 0;
    private long lastCooldownUpdate     = 0;
    private long lastGameModeUpdate		= 0;
    private long lastCollisionUpdate    = 0;
    
    /* Teko�lyn nopeutus vihollisaaltojen aikana */
    private float updateSpeedUp = 1;
    
    /* Muille luokille v�litett�v�t muuttujat (tallennetaan v�liaikaisesti, sill� muut
       luokat voidaan luoda vasta kun render�ij� on ladannut kaikki grafiikat) */
    private DisplayMetrics dm;
    private Context        context;
    private GLSurfaceView  surface;
    private GameActivity   gameActivity;

    /**
     * Alustaa luokan muuttujat ja luo pelitilan.
     * 
     * @param DisplayMetrics N�yt�n tiedot
     * @param Context		 Ohjelman konteksti
     * @param GLSurfaceView  OpenGL-pinta
     * @param GameActivity   Pelin aloittava aktiviteetti
     */
    public GameThread(DisplayMetrics _dm, Context _context, GLSurfaceView _surface, GameActivity _gameActivity)
    {
        wrapper = Wrapper.getInstance();
        
        dm      	 = _dm;
        context 	 = _context;
        surface 	 = _surface;
        gameActivity = _gameActivity;
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
     * Suoritt�� s�ikeen.
     */
    @Override
    public void run()
    {
    	/* Ladataan pelitila */
    	// Luodaan Hud
        hud = new Hud(context);
    	
        // Luodaan SurvivalMode
        gameMode = new SurvivalMode(gameActivity, dm, context);
        
        // Luodaan TouchManager
        touchManager = new TouchManager(dm, surface, context, hud);
        
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
        
        /* Suoritetaan s�iett� kunnes se m��ritet��n pys�ytett�v�ksi */
        while (running) {
            
            // Haetaan t�m�nhetkinen aika
            long currentTime = android.os.SystemClock.uptimeMillis();
            
            /* P�ivitet��n sijainnit ja liikkuminen */
            if (currentTime - lastMovementUpdate >= 10) {
                
                lastMovementUpdate = currentTime;
                
                for (int i = wrapper.enemies.size()-1; i >= 0; --i) {
                    if (wrapper.enemyStates.get(i) == Wrapper.FULL_ACTIVITY ||
                        wrapper.enemyStates.get(i) == Wrapper.ANIMATION_AND_MOVEMENT) {
                        
                        wrapper.enemies.get(i).updateMovement(currentTime);
                    }
                }
                
                for (int i = wrapper.projectiles.size()-1; i >= 0; --i) {
                    if (wrapper.projectileStates.get(i) == Wrapper.FULL_ACTIVITY ||
                        wrapper.projectileStates.get(i) == Wrapper.ANIMATION_AND_MOVEMENT) {
                        
                        wrapper.projectiles.get(i).updateMovement(currentTime);
                    }
                }
            }
            
            /* P�ivitet��n teko�lyjen p�ivitysv�lit */
            if (currentTime - waveStartTime >= 3000) {
                updateSpeedUp = 2;
            }
           
            /* P�ivitet��n teko�lyt */
            if (wrapper.player != null) {

                // P�ivitet��n tila 1
                if (currentTime - lastAiUpdateStateOne >= (400 / updateSpeedUp)) {
                    lastAiUpdateStateOne = currentTime;
                    
                    for (int i : wrapper.priorityOneEnemies) {
                        if (wrapper.enemyStates.get(i) == Wrapper.FULL_ACTIVITY) {
                            wrapper.enemies.get(i).ai.handleAi();
                        }
                    }
                    
                    for (int i : wrapper.priorityOneProjectiles) {
                        if (wrapper.projectileStates.get(i) == Wrapper.FULL_ACTIVITY) {
                        	if (wrapper.projectiles.get(i).ai.active) {
                        		wrapper.projectiles.get(i).ai.handleAi();
                        	}
                        }
                    }
                }

                // P�ivitet��n tila 2
                if (currentTime - lastAiUpdateStateTwo >= (200 / updateSpeedUp)) {
                    lastAiUpdateStateTwo = currentTime;
                    
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
                if (currentTime - lastAiUpdateStateThree >= (100 / updateSpeedUp)) {
                    lastAiUpdateStateThree = currentTime;
                    
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
                if (currentTime - lastAiUpdateStateFour >= (50 / updateSpeedUp)) {
                    lastAiUpdateStateFour = currentTime;
                    
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
            
            /* Tarkistetaan t�rm�ykset */
            if (currentTime - lastCollisionUpdate >= 50) {
            	lastCollisionUpdate = currentTime;
            	
            	for (int i = wrapper.projectiles.size()-1; i >= 0; --i) {
            		if (wrapper.projectileStates.get(i) == Wrapper.FULL_ACTIVITY) {
            			wrapper.projectiles.get(i).checkCollision();
            		}
            	}
            	for (int i = wrapper.enemies.size()-1; i >= 0; --i) {
            		if (wrapper.enemyStates.get(i) == Wrapper.FULL_ACTIVITY) {
            			//wrapper.enemies.get(i).checkCollision();
            		}
            	}
            }
            
            /* P�ivitet��n aseiden cooldownit */
            if (currentTime - lastCooldownUpdate >= 100) {
            	lastCooldownUpdate = currentTime;
                gameMode.weaponManager.updateCooldowns();
            }
            
            /* P�ivitet��n vihollisaallot */
            if (currentTime - lastGameModeUpdate >= 1000) {
                if (SurvivalMode.enemiesLeft == 0) {
                    waveStartTime = android.os.SystemClock.uptimeMillis();
                    updateSpeedUp = 1;
                    
                    gameMode.startWave();
                }
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