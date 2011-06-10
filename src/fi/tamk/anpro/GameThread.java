package fi.tamk.anpro;

import android.content.Context;
import android.content.res.Resources;
import android.gesture.GestureLibrary;
import android.opengl.GLSurfaceView;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

class GameThread extends Thread {
    private boolean running = false;
    
    private GLSurfaceView surface;
    private GLRenderer    renderer;
    private Wrapper       wrapper;
    
    public Enemy  enemy;
    public Player player;
    
    private long lastMovementUpdate = 0;
    private long lastAiUpdate       = 0;
    
    GestureLibrary mLibrary;
    
    public GameThread(GLSurfaceView _glSurfaceView, GLRenderer _glRenderer) {
    	surface  = _glSurfaceView;
        renderer = _glRenderer;
        wrapper  = Wrapper.getInstance();
    }

    public void setRunning(boolean run) {
        running = run;
    }

    @Override
    public void run() {
        player = new Player(5, 1);
        player.setDrawables(null, renderer.playerTextures);
        player.x = 0;
        player.y = 0;
        
        enemy = new Enemy(5, 1, 1, 1, 1);
        enemy.setDrawables(null, renderer.enemyTextures);
        enemy.direction = 0;
        enemy.x = 240;
        enemy.y = 400;
        enemy.turningDirection = 0;
        
    	lastMovementUpdate = android.os.SystemClock.uptimeMillis();
    	lastAiUpdate       = android.os.SystemClock.uptimeMillis();
    	
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
            }
            
            // Päivitä tekoälyt
            if (wrapper.player != null) {
	            if (currentTime - lastAiUpdate >= 100) {
	            	lastAiUpdate = currentTime;
	            	
	                for (int i = wrapper.enemies.size()-1; i >= 0; --i) {
	                	if (wrapper.enemyStates.get(i) == 1) {
	                		wrapper.enemies.get(i).ai.handleAi();
	                	}
	                }
	            }
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