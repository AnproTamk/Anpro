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
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener
{
    public static Context context;

	// TODO: Kommentit
    // TODO: Kaikkia näistä ei käytetä. Vältetään tallentamista, mikäli käytetään vain kerran.
    public static final String PREFS_NAME = "SharedPrefs";
    public static final String PREF_STRING = "PrefString";
    public static final String PREF_BOOL_PAR = "PrefBoolPar";
    public static final String PREF_BOOL_MUS = "PrefBoolMUS";
    public static final String PREF_BOOL_SOU = "PrefBoolSOU";
    public static final String PREF_BOOL_VIB = "PrefBoolVib";
    private SharedPreferences mPrefs;
    
    /* Valintanapit asetuksille */
    private CheckBox particleCheckBox;
    private CheckBox musicCheckBox;
    private CheckBox soundCheckBox;
    private CheckBox vibrationCheckBox;

	/**
	 * Luo päävalikon ja aloittaa koko pelin. Android kutsuu tätä automaattisesti.
	 * 
	 * @param Bundle Pelin aiempi tila
	 */
    @Override
    public void onCreate(Bundle _savedInstanceState)
    {	
        super.onCreate(_savedInstanceState);
        
        // Asetetaan aktiviteetti koko näytölle
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
                                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        
        setContentView(R.layout.main);
        
        context = getApplicationContext();
        mPrefs = getSharedPreferences(PREFS_NAME,0);

        // Ladataan näytön tiedot
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        // Ladataan SoundManager käyttöön ja alustetaan se
        SoundManager.getInstance();
        SoundManager.initSounds(this);
        
        // Ladataan laitteen ominaisuudet
        Configuration config = getResources().getConfiguration();
        Options.initialize(config, getPackageManager(), dm);
       
        // Ladataan VibrateManager käyttöön
        VibrateManager.getInstance();
        
        // Määritetään aktiviteetin asetukset
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
        
        // Ladataan valikon painikkeet käyttöön
        View survivalButton = findViewById(R.id.button_survival);
        survivalButton.setOnClickListener(this);
        
        View helpButton = findViewById(R.id.button_help);
        helpButton.setOnClickListener(this);
        
        View highscoresButton = findViewById(R.id.button_highscores);
        highscoresButton.setOnClickListener(this);
        
        View quitButton = findViewById(R.id.button_quit);
        quitButton.setOnClickListener(this);
        
        particleCheckBox = (CheckBox) findViewById(R.id.checkBoxParticle);
        particleCheckBox.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
            	// Suorita toiminto klikatessa, riippuen onko nappula ruksattu
                if (((CheckBox) v).isChecked()) {
                	SoundManager.playSound(SoundManager.SOUND_BUTTONCLICK, 1);
                    Toast.makeText(MainActivity.this, "Particles Enabled", Toast.LENGTH_SHORT).show();
                    Options.particles = true;
                } else {
                	SoundManager.playSound(SoundManager.SOUND_BUTTONCLICK, 1);
                    Toast.makeText(MainActivity.this, "Particles Disabled", Toast.LENGTH_SHORT).show();
                    Options.particles = false;
                }
            }
        });

        musicCheckBox = (CheckBox) findViewById(R.id.checkBoxMusic);
        musicCheckBox.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                // Suorita toiminto klikatessa, riippuen onko nappula ruksattu
                if (((CheckBox) v).isChecked()) {
                	SoundManager.playSound(SoundManager.SOUND_BUTTONCLICK, 1);
                    Toast.makeText(MainActivity.this, "Music Enabled", Toast.LENGTH_SHORT).show();
                    Options.music = true;
                    
                } else {
                	SoundManager.playSound(SoundManager.SOUND_BUTTONCLICK, 1);
                    Toast.makeText(MainActivity.this, "Music Disabled", Toast.LENGTH_SHORT).show();
                    Options.music = false;
                }
            }
        });
        
        soundCheckBox = (CheckBox) findViewById(R.id.checkBoxSound);
        soundCheckBox.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
            	// Suorita toiminto klikatessa, riippuen onko nappula ruksattu
                if (((CheckBox) v).isChecked()) {
                	SoundManager.playSound(SoundManager.SOUND_BUTTONCLICK, 1);
                    Toast.makeText(MainActivity.this, "Sounds Enabled", Toast.LENGTH_SHORT).show();
                    Options.sounds = true;
                } else {
                	SoundManager.playSound(SoundManager.SOUND_BUTTONCLICK, 1);
                    Toast.makeText(MainActivity.this, "Sounds Disabled", Toast.LENGTH_SHORT).show();
                    Options.sounds = false;
                }
            }
        });
        
        vibrationCheckBox = (CheckBox) findViewById(R.id.checkBoxVibration);
        vibrationCheckBox.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
            	// Suorita toiminto klikatessa, riippuen onko nappula ruksattu
                if (((CheckBox) v).isChecked()) {
                	SoundManager.playSound(SoundManager.SOUND_BUTTONCLICK, 1);
                    Toast.makeText(MainActivity.this, "Vibrations Enabled", Toast.LENGTH_SHORT).show();
                    Options.vibration = true;
                } else {
                	SoundManager.playSound(SoundManager.SOUND_BUTTONCLICK, 1);
                    Toast.makeText(MainActivity.this, "Vibrations Disabled", Toast.LENGTH_SHORT).show();
                    Options.vibration = false;
                }
            }
        });
    }

	/**
	 * Käsittelee nappuloiden painamisen.
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
     * Android kutsuu tätä automaattisesti kun ohjelma palaa taukotilasta.
     */
    @Override
    protected void onResume() {
    	particleCheckBox.setChecked(mPrefs.getBoolean(PREF_BOOL_PAR, true));
        musicCheckBox.setChecked(mPrefs.getBoolean(PREF_BOOL_MUS, true));
        soundCheckBox.setChecked(mPrefs.getBoolean(PREF_BOOL_SOU, true));
        vibrationCheckBox.setChecked(mPrefs.getBoolean(PREF_BOOL_VIB, true));
        super.onResume();
    }
    
    /**
     * Siirtää pelin taukotilaan ja tallentaa ohjelman tilan, sillä onPausen jälkeen prosessi
     * saatetaan keskeyttää kokonaan. Android kutsuu tätä automaattisesti.
     */
    @Override
    protected void onPause() {
    	Editor e = mPrefs.edit();
    	e.putBoolean(PREF_BOOL_PAR, particleCheckBox.isChecked());
    	e.putBoolean(PREF_BOOL_MUS, musicCheckBox.isChecked());
    	e.putBoolean(PREF_BOOL_SOU, soundCheckBox.isChecked());
    	e.putBoolean(PREF_BOOL_SOU, vibrationCheckBox.isChecked());
    	e.commit();
        
    	super.onPause();
    }
        
    /**
     * Tallentaa ohjelman tilan, sillä onStopin jälkeen prosessi saatetaan keskeyttää kokonaan.
     * Android kutsuu tätä automaattisesti kun ohjelma ei ole enää aktiivinen.
     */
    @Override
    protected void onStop() {
    	super.onStop();
    }
}
