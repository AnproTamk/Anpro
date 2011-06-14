package fi.tamk.anpro;

import android.opengl.GLSurfaceView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

/*
 * Hoitaa kosketuksen lisäämällä OpenGL-pintaan tarvittavat
 * TouchListenerit ja tutkii jokaisen kosketuksen
 */
public class TouchEngine {
	
	private static TouchEngine instance = null;
	
	// Muuttujat
	private GLSurfaceView surface;
	public  WeaponStorage weaponStorage;
	public  int xTouch;
	public  int yTouch;
	public  int xClickOffset;
	public  int yClickOffset;
	//public  int xOffset;
	//public  int yOffset;
	public  int touchMarginal = 16; // Alue, jonka kosketus antaa anteeksi hyväksyäkseen kosketuksen pelkäksi painallukseksi
   
	int screenWidth  = GLRenderer.width;  // Ruudun leveys
    int screenHeight = GLRenderer.height; // Ruudun korkeus
    
    private static final String TAG = "TouchEngine"; // Loggaus
	
	/*
	 * Rakentaja
	 */
    protected TouchEngine() {
    	weaponStorage = WeaponStorage.getInstance();
    }
    
    /*
     * Palauttaa pointterin tästä luokasta
     */
    public static TouchEngine getInstance() {
        if(instance == null) {
            instance = new TouchEngine();
        }
        return instance;
    }
    
    /*
     * Käsittelee kosketustapahtumat
     */

	/*public boolean onTouchEvent(MotionEvent event) {
		// Painamisen aloitus
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
        	//xTouch = (int) event.getX();
            //yTouch = (int) event.getY();
            //xClickOffset = xTouch;
            //yClickOffset = yTouch;
        	xClickOffset = (int) event.getX();
        	yClickOffset = (int) event.getY();
        }
        // Liikutetaan sormea
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
        	if (Math.abs(event.getX() - xClickOffset) < touchMarginal
             && Math.abs(event.getY() - yClickOffset) < touchMarginal) {
        		// TÄHÄN KOHTAAN PAINALLUKSESTA TAPAHTUVAT ASIAT
        		// Jos painetaan oikealla HUD:n alueella
        		// Oikean reunan ylin nappula
        		if (xClickOffset > screenWidth - 48 && xClickOffset < screenWidth
        	     && yClickOffset < screenHeight / 2 && yClickOffset > 0) {
        			
        			// Oikean reunan alin nappula
        			if (xClickOffset < screenWidth - 48 && xClickOffset < screenWidth
               	          && yClickOffset < screenHeight / 2 - 96 && yClickOffset > 48) {
        				int a = 0;
        			}
        			
        			// Oikean reunan keskimmäinen nappula
        			else if (xClickOffset < screenWidth - 48 && xClickOffset < screenWidth
        	         && yClickOffset < screenHeight / 2 - 48 && yClickOffset > 96) {
        				int a = 1;
        			}

        			// Oikean reunan ylin nappula
        			else {
        				int a = 2;
        			}
        		}
        		 
        		// Painetaan vasemmalla HUD:n alueella
        		// Vasemman reunan ylin nappula
        		else if (xClickOffset > -screenWidth / 2 && xClickOffset < -screenWidth / 2 + 48
        			  && yClickOffset < -screenHeight / 2 + 96 && yClickOffset > -screenHeight / 2) {
        				// Vasemman reunan alempi nappula
        				if (xClickOffset > -screenWidth / 2 && xClickOffset < -screenWidth / 2 + 48
        	        	 && yClickOffset < -screenHeight / 2 + 48 && yClickOffset > -screenHeight / 2) {
        					int b = 0;
        				}
        				
        				// Vasemman reunan ylempi nappula
        				else {
        					int b = 1;
        				}
        			}
        		
        		// Painetaan pelikentältä
        		else {
        			int b = 3;
        			//weaponStorage.triggerShoot((int)event.getX(), (int)event.getY());
        		}
        		// TÄHÄN KOHTAAN PAINALLUKSESTA TAPAHTUVAT ASIAT
            }
        }
        return true;
	}*/

	public void setSurfaceListeners(GLSurfaceView _glSurfaceView) {
		
		Log.v(TAG, "setSurfaceListeners()-funktion sisällä");
		
		surface = _glSurfaceView;
		
		Log.v(TAG, "Mennään surface.setOnTouchListener():n sisälle");
		
		surface.setOnTouchListener(new OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {
				
				Log.v(TAG, "onTouchin sisällä ^___^!");
				
				// Painamisen aloitus
		        if (event.getAction() == MotionEvent.ACTION_DOWN) {
		        	//xTouch = (int) event.getX();
		            //yTouch = (int) event.getY();
		            //xClickOffset = xTouch;
		            //yClickOffset = yTouch;
		        	Log.v(TAG, "ACTION_DOWN:n sisällä :OO");
		        	
		        	Log.v(TAG, "PRExClickOffset=" + xClickOffset + " PREyClickOffset=" + yClickOffset);
		        	
		        	xClickOffset = (int) event.getX();
		        	yClickOffset = (int) event.getY();
		        	
		        	Log.v(TAG, "POSTxClickOffset=" + xClickOffset + " POSTyClickOffset=" + yClickOffset);
		        	return true;
		        }
		        // Liikutetaan sormea
		        if (event.getAction() == MotionEvent.ACTION_MOVE) {
		            //xOffset += xTouch - (int) event.getX();
		            //yOffset += yTouch - (int) event.getY();
		        	Log.v(TAG, "ACTION_MOVE:n sisällä D:::");
		            //xTouch = (int) event.getX();
		            //yTouch = (int) event.getY();
		        	
		        	// TÄHÄN KOHTAAN LIIKUTUKSESSA TAPAHTUVAT ASIAT
		        	//
		        	// TÄHÄN KOHTAAN LIIKUTUKSESSA TAPAHTUVAT ASIAT
		        	return true;
		        }
		        // Painamisen loputtua
		        if (event.getAction() == MotionEvent.ACTION_UP) {
		        	
		        	Log.v(TAG, "Painamisen lopettaminen alkaa nyt.");
		        	
		        	if (Math.abs(event.getX() - xClickOffset) < touchMarginal
		             && Math.abs(event.getY() - yClickOffset) < touchMarginal) {
		        		// Jos painetaan oikealla HUD:n alueella
		        		// Oikean reunan ylin nappula
		        		if (xClickOffset > screenWidth - 48 && xClickOffset < screenWidth
		        	     && yClickOffset < screenHeight / 2 && yClickOffset > 0) {
		        			
		        			// Oikean reunan alin nappula
		        			if (xClickOffset < screenWidth - 48 && xClickOffset < screenWidth
		               	          && yClickOffset < screenHeight / 2 - 96 && yClickOffset > 48) {
		        				Log.v(TAG, "Oikean reunan alin nappula.");
		        			}
		        			
		        			// Oikean reunan keskimmäinen nappula
		        			else if (xClickOffset < screenWidth - 48 && xClickOffset < screenWidth
		        	         && yClickOffset < screenHeight / 2 - 48 && yClickOffset > 96) {
		        				Log.v(TAG, "Oikean reunan keskimmäinen nappula.");
		        			}

		        			// Oikean reunan ylin nappula
		        			else {
		        				Log.v(TAG, "Oikean reunan ylin nappula.");
		        			}
		        		}
		        		 
		        		// Painetaan vasemmalla HUD:n alueella
		        		// Vasemman reunan ylin nappula
		        		else if (xClickOffset > -screenWidth / 2 && xClickOffset < -screenWidth / 2 + 48
		        			  && yClickOffset < -screenHeight / 2 + 96 && yClickOffset > -screenHeight / 2) {
		        				// Vasemman reunan alempi nappula
		        				if (xClickOffset > -screenWidth / 2 && xClickOffset < -screenWidth / 2 + 48
		        	        	 && yClickOffset < -screenHeight / 2 + 48 && yClickOffset > -screenHeight / 2) {
		        				}
		        				
		        				// Vasemman reunan ylempi nappula
		        				else {
		        				}
		        			}
		        		
		        		// Painetaan pelikentältä
		        		else {
		        			//weaponStorage.triggerShoot((int)event.getX(), (int)event.getY());
		        		}
		            }
		        	Log.v(TAG, "Painaminen loppu NY.");
		        	return true;
		        }
		        Log.v(TAG, "Returnoidaanpa täältä FALSE..");
				return false;
			}
        });
	}
}
