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
    private AbstractMode  gameMode;
    @SuppressWarnings("unused")
	private TouchManager  touchManager;
    public  Hud           hud;
    private WeaponManager weaponManager;
    
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
        wrapper = Wrapper.getInstance();
        
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
     * Suorittää säikeen.
     */
    @Override
    public void run()
    {
        // Luodaan SurvivalMode
        gameMode = new SurvivalMode(gameActivity, dm, context, weaponManager);

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
        
        /* Suoritetaan säiettä kunnes se määritetään pysäytettäväksi */
        while (running) {
            
            // Haetaan tämänhetkinen aika
            long currentTime = android.os.SystemClock.uptimeMillis();
            
            /* Päivitetään sijainnit ja liikkuminen */
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
            
            /* Päivitetään tekoälyjen päivitysvälit */
            if (currentTime - waveStartTime >= 3000) {
                updateSpeedUp = 2;
            }
           
            /* Päivitetään tekoälyt */
            if (wrapper.player != null) {

                // Päivitetään tila 1
                if (currentTime - lastAiUpdateStateOne >= (300 / updateSpeedUp)) {
                    lastAiUpdateStateOne = currentTime;
                    
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
                if (currentTime - lastAiUpdateStateTwo >= (150 / updateSpeedUp)) {
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

                // Päivitetään tila 3
                if (currentTime - lastAiUpdateStateThree >= (75 / updateSpeedUp)) {
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

                // Päivitetään tila 4
                if (currentTime - lastAiUpdateStateFour >= (40 / updateSpeedUp)) {
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
            
            /* Tarkistetaan törmäykset */
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
            
            /* Päivitetään aseiden cooldownit */
            if (currentTime - lastCooldownUpdate >= 100) {
            	lastCooldownUpdate = currentTime;
                gameMode.weaponManager.updateCooldowns();
                hud.updateCooldowns();
            }
            
            /* Päivitetään pelaajan armorit */
            if (currentTime - lastArmorUpdate >= 10000) {
            	lastArmorUpdate = currentTime;
            	if (wrapper.player.currentArmor <= 0) {
            		wrapper.player.currentArmor += 25;
            	}
            }
            
            /* Päivitetään vihollisaallot */
            if (currentTime - lastGameModeUpdate >= 1000) {
                if (SurvivalMode.enemiesLeft == 0) {
                    waveStartTime = android.os.SystemClock.uptimeMillis();
                    updateSpeedUp = 1;
                    
                    gameMode.startWave();
                }
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