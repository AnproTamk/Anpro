package fi.tamk.anpro;

import fi.tamk.anpro.R;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;

public class SettingsActivity extends PreferenceActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.layout.settings);
        
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		
		// Preference particlesPref = findPreference("particles");
		// particlesPref.setOnPreferenceClickListener((OnPreferenceClickListener) this);  WHAAAT? 
	}
	
	public void onClick(CheckBoxPreference p) {
		XmlWriter writer = new XmlWriter();
		boolean particleState = false, musicState = false, soundState = false;
		
		if (p.getKey().equals("particles")) {
			if (p.isChecked()) {
				particleState = true;
			}
			else
				particleState = false;
		}
		else if (p.getKey().equals("music")) {
			if (p.isChecked()) {
				musicState = true;
			}
			else
				musicState = false;
		}
		else if (p.getKey().equals("sounds")) {
			if (p.isChecked()) {
				soundState = true;
			}
			else
				soundState = false;
		}
		
		boolean[] settingStates = {particleState, musicState, soundState};
		writer.saveSettings(settingStates);
	}
	
}
