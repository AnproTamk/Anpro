package fi.tamk.anpro;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;

public class SettingsActivity extends PreferenceActivity {
	private boolean particleState;
	private boolean musicState;
	private boolean soundState;
	
	/*public SettingsActivity(boolean _particleState, boolean _musicState, boolean _soundState) {
		particleState = _particleState;
		musicState = _musicState;
		soundState = _soundState;
	}*/
	
	@Override
	protected void onCreate(Bundle _savedInstanceState)
	{
		final XmlWriter writer = new XmlWriter();
		
		super.onCreate(_savedInstanceState);
		addPreferencesFromResource(R.layout.settings);
        
		//setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		
		final Preference particlesPref = (Preference) findPreference("particles");
	    final Preference musicPref = (Preference) findPreference("music");
	    final Preference soundsPref = (Preference) findPreference("sounds");
		
        //PreferenceManager.setDefaultValues(this, R.layout.settings, false);
	    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        
        particleState = prefs.getBoolean("particles", false);
        musicState = prefs.getBoolean("music", true);
        soundState = prefs.getBoolean("sounds", true);
        
        writer.saveSettings(particleState, musicState, soundState);
        
        /*((CheckBoxPreference) particlesPref).setChecked(particleState);
        ((CheckBoxPreference) musicPref).setChecked(musicState);
		((CheckBoxPreference) soundsPref).setChecked(soundState);*/
        
        particlesPref.setOnPreferenceClickListener(new OnPreferenceClickListener() {
			public boolean onPreferenceClick(Preference preference) {
				particleState = ((CheckBoxPreference) particlesPref).isChecked();
				musicState = ((CheckBoxPreference) musicPref).isChecked();
				soundState = ((CheckBoxPreference) soundsPref).isChecked();
				
				
				writer.saveSettings(particleState, musicState, soundState);
			
				return true;
			
			}
		});
		
		musicPref.setOnPreferenceClickListener(new OnPreferenceClickListener() {
			public boolean onPreferenceClick(Preference preference) {
				particleState = ((CheckBoxPreference) particlesPref).isChecked();
				musicState = ((CheckBoxPreference) musicPref).isChecked();
				soundState = ((CheckBoxPreference) soundsPref).isChecked();
				
				XmlWriter writer = new XmlWriter();
				writer.saveSettings(particleState, musicState, soundState);
			
				return true;
			
			}
		});
		
		soundsPref.setOnPreferenceClickListener(new OnPreferenceClickListener() {
			public boolean onPreferenceClick(Preference preference) {
				particleState = ((CheckBoxPreference) particlesPref).isChecked();
				musicState = ((CheckBoxPreference) musicPref).isChecked();
				soundState = ((CheckBoxPreference) soundsPref).isChecked();
				
				XmlWriter writer = new XmlWriter();
				writer.saveSettings(particleState, musicState, soundState);
			
				return true;
			
			}
		});
		
		/*KeyEvent keyEvent = new KeyEvent(0, 0);
		int keyAction; //keyEvent.getAction()
		int keyCode; //keyEvent.getKeyCode();
		if() {
				writer.saveSettings(settingStates);
		}*/
		
		/*KeyEvent keyEvent = new KeyEvent(0, 0);
		int keyAction = keyEvent.getAction();
		int keyCode = keyEvent.getKeyCode();
		if (keyAction == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_BACK) {
			writer.saveSettings(settingStates);
		}*/
	}
}
