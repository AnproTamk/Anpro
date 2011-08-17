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
	public static final byte CONTROLS_UNDEFINED = 0; // Määrittämätön
	public static final byte CONTROLS_NONAV = 1;	 // Ei hardware-suuntapainikkeita
	public static final byte CONTROLS_DPAD = 2;		 // Nelisuuntaohjain
	public static final byte CONTROLS_TRACKBALL = 3; // Trackball
	public static final byte CONTROLS_WHEEL = 4;	 // Rulla
	
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
	public static float scale;				// Skaalaus asioille, joita ei voi skaalata X- tai Y-akselien mukaan (esim. radiukset)
	public static float scaleX;				// Skaalaus X-akselille
	public static float scaleY;				// Skaalaus Y-akselille
	public static int   scaledScreenWidth;	// Skaalattu ruudun leveys
	public static int   scaledScreenHeight; // Skaalattu ruudun korkeus
	
	/* Muuttujat */
	public static int   screenWidth;		// Ruudun leveys
	public static int   screenHeight;		// Ruudun korkeus

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
        sounds    = true;
        vibration = false;
        
        // Ottaa multitouchin käyttöön tai pois käytöstä laitteen ominaisuuksista riippuen
        if (_pManager.hasSystemFeature(PackageManager.FEATURE_TOUCHSCREEN_MULTITOUCH)) {
        	// Käynnistetään multiTouch ja joystick
        	multiTouch = true;
        	joystick   = true;
        }
        else {
        	// Sammutetaan multiTouch ja joystick
        	multiTouch = false;
        	joystick   = false;
        }
        
    	scaleX      	   = (float)_dm.widthPixels / 800;  // X-skaalaus ruudun leveyden mukaan
    	scaleY             = (float)_dm.heightPixels / 480; // Y-skaalaus ruudun korkeuden mukaan
    	
    	screenWidth        = _dm.widthPixels;				// Ruudun leveys
    	screenHeight       = _dm.heightPixels;				// Ruudun korkeus
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
