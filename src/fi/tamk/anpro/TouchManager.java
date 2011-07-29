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
 * jokaisen kosketuksen. Kutsuu kosketuksen perusteella WeaponManageria, Joystickia tai Hudia.
 */
public class TouchManager
{	
	/* Vakiot */
    private final byte JOYSTICK_TRESHOLD = (byte) (80 * Options.scale); // Joystickin toimintasäde
    
    /* Osoittimet muihin luokkiin */
    private GLSurfaceView surface;       // Pinta, jolla halutaan käsitellä kosketukset
    private WeaponManager weaponManager; // WeaponManager
    private Hud           hud;			 // HUD
    private Wrapper       wrapper;		 // Wrapper

    /* Kosketuksen tiedot */
    private int     xFirstTouch;	    	   // Ensimmäisen kosketuksen X-koordinaatti
    private int     yFirstTouch;			   // Ensimmäisen kosketuksen Y-koordinaatti
    private int     xSecondTouch;			   // Toisen kosketuksen X-koordinaatti
    private int     ySecondTouch;			   // Toisen kosketuksen Y-koordinaatti
    
    private int     yFirstButtonBorder;		   // Korkeimmalla olevan napin yläreunan raja
    private int	    ySecondButtonBorder; 	   // Keskimmäisen napin yläreunan raja
    private int	    yThirdButtonBorder; 	   // Alimman napin yläreunan raja
    
    private int     touchMarginal        = 32; // Alue, jonka kosketus antaa anteeksi hyväksyäkseen kosketuksen pelkäksi painallukseksi
    
    private int[][] touchPath;			       // Taulukko motioneventin pisteille
    private int     pointerCount         = 1;  // Indeksi motioneventin pisteiden määrälle
    
    private int 	action;					   // Viimeisin kosketustapahtuma
    private int 	actionCode;				   // Viimeisimmän kosketustapahtuman tyyppi
    //private int 	actionPointerId;		   // Viimeisimmän kosketustapahtuman index

    /* Kuvan tiedot */
    private int screenWidth;  // Ruudun leveys
    private int screenHeight; // Ruudun korkeus

    /* Joystickin tiedot */
    private int  joystickX  = Joystick.joystickX; // Joystickin X-koordinaatti HUD:ssa
    private int  joystickY  = Joystick.joystickY; // Joystickin Y-koordinaatti HUD:ssa

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
        surface 	  = _glSurfaceView;
        
        // Tallennetaan näytön koot
        screenWidth   = _dm.widthPixels;
        screenHeight  = _dm.heightPixels;
        
        // Alustetaan liikkeen polun taulukko [pisteen järjestysnumero][x = 0 ja y = 1]
        touchPath 		= new int[10][2];
        touchPath[0][0] = 0;
        touchPath[0][1] = 0;
        
        // Määritetään kentällä olevat rajat nappien painamiselle eri näytön korkeuksien mukaan
        // TODO: SCALING (Tälle toteutukselle tarvitaan dynaaminen muoto ja lisää vaihtoehtoja?)

        /* 480x320 */
        if (screenHeight == 320) {
        	yFirstButtonBorder  = -48;
        	ySecondButtonBorder = -96;
        	yThirdButtonBorder  = -128;
        }
        /* 800x480 */
        else if (screenHeight == 480) {
        	yFirstButtonBorder  = -32;
        	ySecondButtonBorder = -100;
        	yThirdButtonBorder  = -168;
        }
        else {
        	yFirstButtonBorder  = -32;
        	ySecondButtonBorder = -100;
        	yThirdButtonBorder  = -168;
        }
        
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
            	
            	/* 
            	 * KAHDELLA SORMELLA KOSKETTAMINEN
            	 */
            	if (event.getPointerCount() > 1) {	
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
            	    		xFirstTouch = (int) event.getX(i) - screenWidth / 2;
            	    		yFirstTouch = screenHeight / 2 - (int) event.getY(i);
            	    		
            	    		//Log.d("testi", i + 1 + ". kosketus asetettu.");
            	    		/* Joystickin aktivointi */
                            if (!Joystick.joystickInUse && Joystick.joystickDown && Utility.getDistance((float)xFirstTouch, (float)yFirstTouch, (float)joystickX, (float)joystickY) < JOYSTICK_TRESHOLD) {
                                    Joystick.joystickInUse = true;
                            }
                            
                            else if (Joystick.joystickInUse) {
                            	// Käytetään joystickia
                            	Joystick.useJoystick(xFirstTouch, yFirstTouch, (int)event.getX(), (int)event.getY(), wrapper);
                            	
                            	// Lopetetaan joystickin käyttäminen, jos sormen etäisyys siitä on yli sallitun rajan
                            	if (Utility.getDistance((float)xFirstTouch, (float)yFirstTouch, (float)joystickX, (float)joystickY) > JOYSTICK_TRESHOLD) {
                            		Joystick.joystickDown  = false;
                            		Joystick.joystickInUse = false;
                            		
                                    // Asetetaan pelaajan jarrutus
                                    wrapper.player.movementAcceleration = -6;
                            	}
                            }
            	    	}
            	    	
            	    	/* Toinen kosketus */
            	    	if (i == 1) {
            	    		xSecondTouch = (int) event.getX(i) - screenWidth / 2;
            	    		ySecondTouch = screenHeight / 2 - (int) event.getY(i);
            	    		//Log.d("testi", i + 1 + ". kosketus asetettu.");
            	    		
                            /* Sarjatuliaseen käyttäminen päästämättä kosketusta irti */
                            if (weaponManager.currentWeapon == WeaponManager.WEAPON_SPITFIRE &&
                            	xSecondTouch > (screenWidth / 2) - 100 * Options.scaleX && xSecondTouch < (screenWidth / 2) &&
                    	    	ySecondTouch < yFirstButtonBorder) {
                            	
                            	weaponManager.triggerPlayerShoot(xSecondTouch, ySecondTouch);
                            }
                            
                            /* Ajasta riippumattoman kosketuspolun seuranta */
                            else if (weaponManager.isUsingMotionEvents) {
                            	// Tarkistetaan onko seuraava kosketuskohta 8px päässä edellisestä kosketuskohdasta
                            	if (pointerCount < 10) {
                            		if (Math.abs(touchPath[pointerCount - 1][0] - xFirstTouch) >= 8 || Math.abs(touchPath[pointerCount - 1][1] - yFirstTouch) >= 8) {
                            			setPathPoint(xFirstTouch, yFirstTouch, pointerCount);
                            			pointerCount++;
                            		}
                            	}
                            }
            	    	
            	    	}
            	    }   
            	    
            	    /* ACTION_POINTER_DOWN tapahtuu, kun toinen sormi koskettaa ruutua */
            	    if (actionCode == MotionEvent.ACTION_POINTER_DOWN) {
            	    	
            	    	//Log.d("testi", "ACTION_POINTER_DOWN");
            	    	
            	    	/* Tarkistetaan painetaanko nappien päältä */
            	    	if (xSecondTouch > (screenWidth / 2) - 100 * Options.scaleX && xSecondTouch < (screenWidth / 2) &&
            	    	    ySecondTouch < yFirstButtonBorder) {
            	    		
            	    		// Oikean reunan alin nappula
                            if (ySecondTouch < yThirdButtonBorder && ySecondTouch > (-screenHeight / 2)) {
                            	Log.e("testi", "ALIN NAPPULA");
                                hud.triggerClick(Hud.BUTTON_1);
                            }

                            // Oikean reunan keskimmäinen nappula
                            else if (ySecondTouch < ySecondButtonBorder && ySecondTouch > yThirdButtonBorder) {
                            	Log.e("testi", "KESKIMMÄINEN NAPPULA");
                                hud.triggerClick(Hud.BUTTON_2);
                            }
            	    	 }
            	    	
            	    	/* Ammutaan */
            	    	else {
            	    		weaponManager.triggerPlayerShoot(xSecondTouch, ySecondTouch);
            	    	}
            	    }
            	    
            	    if (actionCode == MotionEvent.ACTION_POINTER_UP) {
            	    	Log.d("testi", "ACTION_POINTER_UP");
            	    	// TODO: Pitäisikö napin painaminen aktivoida vasta tässä?
            	    	
            	    	Joystick.joystickDown  = false;
            	    	Joystick.joystickInUse = false;
            	    	
            	    	/* Lähetetään ja nollataan kosketuspolun indeksointi, jos oikea ase on valittuna */
                        if (weaponManager.isUsingMotionEvents) {
                        	
                        	// Laukaisee ampumisen touchPath[][]-taulukon arvoilla
                    		weaponManager.triggerMotionShoot(touchPath);
                    		
                    		// Tyhjennetään  touchPath[][]-taulukko seuraavaa ampumiskertaa varten
                        	for (int i = 0; i < 10; i++) {
                        		// Tulostaa touchPath[][]-taulukon LogCatiin
                    			//Log.v("TouchManager", "touchPath[" + i + "][" + touchPath[i][0] + "][" + touchPath[i][1] + "]");

                    			touchPath[i][0] = 0;
                    			touchPath[i][1] = 0;
                    		}
                        	pointerCount = 1;
                        }
            	    }
            	}
            	
            	/*
            	 * YHDELLÄ SORMELLA KOSKETTAMINEN
            	 */
            	else {
            		xFirstTouch = (int) event.getX() - screenWidth / 2;
            		yFirstTouch = screenHeight / 2 - (int) event.getY();
            		
            		//Log.e("testi", "1 sormen kosketus");
            		
            		/* ACTION_DOWN */
                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    	// Muistetaan mistä painallus alkaa
                    	xFirstTouch = (int) event.getX() - screenWidth / 2;
                        yFirstTouch = screenHeight / 2 - (int) event.getY();
                        //Log.e("testi", "ACTION_DOWN: xFirst = " + xClickOffsetFirstTouch + " yFirst = " + yClickOffsetFirstTouch);
                        
                        /* Oikean reunan napit */
                        if (xFirstTouch > (screenWidth / 2) - 100 * Options.scaleX && xFirstTouch < (screenWidth / 2) &&
                        	yFirstTouch < yFirstButtonBorder) {
                        	
                            // Oikean reunan alin nappula
                            if (yFirstTouch < yThirdButtonBorder && yFirstTouch > (-screenHeight / 2)) {
                            	Log.e("testi", "ALIN NAPPULA");
                                hud.triggerClick(Hud.BUTTON_1);
                            }

                            // Oikean reunan keskimmäinen nappula
                            else if (yFirstTouch < ySecondButtonBorder && yFirstTouch > yThirdButtonBorder) {
                            	Log.e("testi", "KESKIMMÄINEN NAPPULA");
                                hud.triggerClick(Hud.BUTTON_2);
                            }
                        }
                        
                        /* Painetaan joystickin päällä */
                        if (joystickX != 0 && joystickY != 0 && Utility.getDistance((float)xFirstTouch, (float)yFirstTouch, (float)joystickX, (float)joystickY) < JOYSTICK_TRESHOLD) {
                                 Joystick.joystickDown = true;
                        }
                        
                        /* Painetaan pelikentältä */
                        else {
                        	if (xFirstTouch < (screenWidth / 2) - 100 * Options.scaleX && yFirstTouch > yFirstButtonBorder ||
                        		xFirstTouch < (screenWidth / 2) - 100 * Options.scaleX && yFirstTouch < yFirstButtonBorder ||
                        		xFirstTouch > (screenWidth / 2) - 100 * Options.scaleX && yFirstTouch > yFirstButtonBorder) {
                        		weaponManager.triggerPlayerShoot(event.getX() - screenWidth/2, -((-screenHeight / 2) + event.getY()));
                        	}
                        }
                    }
                    
                    /* ACTION_MOVE */
                    if (event.getAction() == MotionEvent.ACTION_MOVE) {
                    	xFirstTouch = (int) event.getX() - screenWidth / 2;
                        yFirstTouch = screenHeight / 2 - (int) event.getY();
                        //Log.e("testi", "ACTION_MOVE");
                        
                        /* Painetaan joystickin päällä */
                        if (joystickX != 0 && joystickY != 0 && Utility.getDistance((float)xFirstTouch, (float)yFirstTouch, (float)joystickX, (float)joystickY) < JOYSTICK_TRESHOLD) {
                                 Joystick.joystickDown = true;
                        }
                        
                        /* Sarjatuliaseen käyttäminen päästämättä kosketusta irti */
                        else if (weaponManager.currentWeapon == WeaponManager.WEAPON_SPITFIRE) {
                        	weaponManager.triggerPlayerShoot(xFirstTouch, yFirstTouch);
                        }
                        
                        /* Ajasta riippumattoman kosketuspolun seuranta */
                        if (weaponManager.isUsingMotionEvents) {
                        	// Tarkistetaan onko seuraava kosketuskohta 8px päässä edellisestä kosketuskohdasta
                        	if (pointerCount < 10) {
                        		if (Math.abs(touchPath[pointerCount - 1][0] - xFirstTouch) >= 8 || Math.abs(touchPath[pointerCount - 1][1] - yFirstTouch) >= 8) {
                        			setPathPoint(xFirstTouch, yFirstTouch, pointerCount);
                        			pointerCount++;
                        		}
                        	}
                        }
                        
                        /* Joystickin aktivointi */
                        if (!Joystick.joystickInUse && Joystick.joystickDown && Utility.getDistance((float)xFirstTouch, (float)yFirstTouch, (float)joystickX, (float)joystickY) < JOYSTICK_TRESHOLD) {
                                Joystick.joystickInUse = true;
                        }
                        else if (Joystick.joystickInUse) {
                        	// Käytetään joystickia
                        	Joystick.useJoystick(xFirstTouch, yFirstTouch, (int)event.getX(), (int)event.getY(), wrapper);
                        	
                        	// Lopetetaan joystickin käyttäminen, jos sormen etäisyys siitä on yli sallitun rajan
                        	if (Utility.getDistance((float)xFirstTouch, (float)yFirstTouch, (float)joystickX, (float)joystickY) > JOYSTICK_TRESHOLD) {
                        		Joystick.joystickDown  = false;
                        		Joystick.joystickInUse = false;
                        		
                        		// Asetetaan pelaajan jarrutus
                                wrapper.player.movementAcceleration = -6;
                        	}
                        }
                    }
    				
                    /* Nostetaan sormi pois */
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

                        if (Math.abs(event.getX() - (xFirstTouch - screenWidth / 2)) < touchMarginal &&
                            Math.abs(event.getY() - (yFirstTouch - screenHeight / 2)) < touchMarginal) {
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
