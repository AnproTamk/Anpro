package fi.tamk.anpro;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.media.AudioManager;

/**
 * Sisältää Options-valikon toteutuksen.
 * 
 * 
 * 
 *
 */
// TODO: Turha luokka?
public class SettingsActivity extends Activity implements OnClickListener
{

	@Override
	protected void onCreate(Bundle _savedInstanceState)
	{
		super.onCreate(_savedInstanceState);
        
        // Asetetaan aktiviteetti koko näytölle
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
                                WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        
		setContentView(R.layout.settings);
		
        View musicOnButton = findViewById(R.id.button_music_on);
        musicOnButton.setOnClickListener(this);
        
        View musicOffButton = findViewById(R.id.button_music_off);
        musicOffButton.setOnClickListener(this);
        
        View soundsOnButton = findViewById(R.id.button_sounds_on);
        soundsOnButton.setOnClickListener(this);
        
        View soundsOffButton = findViewById(R.id.button_sounds_off);
        soundsOffButton.setOnClickListener(this);
        
        View particlesHighButton = findViewById(R.id.button_particles_high);
        particlesHighButton.setOnClickListener(this);
        
        View particlesLowButton = findViewById(R.id.button_particles_low);
        particlesLowButton.setOnClickListener(this);
        
        View vibrationOnButton = findViewById(R.id.button_vibration_on);
        vibrationOnButton.setOnClickListener(this);
        
        View vibrationOffButton = findViewById(R.id.button_vibration_off);
        vibrationOffButton.setOnClickListener(this);
		
		View mainmenuButton = findViewById(R.id.button_mainmenu);
        mainmenuButton.setOnClickListener(this);
	}
	
	/**
	 * Käsittelee nappuloiden painamisen.
	 * 
	 * @param View Nappi jota painettiin
	 */
    public void onClick(View _v)
    {
        switch(_v.getId()) {                
            case R.id.button_music_on:
                SoundManager.playSound(SoundManager.SOUND_BUTTONCLICK, 1);
                break;
                
            case R.id.button_music_off:
                SoundManager.playSound(SoundManager.SOUND_BUTTONCLICK, 1);
                break;
            
            case R.id.button_sounds_on:
            	SoundManager.playSound(SoundManager.SOUND_BUTTONCLICK, 1);
                break;
                
            case R.id.button_sounds_off:
            	SoundManager.playSound(SoundManager.SOUND_BUTTONCLICK, 1);
                break;
                
            case R.id.button_particles_high:
            	SoundManager.playSound(SoundManager.SOUND_BUTTONCLICK, 1);
                break;
                
            case R.id.button_particles_low:
            	SoundManager.playSound(SoundManager.SOUND_BUTTONCLICK, 1);
                break;
                
            case R.id.button_vibration_on:
            	SoundManager.playSound(SoundManager.SOUND_BUTTONCLICK, 1);
                break;
                
            case R.id.button_vibration_off:
            	SoundManager.playSound(SoundManager.SOUND_BUTTONCLICK, 1);
                break;
                
	    	case R.id.button_mainmenu:
	    		Intent i_mainmenu = new Intent(this, MainActivity.class);
	    		startActivity(i_mainmenu);
	    		finish();
	    		break;
        }
    }
}
        
	
		

