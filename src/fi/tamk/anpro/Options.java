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
	public static final byte CONTROLS_UNDEFINED = 0; // M��ritt�m�t�n
	public static final byte CONTROLS_NONAV = 1;	 // Ei hardware-suuntapainikkeita
	public static final byte CONTROLS_DPAD = 2;		 // Nelisuuntaohjain
	public static final byte CONTROLS_TRACKBALL = 3; // Trackball
	public static final byte CONTROLS_WHEEL = 4;	 // Rulla
	
	/* Osoitin t�h�n luokkaan (singleton-toimintoa varten) */
	private static Options instance = null;
	
	/* Asetukset */
	public static boolean particles;  // Partikkelit
	public static boolean sounds;	  // ��niefektit
	public static boolean music;	  // Musiikit
	public static boolean vibration;  // T�rin�t
	public static boolean multiTouch; // Multitouch
	public static boolean joystick;	  // Joystick
	
	/**
	 * M��rittelee mik� lis�ohjaustapa laitteessa on: <br>
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
     * Palauttaa osoittimen t�st� luokasta.
     * 
     * @return Options Osoitin t�h�n luokkaan
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
     * @param _config   Laitteen kaikki asetukset, jotka vaikuttavat resursseihin, joita ohjelma k�ytt��
     * @param _pManager Laitteeseen asennettujen ominaisuuksien tietoja (esim. multitouch)
     * @param _dm		N�yt�n tiedot
     */
    public final static void initialize(Configuration _config, PackageManager _pManager, DisplayMetrics _dm)
    {
    	controlType = (byte)_config.navigation;
        
        particles = true;
        music     = true;
        sounds    = true;
        vibration = false;
        
        // Ottaa multitouchin k�ytt��n tai pois k�yt�st� laitteen ominaisuuksista riippuen
        if (_pManager.hasSystemFeature(PackageManager.FEATURE_TOUCHSCREEN_MULTITOUCH)) {
        	// K�ynnistet��n multiTouch ja joystick
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
    	scaledScreenWidth  = _dm.widthPixels / 2; 		    // T�t� k�ytet��n AbstractProjectile-luokassa
    	scaledScreenHeight = _dm.heightPixels / 2; 			// T�t� k�ytet��n AbstractProjectile-luokassa    
    	
    	if (scaleX == 1 && scaleY == 1) {
    		scale = 1.0f; 							     	// TODO: Pit�isik� olla jokin muu kuin 1.0f?
    	}
    	else {
    		scale = ((float)screenWidth / 800 + (float)screenHeight / 480) / 2;
    	}
    	
    }
}
