package fi.tamk.anpro;

import android.opengl.GLSurfaceView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

/*
 * Hoitaa kosketuksen lis��m�ll� OpenGL-pintaan tarvittavat
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
	public  int touchMarginal = 16; // Alue, jonka kosketus antaa anteeksi hyv�ksy�kseen kosketuksen pelk�ksi painallukseksi
   
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
     * Palauttaa pointterin t�st� luokasta
     */
    public static TouchEngine getInstance() {
        if(instance == null) {
            instance = new TouchEngine();
        }
        return instance;
    }
    
    /*
     * K�sittelee kosketustapahtumat
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
        	
        	// T�H�N KOHTAAN LIIKUTUKSESSA TAPAHTUVAT ASIAT
        	//
        	// T�H�N KOHTAAN LIIKUTUKSESSA TAPAHTUVAT ASIAT
        	
        }
        // Painamisen loputtua
        else if (event.getAction() == MotionEvent.ACTION_UP) {
        	if (Math.abs(event.getX() - xClickOffset) < touchMarginal
             && Math.abs(event.getY() - yClickOffset) < touchMarginal) {
        		// T�H�N KOHTAAN PAINALLUKSESTA TAPAHTUVAT ASIAT
        		// Jos painetaan oikealla HUD:n alueella
        		// Oikean reunan ylin nappula
        		if (xClickOffset > screenWidth - 48 && xClickOffset < screenWidth
        	     && yClickOffset < screenHeight / 2 && yClickOffset > 0) {
        			
        			// Oikean reunan alin nappula
        			if (xClickOffset < screenWidth - 48 && xClickOffset < screenWidth
               	          && yClickOffset < screenHeight / 2 - 96 && yClickOffset > 48) {
        				int a = 0;
        			}
        			
        			// Oikean reunan keskimm�inen nappula
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
        		
        		// Painetaan pelikent�lt�
        		else {
        			int b = 3;
        			//weaponStorage.triggerShoot((int)event.getX(), (int)event.getY());
        		}
        		// T�H�N KOHTAAN PAINALLUKSESTA TAPAHTUVAT ASIAT
            }
        }
        return true;
	}*/

	public void setSurfaceListeners(GLSurfaceView _glSurfaceView) {
		
		Log.v(TAG, "setSurfaceListeners()-funktion sis�ll�");
		
		surface = _glSurfaceView;
		
		Log.v(TAG, "Menn��n surface.setOnTouchListener():n sis�lle");
		
		surface.setOnTouchListener(new OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {
				
				Log.v(TAG, "onTouchin sis�ll� ^___^!");
				
				// Painamisen aloitus
		        if (event.getAction() == MotionEvent.ACTION_DOWN) {
		        	//xTouch = (int) event.getX();
		            //yTouch = (int) event.getY();
		            //xClickOffset = xTouch;
		            //yClickOffset = yTouch;
		        	Log.v(TAG, "ACTION_DOWN:n sis�ll� :OO");
		        	
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
		        	Log.v(TAG, "ACTION_MOVE:n sis�ll� D:::");
		            //xTouch = (int) event.getX();
		            //yTouch = (int) event.getY();
		        	
		        	// T�H�N KOHTAAN LIIKUTUKSESSA TAPAHTUVAT ASIAT
		        	//
		        	// T�H�N KOHTAAN LIIKUTUKSESSA TAPAHTUVAT ASIAT
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
		        			
		        			// Oikean reunan keskimm�inen nappula
		        			else if (xClickOffset < screenWidth - 48 && xClickOffset < screenWidth
		        	         && yClickOffset < screenHeight / 2 - 48 && yClickOffset > 96) {
		        				Log.v(TAG, "Oikean reunan keskimm�inen nappula.");
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
		        		
		        		// Painetaan pelikent�lt�
		        		else {
		        			//weaponStorage.triggerShoot((int)event.getX(), (int)event.getY());
		        		}
		            }
		        	Log.v(TAG, "Painaminen loppu NY.");
		        	return true;
		        }
		        Log.v(TAG, "Returnoidaanpa t��lt� FALSE..");
				return false;
			}
        });
	}
}
