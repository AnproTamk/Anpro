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
	/* Vakiot */
    private final byte JOYSTICK_TRESHOLD = 80; // Säde jolla joystick toimii
    
    /* Osoittimet muihin luokkiin */
    private GLSurfaceView surface;
    private WeaponManager weaponManager;
    private Hud           hud;
    private Wrapper       wrapper;

    /* Kosketuksen tiedot */
    private int     xClickOffsetFirstTouch;
    private int     yClickOffsetFirstTouch;
    private int     xClickOffsetSecondTouch;
    private int     yClickOffsetSecondTouch;
    private int     yClickFirstBorder;
    private int	    yClickSecondBorder;
    private int	    yClickThirdBorder;
    private int     touchMarginal      		 = 32; // Alue, jonka kosketus antaa anteeksi hyväksyäkseen kosketuksen pelkäksi painallukseksi
    private int[][] touchPath;					   // Taulukko motioneventin pisteille
    private int     pointerCount       		 = 1;  // Indeksi motioneventin pisteiden määrälle
    private int 	action;					 	   
    private int 	actionCode;				 	   
    //private int 	actionPointerId;		 	   

    /* Kuvan tiedot */
    private int screenWidth;  // Ruudun leveys
    private int screenHeight; // Ruudun korkeus

    /* Joystickin tiedot */
    private int  joystickX  = Joystick.joystickX;
    private int  joystickY  = Joystick.joystickY;

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
        
        // Määritetään kentällä olevat rajat nappien painamiselle eri näytön korkeuksien mukaan
        // TODO: SCALING (Tälle toteutukselle tarvitaan dynaaminen muoto.)
        if (screenHeight == 320) {
        	yClickFirstBorder  = -48;
        	yClickSecondBorder = -96;
        	yClickThirdBorder  = -128;
        }
        else if (screenHeight == 480) {
        	yClickFirstBorder  = -32;
        	yClickSecondBorder = -100;
        	yClickThirdBorder  = -168;
        }
        else {
        	yClickFirstBorder  = -32;
        	yClickSecondBorder = -100;
        	yClickThirdBorder  = -168;
        }
        
        Log.e("testi", "FirstBorder: " + yClickFirstBorder + " SecondBorder: " + yClickSecondBorder + " ThirdBorder" + yClickThirdBorder);
        
        // Asetetaan TouchListenerit
        setSurfaceListeners();
    }

	/* =======================================================
	 * Uudet funktiot
	 * ======================================================= */
    /**
     * Asettaa TouchListenerit ja käsittelee kosketustapahtumat.
     */
    public final void setSurfaceListeners()
    {
        surface.setOnTouchListener(new OnTouchListener() {
        	
            public boolean onTouch(View v, MotionEvent event) {
            	//dumpEvent(event);
            	action = event.getAction();
            	
            	/* KAHDELLA SORMELLA KOSKETTAMINEN */
            	if (event.getPointerCount() > 1) {	
            		//Log.d("testi", "2 sormea");
            		//actionPointerId = action & MotionEvent.ACTION_POINTER_ID_MASK;
            		actionCode = action & MotionEvent.ACTION_MASK;
            		
            	    action = event.getAction();
            	    
            	    /* Tarkistetaan sormien kosketukset */
            	    for (int i = 0; i < event.getPointerCount(); i++) {
            	    	/* Log.d("testi", "i = " + i +
            	    	 * 		 " getPointerId(i) = " + event.getPointerId(i) +
            	    	 * 		 " getX(i) = " + (int)event.getX(i) +
            	    	 * 		 " getY(i) = " + (int)event.getY(i));
            	    	 * Log.d("testi", "Asetetaan " + (i + 1) + ". kosketuksen koordinaateiksi: x = " + ((int)event.getX(i) - screenWidth / 2) + " y = " + (screenHeight / 2 - (int)event.getY(i)));
            	    	 * Log.d("testi", "Asetetaan " + (i + 1) + ". kosketuksen koordinaateiksi: x = " + ((int)event.getX(i) - screenWidth / 2) + " y = " + (screenHeight / 2 - (int)event.getY(i)));
            	    	 */
            	    	
            	    	/* 
            	    	 * i = 0 -> ensimmäinen kosketus
            	    	 * i = 1 -> toinen kosketus
            	    	 * getPointerId(0) = 0 -> ensimmäinen kosketus
            	    	 * getPointerId(1) = 1 -> toinen kosketus
            	    	 * getX(0)         = 0 -> ensimmäinen kosketus
            	    	 * getX(1)		   = 1 -> toinen kosketus
            	    	 */
            	    	
            	    	/* Ensimmäinen kosketus */
            	    	if (i == 0) {
            	    		xClickOffsetFirstTouch = (int) event.getX(i) - screenWidth / 2;
            	    		yClickOffsetFirstTouch = screenHeight / 2 - (int) event.getY(i);
            	    		
            	    		//Log.d("testi", i + 1 + ". kosketus asetettu.");
            	    		/* Joystickin aktivointi */
                            if (!Joystick.joystickInUse && Joystick.joystickDown && Utility.getDistance((float)xClickOffsetFirstTouch, (float)yClickOffsetFirstTouch, (float)joystickX, (float)joystickY) < JOYSTICK_TRESHOLD) {
                                    Joystick.joystickInUse = true;
                            }
                            
                            else if (Joystick.joystickInUse) {
                            	// Käytetään joystickia
                            	Joystick.useJoystick(xClickOffsetFirstTouch, yClickOffsetFirstTouch, (int)event.getX(), (int)event.getY(), wrapper);
                            	
                            	// Lopetetaan joystickin käyttäminen, jos sormen etäisyys siitä on yli sallitun rajan
                            	if (Utility.getDistance((float)xClickOffsetFirstTouch, (float)yClickOffsetFirstTouch, (float)joystickX, (float)joystickY) > JOYSTICK_TRESHOLD) {
                            		Joystick.joystickDown  = false;
                            		Joystick.joystickInUse = false;
                            		
                                    // Asetetaan pelaajan jarrutus
                                    wrapper.player.movementAcceleration = -6;
                            	}
                            }
            	    	}
            	    	
            	    	/* Toinen kosketus */
            	    	if (i == 1) {
            	    		xClickOffsetSecondTouch = (int) event.getX(i) - screenWidth / 2;
            	    		yClickOffsetSecondTouch = screenHeight / 2 - (int) event.getY(i);
            	    		//Log.d("testi", i + 1 + ". kosketus asetettu.");
            	    		
            	    		// Tähän triggerMotionShoot() käytettäväksi samaan aikaan kuin joystick.
            	    		
                            /* Sarjatuliaseen käyttäminen päästämättä kosketusta irti */
                            if (weaponManager.currentWeapon == WeaponManager.WEAPON_SPITFIRE) {
                            	weaponManager.triggerPlayerShoot(xClickOffsetSecondTouch, yClickOffsetSecondTouch);
                            }
            	    	
            	    	}
            	    }   
            	    
            	    /* ACTION_POINTER_DOWN tapahtuu, kun toinen sormi lasketaan kentälle */
            	    if (actionCode == MotionEvent.ACTION_POINTER_DOWN) {
            	    	//Log.d("testi", "ACTION_POINTER_DOWN");
            	    	 weaponManager.triggerPlayerShoot(xClickOffsetSecondTouch, yClickOffsetSecondTouch);
            	    }
            	    
            	    if (actionCode == MotionEvent.ACTION_POINTER_UP) {
            	    	Log.d("testi", "ACTION_POINTER_UP");
            	    	
            	    	// Tähän painikkeiden painamiset samaan aikaan kun joystick on jo toisella sormella käytössä.
            	    	
            	    }
            	}
            	
            	/* YHDELLÄ SORMELLA KOSKETTAMINEN */
            	else {
            		xClickOffsetFirstTouch = (int) event.getX() - screenWidth / 2;
            		yClickOffsetFirstTouch = screenHeight / 2 - (int) event.getY();
            		
            		//Log.e("testi", "1 sormen kosketus");
            		
            		/* ACTION_DOWN */
                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    	// Muistetaan mistä painallus alkaa
                    	xClickOffsetFirstTouch = (int) event.getX() - screenWidth / 2;
                        yClickOffsetFirstTouch = screenHeight / 2 - (int) event.getY();
                        Log.e("testi", "ACTION_DOWN: xFirst = " + xClickOffsetFirstTouch + " yFirst = " + yClickOffsetFirstTouch);
                        
                        /* Oikean reunan napit */
                        if (xClickOffsetFirstTouch > (screenWidth / 2) - 100 * Options.scaleX && xClickOffsetFirstTouch < (screenWidth / 2) &&
                        	yClickOffsetFirstTouch < yClickFirstBorder) {
                        	
                        	Log.e("testi", "OIKEASSA REUNASSA..");
                        	
                            // Oikean reunan alin nappula
                            if (yClickOffsetFirstTouch < yClickThirdBorder && yClickOffsetFirstTouch > (-screenHeight / 2)) {
                            	Log.e("testi", "ALIN NAPPULA");
                                hud.triggerClick(Hud.BUTTON_3);
                            }

                            // Oikean reunan keskimmäinen nappula
                            else if (yClickOffsetFirstTouch < yClickSecondBorder && yClickOffsetFirstTouch > yClickThirdBorder) {
                            	Log.e("testi", "KESKIMMÄINEN NAPPULA");
                                hud.triggerClick(Hud.BUTTON_2);
                            }

                            // Oikean reunan ylin nappula
                            else if (yClickOffsetFirstTouch < yClickFirstBorder && yClickOffsetFirstTouch > yClickSecondBorder) {
                            	Log.e("testi", "YLIN NAPPULA");
                                hud.triggerClick(Hud.BUTTON_1);
                            }
                        }
                        
                        /* Painetaan joystickin päällä */
                        if (joystickX != 0 && joystickY != 0 && Utility.getDistance((float)xClickOffsetFirstTouch, (float)yClickOffsetFirstTouch, (float)joystickX, (float)joystickY) < JOYSTICK_TRESHOLD) {
                                 Joystick.joystickDown = true;
                        }
                        
                        /* Painetaan pelikentältä */
                        else {
                            weaponManager.triggerPlayerShoot(event.getX() - screenWidth/2, -((-screenHeight / 2) + event.getY()));
                        }
                    }
                    
                    /* ACTION_MOVE */
                    if (event.getAction() == MotionEvent.ACTION_MOVE) {
                    	xClickOffsetFirstTouch = (int) event.getX() - screenWidth / 2;
                        yClickOffsetFirstTouch = screenHeight / 2 - (int) event.getY();
                        //Log.e("testi", "ACTION_MOVE");
                        
                        /* Painetaan joystickin päällä */
                        if (joystickX != 0 && joystickY != 0 && Utility.getDistance((float)xClickOffsetFirstTouch, (float)yClickOffsetFirstTouch, (float)joystickX, (float)joystickY) < JOYSTICK_TRESHOLD) {
                                 Joystick.joystickDown = true;
                        }
                        
                        /* Sarjatuliaseen käyttäminen päästämättä kosketusta irti */
                        else if (weaponManager.currentWeapon == WeaponManager.WEAPON_SPITFIRE) {
                        	weaponManager.triggerPlayerShoot(xClickOffsetFirstTouch, yClickOffsetFirstTouch);
                        }
                        
                        /* Ajasta riippumattoman kosketuspolun seuranta */
                        if (weaponManager.isUsingMotionEvents) {
                        	// Tarkistetaan onko seuraava kosketuskohta 8px päässä edellisestä kosketuskohdasta
                        	if (pointerCount < 10) {
                        		if (Math.abs(touchPath[pointerCount - 1][0] - xClickOffsetFirstTouch) >= 8 || Math.abs(touchPath[pointerCount - 1][1] - yClickOffsetFirstTouch) >= 8) {
                        			setPathPoint(xClickOffsetFirstTouch, yClickOffsetFirstTouch, pointerCount);
                        			pointerCount++;
                        		}
                        	}
                        }
                        
                        /* Joystickin aktivointi */
                        if (!Joystick.joystickInUse && Joystick.joystickDown && Utility.getDistance((float)xClickOffsetFirstTouch, (float)yClickOffsetFirstTouch, (float)joystickX, (float)joystickY) < JOYSTICK_TRESHOLD) {
                                Joystick.joystickInUse = true;
                        }
                        
                        else if (Joystick.joystickInUse) {
                        	// Käytetään joystickia
                        	Joystick.useJoystick(xClickOffsetFirstTouch, yClickOffsetFirstTouch, (int)event.getX(), (int)event.getY(), wrapper);
                        	
                        	// Lopetetaan joystickin käyttäminen, jos sormen etäisyys siitä on yli sallitun rajan
                        	if (Utility.getDistance((float)xClickOffsetFirstTouch, (float)yClickOffsetFirstTouch, (float)joystickX, (float)joystickY) > JOYSTICK_TRESHOLD) {
                        		Joystick.joystickDown  = false;
                        		Joystick.joystickInUse = false;
                        		
                        		// Asetetaan pelaajan jarrutus
                                wrapper.player.movementAcceleration = -6;
                        	}
                        }
                    }
    				
                    // Nostetaan sormi pois
                    if (event.getAction() == MotionEvent.ACTION_UP) {
                    	// Poistetaan joystick käytöstä
                        Joystick.joystickInUse = false;
                        Joystick.joystickDown  = false;
                        //Log.e("testi", "ACTION_UP");
                        
                        // Asetetaan pelaajan jarrutus
                        wrapper.player.movementAcceleration = -6;
                        
                        /* Lähetetään ja nollataan kosketuspolun indeksointi, jos oikea ase on valittuna */
                        if (weaponManager.isUsingMotionEvents) {
                        	
                        	// Laukaisee ampumisen touchPath[][]-taulukon arvoilla
                    		weaponManager.triggerMotionShoot(touchPath);
                    		
                    		// Tyhjennetään  touchPath[][]-taulukko seuraavaa ampumiskertaa varten
                        	for (int i = 0; i < 10; i++) {
                        		// Tulostaa taulukon LogCatiin
                    			//Log.v("TouchManager", "touchPath[" + i + "][" + touchPath[i][0] + "][" + touchPath[i][1] + "]");

                    			touchPath[i][0] = 0;
                    			touchPath[i][1] = 0;
                    		}
                        	pointerCount = 1;
                        }

                        if (Math.abs(event.getX() - (xClickOffsetFirstTouch - screenWidth / 2)) < touchMarginal &&
                            Math.abs(event.getY() - (yClickOffsetFirstTouch - screenHeight / 2)) < touchMarginal) {
                        	// Tässä kohtaa pelaaja nostaa sormensa napin päältä liikuttamatta sormeaan pois napin alueelta ...
                        }
                    }
            	}
            	
                // Käytä painaminen
				return true;
            }
        });
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
    
    /** Debuggausta varten multitouchin dumppaus LogCatiin */
    /*
    private void dumpEvent(MotionEvent event) {
       String names[] = { "DOWN" , "UP" , "MOVE" , "CANCEL" , "OUTSIDE" ,
          "POINTER_DOWN" , "POINTER_UP" , "7?" , "8?" , "9?" };
       StringBuilder sb = new StringBuilder();
       action = event.getAction();
       actionCode = action & MotionEvent.ACTION_MASK;
       sb.append("event ACTION_" ).append(names[actionCode]);
       if (actionCode == MotionEvent.ACTION_POINTER_DOWN
             || actionCode == MotionEvent.ACTION_POINTER_UP) {
          sb.append("(pid " ).append(
          action >> MotionEvent.ACTION_POINTER_ID_SHIFT);
          sb.append(")" );
       }
       sb.append("[" );
       for (int i = 0; i < event.getPointerCount(); i++) {
          sb.append("#" ).append(i);
          sb.append("(pid " ).append(event.getPointerId(i));
          sb.append(")=" ).append((int) event.getX(i));
          sb.append("," ).append((int) event.getY(i));
          if (i + 1 < event.getPointerCount())
             sb.append(";" );
       }
       sb.append("]" );
       Log.d("testi", sb.toString());
    }*/
}
