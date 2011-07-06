package fi.tamk.anpro;

import android.util.Log;
import android.view.KeyEvent;

/**
 * Käsittelee hardware-näppäinten toiminnot.
 */
public class InputController
{
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
			return false;
		}
	    else if (_keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
			Log.v("navigare", "LEFT");
			// TODO: Liikuta vasemmalle
			return false;
		}
		else if (_keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
			Log.v("navigare", "RIGHT");
			// TODO: Liikuta oikealle
			return false;
		}
		else if (_keyCode == KeyEvent.KEYCODE_DPAD_UP) {
			Log.v("navigare", "UP");
			// TODO: Liikuta ylös
			return false;
		}
		else if (_keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
			Log.v("navigare", "DOWN");
			// TODO: Liikuta alas
			return false;
		}
	    
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
			return false;
		}
		else if (_keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
			Log.v("navigare", "RIGHT UP");
			// TODO: Oikealle painaminen loppuu
			return false;
		}
		else if (_keyCode == KeyEvent.KEYCODE_DPAD_UP) {
			Log.v("navigare", "UP UP");
			// TODO: Ylös painaminen loppuu
			return false;
		}
		else if (_keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
			Log.v("navigare", "DOWN UP");
			// TODO: Alas painaminen loppuu
			return false;
		}
	    
	    return true;
	}
}
