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
 * Pelitilan alkupiste. Luo renderöijän ja pelisäikeen. Hallitsee myös
 * pelin keskeyttämisen ja palautumisen.
 * 
 * @extends Activity
 */
public class GameActivity extends Activity
{
    /* Renderöijä ja OpenGL-pinta */
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
     * Määrittää asetukset ja luo tarvittavat oliot, kuten renderöijän, HUDin,
     * GameThreadin ja TouchManagerin. Android kutsuu tätä automaattisesti.
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
        
        // Asetetaan äänensäätönapit muuttamaan media volumea
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
    }
    
    /**
     * Android kutsuu tätä automaattisesti onCreaten jälkeen.
     */
    @Override
    protected void onStart()
    {
        super.onStart();
        
        // Luodaan InputController, mikäli laitteessa on sellainen
        if (Options.controlType != Options.CONTROLS_NONAV && Options.controlType != Options.CONTROLS_UNDEFINED) {
        	inputController = new InputController();
        }
        
        // Ladataan näytön tiedot
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        
        // Luodaan OpenGL-pinta ja renderöijä
        surfaceView = new GLSurfaceView(this);
        renderer    = new GLRenderer(this, surfaceView, dm);
        
        // Luodaan ja käynnistetään pelin säie
        gameThread = new GameThread(dm, getBaseContext(), this, renderer, surfaceView);
        renderer.connectToGameThread(gameThread);
        
        // Määritetään renderöijän asetukset ja otetaan se käyttöön
        surfaceView.setEGLConfigChooser(8, 8, 8, 8, 0, 0);
        surfaceView.getHolder().setFormat(PixelFormat.TRANSLUCENT);
        surfaceView.setRenderer(renderer);
        
        // Asetetaan käytettävä pinta
        setContentView(surfaceView);
        
        // Käynnistetään pelisäie
        gameThread.setRunning(true);
        gameThread.start();
    }
        
    /**
     * Android kutsuu tätä automaattisesti kun ohjelma käynnistyy uudelleen.
     */
    @Override
    protected void onRestart()
    {
        super.onRestart();
        // TODO: Tee toteutus
    }
    
    /**
     * Android kutsuu tätä automaattisesti kun ohjelma palaa taukotilasta.
     */
    @Override
    protected void onResume()
    {
        super.onResume();
        
        gameThread.setRunning(true);
    }
    
    /**
     * Siirtää pelin taukotilaan ja tallentaa ohjelman tilan, sillä onPausen jälkeen prosessi
     * saatetaan keskeyttää kokonaan. Android kutsuu tätä automaattisesti.
     */
    @Override
    protected void onPause()
    {
        super.onPause();
    }
        
    /**
     * Tallentaa ohjelman tilan, sillä onStopin jälkeen prosessi saatetaan keskeyttää kokonaan.
     * Android kutsuu tätä automaattisesti kun ohjelma ei ole enää aktiivinen.
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
     * Android kutsuu tätä automaattisesti kun ohjelma lopetetaan kokonaan.
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
     * Ottaa pelaajan pisteet vastaan parametrina, lähettää pisteet 
     * ja siirtyy HighScoresActivityyn.
     * 
     * @param int Pelitilan pisteet
     */
    public void continueToHighscores(int _score)
	{
		Intent i_highscores = new Intent(this, HighScoresActivity.class);
		
		// Luodaan uusi Bundle
		Bundle bundle = new Bundle();
		
		// Lähetetään pelaajan pisteet Bundlessa HighScoresActivitylle
		bundle.putInt("Scores", _score);
		i_highscores.putExtras(bundle);
		
		// Käynnistetään HighScoresActivity ja sammutetaan GameActivity
		startActivity(i_highscores);
		finish();
	}
    
    /**
     * Ottaa pelaajan pisteet parametrina, käynnistää MothershipActivityn ja
     * lähettää pelaajan pisteet Bundlena.
     * 
     * @param int _score Pelaajan pisteet
     */
    public void continueToMothership(int _score)
    {
    	Intent i_mothership = new Intent(this, MothershipActivity.class);
    	
    	// Luodaan uusi bundle
    	Bundle bundle = new Bundle();
    	
    	// Lähetetään pelaajan pisteet Bundlessa MothershipActivitylle
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
