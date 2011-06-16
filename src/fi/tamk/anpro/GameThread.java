package fi.tamk.anpro;

import java.util.ArrayList;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

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
    
    /* Pelaaja (VÄLIAIKAINEN!!!) */
    public Player player;
    
    /* Ajanoton muuttujat */
    private long lastMovementUpdate  = 0;
    private long lastAiUpdate        = 0;
    private long lastCooldownUpdate  = 0;
    private long lastAnimationUpdate = 0;
    
    /* Kriittisten päivitysten lista tekoälyjä varten */
    public static ArrayList<AbstractAi> criticalUpdates;

    /**
     * Alustaa luokan muuttujat ja luo pelitilan.
     * 
     * @param DisplayMetrics Näytön tiedot
     * @param Context		 Ohjelman konteksti
     */
    public GameThread(DisplayMetrics _dm, Context _context)
    {
        wrapper = Wrapper.getInstance();
        
        criticalUpdates = new ArrayList<AbstractAi>();

        gameMode = new SurvivalMode(_dm, _context);
        
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
        // Haetaan päivityksille aloitusajat
        lastMovementUpdate  = android.os.SystemClock.uptimeMillis();
        lastAiUpdate        = lastMovementUpdate;
        lastCooldownUpdate  = lastMovementUpdate;
        lastAnimationUpdate = lastMovementUpdate;
        
        // Suoritetaan säiettä kunnes se määritetään pysäytettäväksi
        while (running) {
        	
        	// Haetaan tämänhetkinen aika
            long currentTime = android.os.SystemClock.uptimeMillis();
            
            // Päivitetään sijainnit ja liikkuminen
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
            
            // Päivitetään animaatiot
            if (currentTime - lastAnimationUpdate >= 40) {
                lastAnimationUpdate = currentTime;
                
                // päivitetään pelaajan animaatio
                if (wrapper.player.usedAnimation != -1) {
                    wrapper.player.update();
                }
                
                for (int i = wrapper.enemies.size()-1; i >= 0; --i) {
                    if (wrapper.enemyStates.get(i) == 1 && wrapper.enemies.get(i).usedAnimation != -1) {
                        wrapper.enemies.get(i).update();
                    }
                }
                
                for (int i = wrapper.projectiles.size()-1; i >= 0; --i) {
                    if (wrapper.projectileStates.get(i) == 1 && wrapper.projectiles.get(i).usedAnimation != -1) {
                        wrapper.projectiles.get(i).update();
                    }
                }
            }
            
            // Päivitetään kriittiset tekoälyt
            if (wrapper.player != null) {
                if (currentTime - lastAiUpdate >= 50) {
                    for (int i = criticalUpdates.size()-1; i >= 0; --i) {
                        criticalUpdates.get(i).handleAi();
                    }
                }
                
                // Tyhjennetään päivityslista
                criticalUpdates.clear();
            }
            
            // Päivitetään tekoälyt
            if (wrapper.player != null) {
                if (currentTime - lastAiUpdate >= 100) {
                    lastAiUpdate = currentTime;
                    
                    for (int i = wrapper.enemies.size()-1; i >= 0; --i) {
                        if (wrapper.enemyStates.get(i) == 1) {
                            wrapper.enemies.get(i).ai.handleAi();
                        }
                    }
                    
                    for (int i = wrapper.projectiles.size()-1; i >= 0; --i) {
                        if (wrapper.projectileStates.get(i) == 1) {
                            wrapper.projectiles.get(i).handleAi();
                        }
                    }
                }
            }
            
            // Päivitetään aseiden cooldownit
            if (currentTime - lastCooldownUpdate >= 100) {
            	gameMode.weaponManager.updateCooldowns();
            }

            // Hidastetaan säiettä pakottamalla se odottamaan 20 ms
            try {
                Thread.sleep(20);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}