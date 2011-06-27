package fi.tamk.anpro;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

/**
 * Hoitaa kosketuksen lis��m�ll� OpenGL-pintaan tarvittavat TouchListenerit ja tutkii
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
    public  int xTouch;
    public  int yTouch;
    public  int xClickOffset;
    public  int yClickOffset;
    public 	int yClickFirstBorder;
    public 	int yClickSecondBorder;
    public 	int yClickThirdBorder;
    public  int touchMarginal = 16; // Alue, jonka kosketus antaa anteeksi hyv�ksy�kseen kosketuksen pelk�ksi painallukseksi

    /* Kuvan tiedot */
    private int screenWidth;  // Ruudun leveys
    private int screenHeight; // Ruudun korkeus

    /* Joystickin tiedot */
    private static float   joystickX;
    private static float   joystickY;
    private static boolean joystickActivated = false;
    private static boolean joystickInUse     = false;
    private long           startTime         = 0;

    /**
     * Alustaa luokan muuttujat.
     *
     * @param DisplayMetrics Kuvan tiedot
     * @param GLSurfaceView  OpenGL-pinta
     * @param Context		 Ohjelman konteksti
     * @param Hud			 Pelin k�ytt�liittym�
     */
    protected TouchManager(DisplayMetrics _dm, GLSurfaceView _glSurfaceView, Context _context)
    {
        weaponManager = WeaponManager.getConnection();
        hud           = GLRenderer.hud;
        screenWidth   = _dm.widthPixels;
        screenHeight  = _dm.heightPixels;

        yClickFirstBorder = screenHeight / 2 - (int)(176 * Options.scaleY + 0.5f);
        yClickSecondBorder = screenHeight / 2 - (int)(110 * Options.scaleY + 0.5f);
        yClickThirdBorder = screenHeight / 2 - (int)(44 * Options.scaleY + 0.5f);
        
        wrapper = Wrapper.getInstance();

        setSurfaceListeners(_glSurfaceView);
    }

    /**
     * Asettaa TouchListenerit ja k�sittelee kosketustapahtumat.
     *
     * @param GLSurfaceView OpenGL-pinta
     */
    public final void setSurfaceListeners(GLSurfaceView _surface)
    {
        surface = _surface;

        surface.setOnTouchListener(new OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {

                if (event.getAction() == MotionEvent.ACTION_MOVE) {
                    
                    if (!joystickInUse) {
                        if (startTime == 0) {
                            startTime = android.os.SystemClock.uptimeMillis();
                            return true;
                        }
                        else if (android.os.SystemClock.uptimeMillis() - startTime >= 1000) {
                            joystickInUse = true;
                        }
                    }
                    
                    if (joystickInUse) {
                    	
                    	/* Verrataan sormen sijaintia joystickin sijaintiin */
                        double xDiff = Math.abs((double)(event.getRawX() - 600));
                        double yDiff = Math.abs((double)(event.getRawY() - 360));
                        
                        /* M��ritet��n sormen ja joystickin v�linen kulma */
                        int angle;
                        
                        // Jos sormi on pelaajan joystickin puolella:
                        if (event.getRawX() > 600) {
                            // Jos sormi on joystickin alapuolella:
                            if (event.getRawY() < 360) {
                                angle = (int) ((Math.atan(yDiff/xDiff)*180)/Math.PI);
                            }
                            // Jos sormi on joystickin yl�puolella:
                            else if (event.getRawY() > 360) {
                                angle = (int) (360 - (Math.atan(yDiff/xDiff)*180)/Math.PI);
                            }
                            else {
                                angle = 0;
                            }
                        }
                        // Jos sormi on joystickin oikealla puolella:
                        else if (event.getRawX() < 600) {
                            // Jos sormi on joystickin yl�puolella:
                            if (event.getRawY() > 360) {
                                angle = (int) (180 + (Math.atan(yDiff/xDiff)*180)/Math.PI);
                            }
                            // Jos sormi on joystickin alapuolella:
                            else if (event.getRawY() < 360) {
                                angle = (int) (180 - (Math.atan(yDiff/xDiff)*180)/Math.PI);
                            }
                            else {
                                angle = 180;
                            }
                        }
                        // Jos sormi on suoraan joystickin yl�- tai alapuolella
                        else {
                            if (event.getRawY() > 360) {
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

                    // Oikean reunan napit
                    if (xClickOffset > screenWidth - 100 * Options.scaleX && xClickOffset < screenWidth &&
                    	yClickOffset < yClickThirdBorder) {

                        // Oikean reunan alin nappula
                        if (yClickOffset < yClickFirstBorder && yClickOffset > 0) {
                            // ***** OIKEAN REUNAN ALIN NAPPULA *****
                            hud.triggerClick(Hud.BUTTON_3);
                        }

                        // Oikean reunan keskimm�inen nappula
                        else if (yClickOffset < yClickSecondBorder && yClickOffset > 66 * Options.scale) {
                            // ***** OIKEAN REUNAN KESKIMM�INEN NAPPULA *****
                            hud.triggerClick(Hud.BUTTON_2);
                        }

                        // Oikean reunan ylin nappula
                        else if (yClickOffset < yClickThirdBorder && yClickOffset > 132 * Options.scale) {
                            // ***** OIKEAN REUNAN YLIN NAPPULA *****
                            hud.triggerClick(Hud.BUTTON_1);
                        }
                    }
                    // Vasemman reunan napit
                    else if (xClickOffset < screenWidth - 700 * Options.scale && xClickOffset > 0 &&
                    		 yClickOffset < yClickThirdBorder) {

                        // Vasemman reunan alempi nappula
                        if (yClickOffset < yClickFirstBorder && yClickOffset > 0) {
                            // ***** VASEMMAN REUNAN ALEMPI NAPPULA *****
                            hud.triggerClick(Hud.SPECIAL_2);
                        }
                        // Vasemman reunan ylempi nappula
                        else if (yClickOffset < yClickSecondBorder && yClickOffset > 66 * Options.scale) {
                            // ***** VASEMMAN REUNAN YLEMPI NAPPULA *****
                            hud.triggerClick(Hud.SPECIAL_1);
                        }
                    }
                    // Painetaan pelikent�lt�
                    else {
                        weaponManager.triggerShoot(convertCoords((int)event.getX(), (int)event.getY()));
                        // ***** PELIKENTT� *****
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
     * Muuntaa n�yt�n koordinaatit pelimaailman koordinaateiksi.
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

    /**
     * Ottaa Joystickin k�ytt��n ja tallentaa sen koordinaatit.
     *
     * @param int X-koordinaatti
     * @param int Y-koordinaatti
     */
    public static void initJoystick(float _x, float _y)
    {
        joystickX         = _x;
        joystickY         = _y;
        joystickActivated = true;
    }
}
