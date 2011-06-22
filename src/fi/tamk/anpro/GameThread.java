package fi.tamk.anpro;

import java.util.ArrayList;

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
    private boolean running = false;
    
    /* Tarvittavat luokat */
    private Wrapper      wrapper;
    private AbstractMode gameMode;
    private TouchManager touchManager;
    private Hud          hud;
    
    /* Pelaaja (VÄLIAIKAINEN!!!) */
    public Player player;
    
    /* Ajanoton muuttujat */
    private long lastMovementUpdate     = 0;
    private long lastAiUpdateStateOne   = 0;
    private long lastAiUpdateStateTwo   = 0;
    private long lastAiUpdateStateThree = 0;
    private long lastAiUpdateStateFour  = 0;
    private long lastCooldownUpdate     = 0;
    private long lastAnimationUpdate    = 0;
    private long lastGameModeUpdate		= 0;
    
    /* Muille luokille välitettävät muuttujat (tallennetaan väliaikaisesti, sillä muut
       luokat voidaan luoda vasta kun renderöijä on ladannut kaikki grafiikat) */
    private DisplayMetrics dm;
    private Context        context;
    private GLSurfaceView  surface;

    /**
     * Alustaa luokan muuttujat ja luo pelitilan.
     * 
     * @param DisplayMetrics Näytön tiedot
     * @param Context		 Ohjelman konteksti
     * @param GLSurfaceView  OpenGL-pinta
     */
    public GameThread(DisplayMetrics _dm, Context _context, GLSurfaceView _surface)
    {
        wrapper = Wrapper.getInstance();
        
        dm      = _dm;
        context = _context;
        surface = _surface;
        
        /* DEBUGGIA!!!! */
        player = new Player(5, 2);
        player.x = 0;
        player.y = 0;
    }

    /**
     * Määrittää säikeen päälle tai pois
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
        /* Haetaan päivityksille aloitusajat */
        lastMovementUpdate     = android.os.SystemClock.uptimeMillis();
        lastAiUpdateStateOne   = lastMovementUpdate;
        lastAiUpdateStateTwo   = lastMovementUpdate;
        lastAiUpdateStateThree = lastMovementUpdate;
        lastAiUpdateStateFour  = lastMovementUpdate;
        lastCooldownUpdate     = lastMovementUpdate;
        lastAnimationUpdate    = lastMovementUpdate;
        lastGameModeUpdate	   = lastMovementUpdate;

        /* Luodaan pelitila */
        gameMode = new SurvivalMode(dm, context);
        
        /* Luodaan TouchManager ja HUD */
        hud          = new Hud(context);
        touchManager = new TouchManager(dm, surface, context, hud);
        
        /* Suoritetaan säiettä kunnes se määritetään pysäytettäväksi */
        while (running) {
            
            // Haetaan tämänhetkinen aika
            long currentTime = android.os.SystemClock.uptimeMillis();
            
            /* Päivitetään sijainnit ja liikkuminen */
            if (currentTime - lastMovementUpdate >= 20) {
                
                lastMovementUpdate = currentTime;
                
                for (int i = wrapper.enemies.size()-1; i >= 0; --i) {
                    if (wrapper.enemyStates.get(i) == 1) {
                        wrapper.enemies.get(i).updateMovement(currentTime);
                    }
                }
                
                for (int i = wrapper.projectiles.size()-1; i >= 0; --i) {
                    if (wrapper.projectileStates.get(i) == 1) {
                        wrapper.projectiles.get(i).updateMovement(currentTime);
                    }
                }
            }
            
            /* Päivitetään animaatiot */
            if (currentTime - lastAnimationUpdate >= 40) {
                
                lastAnimationUpdate = currentTime;
                
                if (wrapper.player.usedAnimation != -1) {
                    wrapper.player.update();
                }
                
                for (int i = wrapper.enemies.size()-1; i >= 0; --i) {
                    if (wrapper.enemyStates.get(i) > 0 && wrapper.enemies.get(i).usedAnimation != -1) {
                        wrapper.enemies.get(i).update();
                    }
                }
                
                for (int i = wrapper.projectiles.size()-1; i >= 0; --i) {
                    if (wrapper.projectileStates.get(i) > 0 && wrapper.projectiles.get(i).usedAnimation != -1) {
                        wrapper.projectiles.get(i).update();
                    }
                }
            }
           
            /* Päivitetään tekoälyt */
            if (wrapper.player != null) {

                // Päivitetään tila 1
                if (currentTime - lastAiUpdateStateOne >= 50) {
                    lastAiUpdateStateOne = currentTime;
                    
                    for (int i : wrapper.priorityOneEnemies) {
                        if (wrapper.enemyStates.get(i) == 1) {
                            wrapper.enemies.get(i).ai.handleAi();
                        }
                    }
                    
                    for (int i : wrapper.priorityOneProjectiles) {
                        if (wrapper.projectileStates.get(i) == 1) {
                            wrapper.projectiles.get(i).handleAi();
                        }
                    }
                }

                // Päivitetään tila 2
                if (currentTime - lastAiUpdateStateTwo >= 100) {
                    lastAiUpdateStateTwo = currentTime;
                    
                    for (int i : wrapper.priorityTwoEnemies) {
                        if (wrapper.enemyStates.get(i) == 1) {
                            wrapper.enemies.get(i).ai.handleAi();
                        }
                    }
                    
                    for (int i : wrapper.priorityTwoProjectiles) {
                        if (wrapper.projectileStates.get(i) == 1) {
                            wrapper.projectiles.get(i).handleAi();
                        }
                    }
                }

                // Päivitetään tila 3
                if (currentTime - lastAiUpdateStateThree >= 200) {
                    lastAiUpdateStateThree = currentTime;
                    
                    for (int i : wrapper.priorityThreeEnemies) {
                        if (wrapper.enemyStates.get(i) == 1) {
                            wrapper.enemies.get(i).ai.handleAi();
                        }
                    }
                    
                    for (int i : wrapper.priorityThreeProjectiles) {
                        if (wrapper.projectileStates.get(i) == 1) {
                            wrapper.projectiles.get(i).handleAi();
                        }
                    }
                }

                // Päivitetään tila 4
                if (currentTime - lastAiUpdateStateFour >= 400) {
                    lastAiUpdateStateFour = currentTime;
                    
                    for (int i : wrapper.priorityFourEnemies) {
                        if (wrapper.enemyStates.get(i) == 1) {
                            wrapper.enemies.get(i).ai.handleAi();
                        }
                    }
                    
                    for (int i : wrapper.priorityFourProjectiles) {
                        if (wrapper.projectileStates.get(i) == 1) {
                            wrapper.projectiles.get(i).handleAi();
                        }
                    }
                }
            }
            
            /* Päivitetään aseiden cooldownit */
            if (currentTime - lastCooldownUpdate >= 100) {
                gameMode.weaponManager.updateCooldowns();
            }
            
            /* Päivitetään vihollisaallot */
            if (currentTime - lastGameModeUpdate >= 1000) {
                if (SurvivalMode.enemiesLeft == 0) {
                	// TODO:
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