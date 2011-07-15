package fi.tamk.anpro;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.GridView;
import android.widget.Toast;
import android.widget.ViewFlipper;
import android.widget.ViewSwitcher;
public class MothershipActivity extends Activity implements OnClickListener
{
	
	ViewFlipper viewFlipper;
	Button next;
	Button previous;
	
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
	    setContentView(R.layout.hangar_main);
	    
	    
	    viewFlipper = (ViewFlipper) findViewById(R.id.ViewFlipper01);
	    
	    previous = (Button) findViewById(R.id.button_previous);
	    previous.setOnClickListener(this);
	    next = (Button) findViewById(R.id.button_next);
	    next.setOnClickListener(this);
	    
	    
	}

	
	
    public void onClick(View _v)
    {
    	if (_v == next) {
    		viewFlipper.showNext();
    	}
    	if (_v == previous) {
    		viewFlipper.showPrevious();
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
