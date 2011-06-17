package fi.tamk.anpro;

/**
 * Sis‰lt‰‰ ja hallitsee pelin globaaleja asetuksia.
 */
public class Options
{
	/* Osoitin t‰h‰n luokkaan */
	private static Options instance = null;
	
	/* Asetukset */
	public static boolean   particles;
	public static boolean   sounds;
	public static boolean   music;
	public static boolean[] settings;
	
	/**
	 * Alustaa luokan muuttujat ja lukee asetukset.
	 */
    protected Options()
    {
    	XmlReader reader = new XmlReader(MainActivity.context);
    	settings = (boolean[])reader.readSettings();
    }
    
    /**
     * Palauttaa osoittimen t‰st‰ luokasta.
     * 
     * @return Options Osoitin t‰h‰n luokkaan
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
}
