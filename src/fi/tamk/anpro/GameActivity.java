package fi.tamk.anpro;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.graphics.PixelFormat;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Window;
import android.view.WindowManager;
import android.media.AudioManager;

/**
 * Pelitilan alkupiste. Luo render�ij�n, pelis�ikeen, HUDin ja TouchManagerin.
 */
public class GameActivity extends Activity
{
	/* Pelitilat */
	public static final int SURVIVAL_MODE = 1;
	public static final int STORY_MODE    = 2;
	
	/* Render�ij� ja OpenGL-pinta */
    private GLSurfaceView surfaceView;
    private GLRenderer    renderer;
    
    /* Muut luokat */
    private GameThread   gameThread;
    private TouchManager touchManager;
    private Hud          hud;
    
    /* Aktiivinen pelitila (asetetaan p��valikossa) */
    public static int activeMode = 1;
        
    /**
     * M��ritt�� asetukset ja luo tarvittavat oliot, kuten render�ij�n, HUDin,
     * GameThreadin ja TouchManagerin.
     * 
     * @param Bundle Pelin aiempi tila
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
        
        // Asetetaan ��nens��t�napit muuttamaan media volumea
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
        
        // Luodaan OpenGL-pinta ja render�ij�
        surfaceView = new GLSurfaceView(this);
        renderer    = new GLRenderer(this);
        
        // M��ritet��n render�ij�n asetukset ja otetaan se k�ytt��n
        surfaceView.setEGLConfigChooser(8, 8, 8, 8, 0, 0);
        surfaceView.getHolder().setFormat(PixelFormat.TRANSLUCENT);
        surfaceView.setRenderer(renderer);

        // Ladataan n�yt�n tiedot
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        
        // Asetetaan k�ytett�v� pinta
        setContentView(surfaceView);
        
        // Luodaan ja k�ynnistet��n pelin s�ie
        gameThread = new GameThread(dm, getBaseContext());
        renderer.connectToGameThread(gameThread);
        
        // Luodaan TouchManager ja HUD
        hud          = new Hud(getBaseContext());
        touchManager = new TouchManager(dm, surfaceView, getBaseContext(), hud);
    }
    
    /**
     * Palauttaa pelin taukotilasta.
     */
    @Override
    protected void onResume()
    {
        super.onResume();
        
        surfaceView.onResume();
        
        // Pys�ytet��n s�ie
        boolean retry = true;
        gameThread.setRunning(false);
        while (retry) {
            try {
                gameThread.join();
                retry = false;
            }
            catch (InterruptedException e) {
                // Yritet��n uudelleen kunnes onnistuu
            }
        }
    }
    
    /**
     * Siirt�� pelin taukotilaan. 
     */
    @Override
    protected void onPause()
    {
        super.onPause();
        
        surfaceView.onPause();
        
        // Pys�ytet��n s�ie
        boolean retry = true;
        gameThread.setRunning(false);
        while (retry) {
            try {
                gameThread.join();
                retry = false;
            }
            catch (InterruptedException e) {
                // Yritet��n uudelleen kunnes onnistuu
            }
        }
    }
}
