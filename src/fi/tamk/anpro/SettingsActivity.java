package fi.tamk.anpro;

import java.io.IOException;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import fi.tamk.anpro.R;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.XmlResourceParser;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;
import android.view.KeyEvent;
import android.view.View.OnClickListener;
import android.widget.Button;

public class SettingsActivity extends PreferenceActivity {
	private Context context;
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
		super.onCreate(_savedInstanceState);
		addPreferencesFromResource(R.layout.settings);
        
		//setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		
		
        
        final Preference particlesPref = (Preference) findPreference("particles");
        final Preference musicPref = (Preference) findPreference("music");
        final Preference soundsPref = (Preference) findPreference("sounds");
		
        //particleState = particlesPref.setDefaultValue();
        
		particlesPref.setOnPreferenceClickListener(new OnPreferenceClickListener() {
			public boolean onPreferenceClick(Preference preference) {
			
				particleState = ((CheckBoxPreference) particlesPref).isChecked();
				musicState = ((CheckBoxPreference) musicPref).isChecked();
				soundState = ((CheckBoxPreference) soundsPref).isChecked();
				
				XmlWriter writer = new XmlWriter();
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
