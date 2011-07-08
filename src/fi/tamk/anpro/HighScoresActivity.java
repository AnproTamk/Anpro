package fi.tamk.anpro;

import java.io.UnsupportedEncodingException;

import org.xmlpull.v1.XmlPullParserException;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.media.AudioManager;

/**
 * Sis�lt�� Highscores-valikon toteutuksen. Listaa pisteet ja tarjoaa mahdollisuuden
 * l�hett�� pisteet OpenFeintiin. TODO: Vai?
 */
public class HighScoresActivity extends Activity implements OnClickListener
{
	/* Luodaan muuttujat */
	private int 	  scoreList[] = new int[5];
	private int 	  score 	  = 0;
	private XmlReader reader 	  = new XmlReader(MainActivity.context);
	private XmlWriter writer 	  = new XmlWriter();
	
	/**
	 * Luo Highscores-valikon. Android kutsuu t�t� automaattisesti.
	 * 
	 * @param Bundle Pelin aiempi tila
	 */
	@Override
	protected void onCreate(Bundle _savedInstanceState)
	{
		super.onCreate(_savedInstanceState);
        
        // Asetetaan aktiviteetti koko n�yt�lle
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
                                WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        
		setContentView(R.layout.highscores);
		
		/*
		 * Tallennetaan xml-tiedostosta luetut pisteet taulukkoon 
		 * ruudulle piirt�mist� varten
		 */
		
		
		
		
		/* 
		 * Otetaan talteen GameActivitysta l�hetetyt pelaajan pisteet 
		 * Luodaan funktio, joka tulostaa pisteet n�kyviin.
		 */ 
		
		// Luodaan Bundle
		Bundle bundle = getIntent().getExtras();
		
		// Tallennetaan Bundlesta saadut pelaajan pisteet
		if (bundle != null) {
			score = bundle.getInt("Scores");
		}
		
		scoreList = reader.readHighScores();
		writer.saveHighScores(score);
		
		/*// Tarkistetaan onko pelaajan pisteet suuremmat kuin nolla.
		if (score == 0) {
			writer.saveHighScores(0);
		}*/
		
		
        // Asetetaan ��nens��t�napit muuttamaan media volumea
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
        
		View resetButton = findViewById(R.id.button_reset);
        resetButton.setOnClickListener(this);
        
        View mainmenuButton = findViewById(R.id.button_mainmenu);
        mainmenuButton.setOnClickListener(this); 
	}
	
	/**
	 * K�sittelee nappuloiden painamisen.
	 * 
	 * @param View Nappi jota painettiin
	 */
	public void onClick(View _v)
	{
    	switch(_v.getId()) {
	    	case R.id.button_reset:
	    		break;
	    	case R.id.button_mainmenu:
	    		Intent i_mainmenu = new Intent(this, MainActivity.class);
	    		startActivity(i_mainmenu);
	    		finish();
	    		break;
    	}
	}
}
