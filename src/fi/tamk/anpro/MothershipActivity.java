package fi.tamk.anpro;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.media.AudioManager;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageButton;
import android.widget.Toast;
import android.widget.ViewFlipper;
public class MothershipActivity extends Activity implements OnClickListener
{
	// Alustetaan muuttujat.
	ViewFlipper viewFlipper;
	ImageButton skills;
	ImageButton repair;
	
	private Animation inFromRightAnimation() {
		Animation inFromRight = new TranslateAnimation(Animation.RELATIVE_TO_PARENT, +2.5f, Animation.RELATIVE_TO_PARENT, 0.0f,
													   Animation.RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_PARENT, 0.0f);
		inFromRight.setDuration(750);
		inFromRight.setInterpolator(new AccelerateInterpolator());
		return inFromRight;
	}
	
	private Animation outToLeftAnimation() {
		Animation outtoLeft = new TranslateAnimation(Animation.RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_PARENT, -1.0f,
													 Animation.RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_PARENT, 0.0f);
		outtoLeft.setDuration(750);
		outtoLeft.setInterpolator(new AccelerateInterpolator());
		return outtoLeft;
	}
	
	private Animation inFromLeftAnimation() {
		Animation inFromLeft = new TranslateAnimation(Animation.RELATIVE_TO_PARENT, -2.5f, Animation.RELATIVE_TO_PARENT, 0.0f,
													  Animation.RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_PARENT, 0.0f);
		inFromLeft.setDuration(750);
		inFromLeft.setInterpolator(new AccelerateInterpolator());
		return inFromLeft;
	}
	
	private Animation outToRightAnimation() {
		Animation outtoRight = new TranslateAnimation(Animation.RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_PARENT, +1.0f,
													  Animation.RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_PARENT, 0.0f);
		outtoRight.setDuration(750);
		outtoRight.setInterpolator(new AccelerateInterpolator());
		return outtoRight;
	}
	
	/**
	 * Luo emoalusvalikon. Android kutsuu t‰t‰ automaattisesti.
	 * 
	 * @param Bundle Pelin aiempi tila
	 */
    @Override
	public void onCreate(Bundle _savedInstanceState)
	{
		super.onCreate(_savedInstanceState);

	    // Piiloitetaan otsikko ja vaihdetaan kokoruuduntilaan
	    getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
	                         WindowManager.LayoutParams.FLAG_FULLSCREEN);
	    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
	    
	    // Asetetaan ‰‰nens‰‰tˆnapit muuttamaan media volumea
	    setVolumeControlStream(AudioManager.STREAM_MUSIC);
	    
	    // Asetetaan activityn ulkoasu
	    setContentView(R.layout.hangar);
	    
	    // Luodaan olio, joka sis‰lt‰‰ emoaluksen eri n‰kym‰t.
	    viewFlipper = (ViewFlipper) findViewById(R.id.ViewFlipper01);
	    
	    
	    // Luodaan emoaluksen painikkeet.
	    repair = (ImageButton) findViewById(R.id.button_repair);
	    repair.setOnClickListener(this);
	    
	    skills = (ImageButton) findViewById(R.id.button_skills);
	    skills.setOnClickListener(this);
	}

	
	/**
	 * Toiminta nappeja painettaessa.
	 * 
	 * @param _v View-luokan muuttuja painikkeita varten.
	*/ 
    public void onClick(View _v)
    {
    	switch(_v.getId()) {
    		case R.id.button_skills:
    			viewFlipper.setInAnimation(inFromRightAnimation());
    			viewFlipper.setOutAnimation(outToLeftAnimation());
    			viewFlipper.showNext();
    			break;
    		case R.id.button_repair:
    			viewFlipper.setInAnimation(inFromLeftAnimation());
    			viewFlipper.setOutAnimation(outToRightAnimation());
    			viewFlipper.showPrevious();
    			break;
    	}
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
