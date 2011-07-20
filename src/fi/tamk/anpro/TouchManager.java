package fi.tamk.anpro;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

/**
 * Hoitaa kosketuksen lis‰‰m‰ll‰ OpenGL-pintaan tarvittavat TouchListenerit ja tutkii
 * jokaisen kosketuksen. Kutsuu kosketuksen perusteella joko WeaponManageria tai Hudia.
 */
public class TouchManager
{	
	/* Vakiot */
    private final byte JOYSTICK_TRESHOLD = 80; // S‰de jolla joystick toimii
    
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
    private int     touchMarginal      		 = 32; // Alue, jonka kosketus antaa anteeksi hyv‰ksy‰kseen kosketuksen pelk‰ksi painallukseksi
    private int[][] touchPath;					   // Taulukko motioneventin pisteille
    private int     pointerCount       		 = 1;  // Indeksi motioneventin pisteiden m‰‰r‰lle
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
     * @param DisplayMetrics N‰ytˆn tiedot
     * @param GLSurfaceView  OpenGL-pinta
     * @param Context		 Ohjelman konteksti
     * @param Hud			 Pelin k‰yttˆliittym‰
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
        
        // Tallennetaan n‰ytˆn koot
        screenWidth   = _dm.widthPixels;
        screenHeight  = _dm.heightPixels;
        
        // Alustetaan liikkeen polun taulukko
        touchPath 		= new int[10][2];
        touchPath[0][0] = 0;
        touchPath[0][1] = 0;
        
        // M‰‰ritet‰‰n kent‰ll‰ olevat rajat nappien painamiselle eri n‰ytˆn korkeuksien mukaan
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
        
        // Asetetaan TouchListenerit
        setSurfaceListeners();
    }

    /**
     * Asettaa TouchListenerit ja k‰sittelee kosketustapahtumat.
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
            	    	 * i = 0 -> ensimm‰inen kosketus
            	    	 * i = 1 -> toinen kosketus
            	    	 * getPointerId(0) = 0 -> ensimm‰inen kosketus
            	    	 * getPointerId(1) = 1 -> toinen kosketus
            	    	 * getX(0)         = 0 -> ensimm‰inen kosketus
            	    	 * getX(1)		   = 1 -> toinen kosketus
            	    	 */
            	    	
            	    	/* Ensimm‰inen kosketus */
            	    	if (i == 0) {
            	    		xClickOffsetFirstTouch = (int) event.getX(i) - screenWidth / 2;
            	    		yClickOffsetFirstTouch = screenHeight / 2 - (int) event.getY(i);
            	    		
            	    		//Log.d("testi", i + 1 + ". kosketus asetettu.");
            	    		/* Joystickin aktivointi */
                            if (!Joystick.joystickInUse && Joystick.joystickDown && Utility.getDistance((float)xClickOffsetFirstTouch, (float)yClickOffsetFirstTouch, (float)joystickX, (float)joystickY) < JOYSTICK_TRESHOLD) {
                                    Joystick.joystickInUse = true;
                            }
                            
                            else if (Joystick.joystickInUse) {
                            	// K‰ytet‰‰n joystickia
                            	Joystick.useJoystick(xClickOffsetFirstTouch, yClickOffsetFirstTouch, (int)event.getX(), (int)event.getY(), wrapper);
                            	
                            	// Lopetetaan joystickin k‰ytt‰minen, jos sormen et‰isyys siit‰ on yli sallitun rajan
                            	if (Utility.getDistance((float)xClickOffsetFirstTouch, (float)yClickOffsetFirstTouch, (float)joystickX, (float)joystickY) > JOYSTICK_TRESHOLD) {
                            		Joystick.joystickDown = false;
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
            	    		
            	    		// T‰h‰n triggerMotionShoot() k‰ytett‰v‰ksi samaan aikaan kuin joystick.
            	    		
                            /* Sarjatuliaseen k‰ytt‰minen p‰‰st‰m‰tt‰ kosketusta irti */
                            if (weaponManager.currentWeapon == WeaponManager.WEAPON_SPITFIRE) {
                            	weaponManager.triggerPlayerShoot(xClickOffsetSecondTouch, yClickOffsetSecondTouch);
                            }
            	    	
            	    	}
            	    }   
            	    
            	    /* ACTION_POINTER_DOWN tapahtuu, kun toinen sormi lasketaan kent‰lle */
            	    if (actionCode == MotionEvent.ACTION_POINTER_DOWN) {
            	    	//Log.d("testi", "ACTION_POINTER_DOWN");
            	    	 weaponManager.triggerPlayerShoot(xClickOffsetSecondTouch, yClickOffsetSecondTouch);
            	    }
            	    
            	    if (actionCode == MotionEvent.ACTION_POINTER_UP) {
            	    	Log.d("testi", "ACTION_POINTER_UP");
            	    	
            	    	// T‰h‰n painikkeiden painamiset samaan aikaan kun joystick on jo toisella sormella k‰ytˆss‰.
            	    	
            	    }
            	}
            	
            	/* YHDELLƒ SORMELLA KOSKETTAMINEN */
            	else {
            		xClickOffsetFirstTouch = (int) event.getX() - screenWidth / 2;
            		yClickOffsetFirstTouch = screenHeight / 2 - (int) event.getY();
            		
            		//Log.e("testi", "1 sormen kosketus");
            		
            		/* ACTION_DOWN */
                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    	// Muistetaan mist‰ painallus alkaa
                    	xClickOffsetFirstTouch = (int) event.getX() - screenWidth / 2;
                        yClickOffsetFirstTouch = screenHeight / 2 - (int) event.getY();
                        //Log.e("testi", "ACTION_DOWN");
                        
                        /* Oikean reunan napit */
                        if (xClickOffsetFirstTouch > screenWidth - 100 * Options.scaleX && xClickOffsetFirstTouch < screenWidth &&
                        	yClickOffsetFirstTouch < yClickFirstBorder) {
                        	
                            // Oikean reunan alin nappula
                            if (yClickOffsetFirstTouch < yClickThirdBorder && yClickOffsetFirstTouch > 0) {
                                hud.triggerClick(Hud.BUTTON_3);
                            }

                            // Oikean reunan keskimm‰inen nappula
                            else if (yClickOffsetFirstTouch < yClickSecondBorder && yClickOffsetFirstTouch > yClickThirdBorder) {
                                hud.triggerClick(Hud.BUTTON_2);
                            }

                            // Oikean reunan ylin nappula
                            else if (yClickOffsetFirstTouch < yClickFirstBorder && yClickOffsetFirstTouch > yClickSecondBorder) {
                                hud.triggerClick(Hud.BUTTON_1);
                            }
                        }
                        
                        /* Painetaan joystickin p‰‰ll‰ */
                        if (joystickX != 0 && joystickY != 0 && Utility.getDistance((float)xClickOffsetFirstTouch, (float)yClickOffsetFirstTouch, (float)joystickX, (float)joystickY) < JOYSTICK_TRESHOLD) {
                                 Joystick.joystickDown = true;
                        }
                        
                        /* Painetaan pelikent‰lt‰ */
                        else {
                            weaponManager.triggerPlayerShoot(event.getX() - screenWidth/2, -((-screenHeight / 2) + event.getY()));
                        }
                    }
                    
                    /* ACTION_MOVE */
                    if (event.getAction() == MotionEvent.ACTION_MOVE) {
                    	xClickOffsetFirstTouch = (int) event.getX() - screenWidth / 2;
                        yClickOffsetFirstTouch = screenHeight / 2 - (int) event.getY();
                        //Log.e("testi", "ACTION_MOVE");
                        
                        /* Painetaan joystickin p‰‰ll‰ */
                        if (joystickX != 0 && joystickY != 0 && Utility.getDistance((float)xClickOffsetFirstTouch, (float)yClickOffsetFirstTouch, (float)joystickX, (float)joystickY) < JOYSTICK_TRESHOLD) {
                                 Joystick.joystickDown = true;
                        }
                        
                        /* Sarjatuliaseen k‰ytt‰minen p‰‰st‰m‰tt‰ kosketusta irti */
                        else if (weaponManager.currentWeapon == WeaponManager.WEAPON_SPITFIRE) {
                        	weaponManager.triggerPlayerShoot(xClickOffsetFirstTouch, yClickOffsetFirstTouch);
                        }
                        
                        /* Ajasta riippumattoman kosketuspolun seuranta */
                        if (weaponManager.isUsingMotionEvents) {
                        	// Tarkistetaan onko seuraava kosketuskohta 8px p‰‰ss‰ edellisest‰ kosketuskohdasta
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
                        	// K‰ytet‰‰n joystickia
                        	Joystick.useJoystick(xClickOffsetFirstTouch, yClickOffsetFirstTouch, (int)event.getX(), (int)event.getY(), wrapper);
                        	
                        	// Lopetetaan joystickin k‰ytt‰minen, jos sormen et‰isyys siit‰ on yli sallitun rajan
                        	if (Utility.getDistance((float)xClickOffsetFirstTouch, (float)yClickOffsetFirstTouch, (float)joystickX, (float)joystickY) > JOYSTICK_TRESHOLD) {
                        		Joystick.joystickDown = false;
                        		Joystick.joystickInUse = false;
                        		
                        		// Asetetaan pelaajan jarrutus
                                wrapper.player.movementAcceleration = -6;
                        	}
                        }
                    }
    				
                    // Nostetaan sormi pois
                    if (event.getAction() == MotionEvent.ACTION_UP) {
                    	// Poistetaan joystick k‰ytˆst‰
                        Joystick.joystickInUse = false;
                        Joystick.joystickDown  = false;
                        //Log.e("testi", "ACTION_UP");
                        
                        // Asetetaan pelaajan jarrutus
                        wrapper.player.movementAcceleration = -6;
                        
                        /* L‰hetet‰‰n ja nollataan kosketuspolun indeksointi, jos oikea ase on valittuna */
                        if (weaponManager.isUsingMotionEvents) {
                        	
                        	// Laukaisee ampumisen touchPath[][]-taulukon arvoilla
                    		weaponManager.triggerMotionShoot(touchPath);
                    		
                    		// Tyhjennet‰‰n  touchPath[][]-taulukko seuraavaa ampumiskertaa varten
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
                        	// T‰ss‰ kohtaa pelaaja nostaa sormensa napin p‰‰lt‰ liikuttamatta sormeaan pois napin alueelta ...
                        }
                    }
            	}
            	
                // K‰yt‰ painaminen
				return true;
            }
        });
    }
    
    /**
     * Tallentaa kosketusreitin pisteit‰
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
