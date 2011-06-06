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
    
    private Context       context;
    private Resources     resources;
    private GLSurfaceView surface;
    private GLRenderer    renderer;
    
    public Player player;
    
    private long _lastUpdate = 0;
    
    GestureLibrary mLibrary;
    
    public GameThread(Context _context, Resources _resources, GLSurfaceView _glSurfaceView, GLRenderer _glRenderer) {
        context    = _context;
        resources  = _resources;
        renderer   = _glRenderer;
        
        player = new Player(10, 5);
        player.setDrawables(null, renderer.playerTextures);
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
            
            // Tarkista sijainnit
            if (currentTime - _lastUpdate >= 20) {
                _lastUpdate = currentTime;
                
                
                /*
                 * PELIN PÄÄSILMUKKA ALKAA TÄSTÄ
                 */
                
                // ...
                
                /*
                 * PELIN PÄÄSILMUKKA LOPPUU TÄHÄN
                 */
            }
            
            // Tarkista tekoälyn tila 100 ms välein
            if (currentTime - _lastUpdate >= 100) {
            	
            }
            
            // Tarkista animaatiot
            if (currentTime - _lastUpdate >= 15) {
            	
            }
        }
    }
}