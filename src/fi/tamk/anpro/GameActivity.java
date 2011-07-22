package fi.tamk.anpro;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.PixelFormat;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.WindowManager;
import android.media.AudioManager;

/**
 * Pelitilan alkupiste. Luo render�ij�n ja pelis�ikeen. Hallitsee my�s
 * pelin keskeytt�misen ja palautumisen.
 * 
 * @extends Activity
 */
public class GameActivity extends Activity
{
    /* Render�ij� ja OpenGL-pinta */
    private GLSurfaceView surfaceView;
    private GLRenderer    renderer;
    
    /* Tarvittavat luokat */
    private GameThread      gameThread;
    private WeaponManager   weaponManager;
    private InputController inputController;
    
    /* =======================================================
     * Perityt funktiot
     * ======================================================= */
    /**
     * M��ritt�� asetukset ja luo tarvittavat oliot, kuten render�ij�n, HUDin,
     * GameThreadin ja TouchManagerin. Android kutsuu t�t� automaattisesti.
     * 
     * @param Bundle Pelin aiempi tila
     */
    @Override
    public void onCreate(Bundle _savedInstanceState)
    {
        super.onCreate(_savedInstanceState);

        // Piiloitetaan otsikko ja vaihdetaan kokoruuduntilaan
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                             WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        
        // Asetetaan ��nens��t�napit muuttamaan media volumea
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
    }
    
    /**
     * Android kutsuu t�t� automaattisesti onCreaten j�lkeen.
     */
    @Override
    protected void onStart()
    {
        super.onStart();
        
        // Luodaan InputController, mik�li laitteessa on sellainen
        if (Options.controlType != Options.CONTROLS_NONAV && Options.controlType != Options.CONTROLS_UNDEFINED) {
        	inputController = new InputController();
        }
        
        // Ladataan n�yt�n tiedot
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        
        // Luodaan OpenGL-pinta ja render�ij�
        surfaceView = new GLSurfaceView(this);
        renderer    = new GLRenderer(this, surfaceView, dm);
        
        // Luodaan ja k�ynnistet��n pelin s�ie
        gameThread = new GameThread(dm, getBaseContext(), this, renderer, surfaceView);
        renderer.connectToGameThread(gameThread);
        
        // M��ritet��n render�ij�n asetukset ja otetaan se k�ytt��n
        surfaceView.setEGLConfigChooser(8, 8, 8, 8, 0, 0);
        surfaceView.getHolder().setFormat(PixelFormat.TRANSLUCENT);
        surfaceView.setRenderer(renderer);
        
        // Asetetaan k�ytett�v� pinta
        setContentView(surfaceView);
        
        // K�ynnistet��n pelis�ie
        gameThread.setRunning(true);
        gameThread.start();
    }
        
    /**
     * Android kutsuu t�t� automaattisesti kun ohjelma k�ynnistyy uudelleen.
     */
    @Override
    protected void onRestart()
    {
        super.onRestart();
        // TODO: Tee toteutus
    }
    
    /**
     * Android kutsuu t�t� automaattisesti kun ohjelma palaa taukotilasta.
     */
    @Override
    protected void onResume()
    {
        super.onResume();
        
        gameThread.setRunning(true);
    }
    
    /**
     * Siirt�� pelin taukotilaan ja tallentaa ohjelman tilan, sill� onPausen j�lkeen prosessi
     * saatetaan keskeytt�� kokonaan. Android kutsuu t�t� automaattisesti.
     */
    @Override
    protected void onPause()
    {
        super.onPause();
    }
        
    /**
     * Tallentaa ohjelman tilan, sill� onStopin j�lkeen prosessi saatetaan keskeytt�� kokonaan.
     * Android kutsuu t�t� automaattisesti kun ohjelma ei ole en�� aktiivinen.
     */
    @Override
    protected void onStop()
    {
        super.onStop();
        
        Wrapper.destroy();
        CameraManager.destroy();
        EffectManager.destroy();
        gameThread.interrupt();
        gameThread    = null;
        surfaceView   = null;
        renderer      = null;
        weaponManager = null;
    }
        
    /**
     * Android kutsuu t�t� automaattisesti kun ohjelma lopetetaan kokonaan.
     */
    @Override
    protected void onDestroy()
    {
        super.onDestroy();
    }
    
    /* =======================================================
     * Uudet funktiot
     * ======================================================= */    
    /**
     * Ottaa pelaajan pisteet vastaan parametrina, l�hett�� pisteet 
     * ja siirtyy HighScoresActivityyn.
     * 
     * @param int Pelitilan pisteet
     */
    public void continueToHighscores(int _score)
	{
		Intent i_highscores = new Intent(this, HighScoresActivity.class);
		
		// Luodaan uusi Bundle
		Bundle bundle = new Bundle();
		
		// L�hetet��n pelaajan pisteet Bundlessa HighScoresActivitylle
		bundle.putInt("Scores", _score);
		i_highscores.putExtras(bundle);
		
		// K�ynnistet��n HighScoresActivity ja sammutetaan GameActivity
		startActivity(i_highscores);
		finish();
	}
    
    /**
     * Ottaa pelaajan pisteet parametrina, k�ynnist�� MothershipActivityn ja
     * l�hett�� pelaajan pisteet Bundlena.
     * 
     * @param int _score Pelaajan pisteet
     */
    public void continueToMothership(int _score)
    {
    	Intent i_mothership = new Intent(this, MothershipActivity.class);
    	
    	// Luodaan uusi bundle
    	Bundle bundle = new Bundle();
    	
    	// L�hetet��n pelaajan pisteet Bundlessa MothershipActivitylle
    	bundle.putInt("Score", _score);
    	i_mothership.putExtras(bundle);
		
		startActivity(i_mothership);
    }
	
	@Override
	public boolean onKeyDown(int _keyCode, KeyEvent _event)
	{
		if (_keyCode == KeyEvent.KEYCODE_BACK && _event.getRepeatCount() == 0) {
			gameThread.setRunning(false); // TODO: Wat..?
	        Intent i_pausemenu = new Intent(this, PauseMenuActivity.class);
	        startActivityIfNeeded(i_pausemenu, 1);
			return true;
	    }
		else if (_keyCode == KeyEvent.KEYCODE_HOME && _event.getRepeatCount() == 0) {
			gameThread.setRunning(false); // TODO: Wat..?
			Intent i_mainmenu = new Intent(this, MainActivity.class);
			startActivity(i_mainmenu);
			finish();
			return true;
		}
		
	    return inputController.handleKeyDown(_keyCode, _event); 
	}
	
	@Override
	public boolean onKeyUp(int _keyCode, KeyEvent _event)
	{
		if (_keyCode == KeyEvent.KEYCODE_BACK && _event.getRepeatCount() == 0) {
	        return true;
	    }
		
	    return inputController.handleKeyUp(_keyCode, _event); 
	}
	
	@Override
	protected void onActivityResult(int _requestCode, int _resultCode, Intent _data)
	{
		super.onActivityResult(_requestCode, _resultCode, _data);
		
		if (_resultCode == RESULT_CANCELED) {
			finish();
		}
		else {
			gameThread.setRunning(false); // TODO: Wat..?
		}
	}
}
