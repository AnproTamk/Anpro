package fi.tamk.anpro;

import java.util.ArrayList;

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
    private boolean running = false;
    
    /* Tarvittavat luokat */
    private Wrapper      wrapper;
    private AbstractMode gameMode;
    private TouchManager touchManager;
    private Hud          hud;
    
    /* Pelaaja (V�LIAIKAINEN!!!) */
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
    
    /* Muille luokille v�litett�v�t muuttujat (tallennetaan v�liaikaisesti, sill� muut
       luokat voidaan luoda vasta kun render�ij� on ladannut kaikki grafiikat) */
    private DisplayMetrics dm;
    private Context        context;
    private GLSurfaceView  surface;

    /**
     * Alustaa luokan muuttujat ja luo pelitilan.
     * 
     * @param DisplayMetrics N�yt�n tiedot
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
     * M��ritt�� s�ikeen p��lle tai pois
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
        /* Haetaan p�ivityksille aloitusajat */
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
        
        /* Suoritetaan s�iett� kunnes se m��ritet��n pys�ytett�v�ksi */
        while (running) {
            
            // Haetaan t�m�nhetkinen aika
            long currentTime = android.os.SystemClock.uptimeMillis();
            
            /* P�ivitet��n sijainnit ja liikkuminen */
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
            
            /* P�ivitet��n animaatiot */
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
           
            /* P�ivitet��n teko�lyt */
            if (wrapper.player != null) {

                // P�ivitet��n tila 1
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

                // P�ivitet��n tila 2
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

                // P�ivitet��n tila 3
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

                // P�ivitet��n tila 4
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
            
            /* P�ivitet��n aseiden cooldownit */
            if (currentTime - lastCooldownUpdate >= 100) {
                gameMode.weaponManager.updateCooldowns();
            }
            
            /* P�ivitet��n vihollisaallot */
            if (currentTime - lastGameModeUpdate >= 1000) {
                if (SurvivalMode.enemiesLeft == 0) {
                	// TODO:
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