package fi.tamk.anpro;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class LogoActivity extends Activity
{
	// Alustetaan muuttujat
	ImageView logoPresents;
	ImageView logoName;
	
	long currentTime;
	long timer;
	
	public void onCreate(Bundle _savedInstanceState)
	{
		timer = android.os.SystemClock.uptimeMillis();
		super.onCreate(_savedInstanceState);
		
		// Asetetaan aktiviteetti koko n‰ytˆlle
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
                             WindowManager.LayoutParams.FLAG_FULLSCREEN);
        
        // Asetetaan n‰kym‰
        setContentView(R.layout.logo);
       
        // Haetaan ruudulla n‰kyv‰t kuvat resursseista.
        logoPresents = (ImageView) findViewById(R.id.logo_presents);
        logoName 	 = (ImageView) findViewById(R.id.logo_name);
        
        // Asetetaan toinen kuva hetkeksi pois n‰kyvist‰.
        logoName.setVisibility(View.INVISIBLE);
        
        // K‰ynnistet‰‰n logo-animaatiot.
        logoAnimations();
        
		// Pys‰ytet‰‰n ohjelma x-m‰‰r‰ksi animaation j‰lkeen ja k‰ynnistet‰‰n MainActivity.
		Handler handler = new Handler(); 
	    handler.postDelayed(new Runnable() { 
	         public void run() { 
	        	startMainActivity();
	         } 
	    }, 4000);
	}
	
	/**
	 * Animaatiot logoille.
	 */
	private void logoAnimations()
	{
		// Ensimm‰isen kuvan animaatio.
		logoPresents.startAnimation(AnimationUtils.loadAnimation(getBaseContext(), R.anim.logo_anim_1));
		
		// Asetetaan pieni tauko ensimm‰isen animaation j‰lkeen ja k‰ynnistet‰‰n toinen animaatio.
		Handler handler = new Handler();
		handler.postDelayed(new Runnable() {
			public void run() {
				// Asetetaan toinen kuva n‰kyviin.
				logoName.setVisibility(View.VISIBLE);
	        	logoName.startAnimation(AnimationUtils.loadAnimation(getBaseContext(), R.anim.logo_anim_2));
	         }
		}, 1500);
	}
	
	/**
	 * K‰ynnistet‰‰n MainActivity.
	 */
	public void startMainActivity()
	{
		Intent i_main = new Intent(this, MainActivity.class);
		startActivity(i_main);
		finish();
	}
	
	/**
     * Android kutsuu t‰t‰ automaattisesti kun ohjelma palaa taukotilasta.
     */
    @Override
    protected void onResume() {
    	super.onResume();
    }
    
    /**
     * Siirt‰‰ pelin taukotilaan ja tallentaa ohjelman tilan, sill‰ onPausen j‰lkeen prosessi
     * saatetaan keskeytt‰‰ kokonaan. Android kutsuu t‰t‰ automaattisesti.
     */
    @Override
    protected void onPause() {
    	super.onPause();
    }
        
    /**
     * Tallentaa ohjelman tilan, sill‰ onStopin j‰lkeen prosessi saatetaan keskeytt‰‰ kokonaan.
     * Android kutsuu t‰t‰ automaattisesti kun ohjelma ei ole en‰‰ aktiivinen.
     */
    @Override
    protected void onStop() {
    	super.onStop();
    }
}
