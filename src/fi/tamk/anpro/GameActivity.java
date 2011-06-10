package fi.tamk.anpro;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.graphics.PixelFormat;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Window;
import android.view.WindowManager;

public class GameActivity extends Activity
{
    private GLSurfaceView glSurfaceView;
    private GLRenderer    glRenderer;
    private GameThread    gameThread;
    
    /*
     * P‰‰funktio, joka kutsutaan aktiviteetin k‰ynnistyess‰.
     */
    @Override
    public void onCreate(Bundle _savedInstanceState)
    {
        super.onCreate(_savedInstanceState);
        
        // Piiloitetaan otsikko ja vaihdetaan kokoruuduntilaan
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                             WindowManager.LayoutParams.FLAG_FULLSCREEN);
        
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        
        // Luodaan OpenGL-n‰kym‰ ja renderˆij‰
        glSurfaceView = new GLSurfaceView(this);
        glRenderer    = new GLRenderer(this);
        
        // M‰‰ritet‰‰n renderˆij‰n asetukset ja otetaan se k‰yttˆˆn
        glSurfaceView.setEGLConfigChooser(8, 8, 8, 8, 0, 0);
        glSurfaceView.getHolder().setFormat(PixelFormat.TRANSLUCENT);
        glSurfaceView.setRenderer(glRenderer);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        
        setContentView(glSurfaceView);
        
        // Luodaan ja k‰ynnistet‰‰n pelin s‰ie
        gameThread = new GameThread(glSurfaceView, glRenderer);

        gameThread.setRunning(true);
        glRenderer.gameThread = gameThread;
    }
    
    /*
     * Kutsutaan kun ohjelma palaa taustalta tai k‰nnykk‰ palaa valmiustilasta
     */
    @Override
    protected void onResume()
    {
        super.onResume();
        /*glSurfaceView.onResume();
        
        // Pys‰ytet‰‰n s‰ie
        boolean retry = true;
        gameThread.setRunning(false);
        while (retry) {
            try {
                gameThread.join();
                retry = false;
            } catch (InterruptedException e) {
                // Yritet‰‰n uudelleen kunnes onnistuu
            }
        }*/
    }
    
    /*
     * Kutsutaan kun ohjelma pys‰ytet‰‰n
     */
    @Override
    protected void onPause()
    {
        super.onPause();
        /*glSurfaceView.onPause();
        
        // Pys‰ytet‰‰n s‰ie
        boolean retry = true;
        gameThread.setRunning(false);
        while (retry) {
            try {
                gameThread.join();
                retry = false;
            } catch (InterruptedException e) {
                // Yritet‰‰n uudelleen kunnes onnistuu
            }
        }*/
    }
}
