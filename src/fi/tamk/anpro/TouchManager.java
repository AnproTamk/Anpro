package fi.tamk.anpro;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.DisplayMetrics;
import android.util.Log;
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
    private Wrapper       wrapper;

    /* Kosketuksen tiedot */
    private int xTouch;
    private int yTouch;
    private int xClickOffset;
    private int yClickOffset;
    private int yClickFirstBorder;
    private int yClickSecondBorder;
    private int yClickThirdBorder;
    private int touchMarginal      = 16; // Alue, jonka kosketus antaa anteeksi hyväksyäkseen
    									 // kosketuksen pelkäksi painallukseksi

    /* Kuvan tiedot */
    private int screenWidth;  // Ruudun leveys
    private int screenHeight; // Ruudun korkeus

    /* Joystickin tiedot */
    private static float     joystickX;
    private static float     joystickY;
    private static boolean 	 joystickActivated = false;
    private static boolean   joystickInUse     = false;
    private long             startTime         = 0;

    /**
     * Alustaa luokan muuttujat.
     *
     * @param DisplayMetrics Näytön tiedot
     * @param GLSurfaceView  OpenGL-pinta
     * @param Context		 Ohjelman konteksti
     * @param Hud			 Pelin käyttöliittymä
     */
    public TouchManager(DisplayMetrics _dm, GLSurfaceView _glSurfaceView, Context _context, Hud _hud)
    {
    	// Tallennetaan tarvittavien luokkien osoittimet
        weaponManager = WeaponManager.getConnection();
        wrapper       = Wrapper.getInstance();
        hud           = _hud;
        
        // Tallennetaan pinta
        surface = _glSurfaceView;
        
        // Tallennetaan näytön koot
        screenWidth   = _dm.widthPixels;
        screenHeight  = _dm.heightPixels;
        
        // Määritetään kentän rajat
        if (screenHeight == 320) {
        	yClickFirstBorder  = screenHeight / 2 - (int)(96 * Options.scaleY + 0.5f);		// 96
        	yClickSecondBorder = screenHeight / 2 - (int)(96 * Options.scaleY + 0.5f) - 32; // 64
        	yClickThirdBorder  = screenHeight / 2 - (int)(96 * Options.scaleY + 0.5f) - 64; // 32
        }
        else if (screenHeight == 480) {
        	yClickFirstBorder  = screenHeight / 2 - (int)(144 * Options.scaleY + 0.5f);		 // 96
        	yClickSecondBorder = screenHeight / 2 - (int)(144 * Options.scaleY + 0.5f) - 32; // 64
        	yClickThirdBorder  = screenHeight / 2 - (int)(144 * Options.scaleY + 0.5f) - 64; // 32
        }
        
        /*
         *  <button id="weapon_button_1" x="384" y="-224" type="weapon" />
         *  <button id="weapon_button_2" x="384" y="-192" type="weapon" />
         */
        
        Log.v("TouchManager", "FirstBorder="  + yClickFirstBorder +
        	                  " SecondBorder=" + yClickSecondBorder +
        	                  " ThirdBorder="  + yClickThirdBorder);
        
        // Käsitellään kosketustapahtumat
        setSurfaceListeners();
    }

    /**
     * Asettaa TouchListenerit ja käsittelee kosketustapahtumat.
     */
    private final void setSurfaceListeners()
    {

        surface.setOnTouchListener(new OnTouchListener() {
        	
            public boolean onTouch(View v, MotionEvent event) {

                if (event.getAction() == MotionEvent.ACTION_MOVE) {
                    
                    if (!joystickInUse) {
                        if (startTime == 0) {
                            startTime = android.os.SystemClock.uptimeMillis();
                            return true;
                        }
                        else if (android.os.SystemClock.uptimeMillis() - startTime >= 750) {
                            joystickInUse = true;
                        }
                    }
                    
                    if (joystickInUse) {
                    	
                    	/* Verrataan sormen sijaintia joystickin sijaintiin */
                        double xDiff = Math.abs((double)(event.getRawX() - joystickX));
                        double yDiff = Math.abs((double)(event.getRawY() - joystickY));
                        
                        /* Määritetään sormen ja joystickin välinen kulma */
                        int angle;
                        
                        // Jos sormi on pelaajan joystickin puolella:
                        if (event.getRawX() > joystickX) {
                            // Jos sormi on joystickin alapuolella:
                            if (event.getRawY() < joystickY) {
                                angle = (int) ((Math.atan(yDiff/xDiff)*180)/Math.PI);
                            }
                            // Jos sormi on joystickin yläpuolella:
                            else if (event.getRawY() > joystickY) {
                                angle = (int) (360 - (Math.atan(yDiff/xDiff)*180)/Math.PI);
                            }
                            else {
                                angle = 0;
                            }
                        }
                        // Jos sormi on joystickin oikealla puolella:
                        else if (event.getRawX() < joystickX) {
                            // Jos sormi on joystickin yläpuolella:
                            if (event.getRawY() > joystickY) {
                                angle = (int) (180 + (Math.atan(yDiff/xDiff)*180)/Math.PI);
                            }
                            // Jos sormi on joystickin alapuolella:
                            else if (event.getRawY() < joystickY) {
                                angle = (int) (180 - (Math.atan(yDiff/xDiff)*180)/Math.PI);
                            }
                            else {
                                angle = 180;
                            }
                        }
                        // Jos sormi on suoraan joystickin ylä- tai alapuolella
                        else {
                            if (event.getRawY() > joystickY) {
                                angle = 270;
                            }
                            else {
                                angle = 90;
                            }
                        }
                
                        wrapper.player.direction = angle;
                    }
                    
                    return true;
                }
                
                // Painamisen aloitus
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    xClickOffset = (int) event.getX();
                    yClickOffset = screenHeight - (int) event.getY();
                    // Log.v("TouchManager", "xClickOffset=" + xClickOffset + "yClickOffset=" + yClickOffset + "rawX=" + event.getRawX() + " rawY=" + event.getRawY());
                    // Log.v("TouchManager", "getX()=" + event.getX() + " getY()" + event.getY() + " rawX=" + event.getRawX() + " rawY=" + event.getRawY());
                    // Oikean reunan napit
                    if (xClickOffset > screenWidth - 100 * Options.scaleX && xClickOffset < screenWidth &&
                    	yClickOffset < yClickFirstBorder) {
                    	
                        // Oikean reunan alin nappula
                        if (yClickOffset < yClickThirdBorder && yClickOffset > 0) {
                        	Log.v("TouchManager", "**Oikean reunan alin nappula**");
                            // ***** OIKEAN REUNAN ALIN NAPPULA *****
                            hud.triggerClick(Hud.BUTTON_3);
                        }

                        // Oikean reunan keskimmäinen nappula
                        else if (yClickOffset < yClickSecondBorder && yClickOffset > yClickThirdBorder) {
                            // ***** OIKEAN REUNAN KESKIMMÄINEN NAPPULA *****
                        	Log.v("TouchManager", "**Oikean reunan keskimmäinen nappula**");
                            hud.triggerClick(Hud.BUTTON_2);
                        }

                        // Oikean reunan ylin nappula
                        else if (yClickOffset < yClickFirstBorder && yClickOffset > yClickSecondBorder) {
                            // ***** OIKEAN REUNAN YLIN NAPPULA *****
                        	Log.v("TouchManager", "**Oikean reunan ylin nappula**");
                            hud.triggerClick(Hud.BUTTON_1);
                        }
                    }
                    // Vasemman reunan napit
                    else if (xClickOffset < screenWidth - 700 * Options.scaleX && xClickOffset > 0 &&
                    		 yClickOffset < yClickThirdBorder) {

                        // Vasemman reunan alempi nappula
                        if (yClickOffset < yClickThirdBorder && yClickOffset > 0) {
                            // ***** VASEMMAN REUNAN ALEMPI NAPPULA *****
                            hud.triggerClick(Hud.SPECIAL_2);
                        }
                        // Vasemman reunan ylempi nappula
                        else if (yClickOffset < yClickSecondBorder && yClickOffset > yClickThirdBorder) {
                            // ***** VASEMMAN REUNAN YLEMPI NAPPULA *****
                            hud.triggerClick(Hud.SPECIAL_1);
                        }
                    }
                    // Painetaan pelikentältä
                    else {
                        weaponManager.triggerShoot(convertCoords((int)event.getX(), (int)event.getY()));
                        // ***** PELIKENTTÄ *****
                    }

                    if (!joystickInUse) {
                        if (startTime == 0) {
                            startTime = android.os.SystemClock.uptimeMillis();
                        }
                        else if (android.os.SystemClock.uptimeMillis() - startTime >= 1000) {
                            joystickInUse = true;
                        }
                    }
                    
                    return true;
                }

                // Liikutetaan sormea
                if (event.getAction() == MotionEvent.ACTION_UP) {

                    joystickInUse = false;
                    startTime = 0;

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
    	// TODO: Pitää keksiä toinen tapa tietojen palauttamiseen. int-taulukko jää muistiin :(
        int screenCoords[] = {_x - (screenWidth / 2),
                              -((-screenHeight / 2) + _y)};

        return screenCoords;
    }

    /**
     * Ottaa Joystickin käyttöön ja tallentaa sen koordinaatit.
     *
     * @param int X-koordinaatti
     * @param int Y-koordinaatti
     */
    public static void initJoystick(float _x, float _y)
    {
        joystickX         = _x;
        joystickY         = _y;
        joystickActivated = true; // TODO: Muualla koodissa pitää tarkistaa, onko joystick käytössä vai ei
    }
}
