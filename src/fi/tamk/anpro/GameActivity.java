package fi.tamk.anpro;

import java.util.ArrayList;

import android.app.Activity;
import android.gesture.Gesture;
import android.gesture.GestureLibraries;
import android.gesture.GestureLibrary;
import android.gesture.GestureOverlayView;
import android.gesture.GestureOverlayView.OnGesturePerformedListener;
import android.gesture.Prediction;
import android.graphics.PixelFormat;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

public class GameActivity extends Activity
{
    private GLSurfaceView _glSurfaceView;
    private GLRenderer    _glRenderer;
    private GameThread    _gameThread;
    
    /** P‰‰funktio, joka kutsutaan aktiviteetin k‰ynnistyess‰. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        
        // Piiloitetaan otsikko ja vaihdetaan kokoruuduntilaan
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                             WindowManager.LayoutParams.FLAG_FULLSCREEN);
        
        // Luodaan OpenGL-n‰kym‰ ja renderˆij‰
        _glSurfaceView = new GLSurfaceView(this);
        _glRenderer    = new GLRenderer(this);
        
        // M‰‰ritet‰‰n renderˆij‰n asetukset ja otetaan se k‰yttˆˆn
        _glSurfaceView.setEGLConfigChooser(8, 8, 8, 8, 0, 0);
        _glSurfaceView.getHolder().setFormat(PixelFormat.TRANSLUCENT);
        _glSurfaceView.setRenderer(_glRenderer);
        
        setContentView(_glSurfaceView);
        
        // Luodaan ja k‰ynnistet‰‰n pelin s‰ie
        _gameThread = new GameThread(this, getResources(), _glSurfaceView, _glRenderer);
        _gameThread.setRunning(true);
        try {
            _gameThread.start();
        }
        catch (IllegalThreadStateException exc) {
            finish();
        }
    }
    
    /** Kutsutaan kun ohjelma palaa taustalta tai k‰nnykk‰ palaa valmiustilasta */
    @Override
    protected void onResume()
    {
        super.onResume();
        _glSurfaceView.onResume();
        
        // Pys‰ytet‰‰n s‰ie
        boolean retry = true;
        _gameThread.setRunning(false);
        while (retry) {
            try {
                _gameThread.join();
                retry = false;
            } catch (InterruptedException e) {
                // Yritet‰‰n uudelleen kunnes onnistuu
            }
        }
    }
    
    /** Kutsutaan kun ohjelma pys‰ytet‰‰n */
    @Override
    protected void onPause()
    {
        super.onPause();
        _glSurfaceView.onPause();
        
        // Pys‰ytet‰‰n s‰ie
        boolean retry = true;
        _gameThread.setRunning(false);
        while (retry) {
            try {
                _gameThread.join();
                retry = false;
            } catch (InterruptedException e) {
                // Yritet‰‰n uudelleen kunnes onnistuu
            }
        }
    }
}
