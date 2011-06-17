package fi.tamk.anpro;

import java.util.ArrayList;

/**
 * Sis‰lt‰‰ #1 aseen, eli perusaseen toteutuksen.
 * 
 * @extends AbstractWeapon
 */
public class WeaponDefault extends AbstractWeapon
{
	/* Ammukset */
	private ArrayList<ProjectileLaser> projectiles;

    /**
     * Alustaa luokan muuttujat ja luo tarvittavan m‰‰r‰n ammuksia.
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
	@Override
	public final void activate(int _x, int _y)
	{
		// K‰yd‰‰n l‰pi ammukset ja aktivoidaan ensimm‰inen ep‰aktiivinen
		for (int i = 0; i < 10; ++i) {
			if (projectiles.get(i).active == false) {
				
				// Aktivoidaan ammus ja asetetaan kohteen koordinaatit
				projectiles.get(i).activate( _x, _y);
				
				// Soitetaan ‰‰ni
				SoundManager.playSound(3, 1);
				
				// Keskeytet‰‰n silmukka
				break;
			}
		}
	}
}
