package fi.tamk.anpro;

import android.content.pm.PackageManager;
import android.content.res.Configuration;
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
	
	/* Osoitin t�h�n luokkaan (singleton-toimintoa varten) */
	private static Options instance = null;
	
	/* Asetukset */
	public static boolean particles;	// Partikkelit
	public static boolean sounds;		// ��niefektit
	public static boolean music;		// Musiikit
	public static boolean vibration;	// T�rin�t
	public static boolean multiTouch;
	public static boolean joystick;
	
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
    
    /**
     * Asettaa asetukset.
     * 
     * @param _config   Laitteen kaikki asetukset, jotka vaikuttavat resursseihin, joita ohjelma k�ytt��
     * @param _pManager Laitteeseen asennettujen ominaisuuksien tietoja (esim. multitouch)
     */
    public final static void initialize(Configuration _config, PackageManager _pManager)
    {	
    	//Log.i("navigare", "Navigation = " + _config.navigation);
    	controlType = (byte)_config.navigation;
        
        particles = true;
        music     = true;
        sounds    = false;
        vibration = false;
        
        Log.v("navigare", "Testataan multitouch..");
        
        // Ottaa multitouchin k�ytt��n tai pois k�yt�st� laitteen ominaisuuksista riippuen
        if (_pManager.hasSystemFeature(PackageManager.FEATURE_TOUCHSCREEN_MULTITOUCH)) {
        	// K�ynnistet��n multiTouch ja joystick
        	multiTouch = true;
        	joystick = true;
        	Log.v("navigare", "multiTouch = true");
        	Log.v("navigare", "joystick = true");
        }
        else {
        	// Sammutetaan multiTouch ja joystick
        	multiTouch = false;
        	joystick = false;
        	Log.v("navigare", "multiTouch = false");
        	Log.v("navigare", "joystick = false");
        }
    }
    
	/**
	 * Asettaa skaalauksen.
	 * 
	 * @param _screenWidth N�yt�n leveys
	 * @param _screenHeight N�yt�n korkeus
	 */
    public void getScalingConversion(int _screenWidth, int _screenHeight)
	{	
    	scaleX      	   = (float)_screenWidth / 800;
    	scaleY             = (float)_screenHeight / 480;
    	scale	           = 1.0f; 				// TODO: Pit�isik� olla jokin muu kuin 1.0f?
    	screenWidth        = _screenWidth;
    	screenHeight       = _screenHeight;
    	scaledScreenWidth  = _screenWidth / 2;  // T�t� k�ytet��n AbstractProjectile-luokassa
    	scaledScreenHeight = _screenHeight / 2; // T�t� k�ytet��n AbstractProjectile-luokassa    	
	}
}
