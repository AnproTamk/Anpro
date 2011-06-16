package fi.tamk.anpro;

public class Options {
	
	private static Options instance = null;
	
	public static boolean   particles;
	public static boolean   sounds;
	public static boolean   music;
	public static boolean[] settings;
	
	/*
	 * Rakentaja
	 */
    protected Options() {
    	XmlReader reader = new XmlReader(MainActivity.context);
    	settings = (boolean[])reader.readSettings();
    }
    
    /*
     * Palauttaa pointterin tästä luokkaan
     */
    public final static Options getInstance() {
        if(instance == null) {
            instance = new Options();
        }
        return instance;
    }
    
    /*
     * Asettaa asetukset
     */
    public final void setSettings() {
    	particles = settings[0];
    	music = settings[1];
    	sounds = settings[2];
    }
}
