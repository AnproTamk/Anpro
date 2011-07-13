package fi.tamk.anpro;

import android.util.Log;
import android.view.KeyEvent;

/**
 * Käsittelee hardware-näppäinten toiminnot.
 */
public class InputController
{
    private Wrapper wrapper;
    private int		angle;

	
	public InputController()
    {
		wrapper = Wrapper.getInstance();
    }
	
	/**
	 * Käsittelee näppäinpainalluksen.
	 * 
	 * @param _keyCode Näppäimen tunnus
	 * @param _event   Androidin generoima näppäintapahtuma
	 * 
	 * @return Käsiteltiinkö tapahtuma
	 */
	public boolean handleKeyDown(int _keyCode, KeyEvent _event)
	{
	    // Käsitellään DPad-napit
	    if (_keyCode == KeyEvent.KEYCODE_DPAD_CENTER) {
			Log.v("navigare", "OK");
			// TODO: Keskinapin painamisen jälkeiset toiminnot..
			//return false;
		}
	    else if (_keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
			Log.v("navigare", "LEFT");
			// TODO: Liikuta vasemmalle PAREMMIN
			angle = 180;
			//return false;
		}
		else if (_keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
			Log.v("navigare", "RIGHT");
			// TODO: Liikuta oikealle PAREMMIN
			angle = 0;
			//return false;
		}
		else if (_keyCode == KeyEvent.KEYCODE_DPAD_UP) {
			Log.v("navigare", "UP");
			// TODO: Liikuta ylös PAREMMIN
			angle = 90;
			//return false;
		}
		else if (_keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
			Log.v("navigare", "DOWN");
			// TODO: Liikuta alas PAREMMIN
			angle = 270;
			//return false;
		}
	    
        wrapper.player.movementTargetDirection = angle;
        wrapper.player.movementAcceleration    = 0;
        wrapper.player.setMovementSpeed(1.0f);
        wrapper.player.setMovementDelay(1.0f);
        
	    return true;
	}
	
	public boolean handleKeyUp(int _keyCode, KeyEvent _event)
	{
		 // Käsitellään DPad-napit
	    if (_keyCode == KeyEvent.KEYCODE_DPAD_CENTER) {
			Log.v("navigare", "OK UP");
			// TODO: Keskinapin painaminen loppuu
			return false;
		}
	    else if (_keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
			Log.v("navigare", "LEFT UP");
			// TODO: Vasemmalle painaminen loppuu
			
	        // Asetetaan pelaajan jarrutus
	        wrapper.player.movementAcceleration = -6;
	        
			return false;
		}
		else if (_keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
			Log.v("navigare", "RIGHT UP");
			// TODO: Oikealle painaminen loppuu
			
	        // Asetetaan pelaajan jarrutus
	        wrapper.player.movementAcceleration = -6;
	        
			return false;
		}
		else if (_keyCode == KeyEvent.KEYCODE_DPAD_UP) {
			Log.v("navigare", "UP UP");
			// TODO: Ylös painaminen loppuu
			
	        // Asetetaan pelaajan jarrutus
	        wrapper.player.movementAcceleration = -6;
	        
			return false;
		}
		else if (_keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
			Log.v("navigare", "DOWN UP");
			// TODO: Alas painaminen loppuu
			
	        // Asetetaan pelaajan jarrutus
	        wrapper.player.movementAcceleration = -6;
	        
			return false;
		}
   
	    return true;
	}
}
