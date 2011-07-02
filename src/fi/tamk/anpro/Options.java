package fi.tamk.anpro;

/**
 * Hallitsee pelin globaaleja asetuksia.
 */
public class Options
{
	/* Osoitin tähän luokkaan (singleton-toimintoa varten) */
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
     * @param boolean Partikkeliefektit
     * @param boolean Musiikit
     * @param boolean Äänet
     */
    public final static void setSettings(boolean _particles, boolean _music, boolean _sounds)
    {
    	particles = _particles;
    	music     = _music;
    	sounds    = _sounds;
    }
    
	/**
	 * Asettaa skaalauksen näytön dpi-arvon mukaan.
	 * 
	 * @param int Näytön leveys
	 * @param int Näytön korkeus
	 */
    public void scaleConversion(int _screenWidth, int _screenHeight)
	{	
    	scaleX      	   = (float)_screenWidth / 800;
    	scaleY             = (float)_screenHeight / 480;
    	scale	           = 1.0f; // TODO: Pitäisikö olla jokin muu kuin 1.0f?
    	screenWidth        = _screenWidth;
    	screenHeight       = _screenHeight;
    	scaledScreenWidth  = _screenWidth / 2;  // Tätä käytetään AbstractProjectile-luokassa
    	scaledScreenHeight = _screenHeight / 2; // Tätä käytetään AbstractProjectile-luokassa    	
	}

	/**
	 * Skaalaa etäisyydet.
	 */
	/*public static int pixelConversion(int _pixels)
	{
		// Muunnetaan dps:t pikseleiksi tiheyden skaalauksen mukaan ja pyöristetään ylöspäin
		return ((int)(_pixels * scale + 0.5f));
	}*/
}
