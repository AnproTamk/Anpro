package fi.tamk.anpro;

import java.util.ArrayList;

public class WeaponDefault extends Weapon
{
	// Ammukset
	private ArrayList<ProjectileLaser> projectiles;

    /**
     * Alustaa luokan muuttujat.
     */
	public WeaponDefault()
	{
		super();
		
		// Alustetaan ammukset
		projectiles = new ArrayList<ProjectileLaser>(2);
		
		// Luodaan tarvittava määrä ammuksia
		for (int i = 2; i >= 0; --i) {
			projectiles.add(new ProjectileLaser());
		}
	}

    /**
     * Aktivoi ammukset. Tästä eteenpäin ammusten oma tekoäly hoitaa niiden
     * päivittämisen.
     * 
     * @param int Kohteen X-koordinaatti
     * @param int Kohteen Y-koordinaatti
     */
	public void activate(int _x, int _y)
	{
		// Käydään läpi ammukset ja aktivoidaan ensimmäinen epäaktiivinen
		for (int i = 2; i >= 0; --i) {
			if (projectiles.get(i).active == false) {
				projectiles.get(i).activate( _x, _y);
				projectiles.get(i).active = true;
				break;
			}
		}
	}
}
