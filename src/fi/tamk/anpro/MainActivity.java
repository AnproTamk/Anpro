package fi.tamk.anpro;

import fi.tamk.anpro.R;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

public class MainActivity extends Activity implements OnClickListener {
	public static Context context;
	
    /** Kutsutaan kun aktiviteetti luodaan. */
    @Override
    public void onCreate(Bundle _savedInstanceState) {
        super.onCreate(_savedInstanceState);
        setContentView(R.layout.main);
        
        context = getApplicationContext();
        
        Options options = Options.getInstance();
        
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        
        View storyButton = findViewById(R.id.button_story);
        storyButton.setOnClickListener(this);
        
        View survivalButton = findViewById(R.id.button_survival);
        survivalButton.setOnClickListener(this);
        
        View helpButton = findViewById(R.id.button_help);
        helpButton.setOnClickListener(this);
        
        View highscoresButton = findViewById(R.id.button_highscores);
        highscoresButton.setOnClickListener(this);
        
        View optionsButton = findViewById(R.id.button_options);
        optionsButton.setOnClickListener(this);
        
        View quitButton = findViewById(R.id.button_quit);
        quitButton.setOnClickListener(this);
        
    	//Luo ja alusta SoundManager
        SoundManager.getInstance();
        SoundManager.initSounds(this);
    }
    
    public void onClick(View _v) {
    	switch(_v.getId()) {
	    	case R.id.button_story:
	    		SoundManager.playSound(2, 1);
	    		Intent i_story = new Intent(this, LevelSelectActivity.class);
	    		startActivity(i_story);
	    		finish();
	    		break;
	    	case R.id.button_survival:
	    		SoundManager.playSound(2, 1);
	    		Intent i_game = new Intent(this, GameActivity.class);
	    		startActivity(i_game);
	    		break;
	    	case R.id.button_options:
	    		SoundManager.playSound(2, 1);
	    		Intent i_settings = new Intent(this, SettingsActivity.class);
	    		startActivity(i_settings);
	    		break;
	    	case R.id.button_highscores:
	    		SoundManager.playSound(2, 1);
	    		Intent i_highscores = new Intent(this, HighScoresActivity.class);
	    		startActivity(i_highscores);
	    		finish();
	    		break;
	    	case R.id.button_help:
	    		SoundManager.playSound(2, 1);
	    		Intent i_help = new Intent(this, AboutActivity.class);
	    		startActivity(i_help);
	    		break;
	    	case R.id.button_quit:
	    		SoundManager.playSound(2, 1);
	    		android.os.Process.killProcess(android.os.Process.myPid());
	    		break;
    	}
    }
}