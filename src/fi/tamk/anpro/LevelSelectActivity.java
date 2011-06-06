package fi.tamk.anpro;

import java.util.ArrayList;

import fi.tamk.anpro.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;


public class LevelSelectActivity extends Activity implements OnClickListener {
	
	ArrayList<View> views = null;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.levelselect);
		
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

	public void onClick(View v) {
		switch(v.getId()) {
    	case R.id.button_level1:
    		break;
    	case R.id.button_level2:
    		break;
    	case R.id.button_level3:
    		break;
    	case R.id.button_level4:
    		break;
    	case R.id.button_level5:
    		break;
    	case R.id.button_level6:
    		break;
    	case R.id.button_level7:
    		break;
    	case R.id.button_level8:
    		break;
    	case R.id.button_level9:
    		break;
    	case R.id.button_level10:
    		break;
    	case R.id.button_level11:
    		break;
    	case R.id.button_level12:
    		break;
    	case R.id.button_level13:
    		break;
    	case R.id.button_level14:
    		break;
    	case R.id.button_level15:
    		break;
    	case R.id.button_mainmenu:
    		Intent i_mainmenu = new Intent(this, MainActivity.class);			// VAATII OPTIMOINTIA - KÄYNNISTÄÄ JOKA KERTA LUOKAN UUDELLEEN
    		startActivity(i_mainmenu);
    		finish();
    		break;  		
		}	
	}	
}
