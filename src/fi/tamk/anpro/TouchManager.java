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
	public 	int yClickFirstBorder;
	public 	int yClickSecondBorder;
	public 	int yClickThirdBorder;
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
        
        yClickFirstBorder = screenHeight / 2 - 176;
        yClickSecondBorder = screenHeight / 2 - 110;
        yClickThirdBorder = screenHeight / 2 - 44;
        
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
		        	//      750           800        700 &&      750          800 
		        	if (xClickOffset > screenWidth - 100 * Options.scale && xClickOffset < screenWidth) {
			        			
	        			// Oikean reunan alin nappula
	        			if (yClickOffset < yClickFirstBorder * Options.scale && yClickOffset > 0) {
	        				// ***** OIKEAN REUNAN ALIN NAPPULA *****
	        				hud.triggerClick(Hud.BUTTON_3);
	        			}
	        			
	        			// Oikean reunan keskimmäinen nappula
	        			else if (yClickOffset < yClickSecondBorder * Options.scale && yClickOffset > 66 * Options.scale) {
	        				// ***** OIKEAN REUNAN KESKIMMÄINEN NAPPULA *****
	        				hud.triggerClick(Hud.BUTTON_2);
	        			}

	        			// Oikean reunan ylin nappula
	        			else if (yClickOffset < yClickThirdBorder * Options.scale && yClickOffset > 132 * Options.scale) {
	        				// ***** OIKEAN REUNAN YLIN NAPPULA *****
	        				hud.triggerClick(Hud.BUTTON_1);
	        			}
	        		}
	        		// Vasemman reunan napit
		        	//            20              -400
	        		else if (xClickOffset < 64 * Options.scale && xClickOffset > 0) {
	        			
        				// Vasemman reunan alempi nappula
        				if (yClickOffset < yClickFirstBorder * Options.scale && yClickOffset > 0) {
        					// ***** VASEMMAN REUNAN ALEMPI NAPPULA *****
        					hud.triggerClick(Hud.SPECIAL_2);
        				}
        				// Vasemman reunan ylempi nappula
        				else if (yClickOffset < yClickSecondBorder * Options.scale && yClickOffset > 66 * Options.scale) {
        					// ***** VASEMMAN REUNAN YLEMPI NAPPULA *****
        					hud.triggerClick(Hud.SPECIAL_1);
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
