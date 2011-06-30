package fi.tamk.anpro;

import java.util.ArrayList;

/**
 * Sis‰lt‰‰ #6 aseen, eli missilen toteutuksen.
 * 
 * @extends AbstractWeapon
 */
public class WeaponMissile extends AbstractWeapon
{
	/* Ammukset */
	private ArrayList<ProjectileMissile> projectiles;

    /**
     * Alustaa luokan muuttujat ja luo tarvittavan m‰‰r‰n ammuksia.
     */
	public WeaponMissile(Wrapper _wrapper, int _userType)
	{
		super(_wrapper, _userType);
		
		// Alustetaan ammukset
		projectiles = new ArrayList<ProjectileMissile>(10);
		
		// Luodaan tarvittava m‰‰r‰ ammuksia
		for (int i = 0; i < 5; ++i) {
			projectiles.add(new ProjectileMissile(AbstractAi.MOTION_PROJECTILE_AI, _userType));
		}
	}

    /**
     * Aktivoi ammukset. T‰st‰ eteenp‰in ammusten oma teko‰ly hoitaa niiden
     * p‰ivitt‰misen.
     * 
     * @param int Ammuksen reitti
     * @param int L‰htˆpisteen X-koordinaatti
     * @param int L‰htˆpisteen Y-koordinaatti
     */
	public void activate(int[][] _path, float _startX, float _startY)
	{
		// K‰yd‰‰n l‰pi ammukset ja aktivoidaan ensimm‰inen ep‰aktiivinen
		for (int i = 0; i < 5; ++i) {
			if (projectiles.get(i).active == false) {
				
				// Aktivoidaan ammus ja asetetaan kohteen koordinaatit
				projectiles.get(i).activate(_path, false, false, this, _startX, _startY);
				
				// Soitetaan ‰‰ni
				SoundManager.playSound(3, 1);
				
				// Keskeytet‰‰n silmukka
				break;
			}
		}
	}
}
