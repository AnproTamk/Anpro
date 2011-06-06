package fi.tamk.anpro;

import fi.tamk.anpro.R;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;

public class SettingsActivity extends PreferenceActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.layout.settings);
		
		//Preference particlesPref = findPreference("particles");
		//particlesPref.setOnPreferenceClickListener(null);
	}
	
	/*
	public void onClick(Preference p) {
		if (p.getKey() == "particles") {
			
		}
		else if (p.getKey() == "music") {
			
		}
		else if (p.getKey() == "sounds") {
			
		}	
	}*/
	
}
