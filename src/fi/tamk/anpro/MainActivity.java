package fi.tamk.anpro;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.media.AudioManager;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.CheckBox;

public class MainActivity extends Activity implements OnClickListener
{
    public static Context context;

	/**
	 * Luo p��valikon ja aloittaa koko pelin. Android kutsuu t�t� automaattisesti.
	 * 
	 * @param Bundle Pelin aiempi tila
	 */
    @Override
    public void onCreate(Bundle _savedInstanceState)
    {	
        super.onCreate(_savedInstanceState);
        
        // Asetetaan aktiviteetti koko n�yt�lle
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
                                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        
        setContentView(R.layout.main);
        
        context = getApplicationContext();

        // Ladataan n�yt�n tiedot
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        // Ladataan SoundManager k�ytt��n ja alustetaan se
        SoundManager.getInstance();
        SoundManager.initSounds(this);
        
        // Ladataan laitteen ominaisuudet
        Configuration config = getResources().getConfiguration();
        Options.initialize(config, getPackageManager(), dm);
       
        // Ladataan VibrateManager k�ytt��n
        VibrateManager.getInstance();
        
        // M��ritet��n aktiviteetin asetukset
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
        
        // Ladataan valikon painikkeet k�ytt��n
        View survivalButton = findViewById(R.id.button_survival);
        survivalButton.setOnClickListener(this);
        
        View helpButton = findViewById(R.id.button_help);
        helpButton.setOnClickListener(this);
        
        View highscoresButton = findViewById(R.id.button_highscores);
        highscoresButton.setOnClickListener(this);
        
        View settingsButton = findViewById(R.id.button_settings);
        settingsButton.setOnClickListener(this);
        
        View quitButton = findViewById(R.id.button_quit);
        quitButton.setOnClickListener(this);

        

    }

	/**
	 * K�sittelee nappuloiden painamisen.
	 * 
	 * @param View Nappi jota painettiin
	 */
    public void onClick(View _v)
    {
        switch(_v.getId()) {                
            case R.id.button_survival:
                SoundManager.playSound(SoundManager.SOUND_BUTTONCLICK, 1);
                Intent i_game = new Intent(this, GameActivity.class);
                startActivity(i_game);
                finish();
                break;
                
            case R.id.button_highscores:
                SoundManager.playSound(SoundManager.SOUND_BUTTONCLICK, 1);
                Intent i_highscores = new Intent(this, HighScoresActivity.class);
                startActivity(i_highscores);
                finish();
                break;
            
            case R.id.button_settings:
            	SoundManager.playSound(SoundManager.SOUND_BUTTONCLICK, 1);
                Intent i_settings = new Intent(this, SettingsActivity.class);
                startActivity(i_settings);
                finish();
                break;
                
            case R.id.button_help:
            	SoundManager.playSound(SoundManager.SOUND_BUTTONCLICK, 1);
                Intent i_help = new Intent(this, AboutActivity.class);
                startActivity(i_help);
                break;
                
            case R.id.button_quit:
            	SoundManager.cleanUp();
                android.os.Process.killProcess(android.os.Process.myPid());
                finish();
                break;
        }
    }
    
    /**
     * Android kutsuu t�t� automaattisesti kun ohjelma palaa taukotilasta.
     */
    @Override
    protected void onResume() {
        super.onResume();
    }
    
    /**
     * Siirt�� pelin taukotilaan ja tallentaa ohjelman tilan, sill� onPausen j�lkeen prosessi
     * saatetaan keskeytt�� kokonaan. Android kutsuu t�t� automaattisesti.
     */
    @Override
    protected void onPause() {

    	super.onPause();
    }
        
    /**
     * Tallentaa ohjelman tilan, sill� onStopin j�lkeen prosessi saatetaan keskeytt�� kokonaan.
     * Android kutsuu t�t� automaattisesti kun ohjelma ei ole en�� aktiivinen.
     */
    @Override
    protected void onStop() {
    	super.onStop();
    }
}
