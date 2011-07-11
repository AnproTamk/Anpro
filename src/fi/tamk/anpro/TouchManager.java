package fi.tamk.anpro;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.DisplayMetrics;
import android.util.Log;
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
    private int     xClickOffset;
    private int     yClickOffset;
    private int     yClickFirstBorder;
    private int	    yClickSecondBorder;
    private int	    yClickThirdBorder;
    private int     touchMarginal      = 32; // Alue, jonka kosketus antaa anteeksi hyv�ksy�kseen kosketuksen pelk�ksi painallukseksi
    private int[][] touchPath;				 // Taulukko motioneventin pisteille
    private int     pointerCount       = 1;  // Indeksi motioneventin pisteiden m��r�lle

    /* Kuvan tiedot */
    private int screenWidth;  // Ruudun leveys
    private int screenHeight; // Ruudun korkeus

    /* Joystickin tiedot */
    private int  joystickX  = Joystick.joystickX;
    private int  joystickY  = Joystick.joystickY;

    /**
     * Alustaa luokan muuttujat.
     *
     * @param DisplayMetrics N�yt�n tiedot
     * @param GLSurfaceView  OpenGL-pinta
     * @param Context		 Ohjelman konteksti
     * @param Hud			 Pelin k�ytt�liittym�
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
        
        // Tallennetaan n�yt�n koot
        screenWidth   = _dm.widthPixels;
        screenHeight  = _dm.heightPixels;
        
        // Alustetaan liikkeen polun taulukko
        touchPath 		= new int[10][2];
        touchPath[0][0] = 0;
        touchPath[0][1] = 0;
        
        // M��ritet��n kent�ll� olevat rajat nappien painamiselle eri n�yt�n korkeuksien mukaan
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

        // K�sitell��n kosketustapahtumat
        setSurfaceListeners();
    }

    /**
     * Asettaa TouchListenerit ja k�sittelee kosketustapahtumat.
     */
    private final void setSurfaceListeners()
    {

        surface.setOnTouchListener(new OnTouchListener() {
        	
            public boolean onTouch(View v, MotionEvent event) {

            	/* ACTION_DOWN */
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    //xClickOffset = (int) event.getX();
                    //yClickOffset = screenHeight - (int) event.getY();
                	xClickOffset = (int) event.getX() - screenWidth / 2;
                    yClickOffset = screenHeight / 2 - (int) event.getY();
                    
                    Log.v("navigare","x = " + xClickOffset + " y = " + yClickOffset + " jX = " + joystickX + " jY = " + joystickY );

                    /* Oikean reunan napit */
                    if (xClickOffset > screenWidth - 100 * Options.scaleX && xClickOffset < screenWidth &&
                    	yClickOffset < yClickFirstBorder) {
                    	
                        // Oikean reunan alin nappula
                        if (yClickOffset < yClickThirdBorder && yClickOffset > 0) {
                            // ***** OIKEAN REUNAN ALIN NAPPULA *****
                            hud.triggerClick(Hud.BUTTON_3);
                        }

                        // Oikean reunan keskimm�inen nappula
                        else if (yClickOffset < yClickSecondBorder && yClickOffset > yClickThirdBorder) {
                            // ***** OIKEAN REUNAN KESKIMM�INEN NAPPULA *****
                            hud.triggerClick(Hud.BUTTON_2);
                        }

                        // Oikean reunan ylin nappula
                        else if (yClickOffset < yClickFirstBorder && yClickOffset > yClickSecondBorder) {
                            // ***** OIKEAN REUNAN YLIN NAPPULA *****
                            hud.triggerClick(Hud.BUTTON_1);
                        }
                    }
                    
                    /* Painetaan joystickin p��ll� */
                    if (joystickX != 0 && joystickY != 0 && xClickOffset > joystickX - 64 && xClickOffset < joystickX + 64 &&
                    	yClickOffset > joystickY - 64 && yClickOffset < joystickY + 64) {
                             Joystick.joystickDown = true;
                    }
                    
                    /* Painetaan pelikent�lt� */
                    else {
                        weaponManager.triggerPlayerShoot(event.getX() - screenWidth/2, -((-screenHeight / 2) + event.getY()));
                    }
                }
                
                /* ACTION_MOVE */
                if (event.getAction() == MotionEvent.ACTION_MOVE) {
                	xClickOffset = (int) event.getX() - screenWidth / 2;
                    yClickOffset = screenHeight / 2 - (int) event.getY();

                    /* Ajasta riippumattoman kosketuspolun seuranta */
                    if (weaponManager.isUsingMotionEvents) {
                    	// Tarkistetaan onko seuraava kosketuskohta 8px p��ss� edellisest� kosketuskohdasta
                    	if (pointerCount < 10) {
                    		if (Math.abs(touchPath[pointerCount - 1][0] - xClickOffset) >= 8 || Math.abs(touchPath[pointerCount - 1][1] - yClickOffset) >= 8) {
                    			setPathPoint(xClickOffset, yClickOffset, pointerCount);
                    			pointerCount++;
                    		}
                    	}
                    }
                    
                    /* Joystickin aktivointi */
                    if (!Joystick.joystickInUse && Joystick.joystickDown && xClickOffset > joystickX - 64 && xClickOffset < joystickX + 64 &&
                    														yClickOffset > joystickY - 64 && yClickOffset < joystickY + 64) {
                            Joystick.joystickInUse = true;
                    }
                    
                    else if (Joystick.joystickInUse) {
                    	Joystick.useJoystick(xClickOffset, yClickOffset, (int)event.getX(), (int)event.getY(), wrapper);
                    	// Lopetetaan joystickin k�ytt�minen, jos sormen et�isyys siit� on yli 50
                    	if (Utility.getDistance((float)xClickOffset, (float)yClickOffset, (float)joystickX, (float)joystickY) > 64) {
                    		Joystick.joystickDown = false;
                    		Joystick.joystickInUse = false;
                    	}
                    }
                    
                    return true;
                }
				
                // Nostetaan sormi pois
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    Joystick.joystickInUse = false;
                    Joystick.joystickDown  = false;
                    
                    // Asetetaan pelaajan jarrutus
                    wrapper.player.movementAcceleration = -6;
                    
                    /* L�hetet��n ja nollataan kosketuspolun indeksointi jos oikea ase on valittuna */
                    if (weaponManager.isUsingMotionEvents) {
                    	for (int i = 0; i < 10; i++) {
                    		// Tulostaa taulukon LogCatiin
                			//Log.v("TouchManager", "touchPath[" + i + "][" + touchPath[i][0] + "][" + touchPath[i][1] + "]");
                    		
                    		// Laukaisee ampumisen touchPath[][]-taulukon arvoilla
                			// weaponManager.triggerMotionShoot(touchPath);
                		
                			touchPath[i][0] = 0;
                			touchPath[i][1] = 0;
                		}
                    	pointerCount = 1;
                    }

                    if (Math.abs(event.getX() - (xClickOffset - screenWidth / 2)) < touchMarginal &&
                        Math.abs(event.getY() - (yClickOffset - screenHeight / 2)) < touchMarginal) {
                    	// T�ss� kohtaa pelaaja nostaa sormensa napin p��lt� liikuttamatta sit� pois napin alueelta ...
                    }
                }
                
                // K�yt� painaminen
				return true;
            }
        });
    }
    
    /**
     * Tallentaa kosketusreitin pisteit�
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
