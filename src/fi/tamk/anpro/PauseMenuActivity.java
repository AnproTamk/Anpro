package fi.tamk.anpro;

import fi.tamk.anpro.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;


public class PauseMenuActivity extends Activity implements OnClickListener {

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		// if(storyMode == true) {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.pausemenu);
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

	public void onClick(View v) {
		if(v.getId() == R.id.button_main_menu) {
			Intent i_mainmenu = new Intent(this, MainActivity.class);
    		startActivity(i_mainmenu);
    		finish();
		}
    	
    	else if(v.getId() == R.id.button_level_select) {
    		Intent i_level_select = new Intent(this, LevelSelectActivity.class);
    		startActivity(i_level_select);
    		finish();
    	}
		
    	else if(v.getId() == R.id.button_resume) {
    		finish();
    	}
	}
}
