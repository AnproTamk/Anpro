package fi.tamk.anpro;

import java.util.ArrayList;

import android.util.Log;

public class WeaponDefault extends Weapon
{
	
	private static final String TAG = "TouchEngine"; // Loggaus
	
	// Ammukset
	private ArrayList<ProjectileLaser> projectiles;

    /**
     * Alustaa luokan muuttujat.
     */
	public WeaponDefault()
	{
		super();
		
		// Alustetaan ammukset
		projectiles = new ArrayList<ProjectileLaser>(10);
		
		// Luodaan tarvittava m‰‰r‰ ammuksia
		for (int i = 0; i < 10; ++i) {
			projectiles.add(new ProjectileLaser());
		}
	}

    /**
     * Aktivoi ammukset. T‰st‰ eteenp‰in ammusten oma teko‰ly hoitaa niiden
     * p‰ivitt‰misen.
     * 
     * @param int Kohteen X-koordinaatti
     * @param int Kohteen Y-koordinaatti
     */
	public void activate(int _x, int _y)
	{
		
		//Log.v(TAG, "WeaponDefault.activate()=" + _x + " " + _y);
		
		// K‰yd‰‰n l‰pi ammukset ja aktivoidaan ensimm‰inen ep‰aktiivinen
		for (int i = 0; i < 10; ++i) {
			
			if (projectiles.get(i).active == false) {
				Log.v(TAG, "# " + i);
				projectiles.get(i).activate( _x, _y);
				SoundManager.playSound(3, 1);
				break;
			}
		}
	}
}
