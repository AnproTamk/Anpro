package fi.tamk.anpro;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

/*
 * Hoitaa kosketuksen lisäämällä OpenGL-pintaan tarvittavat
 * TouchListenerit ja tutkii jokaisen kosketuksen
 */
public class TouchManager
{
	// Muuttujat
	private GLSurfaceView surface;
	public  WeaponManager weaponManager;
	public  HUD           hud;
	public  int xTouch;
	public  int yTouch;
	public  int xClickOffset;
	public  int yClickOffset;
	//public  int xOffset;
	//public  int yOffset;
	public  int touchMarginal = 16; // Alue, jonka kosketus antaa anteeksi hyväksyäkseen kosketuksen pelkäksi painallukseksi
	
	private int screenWidth;  // Ruudun leveys
    private int screenHeight; // Ruudun korkeus
	
    //private static final String TAG = "TouchEngine"; // Loggaus
	
	/*
	 * Rakentaja
	 */
    protected TouchManager(GLSurfaceView _glSurfaceView, Context _context) {
    	weaponManager = WeaponManager.getConnection();
    	hud           = HUD.getConnection();
    	//Log.v(TAG, "PREscreenWidth=" + screenWidth + "PREscreenHeight=" + screenHeight + "Density=" + GameActivity.dm.densityDpi);
    	screenWidth  = GameActivity.dm.widthPixels;
        screenHeight = GameActivity.dm.heightPixels;
       // Log.v(TAG, "POSTscreenWidth=" + screenWidth + "POSTscreenHeight=" + screenHeight);
        
        setSurfaceListeners(_glSurfaceView);
    }
    
    /*
     * Käsittelee kosketustapahtumat
     */

	public final void setSurfaceListeners(GLSurfaceView _surface) {
		
		//Log.v(TAG, "setSurfaceListeners()-funktion sisällä");
		
		surface = _surface;
		
		//Log.v(TAG, "Mennään surface.setOnTouchListener():n sisälle");
		
		surface.setOnTouchListener(new OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {
				
				//Log.v(TAG, "onTouchin sisällä");
				
				// Painamisen aloitus
		        if (event.getAction() == MotionEvent.ACTION_DOWN) {
		        	//xTouch = (int) event.getX();
		            //yTouch = (int) event.getY();
		            //xClickOffset = xTouch;
		            //yClickOffset = yTouch;
		        	//Log.v(TAG, "ACTION_DOWN:n sisällä");
		        	
		        	//Log.v(TAG, "PRExClickOffset=" + xClickOffset + " PREyClickOffset=" + yClickOffset);
		        	
		        	xClickOffset = (int) event.getX();
		        	yClickOffset = screenHeight - (int) event.getY();
		        	
		        	//Log.v(TAG, "POSTxClickOffset=" + xClickOffset + " POSTyClickOffset=" + yClickOffset);
		        	
		        	// Oikean reunan napit
		        	if (xClickOffset > screenWidth - 100 && xClickOffset < screenWidth &&
			            yClickOffset < screenHeight / 2 && yClickOffset > 0) {
			        			
	        			// Oikean reunan alin nappula
	        			if (yClickOffset < screenHeight / 2 - 106 && yClickOffset > 0) {
	        				//Log.v(TAG, "***** OIKEAN REUNAN ALIN NAPPULA *****");
	        				hud.triggerClick(HUD.BUTTON_1);
	        			}
	        			
	        			// Oikean reunan keskimmäinen nappula
	        			else if (yClickOffset < screenHeight / 2 - 53 && yClickOffset > 53) {
	        				//Log.v(TAG, "***** OIKEAN REUNAN KESKIMMÄINEN NAPPULA *****");
	        				hud.triggerClick(HUD.BUTTON_1);
	        			}

	        			// Oikean reunan ylin nappula
	        			else if (yClickOffset < screenHeight / 2 && yClickOffset > 106) {
	        				//Log.v(TAG, "***** OIKEAN REUNAN YLIN NAPPULA *****");
	        				hud.triggerClick(HUD.BUTTON_2);
	        			}
	        		}
		        		 
	        		// Vasemman reunan napit
	        		else if (xClickOffset > -screenWidth / 2 && xClickOffset < -screenWidth / 2 + 100 &&
	        			     yClickOffset < -screenHeight / 2 + 96 && yClickOffset > -screenHeight / 2) {
	        				// Vasemman reunan alempi nappula
	        				if (yClickOffset < -screenHeight / 2 + 48 && yClickOffset > -screenHeight / 2) {
	        					//Log.v(TAG, "***** VASEMMAN REUNAN ALEMPI NAPPULA *****");
	        				}
	        				// Vasemman reunan ylempi nappula
	        				else {
	        					//Log.v(TAG, "***** VASEMMAN REUNAN YLEMPI NAPPULA *****");
	        				}
	        			}
	        		
	        		// Painetaan pelikentältä
	        		else {
	        			weaponManager.triggerShoot(convertCoords((int)event.getX(), (int)event.getY()));
    					//Log.v(TAG, "***** PELIKENTTÄ *****");
	        		}
		        	return true;
		        }
		        // Liikutetaan sormea
		        /*if (event.getAction() == MotionEvent.ACTION_MOVE) {
		            //xOffset += xTouch - (int) event.getX();
		            //yOffset += yTouch - (int) event.getY();
		        	Log.v(TAG, "ACTION_MOVE:n sisällä");
		            //xTouch = (int) event.getX();
		            //yTouch = (int) event.getY();
		        	
		        	// TÄHÄN KOHTAAN LIIKUTUKSESSA TAPAHTUVAT ASIAT
		        	//
		        	// TÄHÄN KOHTAAN LIIKUTUKSESSA TAPAHTUVAT ASIAT
		        	return true;
		        }
		        // Painamisen loputtua*/
		        if (event.getAction() == MotionEvent.ACTION_UP) {
		        	
		        	//Log.v(TAG, "Painamisen lopettaminen alkaa nyt.");
		        	
		        	if (Math.abs(event.getX() - xClickOffset) < touchMarginal &&
		                Math.abs(event.getY() - yClickOffset) < touchMarginal) {

		            }
		        	//Log.v(TAG, "Painaminen loppu NY.");
		        	return true;
		        }
		        //Log.v(TAG, "Returnoidaanpa täältä FALSE..");
				return false;
			}
        });
	}
	
	// Muuntaa näytön koordinaatit pelimaailman koordinaateiksi
	private final int[] convertCoords(int _x, int _y)
	{
		//Log.v(TAG, "screenCoords[{" + (_x - (screenWidth/ 2)) + ", " + -((-screenHeight / 2) + _y) + "}]");
		int screenCoords[] = { _x - (screenWidth / 2),
							   -((-screenHeight / 2) + _y)};
		return screenCoords;
	}
}
