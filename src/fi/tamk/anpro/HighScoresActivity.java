package fi.tamk.anpro;

import fi.tamk.anpro.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;


public class HighScoresActivity extends Activity implements OnClickListener {

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.highscores);
		
		View resetButton = findViewById(R.id.button_reset);
        resetButton.setOnClickListener(this);
        
        View mainmenuButton = findViewById(R.id.button_mainmenu);
        mainmenuButton.setOnClickListener(this); 
	}
	public void onClick(View v) {
    	
    	switch(v.getId()) {
    	case R.id.button_reset:
    		break;
    	case R.id.button_mainmenu:
    		Intent i_mainmenu = new Intent(this, MainActivity.class);			// VAATII OPTIMOINTIA - KÄYNNISTÄÄ JOKA KERTA LUOKAN UUDELLEEN
    		startActivity(i_mainmenu);
    		finish();
    		break;

    	}
	}
}
