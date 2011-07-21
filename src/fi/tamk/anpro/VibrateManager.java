package fi.tamk.anpro;

import android.content.Context;
import android.os.Vibrator;

/**
 * Vastaa peliss‰ tapahtuvista v‰rin‰toiminnoista. 
 */
public class VibrateManager 
{
	// V‰rin‰n kesto (ms) pelaajaan osuttaessa
	private final static long VIB_DURATION_ON_HIT = 300;
	
	// Osoitin t‰h‰n luokkaan (singleton-toimintoa varten)
	private static VibrateManager instance = null;
	
	// Ohjelman konteksti
	private static Context context;
	
	// Vibraattori
	private static Vibrator v;

	/**
	 * Alustaa luokan muuttujat.
	 */
	private VibrateManager()
	{
		context = MainActivity.context;
		v       = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
	}

	/**
     * Palauttaa osoittimen t‰st‰ luokasta.
     * 
     * @return VibrateManager Osoitin t‰h‰n luokkaan
     */
	synchronized public static final VibrateManager getInstance()
    {
        if(instance == null) {
            instance = new VibrateManager();
        }
        return instance;
    }

	/* =======================================================
	 * Uudet funktiot
	 * ======================================================= */
    /**
     * Aiheuttaa v‰rin‰n.
     */
    public static void vibrateOnHit()
    {
    	// TODO: Mahdollisuus m‰‰ritt‰‰ vakiolla v‰rin‰n "tyyppi", esim. tyyliin
    	// VibrateManager.vibrate(PLAYER_HIT); tai jotain vastaavaa
    	if (Options.vibration) {
    		v.vibrate(VIB_DURATION_ON_HIT);
    	}
    	
    	//v.cancel();
    }
}
