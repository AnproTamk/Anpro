package fi.tamk.anpro;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.media.AudioManager;

/**
 * Sisältää Highscores-valikon toteutuksen. Listaa pisteet ja tarjoaa mahdollisuuden
 * lähettää pisteet OpenFeintiin. TODO: Vai?
 */
public class HighScoresActivity extends Activity implements OnClickListener
{
	/* Luodaan muuttujat */
	private int 	  scores[] 	    = new int[5];
	private String	  scoreStr[]	= new String[5];
	private int 	  playerScore   = 0;
	private XmlReader reader 	    = new XmlReader(MainActivity.context);
	private XmlWriter writer 	    = new XmlWriter();
	//private SharedPreferences prefs = getPreferences(0);
	
	TextView score_0;
	TextView score_1;
	TextView score_2;
	TextView score_3;
	TextView score_4;
 
	
	/**
	 * Luo Highscores-valikon. Android kutsuu tätä automaattisesti.
	 * 
	 * @param Bundle Pelin aiempi tila
	 */
	@Override
	protected void onCreate(Bundle _savedInstanceState)
	{
		super.onCreate(_savedInstanceState);
        
        // Asetetaan aktiviteetti koko näytölle
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
                                WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        
		setContentView(R.layout.highscores);
		
		/*
		 * Tallennetaan xml-tiedostosta luetut pisteet taulukkoon 
		 * ruudulle piirtämistä varten
		 */
		
		
		
		
		/* 
		 * Otetaan talteen GameActivitysta lähetetyt pelaajan pisteet 
		 * Luodaan funktio, joka tulostaa pisteet näkyviin.
		 */ 
		
		// Luodaan Bundle
		Bundle bundle = getIntent().getExtras();
		
		// Tallennetaan Bundlesta saadut pelaajan pisteet
		if (bundle != null) {
			playerScore = bundle.getInt("Scores");
		}
		
		scores = reader.readHighScores();
		
		
		scores = writer.saveHighScores(playerScore, scores);
		//scores = getScores();
		//addScores(scores, playerScore);
		
		for (int i = 0; i < scores.length; i++) {
			scoreStr[i] = String.valueOf(scores[i]);
		}
		
		score_0 = (TextView) findViewById(R.id.score_0);
		score_0.setText(scoreStr[0]);
		score_1 = (TextView) findViewById(R.id.score_1);
		score_1.setText(scoreStr[1]);
		score_2 = (TextView) findViewById(R.id.score_2);
		score_2.setText(scoreStr[2]);
		score_3 = (TextView) findViewById(R.id.score_3);
		score_3.setText(scoreStr[3]);
		score_4 = (TextView) findViewById(R.id.score_4);
		score_4.setText(scoreStr[4]);
		
		
		// Asetetaan äänensäätönapit muuttamaan media volumea
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
        
		View resetButton = findViewById(R.id.button_reset);
        resetButton.setOnClickListener(this);
        
        View mainmenuButton = findViewById(R.id.button_mainmenu);
        mainmenuButton.setOnClickListener(this); 
	}
	
	/**
	 * Käsittelee nappuloiden painamisen.
	 * 
	 * @param View Nappi jota painettiin
	 */
	public void onClick(View _v)
	{
    	switch(_v.getId()) {
	    	case R.id.button_reset:
	    		scores = writer.resetHighScores();
	    		
	    		for (int i = 0; i < scores.length; i++) {
	    			scoreStr[i] = String.valueOf(scores[i]);
	    		}
	    		
	    		score_0.setText(scoreStr[0]);
	    		score_1.setText(scoreStr[1]);
	    		score_2.setText(scoreStr[2]);
	    		score_3.setText(scoreStr[3]);
	    		score_4.setText(scoreStr[4]);
	    		
	    		break;
	    	case R.id.button_mainmenu:
	    		Intent i_mainmenu = new Intent(this, MainActivity.class);
	    		startActivity(i_mainmenu);
	    		finish();
	    		break;
    	}
	}
}
