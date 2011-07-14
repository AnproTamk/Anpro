package fi.tamk.anpro;

import android.view.KeyEvent;

/**
 * K�sittelee hardware-n�pp�inten toiminnot.
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
			// TODO: Keskinapin painamisen j�lkeiset toiminnot..
			//return false;
		}
	    else if (_keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
			// TODO: Liikuta vasemmalle PAREMMIN
			angle = 180;
			//return false;
		}
		else if (_keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
			// TODO: Liikuta oikealle PAREMMIN
			angle = 0;
			//return false;
		}
		else if (_keyCode == KeyEvent.KEYCODE_DPAD_UP) {
			// TODO: Liikuta yl�s PAREMMIN
			angle = 90;
			//return false;
		}
		else if (_keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
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
	
	/**
	 * K�sittelee n�pp�inpainalluksen lopettamisen.
	 * 
	 * @param _keyCode N�pp�imen tunnus
	 * @param _event   Androidin generoima n�pp�intapahtuma
	 * 
	 * @return K�siteltiink� tapahtuma
	 */
	public boolean handleKeyUp(int _keyCode, KeyEvent _event)
	{
		 // K�sitell��n DPad-napit
	    if (_keyCode == KeyEvent.KEYCODE_DPAD_CENTER) {
			// TODO: Keskinapin painaminen loppuu
			return false;
		}
	    else if (_keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
			// TODO: Vasemmalle painaminen loppuu
			
	        // Asetetaan pelaajan jarrutus
	        wrapper.player.movementAcceleration = -6;
	        
			return false;
		}
		else if (_keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
			// TODO: Oikealle painaminen loppuu
			
	        // Asetetaan pelaajan jarrutus
	        wrapper.player.movementAcceleration = -6;
	        
			return false;
		}
		else if (_keyCode == KeyEvent.KEYCODE_DPAD_UP) {
			// TODO: Yl�s painaminen loppuu
			
	        // Asetetaan pelaajan jarrutus
	        wrapper.player.movementAcceleration = -6;
	        
			return false;
		}
		else if (_keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
			// TODO: Alas painaminen loppuu
			
	        // Asetetaan pelaajan jarrutus
	        wrapper.player.movementAcceleration = -6;
	        
			return false;
		}
   
	    return true;
	}
}
