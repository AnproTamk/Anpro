package fi.tamk.anpro;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.PixelFormat;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Window;
import android.view.WindowManager;
import android.media.AudioManager;

/**
 * Pelitilan alkupiste. Luo renderˆij‰n, pelis‰ikeen, HUDin ja TouchManagerin.
 * 
 * @extends Activity
 */
public class GameActivity extends Activity
{
    /* Pelitilat */
    public static final int SURVIVAL_MODE = 1;
    public static final int STORY_MODE    = 2;
    
    /* Renderˆij‰ ja OpenGL-pinta */
    private GLSurfaceView surfaceView;
    private GLRenderer    renderer;
    
    /* Muut luokat */
    private GameThread   gameThread;
    
    /* Aktiivinen pelitila (asetetaan p‰‰valikossa) */
    public static int activeMode = 1;
    
    private static Context context;
        
    /**
     * M‰‰ritt‰‰ asetukset ja luo tarvittavat oliot, kuten renderˆij‰n, HUDin,
     * GameThreadin ja TouchManagerin. Android kutsuu t‰t‰ automaattisesti.
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
        
        // Asetetaan ‰‰nens‰‰tˆnapit muuttamaan media volumea
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
        
        context = getBaseContext();

        // Ladataan n‰ytˆn tiedot
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        
        // Luodaan OpenGL-pinta ja renderˆij‰
        surfaceView = new GLSurfaceView(this);
        renderer    = new GLRenderer(this, surfaceView, getResources(), dm);
        
        // M‰‰ritet‰‰n renderˆij‰n asetukset ja otetaan se k‰yttˆˆn
        surfaceView.setEGLConfigChooser(8, 8, 8, 8, 0, 0);
        surfaceView.getHolder().setFormat(PixelFormat.TRANSLUCENT);
        surfaceView.setRenderer(renderer);
        
        // Asetetaan k‰ytett‰v‰ pinta
        setContentView(surfaceView);
        
        // Luodaan ja k‰ynnistet‰‰n pelin s‰ie
        gameThread = new GameThread(dm, getBaseContext(), surfaceView, this);
        renderer.connectToGameThread(gameThread);
    }
        
    /**
     * Android kutsuu t‰t‰ automaattisesti onCreaten j‰lkeen.
     */
    @Override
    protected void onStart()
    {
        super.onStart();
        // TODO: Tee toteutus
    }
        
    /**
     * Android kutsuu t‰t‰ automaattisesti kun ohjelma k‰ynnistyy uudelleen.
     */
    @Override
    protected void onRestart()
    {
        super.onRestart();
        // TODO: Tee toteutus
    }
    
    /**
     * Palauttaa pelin taukotilasta. Android kutsuu t‰t‰ automaattisesti.
     */
    @Override
    protected void onResume()
    {
        super.onResume();
        
        surfaceView.onResume();
        
        // Pys‰ytet‰‰n s‰ie
        boolean retry = true;
        gameThread.setRunning(false);
        while (retry) {
            try {
                gameThread.join();
                retry = false;
            }
            catch (InterruptedException e) {
                // Yritet‰‰n uudelleen kunnes onnistuu
            }
        }
    }
    
    /**
     * Siirt‰‰ pelin taukotilaan ja tallentaa ohjelman tilan, sill‰ onPausen j‰lkeen prosessi
     * saatetaan keskeytt‰‰ kokonaan. Android kutsuu t‰t‰ automaattisesti.
     */
    @Override
    protected void onPause()
    {
        super.onPause();
        
        surfaceView.onPause();
        
        // Pys‰ytet‰‰n s‰ie
        boolean retry = true;
        gameThread.setRunning(false);
        while (retry) {
            try {
                gameThread.join();
                retry = false;
            }
            catch (InterruptedException e) {
                // Yritet‰‰n uudelleen kunnes onnistuu
            }
        }
    }
        
    /**
     * Tallentaa ohjelman tilan, sill‰ onStopin j‰lkeen prosessi saatetaan keskeytt‰‰ kokonaan.
     * Android kutsuu t‰t‰ automaattisesti kun ohjelma ei ole en‰‰ aktiivinen.
     */
    @Override
    protected void onStop()
    {
        super.onStop();
        // TODO: Tee toteutus
    }
        
    /**
     * Android kutsuu t‰t‰ automaattisesti kun ohjelma lopetetaan kokonaan.
     */
    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        // TODO: Tee toteutus
    }
    
    /**
     * Ottaa pelaajan pisteet vastaan parametrina, l‰hett‰‰ pisteet 
     * ja siirtyy HighScoresActivityyn.
     * 
     * @param score
     */
	public void continueToHighscores(long _score) {
		
		
		Intent i_highscores = new Intent(this, HighScoresActivity.class);
		
		// Luodaan uusi Bundle
		Bundle bundle = new Bundle();
		
		// L‰hetet‰‰n pelaajan pisteet Bundlessa HighScoresActivitylle
		bundle.putLong("Scores", _score);
		i_highscores.putExtras(bundle);
		
		startActivity(i_highscores);
		finish();
	}
}
