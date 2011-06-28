package fi.tamk.anpro;

import android.content.Context;
import android.os.Vibrator;

/**
 * Vastaa peliss� tapahtuvista v�rin�toiminnoista. 
 */
public class VibrateManager 
{
	private static VibrateManager instance = null;
	
	private final static long VIB_DURATION_ON_HIT = 300;
	
	static Context context;
	
	static Vibrator v;

	/**
	 * Alustaa luokan muuttujat ja lukee asetukset.
	 */
	private VibrateManager()
	{
		context = MainActivity.context;
		v = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
	}

	/**
     * Palauttaa osoittimen t�st� luokasta.
     * 
     * @return Vibratemanager Osoitin t�h�n luokkaan
     */
    public final static VibrateManager getInstance()
    {
        if(instance == null) {
            instance = new VibrateManager();
        }
        return instance;
    }
    
    public static void vibrateOnHit()
    {
    	v.vibrate(VIB_DURATION_ON_HIT);
    	//v.cancel();
    }
}
