package fi.tamk.anpro;

import android.content.res.Configuration;

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
	public static boolean   particles;	// Partikkelit
	
	public static boolean   sounds;		// ��niefektit
	public static boolean   music;		// Musiikit
	
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
    public final static Options getInstance()
    {
        if(instance == null) {
            instance = new Options();
        }
        return instance;
    }
    
    /**
     * Asettaa asetukset.
     * 
     * @param _config laitteen kaikki asetukset, jotka vaikuttavat resursseihin, joita ohjelma k�ytt��
     */
    public final static void setConfig(Configuration _config)
    {	
    	//Log.i("navigare", "Navigation = " + _config.navigation);
    	controlType = (byte)_config.navigation;
        
        Options.particles = true;
        Options.music     = true;
        Options.sounds    = false;
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

	/**
	 * Skaalaa et�isyydet.
	 */
	/*public static int pixelConversion(int _pixels)
	{
		// Muunnetaan dps:t pikseleiksi tiheyden skaalauksen mukaan ja py�ristet��n yl�sp�in
		return ((int)(_pixels * scale + 0.5f));
	}*/
}
