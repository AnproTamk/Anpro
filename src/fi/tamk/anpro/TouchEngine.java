package fi.tamk.anpro;

import android.view.MotionEvent;

/*
 * Hoitaa kosketuksen lisäämällä OpenGL-pintaan tarvittavat
 * TouchListenerit ja tutkii jokaisen kosketuksen
 */
public class TouchEngine {
	
	private static TouchEngine instance = null;
	
	// Muuttujat
	public WeaponStorage weaponStorage;
	public int xTouch;
	public int yTouch;
	public int xClickOffset;
	public int yClickOffset;
	//public int xOffset;
	//public int yOffset;
	public int tileSize = 48;
	
	// TouchEnginen rakentaja
    protected TouchEngine() {
    	weaponStorage = WeaponStorage.getInstance();
    }
    
    public static TouchEngine getInstance() {
        if(instance == null) {
            instance = new TouchEngine();
        }
        return instance;
    }
    // TouchEnginen rakentaja loppu
	


	public boolean onTouchEvent(MotionEvent event) {
			// Painaminen
	        if (event.getAction() == MotionEvent.ACTION_DOWN) {
	        	//xTouch = (int) event.getX();
	            //yTouch = (int) event.getY();
	            //xClickOffset = xTouch;
	            //yClickOffset = yTouch;
	        	xClickOffset = (int) event.getX();
	        	yClickOffset = (int) event.getY();
	        }
	        // Painamalla liikuttaminen
	        else if(event.getAction() == MotionEvent.ACTION_MOVE) {
	            //xOffset += xTouch - (int) event.getX();
	            //yOffset += yTouch - (int) event.getY();

	            //xTouch = (int) event.getX();
	            //yTouch = (int) event.getY();
	        	
	        	// TÄHÄN KOHTAAN LIIKUTUKSESSA TAPAHTUVAT ASIAT
	        	//
	        	// TÄHÄN KOHTAAN LIIKUTUKSESSA TAPAHTUVAT ASIAT
	        	
	        }
	        // Painamisen loputtua
	        else if (event.getAction() == MotionEvent.ACTION_UP) {
	        	//if (Math.abs(event.getX() - xClickOffset) < tileSize / 2
	            // && Math.abs(event.getY() - yClickOffset) < tileSize / 2){
	        		// TÄHÄN KOHTAAN PAINALLUKSESTA TAPAHTUVAT ASIAT
	        		// Jos painetaan HUD:n alueella
	        		// if (xClickOffset > HANKI NAPPULAN X-SIJAINTI && xClickOffset < HANKI NAPPULAN X-SIJAINTI JA LEVEYS
	        		//  && yClickOffset < HANKI NAPPULAN Y-SIJAINTI && yClickOffset > HANKI NAPPULAN 
	        		// weaponStorage.triggerShoot((int)event.getX(), (int)event.getY());
	        		// TÄHÄN KOHTAAN PAINALLUKSESTA TAPAHTUVAT ASIAT
	            //}
	        }
	        return true;
	    } 
}
