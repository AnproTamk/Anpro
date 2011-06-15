package fi.tamk.anpro;

import java.util.ArrayList;

import android.opengl.GLSurfaceView;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

class GameThread extends Thread {
    private boolean running = false;
    
    private GLSurfaceView surface;
    private GLRenderer    renderer;
    private Wrapper       wrapper;
    private WeaponStorage weaponStorage;
    private SurvivalMode  survivalMode;
    
    public Enemy  enemy;
    public Player player;
    
    private long lastMovementUpdate  = 0;
    private long lastAiUpdate        = 0;
    private long lastCooldownUpdate  = 0;
    private long lastAnimationUpdate = 0;
    
    public static ArrayList<AbstractAi> criticalUpdates;

    /*
     * Rakentaja
     */
    public GameThread(GLSurfaceView _glSurfaceView, GLRenderer _glRenderer) {
        surface       = _glSurfaceView;
        renderer      = _glRenderer;
        wrapper       = Wrapper.getInstance();
        weaponStorage = WeaponStorage.getInstance();
        
        criticalUpdates = new ArrayList<AbstractAi>();

        survivalMode = SurvivalMode.getInstance();
    }

    /*
     * M��ritt�� s�ikeen p��lle tai pois
     */
    public void setRunning(boolean _run) {
        running = _run;
    }

    /*
     * Suoritt�� s�ikeen
     */
    @Override
    public void run() {

        // Haetaan p�ivityksille aloitusajat
        lastMovementUpdate  = android.os.SystemClock.uptimeMillis();
        lastAiUpdate        = lastMovementUpdate;
        lastCooldownUpdate  = lastMovementUpdate;
        lastAnimationUpdate = lastMovementUpdate;
        
        while (running) {
            long currentTime = android.os.SystemClock.uptimeMillis();
            
            // P�ivit� sijainnit ja liikkuminen
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
            
            // P�ivit� animaatiot
            if (currentTime - lastAnimationUpdate >= 40) {
                lastAnimationUpdate = currentTime;
                
                // p�ivitet��n pelaajan animaatio
                if (wrapper.player.usedAnimation != -1) {
                    wrapper.player.update();
                }
                
                // k�yd��n viholliset l�pi
                for (int i = wrapper.enemies.size()-1; i >= 0; --i) {
                    if (wrapper.enemyStates.get(i) == 1 && wrapper.enemies.get(i).usedAnimation != -1) {
                        wrapper.enemies.get(i).update();
                    }
                }
                
                // k�yd��n 1. ammusluokka l�pi
                for (int i = wrapper.projectiles.size()-1; i >= 0; --i) {
                    if (wrapper.projectileStates.get(i) == 1 && wrapper.projectiles.get(i).usedAnimation != -1) {
                        wrapper.projectiles.get(i).update();
                    }
                }
            }
                        
            // p�ivit� kriittiset teko�lyt
            if (wrapper.player != null) {
                if (currentTime - lastAiUpdate >= 50) {
                    for (int i = criticalUpdates.size()-1; i >= 0; --i) {
                        criticalUpdates.get(i).handleAi();
                    }
                }
                
                criticalUpdates.clear();
            }
            
            // P�ivit� teko�lyt
            if (wrapper.player != null) {
                if (currentTime - lastAiUpdate >= 100) {
                    lastAiUpdate = currentTime;
                    
                    // K�yd��n viholliset l�pi
                    for (int i = wrapper.enemies.size()-1; i >= 0; --i) {
                        if (wrapper.enemyStates.get(i) == 1) {
                            wrapper.enemies.get(i).ai.handleAi();
                        }
                    }
                    
                    // k�yd��n 1. ammusluokka l�pi
                    for (int i = wrapper.projectiles.size()-1; i >= 0; --i) {
                        if (wrapper.projectileStates.get(i) == 1) {
                            wrapper.projectiles.get(i).handleAi();
                        }
                    }
                }
            }

            // P�ivitet��n aseiden cooldownit
            if (currentTime - lastCooldownUpdate >= 100) {
                weaponStorage.updateCooldowns();
            }

            // Hidastetaan s�iett� pakottamalla se odottamaan 20 ms
            try {
                Thread.sleep(20);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}