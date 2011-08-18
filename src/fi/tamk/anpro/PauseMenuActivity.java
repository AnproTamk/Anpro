package fi.tamk.anpro;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.media.AudioManager;

/**
 * Sis‰lt‰‰ Pause-valikon toteutuksen. Pause-valikko tunnistaa pelitilan ja
 * tarjoaa toiminnot sen mukaisesti.
 */
public class PauseMenuActivity extends Activity implements OnClickListener
{

	/**
	 * Luo Pause-valikon. Android kutsuu t‰t‰ automaattisesti.
	 * 
	 * @param Bundle Pelin aiempi tila
	 */
	@Override
	public void onCreate(Bundle _savedInstanceState)
	{
		super.onCreate(_savedInstanceState);
		setContentView(R.layout.pausemenu);
		
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		
		// Asetetaan ‰‰nens‰‰tˆnapit muuttamaan media volumea
	    setVolumeControlStream(AudioManager.STREAM_MUSIC);
		
		View mainmenuButton = findViewById(R.id.button_main_menu);
        mainmenuButton.setOnClickListener(this);
        
        View resumeButton = findViewById(R.id.button_resume);
        resumeButton.setOnClickListener(this);
        
        /*musicCheckBox = (CheckBox) findViewById(R.id.checkBoxMusic);
        musicCheckBox.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                // Suorita toiminto klikatessa, riippuen onko nappula ruksattu
                if (((CheckBox) v).isChecked()) {
                	SoundManager.playSound(SoundManager.SOUND_BUTTONCLICK, 1);
                    Options.music = true;
                    
                } else {
                	SoundManager.playSound(SoundManager.SOUND_BUTTONCLICK, 1);
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
                    Options.sounds = true;
                } else {
                	SoundManager.playSound(SoundManager.SOUND_BUTTONCLICK, 1);
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
                    Options.vibration = true;
                } else {
                	SoundManager.playSound(SoundManager.SOUND_BUTTONCLICK, 1);
                    Options.vibration = false;
                }
            }
        });*/
	}
	
	/**
	 * K‰sittelee nappuloiden painamisen.
	 * 
	 * @param View Nappi jota painettiin
	 */
	public void onClick(View _v)
	{
		if(_v.getId() == R.id.button_main_menu) {
    		setResult(0);
    		finish();
		}

    	else if(_v.getId() == R.id.button_resume) {
    		setResult(1);
    		finish();
    	}
	}
	
    /**
     * Android kutsuu t‰t‰ automaattisesti kun ohjelma palaa taukotilasta.
     */
    @Override
    protected void onResume() {
        /*musicCheckBox.setChecked(mPrefs.getBoolean(PREF_BOOL_MUS, true));
        soundCheckBox.setChecked(mPrefs.getBoolean(PREF_BOOL_SOU, true));
        vibrationCheckBox.setChecked(mPrefs.getBoolean(PREF_BOOL_VIB, true)); */
        super.onResume();
    }
    
    /**
     * Siirt‰‰ pelin taukotilaan ja tallentaa ohjelman tilan, sill‰ onPausen j‰lkeen prosessi
     * saatetaan keskeytt‰‰ kokonaan. Android kutsuu t‰t‰ automaattisesti.
     */
    @Override
    protected void onPause() {
    	/*Editor e = mPrefs.edit();
    	e.putBoolean(PREF_BOOL_MUS, musicCheckBox.isChecked());
    	e.putBoolean(PREF_BOOL_SOU, soundCheckBox.isChecked());
    	e.putBoolean(PREF_BOOL_VIB, vibrationCheckBox.isChecked());
    	e.commit();*/
        
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
    
    /**
     * Korvataan back- ja home- nappien toteutukset, jottei peli sammu niit‰ painettaessa pause-menussa.
     */
    @Override
	public boolean onKeyDown(int _keyCode, KeyEvent _event)
	{
		if (_keyCode == KeyEvent.KEYCODE_BACK && _event.getRepeatCount() == 0) {
			return true;
	    }
		else if (_keyCode == KeyEvent.KEYCODE_HOME && _event.getRepeatCount() == 0) {
			return true;
		}
		
	    return false;
	}
}
