package fi.tamk.anpro;

public class Options {
	
	private static Options instance = null;
	
	private static boolean   particles;
	private static boolean   sounds;
	private static boolean   music;
	private static boolean[] settings;
	
	/*
	 * Rakentaja
	 */
    protected Options() {
    	XmlReader reader = new XmlReader(MainActivity.context);
    	settings = (boolean[])reader.readSettings();
    }
    
    /*
     * Palauttaa pointterin t�st� luokkaan
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
