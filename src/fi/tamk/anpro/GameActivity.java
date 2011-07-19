package fi.tamk.anpro;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.PixelFormat;
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
    private CustomSurfaceView surfaceView;
    private GLRenderer    renderer;
    
    /* Tarvittavat luokat */
    private GameThread      gameThread;
    private WeaponManager   weaponManager;
    private InputController inputController;
    
    /* Aktiivinen pelitila (asetetaan päävalikossa) */
    public static int activeMode = 1;
        
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

        // Ladataan näytön tiedot
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        
        // Luodaan OpenGL-pinta ja renderöijä
        surfaceView = new CustomSurfaceView(this);
        renderer    = new GLRenderer(this, surfaceView, dm);
        
        // Määritetään renderöijän asetukset ja otetaan se käyttöön
        surfaceView.setEGLConfigChooser(8, 8, 8, 8, 0, 0);
        surfaceView.getHolder().setFormat(PixelFormat.TRANSLUCENT);
        surfaceView.setRenderer(renderer);
        
        // Asetetaan käytettävä pinta
        setContentView(surfaceView);

        // TODO: Pitää tarkistaa mikä pelitila on käynnistettävä
    	// Luodaan WeaponManager
    	weaponManager = new WeaponManager();
        weaponManager.initialize(GameActivity.activeMode);
        
        // Luodaan InputController, mikäli laitteessa on sellainen
        if (Options.controlType != Options.CONTROLS_NONAV && Options.controlType != Options.CONTROLS_UNDEFINED) {
        	inputController = new InputController();
        }

        // Luodaan ja käynnistetään pelin säie
        gameThread = new GameThread(dm, getBaseContext(), this, weaponManager, surfaceView);
        renderer.connectToGameThread(gameThread);
    }
        
    /**
     * Android kutsuu tätä automaattisesti onCreaten jälkeen.
     */
    @Override
    protected void onStart()
    {
        super.onStart();
        // TODO: Tee toteutus
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
        
        surfaceView.onResume();
    }
    
    /**
     * Siirtää pelin taukotilaan ja tallentaa ohjelman tilan, sillä onPausen jälkeen prosessi
     * saatetaan keskeyttää kokonaan. Android kutsuu tätä automaattisesti.
     */
    @Override
    protected void onPause()
    {
        super.onPause();
        
        surfaceView.onPause();
        
        // Pysäytetään säie
        gameThread.setRunning(false);
        /*boolean retry = true;
        while (retry) {
            try {
                gameThread.join();
                retry = false;
            }
            catch (InterruptedException e) {
                // Yritetään uudelleen kunnes onnistuu
            }
        }*/
    }
        
    /**
     * Tallentaa ohjelman tilan, sillä onStopin jälkeen prosessi saatetaan keskeyttää kokonaan.
     * Android kutsuu tätä automaattisesti kun ohjelma ei ole enää aktiivinen.
     */
    @Override
    protected void onStop()
    {
        super.onStop();
        // TODO: Tee toteutus
    }
        
    /**
     * Android kutsuu tätä automaattisesti kun ohjelma lopetetaan kokonaan.
     */
    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        
        Wrapper.destroy();
        gameThread    = null;
        renderer      = null;
        weaponManager = null;
    }
    
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
     * 
     * 
     * @param TODO:PARAMETRIT!
     */
    public void continueToMothership()
    {
    	Intent i_mothership = new Intent(this, MothershipActivity.class);
		
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
	        gameThread.setRunning(true);
		}
	}
}
