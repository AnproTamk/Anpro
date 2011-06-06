package fi.tamk.anpro;

import fi.tamk.anpro.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;


public class AboutActivity extends Activity implements OnClickListener {

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.about);
		
		View mainmenuButton = findViewById(R.id.button_mainmenu);
        mainmenuButton.setOnClickListener(this);
		
	}

	public void onClick(View v) {
		if(v.getId() == R.id.button_mainmenu) {
			Intent i_mainmenu = new Intent(this, MainActivity.class);			// VAATII OPTIMOINTIA - KÄYNNISTÄÄ JOKA KERTA LUOKAN UUDELLEEN
    		startActivity(i_mainmenu);
    		finish();
		}
		
	}
}
