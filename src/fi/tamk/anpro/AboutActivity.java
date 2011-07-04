package fi.tamk.anpro;

import android.app.Activity;
import android.os.Bundle;
import android.content.pm.ActivityInfo;
import android.media.AudioManager;

/**
 * Sis‰lt‰‰ Help/About-popupin toteutuksen.
 */
public class AboutActivity extends Activity
{
	/**
	 * Luo About-popupin. Android kutsuu t‰t‰ automaattisesti.
	 * 
	 * @param Bundle Pelin aiempi tila
	 */
	@Override
	public void onCreate(Bundle _savedInstanceState)
	{
		super.onCreate(_savedInstanceState);
		setContentView(R.layout.about);
        
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		
        // Asetetaan ‰‰nens‰‰tˆnapit muuttamaan media volumea
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
	}
}
