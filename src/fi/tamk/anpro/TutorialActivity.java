package fi.tamk.anpro;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.media.AudioManager;

/**
 * Sis‰lt‰‰ tutorial-"ilmoitusten" toteutuksen. 
 */
public class TutorialActivity extends Activity implements OnClickListener
{
    /* Tutoriaalin tyyppi ja tarkastukset tutoriaalien n‰ytt‰misell‰ */
    public static byte    tutorialType;
	public static boolean startActive;
	public static boolean newWeaponActive;

	/**
	 * Luo Tutorial-ilmoituksen. Android kutsuu t‰t‰ automaattisesti.
	 * 
	 * @param _savedInstanceState Pelin aiempi tila
	 */
	@Override
	public void onCreate(Bundle _savedInstanceState)
	{
		super.onCreate(_savedInstanceState);
		
        // Piiloitetaan otsikko ja vaihdetaan kokoruuduntilaan
		requestWindowFeature(Window.FEATURE_NO_TITLE);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        
        // Asetetaan ‰‰nens‰‰tˆnapit muuttamaan media volumea
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
        
		// Luodaan Bundle
		Bundle bundle = getIntent().getExtras();
		
		// Haetaan haluttu layout luotavalle tutoriaalille
		if (bundle != null) {
			if (bundle.getInt("Tutorial") == GameMode.TUTORIAL_START) {
				setContentView(R.layout.tutorial_start);
			}
			else if (bundle.getInt("Tutorial") == GameMode.TUTORIAL_NEW_WEAPON_CLUSTER) {
				setContentView(R.layout.tutorial_new_weapon_cluster);				
			}
			else if (bundle.getInt("Tutorial") == GameMode.TUTORIAL_NEW_WEAPON_EMP) {
				setContentView(R.layout.tutorial_new_weapon_emp);				
			}
			else if (bundle.getInt("Tutorial") == GameMode.TUTORIAL_NEW_WEAPON_MISSILE) {
				setContentView(R.layout.tutorial_new_weapon_missile);				
			}
			else if (bundle.getInt("Tutorial") == GameMode.TUTORIAL_NEW_WEAPON_SPINNINGLASER) {
				setContentView(R.layout.tutorial_new_weapon_spinninglaser);				
			}
			else if (bundle.getInt("Tutorial") == GameMode.TUTORIAL_NEW_WEAPON_SPITFIRE) {
				setContentView(R.layout.tutorial_new_weapon_spitfire);				
			}
			else if (bundle.getInt("Tutorial") == GameMode.TUTORIAL_NEW_WEAPON_SWARM) {
				setContentView(R.layout.tutorial_new_weapon_swarm);				
			}
		}

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        
        // Asetetaan ‰‰nens‰‰tˆnapit muuttamaan media volumea
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
        
        View resumeButton = findViewById(R.id.button_resume);
        resumeButton.setOnClickListener(this);
	}
	
	/**
	 * K‰sittelee nappuloiden painamisen.
	 * 
	 * @param View Nappi jota painettiin
	 */
	public void onClick(View _v)
	{
		if(_v.getId() == R.id.button_resume) {
    		setResult(1);
    		finish();
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
