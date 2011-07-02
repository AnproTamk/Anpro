package fi.tamk.anpro;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.ActivityInfo;
import android.media.AudioManager;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
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
    private SharedPreferences mPrefs;
    
    /* Valintanapit asetuksille */
    private CheckBox particleCheckBox;
    private CheckBox musicCheckBox;
    private CheckBox soundCheckBox;

	// TODO: Javadoc-kommentit
    @Override
    public void onCreate(Bundle _savedInstanceState)
    {	
        super.onCreate(_savedInstanceState);
        
        // Asetetaan aktiviteetti koko näytölle
        requestWindowFeature(Window.FEATURE_NO_TITLE);
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
        
        // Ladataan Options käyttöön ja asetetaan asetukset
        Options.getInstance().scaleConversion(dm.widthPixels, dm.heightPixels);
        Options.particles = true;
        Options.music     = true;
        Options.sounds    = false;
        
        // Määritetään aktiviteetin asetukset
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
        
        // Ladataan valikon painikkeet käyttöön
        View storyButton = findViewById(R.id.button_story);
        storyButton.setOnClickListener(this);
        
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
    }

	// TODO: Javadoc-kommentit
    public void onClick(View _v)
    {
        switch(_v.getId()) {
            case R.id.button_story:
                SoundManager.playSound(SoundManager.SOUND_BUTTONCLICK, 1);
                Intent i_story = new Intent(this, LevelSelectActivity.class);
                startActivity(i_story);
                super.onStop();
                break;
                
            case R.id.button_survival:
                SoundManager.playSound(SoundManager.SOUND_BUTTONCLICK, 1);
                Intent i_game = new Intent(this, GameActivity.class);
                startActivity(i_game);
                super.onStop();
                break;
                
            case R.id.button_highscores:
                SoundManager.playSound(SoundManager.SOUND_BUTTONCLICK, 1);
                Intent i_highscores = new Intent(this, HighScoresActivity.class);
                startActivity(i_highscores);
                super.onStop();
                break;
                
            case R.id.button_help:
            	SoundManager.playSound(SoundManager.SOUND_BUTTONCLICK, 1);
                Intent i_help = new Intent(this, AboutActivity.class);
                startActivity(i_help);
                super.onPause();
                break;
                
            case R.id.button_quit:
            	SoundManager.cleanUp();
                android.os.Process.killProcess(android.os.Process.myPid());
                break;
        }
    }

	// TODO: Javadoc-kommentit (voi kopioida GameActivitysta)
    @Override
    protected void onResume() {
    	particleCheckBox.setChecked(mPrefs.getBoolean(PREF_BOOL_PAR, true));
        musicCheckBox.setChecked(mPrefs.getBoolean(PREF_BOOL_MUS, true));
        soundCheckBox.setChecked(mPrefs.getBoolean(PREF_BOOL_SOU, true));
        super.onResume();
    }

	// TODO: Javadoc-kommentit (voi kopioida GameActivitysta)
    @Override
    protected void onPause() {
    	Editor e = mPrefs.edit();
    	e.putBoolean(PREF_BOOL_PAR, particleCheckBox.isChecked());
    	e.putBoolean(PREF_BOOL_MUS, musicCheckBox.isChecked());
    	e.putBoolean(PREF_BOOL_SOU, soundCheckBox.isChecked());
    	e.commit();
        
    	Toast.makeText(this, "Settings Saved.", Toast.LENGTH_SHORT).show();
    	super.onPause();
    }

	// TODO: Javadoc-kommentit (voi kopioida GameActivitysta)
    @Override
    protected void onStop() {
    	super.onStop();
    }
}
