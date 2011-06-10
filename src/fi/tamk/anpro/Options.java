package fi.tamk.anpro;

/*
import java.io.IOException;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.content.Context;
import android.content.res.XmlResourceParser;
*/

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
    	XmlReader reader = new XmlReader(null);
    	settings = (boolean[])reader.readSettings();
    }
    
    /*
     * Palauttaa pointterin tästä luokkaan
     */
    public static Options getInstance() {
        if(instance == null) {
            instance = new Options();
        }
        return instance;
    }
    
    /*
     * Asettaa asetukset
     */
    public void setSettings() {
    	particles = settings[0];
    	music = settings[1];
    	sounds = settings[2];
    }
}
