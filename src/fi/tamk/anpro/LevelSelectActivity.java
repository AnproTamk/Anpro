package fi.tamk.anpro;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Gallery;
import android.widget.Toast;

public class LevelSelectActivity extends Activity implements OnItemClickListener, OnClickListener
{
	private View mainmenuButton;
	private View storyButton;
	
	@Override
	public void onCreate(Bundle _savedInstanceState)
	{
		super.onCreate(_savedInstanceState);
	    setContentView(R.layout.levelselect);

        mainmenuButton = findViewById(R.id.button_mainmenu);
        mainmenuButton.setOnClickListener(this);
        
        storyButton = findViewById(R.id.button_story);
        storyButton.setOnClickListener(this);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

	    Gallery g = (Gallery) findViewById(R.id.gallery);
	    g.setAdapter(new ImageAdapter(this));
	    
	    g.setOnItemClickListener(new OnItemClickListener() {
	        public void onItemClick(AdapterView parent, View v, int position, long id) {
        /*	
	        	// TÄHÄN KLIKKAUKSESTA AIHEUTUVA TOIMINTA ! !
	        	switch(v.getId()) {
			    	case R.id.button_reset:
			    		break;
			    	case R.id.button_mainmenu:
			    		Intent i_mainmenu = new Intent(this, MainActivity.class);
			    		startActivity(i_mainmenu);
			    		finish();
			    		break;
	        	}
	        	
	        	SoundManager.playSound(2, 1);
	    		Intent i_game = new Intent(this, GameActivity.class);
	            storyButton.setVisibility(View.VISIBLE);
	    	//	startActivity(i_game);
	    	//	finish();
*/
	            // storyButton.setVisibility(View.VISIBLE);
	            Toast.makeText(LevelSelectActivity.this, "level" + " " + (position + 1), Toast.LENGTH_LONG).show();
	        }
	    });
	}
		/* OPTIMOINNIN HOMMIA!!!!!!!
		for(int i = 0; i < 15; ++i) {
			try {
				views.add(findViewById(R.id.class.getField("button"+i).getInt(getClass())));
			} catch (Exception e) {
				e.printStackTrace();
			}
			if (views.get(i) != null) {
				views.get(i).setOnClickListener(this);
			}
		}
		*/

		// View level_1Button = findViewById(R.id.button1);

       /*
		View level1_Button = findViewById(R.id.button_level1);
        level1_Button.setOnClickListener(this);
        
        View level2_Button = findViewById(R.id.button_level2);
        level2_Button.setOnClickListener(this);
        
        View level3_Button = findViewById(R.id.button_level3);
        level3_Button.setOnClickListener(this);
        
        View level4_Button = findViewById(R.id.button_level4);
        level4_Button.setOnClickListener(this);
        
        View level5_Button = findViewById(R.id.button_level5);
        level5_Button.setOnClickListener(this);
        
        View level6_Button = findViewById(R.id.button_level6);
        level6_Button.setOnClickListener(this);
        
        View level7_Button = findViewById(R.id.button_level7);
        level7_Button.setOnClickListener(this);
        
        View level8_Button = findViewById(R.id.button_level8);
        level8_Button.setOnClickListener(this);
        
        View level9_Button = findViewById(R.id.button_level9);
        level9_Button.setOnClickListener(this);
        
        View level10_Button = findViewById(R.id.button_level10);
        level10_Button.setOnClickListener(this);
        
        View level11_Button = findViewById(R.id.button_level11);
        level11_Button.setOnClickListener(this);
        
        View level12_Button = findViewById(R.id.button_level12);
        level12_Button.setOnClickListener(this);
        
        View level13_Button = findViewById(R.id.button_level13);
        level13_Button.setOnClickListener(this);
        
        View level14_Button = findViewById(R.id.button_level14);
        level14_Button.setOnClickListener(this);
        
        View level15_Button = findViewById(R.id.button_level15);
        level15_Button.setOnClickListener(this);
        
        View mainmenuButton = findViewById(R.id.button_mainmenu);
        mainmenuButton.setOnClickListener(this);    
	}
*/
	    
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		
	}

	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()) {
    	case R.id.button_story:
    		SoundManager.playSound(2, 1);
    		Intent i_game = new Intent(this, GameActivity.class);
    		startActivity(i_game);
    		finish();
    		break;
    	case R.id.button_mainmenu:
    		SoundManager.playSound(2, 1);
    		Intent i_mainmenu = new Intent(this, MainActivity.class); // VAATII OPTIMOINTIA - KÄYNNISTÄÄ JOKA KERTA LUOKAN UUDELLEEN
    		startActivity(i_mainmenu);
    		finish();
    		break;
		}
		
	}

/*	public void onClick(View _v) {
		switch(_v.getId()) {
	    	case R.id.button_level1:
	    		SoundManager.playSound(2, 1);
	    		Intent i_game = new Intent(this, GameActivity.class);
	    		startActivity(i_game);
	    		finish();
	    		break;
	    	case R.id.button_level2:
	    		SoundManager.playSound(2, 1);
	    		break;
	    	case R.id.button_level3:
	    		SoundManager.playSound(2, 1);
	    		break;
	    	case R.id.button_level4:
	    		SoundManager.playSound(2, 1);
	    		break;
	    	case R.id.button_level5:
	    		SoundManager.playSound(2, 1);
	    		break;
	    	case R.id.button_level6:
	    		SoundManager.playSound(2, 1);
	    		break;
	    	case R.id.button_level7:
	    		SoundManager.playSound(2, 1);
	    		break;
	    	case R.id.button_level8:
	    		SoundManager.playSound(2, 1);
	    		break;
	    	case R.id.button_level9:
	    		SoundManager.playSound(2, 1);
	    		break;
	    	case R.id.button_level10:
	    		SoundManager.playSound(2, 1);
	    		break;
	    	case R.id.button_level11:
	    		SoundManager.playSound(2, 1);
	    		break;
	    	case R.id.button_level12:
	    		SoundManager.playSound(2, 1);
	    		break;
	    	case R.id.button_level13:
	    		SoundManager.playSound(2, 1);
	    		break;
	    	case R.id.button_level14:
	    		SoundManager.playSound(2, 1);
	    		break;
	    	case R.id.button_level15:
	    		SoundManager.playSound(2, 1);
	    		break;
	    	case R.id.button_mainmenu:
	    		SoundManager.playSound(2, 1);
	    		Intent i_mainmenu = new Intent(this, MainActivity.class); // VAATII OPTIMOINTIA - KÄYNNISTÄÄ JOKA KERTA LUOKAN UUDELLEEN
	    		startActivity(i_mainmenu);
	    		finish();
	    		break;  		
		}
	}*/
}

