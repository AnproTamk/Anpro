package fi.tamk.anpro;

import android.view.MotionEvent;

/*
 * Hoitaa kosketuksen lis‰‰m‰ll‰ OpenGL-pintaan tarvittavat
 * TouchListenerit ja tutkii jokaisen kosketuksen
 */
public class TouchEngine {
	public int xTouch;
	public int yTouch;
	public int xClickOffset;
	public int yClickOffset;
	//public int xOffset;
	//public int yOffset;
	public int tileSize = 48;

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
	        // Painaminen & Liikuttaminen
	        else if(event.getAction() == MotionEvent.ACTION_MOVE) {
	            //xOffset += xTouch - (int) event.getX();
	            //yOffset += yTouch - (int) event.getY();

	            //xTouch = (int) event.getX();
	            //yTouch = (int) event.getY();
	        }
	        // Painamisen loputtua
	        else if (event.getAction() == MotionEvent.ACTION_UP) {
	        	if (Math.abs(event.getX() - xClickOffset) < tileSize / 2
	             && Math.abs(event.getY() - yClickOffset) < tileSize / 2){
	        		// TƒHƒN KOHTAAN KLIKKAUKSESTA TAPAHTUVAT ASIAT
	        		//
	        		// TƒHƒN KOHTAAN KLIKKAUKSESTA TAPAHTUVAT ASIAT
	            }
	        }
	        return true;
	    } 
}
