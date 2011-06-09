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
    
    private Context       context;
    private Resources     resources;
    private GLSurfaceView surface;
    private GLRenderer    renderer;
    
    public Enemy enemy;
    public GuiObject testText;
    
    private long _lastUpdate = 0;
    
    GestureLibrary mLibrary;
    
    public GameThread(Context _context, Resources _resources, GLSurfaceView _glSurfaceView, GLRenderer _glRenderer) {
        context    = _context;
        resources  = _resources;
        renderer   = _glRenderer;
        
        enemy = new Enemy(5, 1, 1, 1, 1);
        enemy.setDrawables(null, renderer.enemyTextures);
        enemy.direction = 0;
        enemy.x = 240;
        enemy.y = 400;
        enemy.turningDirection = 0;
        
        testText = new GuiObject();
        testText.setDrawables(renderer.testText);
        testText.x = 240;
        testText.y = 400;
    }

    public void setRunning(boolean run) {
        running = run;
    }

    @Override
    public void run() {
        /*
         * PELIN P��OSIO ALKAA T�ST�
         * �l� koskaan varaa muistia t�ss�! (lukuun ottamatta FPS:n hallintaan
         * vaadittuja muuttujia)
         */
        _lastUpdate = android.os.SystemClock.uptimeMillis();

        while (running) {
            long currentTime = android.os.SystemClock.uptimeMillis();
            
            // Tarkista sijainnit
            //if (true) {
            if (currentTime - _lastUpdate >= 20) {
                _lastUpdate = currentTime;
                enemy.updateMovement(currentTime);
                //enemy.x += 10;
                
                
                /*
                 * PELIN P��SILMUKKA ALKAA T�ST�
                 */
                
                // ...
                
                /*
                 * PELIN P��SILMUKKA LOPPUU T�H�N
                 */
                
                try {
					this.sleep(20);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            }
        }
    }
}