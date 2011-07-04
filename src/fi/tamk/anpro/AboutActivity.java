package fi.tamk.anpro;

import android.app.Activity;
import android.os.Bundle;
import android.content.pm.ActivityInfo;
import android.media.AudioManager;

/**
 * Sisältää Help/About-popupin toteutuksen.
 */
public class AboutActivity extends Activity
{
	// TODO: Javadoc-kommentit (voi kopioida GameActivitysta)
	@Override
	public void onCreate(Bundle _savedInstanceState)
	{
		super.onCreate(_savedInstanceState);
		setContentView(R.layout.about);
        
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		
        // Asetetaan äänensäätönapit muuttamaan media volumea
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
	}
}
