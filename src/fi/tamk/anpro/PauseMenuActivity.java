package fi.tamk.anpro;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
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
		// TODO: Pelitilan voi tarkistaa joko GameActivitysta (pit‰isi siirt‰‰ Optionsiin?)
		// if(storyMode == true) {
			super.onCreate(_savedInstanceState);
			setContentView(R.layout.pausemenu);
	        
	        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
	        
	        // Asetetaan ‰‰nens‰‰tˆnapit muuttamaan media volumea
	        setVolumeControlStream(AudioManager.STREAM_MUSIC);
	        
		//}
		
			/*else if() {
				super.onCreate(savedInstanceState);
				setContentView(R.layout.pausemenu_survival);
			}*/
		
		View mainmenuButton = findViewById(R.id.button_main_menu);
        mainmenuButton.setOnClickListener(this);
        
        View levelselectButton = findViewById(R.id.button_level_select);
        levelselectButton.setOnClickListener(this);
        
        View resumeButton = findViewById(R.id.button_resume);
        resumeButton.setOnClickListener(this);
	}
	
	/**
	 * K‰sittelee nappuloiden painamisen.
	 * 
	 * @param View Nappi jota painettiin
	 */
	public void onClick(View _v)
	{
		if(_v.getId() == R.id.button_main_menu) {
			Intent i_mainmenu = new Intent(this, MainActivity.class);
    		startActivity(i_mainmenu);
    		finish();
		}
    	
    	else if(_v.getId() == R.id.button_level_select) {
    		Intent i_level_select = new Intent(this, LevelSelectActivity.class);
    		startActivity(i_level_select);
    		finish();
    	}
		
    	else if(_v.getId() == R.id.button_resume) {
    		finish();
    	}
	}
}
