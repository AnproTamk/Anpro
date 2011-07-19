package fi.tamk.anpro;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.media.AudioManager;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
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
	
	private Animation topFromBottomAnimation() {
		Animation topFromBottom = AnimationUtils.loadAnimation(this, R.anim.fade_out_anim);
		topFromBottom.setDuration(1500);
		//toFromBottom.setInterpolator(new AccelerateInterpolator());
		return topFromBottom;
	}
	
	private Animation bottomFromTopAnimation() {
		Animation bottomFromTop = AnimationUtils.loadAnimation(this, R.anim.fade_in_anim);
		bottomFromTop.setDuration(1500);
		//bottomFromTop.setInterpolator(new AccelerateInterpolator());
		return bottomFromTop;
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
    			viewFlipper.setOutAnimation(topFromBottomAnimation());
    			viewFlipper.setInAnimation(bottomFromTopAnimation());
    			viewFlipper.showNext();
    			break;
    		case R.id.button_repair:
    			viewFlipper.setOutAnimation(topFromBottomAnimation());
    			viewFlipper.setInAnimation(bottomFromTopAnimation());
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
