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
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.media.AudioManager;

/**
 * Sis‰lt‰‰ Options-valikon toteutuksen.
 * 
 * Lukee ja tallentaa puhelimeen settings.xml-tiedostoon k‰ytt‰j‰n valitsemien
 * asetuksien tilat.
 *
 */
public class SettingsActivity extends Activity implements OnClickListener
{
	// M‰‰ritet‰‰n tarvittavat muuttujat
	
	private int settings[] = new int[4];
	
	View musicOnButton;
	View soundsOnButton;
	View particlesOnButton;
	View vibrationOnButton;
	View mainmenuButton;
	
	protected boolean musicState = true;
	protected boolean soundsState = true;
	protected boolean vibrationState = true;
	protected boolean particlesState = true;
	
	private InputController inputController;
	
	private XmlReader reader 	    = new XmlReader(MainActivity.context);
	private XmlWriter writer 	    = new XmlWriter();

	@Override
	protected void onCreate(Bundle _savedInstanceState)
	{
		super.onCreate(_savedInstanceState);
		
        // Asetetaan aktiviteetti koko n‰ytˆlle
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
                                WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        
		setContentView(R.layout.settings);
		
		setVolumeControlStream(AudioManager.STREAM_MUSIC);
		
		musicOnButton = findViewById(R.id.button_music_on);
		musicOnButton.setOnClickListener(this);
		
        soundsOnButton = findViewById(R.id.button_sounds_on);
        soundsOnButton.setOnClickListener(this);
    
        particlesOnButton = findViewById(R.id.button_particles_on);
        particlesOnButton.setOnClickListener(this);

        vibrationOnButton = findViewById(R.id.button_vibration_on);
        vibrationOnButton.setOnClickListener(this);
        
		mainmenuButton = findViewById(R.id.button_mainmenu);
        mainmenuButton.setOnClickListener(this);
        
        // Luetaan tallennetut asetukset XmlReaderilla ja asetetaan asetuksien tilat sen mukaan mit‰ tiedostoon on tallennettu
        settings = reader.readSettings();
        
		if(settings[0] == 1) {
			musicState = true;
		}
		
		else if (settings[0] == 0) {
			musicOnButton.setBackgroundResource(R.drawable.button_music_off);
			musicState = false;
		}
		if(settings[1] == 1) {
			soundsState = true;
		}
		
		else if (settings[1] == 0) {
			soundsOnButton.setBackgroundResource(R.drawable.button_sounds_off);
			soundsState = false;
		}
		
		if(settings[2] == 1) {
			particlesState = true;
		}
		
		else if (settings[2] == 0) {
			particlesOnButton.setBackgroundResource(R.drawable.button_particles_off);
			particlesState = false;
		}
		
		if(settings[3] == 1) {
			vibrationState = true;
		}
		
		else if (settings[3] == 0) {
			vibrationOnButton.setBackgroundResource(R.drawable.button_vibration_off);
			vibrationState = false;
		}
		
		// Tallennetaan XmlWriterilla uudet asetuksien tilat tiedostoon
        writer.saveSettings(musicState, soundsState, particlesState, vibrationState);

	}
	
	/**
	 * K‰sittelee nappuloiden painamisen.
	 * 
	 * @param View Nappi jota painettiin
	 */
    public void onClick(View _v)
    {
		if(_v == musicOnButton) {
			if(musicState == true) {
				musicOnButton.setBackgroundResource(R.drawable.button_music_off);
				musicState = false;
				SoundManager.playSound(SoundManager.SOUND_BUTTONCLICK, 1);
				Options.music = false;
			}
			else {
				musicOnButton.setBackgroundResource(R.drawable.button_music_on);
				musicState = true;
				SoundManager.playSound(SoundManager.SOUND_BUTTONCLICK, 1);
				Options.music = true;
			}
        }
		
		else if(_v == soundsOnButton) {
			if(soundsState == true) {
				soundsOnButton.setBackgroundResource(R.drawable.button_sounds_off);
				soundsState = false;
				SoundManager.playSound(SoundManager.SOUND_BUTTONCLICK, 1);
				Options.sounds = false;
			}
			else {
				soundsOnButton.setBackgroundResource(R.drawable.button_sounds_on);
				soundsState = true;
				SoundManager.playSound(SoundManager.SOUND_BUTTONCLICK, 1);
				Options.sounds = true;
			}
		}
		
		else if(_v == vibrationOnButton) {
			if(vibrationState == true) {
				vibrationOnButton.setBackgroundResource(R.drawable.button_vibration_off);
				vibrationState = false;
				SoundManager.playSound(SoundManager.SOUND_BUTTONCLICK, 1);
				Options.vibration = false;
			}
			else {
				vibrationOnButton.setBackgroundResource(R.drawable.button_vibration_on);
				vibrationState = true;
				SoundManager.playSound(SoundManager.SOUND_BUTTONCLICK, 1);
				Options.vibration = true;
			}
		}
		
		else if(_v == particlesOnButton) {
			if(particlesState == true) {
				particlesOnButton.setBackgroundResource(R.drawable.button_particles_off);
				particlesState = false;
				SoundManager.playSound(SoundManager.SOUND_BUTTONCLICK, 1);
				Options.particles = false;
			}
			else {
				particlesOnButton.setBackgroundResource(R.drawable.button_particles_on);
				particlesState = true;
				SoundManager.playSound(SoundManager.SOUND_BUTTONCLICK, 1);
				Options.particles = true;
			}
		}
		
		else if(_v == mainmenuButton) {
			writer.saveSettings(musicState, soundsState, particlesState, vibrationState);
			
			if(soundsState = true) {
				SoundManager.playSound(SoundManager.SOUND_BUTTONCLICK, 1);
			}
			
			Intent i_mainmenu = new Intent(this, MainActivity.class);
    		startActivity(i_mainmenu);
    		finish();
		}
    }
    
    @Override
	public boolean onKeyDown(int _keyCode, KeyEvent _event)
	{
		if (_keyCode == KeyEvent.KEYCODE_BACK && _event.getRepeatCount() == 0) {
			
			writer.saveSettings(musicState, soundsState, particlesState, vibrationState);
	        Intent i_mainmenu = new Intent(this, MainActivity.class);
	        startActivityIfNeeded(i_mainmenu, 1);
	        finish();
			return true;
	    }
		
	    return false; //inputController.handleKeyDown(_keyCode, _event); 
	}
}
        
	
		

