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
	            //storybutton.
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


	public void onItemClick(AdapterView<?> arg0, View _v, int arg2, long arg3) {
		// TODO Auto-generated method stub
		
	}

	public void onClick(View v) {
		// TODO button_story täytyy saada kommunikoimaan gallerian kanssa
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
}

