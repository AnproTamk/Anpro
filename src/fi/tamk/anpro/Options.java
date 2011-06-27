package fi.tamk.anpro;

/**
 * Sisältää ja hallitsee pelin globaaleja asetuksia.
 */
public class Options
{
	/* Osoitin tähän luokkaan */
	private static Options instance = null;
	
	/* Asetukset */
	public static boolean   particles;
	public static boolean   sounds;
	public static boolean   music;
	public static boolean[] settings;
	
	/* Skaalausmuuttujat */
	public static float scale;
	public static float scaleX;
	public static float scaleY;	
	public static int   scaledScreenWidth;
	public static int   scaledScreenHeight;

	/**
	 * Alustaa luokan muuttujat ja lukee asetukset.
	 */
    private Options()
    {
    	XmlReader reader = new XmlReader(MainActivity.context);
    	settings = (boolean[])reader.readSettings();
    }
    
    /**
     * Palauttaa osoittimen tästä luokasta.
     * 
     * @return Options Osoitin tähän luokkaan
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
     */
    public final void setSettings()
    {
    	particles = settings[0];
    	music = settings[1];
    	sounds = settings[2];
    }
    
	/**
	 * Asettaa skaalauksen näytön dpi-arvon mukaan ja palauttaa sen
	 * 
	 * @param _screenDpi näytön DPI-lukema (320, 240, 160, 120)
	 */
    public void scaleConversion(int _screenWidth, int _screenHeight)
	{	
    	scaleX      	   = (float)_screenWidth / 800;
    	scaleY             = (float)_screenHeight / 480;
    	scale	           = 1.0f; // TODO:
    	scaledScreenWidth  = _screenWidth / 2;  // Tätä käytetään AbstractProjectile-luokassa
    	scaledScreenHeight = _screenHeight / 2; // Tätä käytetään AbstractProjectile-luokassa
	}

	/**
	 * Etäisyyksien skaalaus
	 */
	/*public static int pixelConversion(int _pixels)
	{
		// Muunnetaan dps:t pikseleiksi tiheyden skaalauksen mukaan ja pyöristetään ylöspäin
		return ((int)(_pixels * scale + 0.5f));
	}*/
	
}
