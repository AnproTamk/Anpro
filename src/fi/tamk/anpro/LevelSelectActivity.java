package fi.tamk.anpro;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.media.AudioManager;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Gallery;

/**
 * Sis�lt�� StoryModen kentt�valikon toteutuksen. Kentt�valikossa pelaaja voi
 * valita l�pipelaamistaan kentist� haluamansa ja pelata sen uudestaan.
 */
public class LevelSelectActivity extends Activity implements OnItemClickListener, OnClickListener
{
	// Valikon napit
	private View mainmenuButton;
	private View storyButton;

	/**
	 * Luo kentt�valikon. Android kutsuu t�t� automaattisesti.
	 * 
	 * @param Bundle Pelin aiempi tila
	 */
	@Override
	public void onCreate(Bundle _savedInstanceState)
	{
		super.onCreate(_savedInstanceState);
		
        // Asetetaan aktiviteetti koko n�yt�lle
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
                                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        
	    setContentView(R.layout.levelselect);

        mainmenuButton = findViewById(R.id.button_mainmenu);
        mainmenuButton.setOnClickListener(this);
        
        storyButton = findViewById(R.id.button_story);
        storyButton.setOnClickListener(this);
        
        // M��ritet��n aktiviteetin asetukset
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setVolumeControlStream(AudioManager.STREAM_MUSIC);

	    Gallery g = (Gallery) findViewById(R.id.gallery);
	    g.setAdapter(new ImageAdapter(this));
	    
	    // TODO: Kommentoidut kohdat t�rkeit�?
	    g.setOnItemClickListener(new OnItemClickListener() {
	        @SuppressWarnings("rawtypes")
			public void onItemClick(AdapterView parent, View v, int position, long id) {
        /*	
	        	// T�H�N KLIKKAUKSESTA AIHEUTUVA TOIMINTA ! !
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

	// TODO: Ei k�ytet�, toteutus puuttuu ja pit�isi kommentoida
	public void onItemClick(AdapterView<?> arg0, View _v, int arg2, long arg3)
	{
		// ...
	}
	
	/**
	 * K�sittelee nappuloiden painamisen.
	 * 
	 * @param View Nappi jota painettiin
	 */
	public void onClick(View v)
	{
		// TODO: button_story t�ytyy saada kommunikoimaan gallerian kanssa
		switch(v.getId()) {
    	case R.id.button_story:
    		SoundManager.playSound(2, 1);
    		Intent i_game = new Intent(this, GameActivity.class);
    		startActivity(i_game);
    		finish();
    		break;
    	case R.id.button_mainmenu:
    		SoundManager.playSound(2, 1);
    		Intent i_mainmenu = new Intent(this, MainActivity.class); // VAATII OPTIMOINTIA - K�YNNIST�� JOKA KERTA LUOKAN UUDELLEEN
    		startActivity(i_mainmenu);
    		finish();
    		break;
		}
		
	}
}

