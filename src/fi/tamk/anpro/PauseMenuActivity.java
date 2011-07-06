package fi.tamk.anpro;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.Toast;
import android.media.AudioManager;

/**
 * Sis‰lt‰‰ Pause-valikon toteutuksen. Pause-valikko tunnistaa pelitilan ja
 * tarjoaa toiminnot sen mukaisesti.
 */
public class PauseMenuActivity extends Activity implements OnClickListener
{
	
	private CheckBox musicCheckBox;
    private CheckBox soundCheckBox;
    
    public static final String PREFS_NAME = "SharedPrefs";
    public static final String PREF_STRING = "PrefString";
    public static final String PREF_BOOL_MUS = "PrefBoolMUS";
    public static final String PREF_BOOL_SOU = "PrefBoolSOU";
    private SharedPreferences mPrefs;
	
	/**
	 * Luo Pause-valikon. Android kutsuu t‰t‰ automaattisesti.
	 * 
	 * @param Bundle Pelin aiempi tila
	 */
	@Override
	public void onCreate(Bundle _savedInstanceState)
	{
		
		boolean storyMode = false;
		
		mPrefs = getSharedPreferences(PREFS_NAME,0);

		// TODO: Pelitilan voi tarkistaa joko GameActivitysta (pit‰isi siirt‰‰ Optionsiin?)
		if(storyMode == true) {
			super.onCreate(_savedInstanceState);
			setContentView(R.layout.pausemenu_survival);
	        
	        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
	        
	        // Asetetaan ‰‰nens‰‰tˆnapit muuttamaan media volumea
	        setVolumeControlStream(AudioManager.STREAM_MUSIC);
	        
	        View levelselectButton = findViewById(R.id.button_level_select);
	        levelselectButton.setOnClickListener(this);
		}
		
		
		else if(storyMode == false) {
			super.onCreate(_savedInstanceState);
			setContentView(R.layout.pausemenu_survival);
		
		}
		
		View mainmenuButton = findViewById(R.id.button_main_menu);
        mainmenuButton.setOnClickListener(this);
        
        View resumeButton = findViewById(R.id.button_resume);
        resumeButton.setOnClickListener(this);
        
        musicCheckBox = (CheckBox) findViewById(R.id.checkBoxMusic);
        musicCheckBox.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                // Suorita toiminto klikatessa, riippuen onko nappula ruksattu
                if (((CheckBox) v).isChecked()) {
                	SoundManager.playSound(SoundManager.SOUND_BUTTONCLICK, 1);
                    Toast.makeText(PauseMenuActivity.this, "Music Enabled", Toast.LENGTH_SHORT).show();
                    Options.music = true;
                    
                } else {
                	SoundManager.playSound(SoundManager.SOUND_BUTTONCLICK, 1);
                    Toast.makeText(PauseMenuActivity.this, "Music Disabled", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(PauseMenuActivity.this, "Sounds Enabled", Toast.LENGTH_SHORT).show();
                    Options.sounds = true;
                } else {
                	SoundManager.playSound(SoundManager.SOUND_BUTTONCLICK, 1);
                    Toast.makeText(PauseMenuActivity.this, "Sounds Disabled", Toast.LENGTH_SHORT).show();
                    Options.sounds = false;
                }
            }
        });
	}
	
	/**
	 * K‰sittelee nappuloiden painamisen.
	 * 
	 * @param View Nappi jota painettiin
	 */
	public void onClick(View _v)
	{
		if(_v.getId() == R.id.button_main_menu) {
			Intent i_mainmenu = new Intent(this, MainActivity.class);
    		startActivity(i_mainmenu);
    		onStop();
    		onDestroy();
		}

    	else if(_v.getId() == R.id.button_resume) {
    		onStop();
    	}
	}
	
    /**
     * Android kutsuu t‰t‰ automaattisesti kun ohjelma palaa taukotilasta.
     */
    @Override
    protected void onResume() {
        musicCheckBox.setChecked(mPrefs.getBoolean(PREF_BOOL_MUS, true));
        soundCheckBox.setChecked(mPrefs.getBoolean(PREF_BOOL_SOU, true));
        super.onResume();
    }
    
    /**
     * Siirt‰‰ pelin taukotilaan ja tallentaa ohjelman tilan, sill‰ onPausen j‰lkeen prosessi
     * saatetaan keskeytt‰‰ kokonaan. Android kutsuu t‰t‰ automaattisesti.
     */
    @Override
    protected void onPause() {
    	Editor e = mPrefs.edit();
    	e.putBoolean(PREF_BOOL_MUS, musicCheckBox.isChecked());
    	e.putBoolean(PREF_BOOL_SOU, soundCheckBox.isChecked());
    	e.commit();
        
    	Toast.makeText(this, "Settings Saved.", Toast.LENGTH_SHORT).show();
    	super.onPause();
    }
        
    /**
     * Tallentaa ohjelman tilan, sill‰ onStopin j‰lkeen prosessi saatetaan keskeytt‰‰ kokonaan.
     * Android kutsuu t‰t‰ automaattisesti kun ohjelma ei ole en‰‰ aktiivinen.
     */
    @Override
    protected void onStop() {
    	super.onStop();
    }
}
