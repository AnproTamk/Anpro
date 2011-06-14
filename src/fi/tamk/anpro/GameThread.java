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
    
    public Enemy  enemy;
    public Player player;
    
    private long lastMovementUpdate  = 0;
    private long lastAiUpdate        = 0;
    private long lastCooldownUpdate  = 0;
    private long lastAnimationUpdate = 0;
    
    public static ArrayList<GenericAi> criticalUpdates;

    /*
     * Rakentaja
     */
    public GameThread(GLSurfaceView _glSurfaceView, GLRenderer _glRenderer) {
    	surface       = _glSurfaceView;
        renderer      = _glRenderer;
        wrapper       = Wrapper.getInstance();
        weaponStorage = WeaponStorage.getInstance();
        
        criticalUpdates = new ArrayList<GenericAi>();

    	/** DEBUG-HÄRPÄKKEITÄ!!!! */
        player = new Player(50, 1);
        // player.setDrawables(null, renderer.playerTextures);
        player.x = 400;
        player.y = 240;

        enemy = new Enemy(5, 1, 1, 1, 1);
        // enemy.setDrawables(null, renderer.enemyTextures);
        enemy.direction = 0;
        enemy.x = 100;
        enemy.y = 100;

        weaponStorage.initialize(1);
        /** DEBUG LOPPUU!!!! */
    }

    /*
     * Määrittää säikeen päälle tai pois
     */
    public void setRunning(boolean _run) {
        running = _run;
    }

    /*
     * Suorittää säikeen
     */
    @Override
    public void run() {

        // Haetaan päivityksille aloitusajat
    	lastMovementUpdate  = android.os.SystemClock.uptimeMillis();
    	lastAiUpdate        = android.os.SystemClock.uptimeMillis();
    	lastCooldownUpdate  = android.os.SystemClock.uptimeMillis();
    	lastAnimationUpdate = android.os.SystemClock.uptimeMillis();
    	
        while (running) {
            long currentTime = android.os.SystemClock.uptimeMillis();
            
        	// Päivitä sijainnit ja liikkuminen
            if (currentTime - lastMovementUpdate >= 20) {
            	lastMovementUpdate = currentTime;
            	
                for (int i = wrapper.enemies.size()-1; i >= 0; --i) {
                	if (wrapper.enemyStates.get(i) == 1) {
                		wrapper.enemies.get(i).updateMovement(currentTime);
                	}
                }
                
                for (int i = wrapper.projectileLasers.size()-1; i >= 0; --i) {
                	if (wrapper.projectileLaserStates.get(i) == 1) {
                		wrapper.projectileLasers.get(i).updateMovement(currentTime);
                	}
                }
            }
            
            // Päivitä animaatiot
            if (currentTime - lastAnimationUpdate >= 40) {
            	lastAnimationUpdate = currentTime;
            	
            	// päivitetään pelaajan animaatio
            	if (wrapper.player.usedAnimation != -1) {
            		wrapper.player.update();
            	}
            	
            	// käydään viholliset läpi
                for (int i = wrapper.enemies.size()-1; i >= 0; --i) {
                	if (wrapper.enemyStates.get(i) == 1 && wrapper.enemies.get(i).usedAnimation != -1) {
                		wrapper.enemies.get(i).update();
                	}
                }
                
                // käydään 1. ammusluokka läpi
                for (int i = wrapper.projectileLasers.size()-1; i >= 0; --i) {
                	if (wrapper.projectileLaserStates.get(i) == 1 && wrapper.projectileLasers.get(i).usedAnimation != -1) {
                		wrapper.projectileLasers.get(i).update();
                	}
                }
            }
                        
            // päivitä kriittiset tekoälyt
            if (wrapper.player != null) {
            	if (currentTime - lastAiUpdate >= 50) {
            		for (int i = criticalUpdates.size()-1; i >= 0; --i) {
            			criticalUpdates.get(i).handleAi();
                	}
                }
	            
            	criticalUpdates.clear();
            }
            
            // Päivitä tekoälyt
            if (wrapper.player != null) {
	            if (currentTime - lastAiUpdate >= 100) {
	            	lastAiUpdate = currentTime;
	            	
	            	// Käydään viholliset läpi
	                for (int i = wrapper.enemies.size()-1; i >= 0; --i) {
	                	if (wrapper.enemyStates.get(i) == 1) {
	                		wrapper.enemies.get(i).ai.handleAi();
	                	}
	                }
	                
	                // käydään 1. ammusluokka läpi
	                for (int i = wrapper.projectileLasers.size()-1; i >= 0; --i) {
	                	if (wrapper.projectileLaserStates.get(i) == 1) {
	                		wrapper.projectileLasers.get(i).handleAi();
	                	}
	                }
	            }
            }

            // Päivitetään aseiden cooldownit
            if (currentTime - lastCooldownUpdate >= 100) {
            	weaponStorage.updateCooldowns();
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