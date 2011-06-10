package fi.tamk.anpro;

import fi.tamk.anpro.R;
import android.app.Activity;
import android.os.Bundle;
import android.content.pm.ActivityInfo;

public class AboutActivity extends Activity {

	@Override
	public void onCreate(Bundle _savedInstanceState)
	{
		super.onCreate(_savedInstanceState);
		setContentView(R.layout.about);
        
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		
	}
}
