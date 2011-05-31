package fi.tamk.anpro;

import android.content.Context;
import android.content.res.Resources;
import android.gesture.GestureLibrary;
import android.opengl.GLSurfaceView;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

class GameThread extends Thread {
    private boolean _running = false;
    
    private Context       _context;
    private Resources     _resources;
    private GLSurfaceView _surface;
    private GLRenderer    _renderer;
    
    private long _lastUpdate = 0;
    
    GestureLibrary mLibrary;
    
    public GameThread(Context context, Resources resources, GLSurfaceView glSurfaceView, GLRenderer glRenderer) {
        _context    = context;
        _resources  = resources;
        _surface    = glSurfaceView;
        _renderer   = glRenderer;
    }

    public void setRunning(boolean run) {
        _running = run;
    }

    @Override
    public void run() {
        /*
         * PELIN PÄÄOSIO ALKAA TÄSTÄ
         * Älä koskaan varaa muistia tässä! (lukuun ottamatta FPS:n hallintaan
         * vaadittuja muuttujia)
         */
        _lastUpdate = android.os.SystemClock.uptimeMillis();
        
        while (_running) {
            long currentTime = android.os.SystemClock.uptimeMillis();
            
            if (currentTime - _lastUpdate >= 30) {
                _lastUpdate = currentTime;
                
                /*
                 * PELIN PÄÄSILMUKKA ALKAA TÄSTÄ
                 */
                
                // ...
                
                /*
                 * PELIN PÄÄSILMUKKA LOPPUU TÄHÄN
                 */
            }
        }
    }
}