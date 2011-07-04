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
    private int     xClickOffset;
    private int     yClickOffset;
    private int     yClickFirstBorder;
    private int	    yClickSecondBorder;
    private int	    yClickThirdBorder;
    private int     touchMarginal      = 32; // Alue, jonka kosketus antaa anteeksi hyväksyäkseen kosketuksen pelkäksi painallukseksi
    private int[][] touchPath;
    private int     pointerCount       = 1;  // Indeksi motioneventin pisteiden määrälle

    /* Kuvan tiedot */
    private int screenWidth;  // Ruudun leveys
    private int screenHeight; // Ruudun korkeus

    /* Joystickin tiedot */
    private static int     joystickX 	 = Joystick.joystickX;
    private static int     joystickY     = Joystick.joystickY;
    private static boolean joystickInUse = false;
    private long           startTime     = 0;

    /**
     * Alustaa luokan muuttujat.
     *
     * @param DisplayMetrics Näytön tiedot
     * @param GLSurfaceView  OpenGL-pinta
     * @param Context		 Ohjelman konteksti
     * @param Hud			 Pelin käyttöliittymä
     * @param WeaponManager  Osoitin WeaponManageriin
     */
    public TouchManager(DisplayMetrics _dm, GLSurfaceView _glSurfaceView, Context _context, Hud _hud, WeaponManager _weaponManager)
    {
    	// Tallennetaan tarvittavien luokkien osoittimet
        weaponManager = _weaponManager;
        wrapper       = Wrapper.getInstance();
        hud           = _hud;
        
        // Tallennetaan pinta
        surface = _glSurfaceView;
        
        // Tallennetaan näytön koot
        screenWidth   = _dm.widthPixels;
        screenHeight  = _dm.heightPixels;
        
        // Alustetaan liikkeen polun taulukko
        touchPath 		= new int[10][2];
        touchPath[0][0] = 0;
        touchPath[0][1] = 0;
        
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
        else {
        	yClickFirstBorder  = screenHeight / 2 - (int)(144 * Options.scaleY + 0.5f);		 // 96
        	yClickSecondBorder = screenHeight / 2 - (int)(144 * Options.scaleY + 0.5f) - 32; // 64
        	yClickThirdBorder  = screenHeight / 2 - (int)(144 * Options.scaleY + 0.5f) - 64; // 32
        }
         
        Log.v("TouchManager", "FirstBorder="   + yClickFirstBorder +
        	                  " SecondBorder=" + yClickSecondBorder +
        	                  " ThirdBorder="  + yClickThirdBorder);
        
        // Käsitellään kosketustapahtumat
        setSurfaceListeners();
        
        // Käynnistetään joystick
        // Joystick.initJoystick();
    }

    /**
     * Asettaa TouchListenerit ja käsittelee kosketustapahtumat.
     */
    private final void setSurfaceListeners()
    {

        surface.setOnTouchListener(new OnTouchListener() {
        	
            public boolean onTouch(View v, MotionEvent event) {

            	// Painamisen aloitus
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    xClickOffset = (int) event.getX();
                    yClickOffset = screenHeight - (int) event.getY();
                    
                    //Log.v("TouchManager", "**ACTION_DOWN** xClickOffset=" + xClickOffset + "yClickOffset=" + yClickOffset +
                    //						" getX=" + event.getX() + " getY=" + event.getY());
                    
                    // Log.v("TouchManager", "xClickOffset=" + xClickOffset + "yClickOffset=" + yClickOffset + "rawX=" + event.getRawX() + " rawY=" + event.getRawY());
                    // Log.v("TouchManager", "getX()=" + event.getX() + " getY()" + event.getY() + " rawX=" + event.getRawX() + " rawY=" + event.getRawY());
                    //Log.v("TouchManager", "**ACTION_DOWN** xClickOffset=" + xClickOffset + "yClickOffset=" + yClickOffset);
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
                    		 yClickOffset < yClickSecondBorder) {

                        // Vasemman reunan alempi nappula
                        if (yClickOffset < yClickThirdBorder && yClickOffset > 0) {
                            // ***** VASEMMAN REUNAN ALEMPI NAPPULA *****
                        	Log.v("TouchManager", "**Vasemman reunan alempi nappula**");
                            hud.triggerClick(Hud.SPECIAL_2);
                        }
                        // Vasemman reunan ylempi nappula
                        else if (yClickOffset < yClickSecondBorder && yClickOffset > yClickThirdBorder) {
                            // ***** VASEMMAN REUNAN YLEMPI NAPPULA *****
                        	Log.v("TouchManager", "**Vasemman reunan ylempi nappula**");
                            hud.triggerClick(Hud.SPECIAL_1);
                        }
                    }
                    // Painetaan pelikentältä
                    else {
                        weaponManager.triggerShoot(convertCoords((int)event.getX(), (int)event.getY()), Wrapper.CLASS_TYPE_PLAYER, 
                        						   wrapper.player.x, wrapper.player.y);
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
                    
                    //return true;
                }
            	
                if (event.getAction() == MotionEvent.ACTION_MOVE) {
                    if (weaponManager.isUsingMotionEvents) {

                    	/* Ajasta riippumattoman kosketuspolun seuranta */
                    	xClickOffset = (int) event.getX() - screenWidth / 2;
                    	yClickOffset = screenHeight / 2 - (int) event.getY();
                    		
                    	//Log.v("TouchManager", "**ACTION_MOVE** xClickOffset=" + xClickOffset + "yClickOffset=" + yClickOffset +
                        //         		      " pointerCount=" + pointerCount);
                    		
                    	// Tarkistetaan onko seuraava kosketuskohta 8px päässä edellisestä kosketuskohdasta
                    	if (pointerCount < 10) {
                    		if (Math.abs(touchPath[pointerCount - 1][0] - xClickOffset) >= 8 || Math.abs(touchPath[pointerCount - 1][1] - yClickOffset) >= 8) {
                    			setPathPoint(xClickOffset, yClickOffset, pointerCount);
                    			pointerCount++;
                    		}
                    	}
                    }
                    else {
                    	return false;
                    }

                	/*
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
                    	
                    	xClickOffset = (int) event.getX() - screenWidth / 2;
                        yClickOffset = screenHeight / 2 - (int) event.getY();
                    	
                    	// Verrataan sormen sijaintia joystickin sijaintiin
                        double xDiff = Math.abs((double)(xClickOffset - joystickX));
                        double yDiff = Math.abs((double)(yClickOffset - joystickY));
                        
                        // Määritetään sormen ja joystickin välinen kulma
                        int angle;
                        
                        // Jos sormi on joystickin oikealla puolella:
                        if (xClickOffset > joystickX) {
                            // Jos sormi on joystickin alapuolella:
                            if (yClickOffset > joystickY) {
                                angle = (int) ((Math.atan(yDiff/xDiff)*180)/Math.PI);
                            }
                            // Jos sormi on joystickin yläpuolella:
                            else if (yClickOffset < joystickY) {
                                angle = (int) (360 - (Math.atan(yDiff/xDiff)*180)/Math.PI);
                            }
                            else {
                                angle = 0;
                            }
                        }
                        // Jos sormi on joystickin vasemmalla puolella:
                        else if (xClickOffset < joystickX) {
                            // Jos sormi on joystickin yläpuolella:
                            if (yClickOffset < joystickY) {
                                angle = (int) (180 + (Math.atan(yDiff/xDiff)*180)/Math.PI);
                            }
                            // Jos sormi on joystickin alapuolella:
                            else if (yClickOffset > joystickY) {
                                angle = (int) (180 - (Math.atan(yDiff/xDiff)*180)/Math.PI);
                            }
                            else {
                                angle = 180;
                            }
                        }
                        // Jos sormi on suoraan joystickin ylä- tai alapuolella
                        else {
                            if (yClickOffset > joystickY) {
                                angle = 90;
                            }
                            else {
                                angle = 270;
                            }
                        }
                
                        wrapper.player.direction = angle;
                    }
                    */
                    //return true;
                }
				
                // Nostetaan sormi pois
                if (event.getAction() == MotionEvent.ACTION_UP) {

                    joystickInUse = false;
                    startTime = 0;
                    
                    /* Lähetetään ja nollataan kosketuspolun indeksointi */
                    for (int i = 0; i < 10; i++) {
                    	// Tulostaa taulukon LogCatiin
                		Log.v("TouchManager", "touchPath[" + i + "][" + touchPath[i][0] + "][" + touchPath[i][1] + "]");
                		
                		weaponManager.triggerMotionShoot(touchPath);
                		
                		touchPath[i][0] = 0;
                		touchPath[i][1] = 0;
                	}
                    pointerCount = 1;
                    
                    Log.v("TouchManager", "**ACTION_UP**");// + xClickOffset + " yClickOffset=" + yClickOffset +
                    					   // " getX=" + event.getX() + " getY=" + event.getY());
                    if (Math.abs(event.getX() - (xClickOffset - screenWidth / 2)) < touchMarginal &&
                        Math.abs(event.getY() - (yClickOffset - screenHeight / 2)) < touchMarginal) {
                    	// Tässä kohtaa pelaaja nostaa sormensa napin päältä ...
                    }

                    //return true;
                }
                // Käytä painaminen
				return true;

                //return true;
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
     * Tallentaa kosketusreitin pisteitä
     * 
     * @param int X-koordinaatti
     * @param int Y-koordinaatti
     * @param int Reitin pisteen indeksi
     */
    private final void setPathPoint(int _x, int _y, int _index)
    {
    	if (_index < 10) {
    		touchPath[_index][0] = _x;
    		touchPath[_index][1] = _y;
    	}
    }
}
