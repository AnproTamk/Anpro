package fi.tamk.anpro;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

/**
 * Hoitaa kosketuksen lisäämällä OpenGL-pintaan tarvittavat TouchListenerit ja tutkii
 * jokaisen kosketuksen. Kutsuu kosketuksen perusteella joko WeaponManageria tai Hudia.
 */
public class TouchManager
{
	/* Osoittimet muihin luokkiin */
	private GLSurfaceView surface;
	public  WeaponManager weaponManager;
	public  Hud           hud;
	
	/* Kosketuksen tiedot */
	public  int xTouch;
	public  int yTouch;
	public  int xClickOffset;
	public  int yClickOffset;
	public  int touchMarginal = 16; // Alue, jonka kosketus antaa anteeksi hyväksyäkseen kosketuksen pelkäksi painallukseksi
	
	/* Kuvan tiedot */
	private int screenWidth;  // Ruudun leveys
    private int screenHeight; // Ruudun korkeus
	
	/**
	 * Alustaa luokan muuttujat.
	 * 
	 * @param DisplayMetrics Kuvan tiedot
	 * @param GLSurfaceView  OpenGL-pinta
	 * @param Context		 Ohjelman konteksti
	 * @param Hud			 Pelin käyttöliittymä
	 */
    protected TouchManager(DisplayMetrics _dm, GLSurfaceView _glSurfaceView, Context _context, Hud _hud)
    {
    	weaponManager = WeaponManager.getConnection();
    	hud           = _hud;
    	screenWidth   = _dm.widthPixels;
        screenHeight  = _dm.heightPixels;
        
        setSurfaceListeners(_glSurfaceView);
    }
    
    /**
     * Asettaa TouchListenerit ja käsittelee kosketustapahtumat.
     * 
     * @param GLSurfaceView OpenGL-pinta
     */
	public final void setSurfaceListeners(GLSurfaceView _surface)
	{
		surface = _surface;
		
		surface.setOnTouchListener(new OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {
				
				// Painamisen aloitus
		        if (event.getAction() == MotionEvent.ACTION_DOWN) {
		        	xClickOffset = (int) event.getX();
		        	yClickOffset = screenHeight - (int) event.getY();
		        	
		        	// Oikean reunan napit
		        	if (xClickOffset > screenWidth - 100 && xClickOffset < screenWidth &&
			            yClickOffset < screenHeight / 2 && yClickOffset > 0) {
			        			
	        			// Oikean reunan alin nappula
	        			if (yClickOffset < screenHeight / 2 - 106 && yClickOffset > 0) {
	        				// ***** OIKEAN REUNAN ALIN NAPPULA *****
	        				hud.triggerClick(Hud.BUTTON_1);
	        			}
	        			
	        			// Oikean reunan keskimmäinen nappula
	        			else if (yClickOffset < screenHeight / 2 - 53 && yClickOffset > 53) {
	        				// ***** OIKEAN REUNAN KESKIMMÄINEN NAPPULA *****
	        				hud.triggerClick(Hud.BUTTON_1);
	        			}

	        			// Oikean reunan ylin nappula
	        			else if (yClickOffset < screenHeight / 2 && yClickOffset > 106) {
	        				// ***** OIKEAN REUNAN YLIN NAPPULA *****
	        				hud.triggerClick(Hud.BUTTON_2);
	        			}
	        		}
	        		// Vasemman reunan napit
	        		else if (xClickOffset > -screenWidth / 2 && xClickOffset < -screenWidth / 2 + 100 &&
	        			     yClickOffset < -screenHeight / 2 + 96 && yClickOffset > -screenHeight / 2) {
	        			
        				// Vasemman reunan alempi nappula
        				if (yClickOffset < -screenHeight / 2 + 48 && yClickOffset > -screenHeight / 2) {
        					// ***** VASEMMAN REUNAN ALEMPI NAPPULA *****
        				}
        				// Vasemman reunan ylempi nappula
        				else {
        					// ***** VASEMMAN REUNAN YLEMPI NAPPULA *****
        				}
        			}
	        		// Painetaan pelikentältä
	        		else {
	        			weaponManager.triggerShoot(convertCoords((int)event.getX(), (int)event.getY()));
    					// ***** PELIKENTTÄ *****
	        		}
		        	return true;
		        }
		        
		        // Liikutetaan sormea
		        if (event.getAction() == MotionEvent.ACTION_UP) {
		        	if (Math.abs(event.getX() - xClickOffset) < touchMarginal &&
		                Math.abs(event.getY() - yClickOffset) < touchMarginal) {

		            }
		        	
		        	return true;
		        }
		        
				return false;
			}
        });
	}
	
	/**
	 * Muuntaa näytön koordinaatit pelimaailman koordinaateiksi.
	 * 
	 * @param int X-koordinaatti
	 * @param int Y-koordinaatti
	 * 
	 * @return int[] Pelimaailman vastaavat koordinaatit
	 */
	private final int[] convertCoords(int _x, int _y)
	{
		int screenCoords[] = {_x - (screenWidth / 2),
							  -((-screenHeight / 2) + _y)};
		
		return screenCoords;
	}
}
