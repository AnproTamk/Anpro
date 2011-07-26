package fi.tamk.anpro;

import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.util.DisplayMetrics;
import android.util.Log;

/**
 * Hallitsee pelin globaaleja asetuksia.
 */
public class Options
{
	/* Ohjaintyypit */
	public static final byte CONTROLS_UNDEFINED = 0;
	public static final byte CONTROLS_NONAV = 1;
	public static final byte CONTROLS_DPAD = 2;
	public static final byte CONTROLS_TRACKBALL = 3;
	public static final byte CONTROLS_WHEEL = 4;
	
	/* Osoitin tähän luokkaan (singleton-toimintoa varten) */
	private static Options instance = null;
	
	/* Asetukset */
	public static boolean particles;  // Partikkelit
	public static boolean sounds;	  // Ääniefektit
	public static boolean music;	  // Musiikit
	public static boolean vibration;  // Tärinät
	public static boolean multiTouch; // Multitouch
	public static boolean joystick;	  // Joystick
	
	/**
	 * Määrittelee mikä lisäohjaustapa laitteessa on: <br>
	 * 0 = UNDEFINED <br>
	 * 1 = NONAV     <br>
	 * 2 = DPAD      <br>
	 * 3 = TRACKBALL <br>
	 * 4 = WHEEL     <br>
	 */
	public static byte controlType;
	
	/* Skaalausmuuttujat */
	public static float scale;
	public static float scaleX;
	public static float scaleY;	
	public static int   scaledScreenWidth;
	public static int   scaledScreenHeight;
	public static int   screenWidth;
	public static int   screenHeight;

	/**
	 * Alustaa luokan muuttujat.
	 */
    private Options()
    {
    	// ...
    }
    
    /**
     * Palauttaa osoittimen tästä luokasta.
     * 
     * @return Options Osoitin tähän luokkaan
     */
    synchronized public final static Options getInstance()
    {
        if(instance == null) {
            instance = new Options();
        }
        return instance;
    }

	/* =======================================================
	 * Uudet funktiot
	 * ======================================================= */
    /**
     * Asettaa asetukset.
     * 
     * @param _config   Laitteen kaikki asetukset, jotka vaikuttavat resursseihin, joita ohjelma käyttää
     * @param _pManager Laitteeseen asennettujen ominaisuuksien tietoja (esim. multitouch)
     * @param _dm		Näytön tiedot
     */
    public final static void initialize(Configuration _config, PackageManager _pManager, DisplayMetrics _dm)
    {
    	controlType = (byte)_config.navigation;
        
        particles = true;
        music     = true;
        sounds    = false;
        vibration = false;
        
        // Ottaa multitouchin käyttöön tai pois käytöstä laitteen ominaisuuksista riippuen
        if (_pManager.hasSystemFeature(PackageManager.FEATURE_TOUCHSCREEN_MULTITOUCH)) {
        	// Käynnistetään multiTouch ja joystick
        	multiTouch = true;
        	joystick = true;
        }
        else {
        	// Sammutetaan multiTouch ja joystick
        	multiTouch = false;
        	joystick = false;
        }
        
    	scaleX      	   = (float)_dm.widthPixels / 800;
    	scaleY             = (float)_dm.heightPixels / 480;
    	
    	screenWidth        = _dm.widthPixels;
    	screenHeight       = _dm.heightPixels;			
    	scaledScreenWidth  = _dm.widthPixels / 2; 		    // Tätä käytetään AbstractProjectile-luokassa
    	scaledScreenHeight = _dm.heightPixels / 2; 			// Tätä käytetään AbstractProjectile-luokassa    
    	
    	if (scaleX == 1 && scaleY == 1) {
    		scale = 1.0f; 							     	// TODO: Pitäisikö olla jokin muu kuin 1.0f?
    	}
    	else {
    		scale = ((float)screenWidth / 800 + (float)screenHeight / 480) / 2;
    	}
    	
    }
}
