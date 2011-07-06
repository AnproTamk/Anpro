package fi.tamk.anpro;

import android.util.Log;
import android.view.KeyEvent;

/**
 * K�sittelee hardware-n�pp�inten toiminnot.
 */
public class InputController
{
	/**
	 * K�sittelee n�pp�inpainalluksen.
	 * 
	 * @param _keyCode N�pp�imen tunnus
	 * @param _event   Androidin generoima n�pp�intapahtuma
	 * 
	 * @return K�siteltiink� tapahtuma
	 */
	public boolean handleKeyDown(int _keyCode, KeyEvent _event)
	{
	    // K�sitell��n DPad-napit
	    if (_keyCode == KeyEvent.KEYCODE_DPAD_CENTER) {
			Log.v("navigare", "OK");
			// TODO: Keskinapin painamisen j�lkeiset toiminnot..
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
			// TODO: Liikuta yl�s
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
		 // K�sitell��n DPad-napit
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
			// TODO: Yl�s painaminen loppuu
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
