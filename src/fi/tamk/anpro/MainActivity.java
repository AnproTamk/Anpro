package fi.tamk.anpro;

import fi.tamk.anpro.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

public class MainActivity extends Activity implements OnClickListener {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        View aboutButton = findViewById(R.id.button_help);
        aboutButton.setOnClickListener(this);
        
        View optionsButton = findViewById(R.id.button_options);
        optionsButton.setOnClickListener(this);
    }
    
    
    //
    public void onClick(View v) {
    	
    	switch(v.getId()) {
    	case R.id.button_story:
    		break;
    	case R.id.button_survival:
    		break;
    	case R.id.button_options:
    		Intent i_settings = new Intent(this, SettingsActivity.class);
    		startActivity(i_settings);
    		break;
    	case R.id.button_help:
    		Intent i_help = new Intent(this, AboutActivity.class);
    		startActivity(i_help);
    		break;
    	case R.id.button_quit:
    		System.exit(RESULT_OK);
    		break;
    	}
    }
}