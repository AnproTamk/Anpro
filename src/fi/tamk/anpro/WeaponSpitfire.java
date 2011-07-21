package fi.tamk.anpro;

import java.util.ArrayList;

/**
 * Sis‰lt‰‰ #7 aseen, eli "sarjatulilaserin" toteutuksen.
 * 
 * @extends AbstractWeapon
 */
public class WeaponSpitfire extends AbstractWeapon
{
	// Ammukset
	private ArrayList<ProjectileSpitfire> projectiles;

    /**
     * Alustaa luokan muuttujat ja luo tarvittavan m‰‰r‰n ammuksia.
     * 
     * @param Wrapper Osoitin Wrapperiin
     * @param int     Aseen k‰ytt‰j‰n tyyppi
     */
	public WeaponSpitfire(Wrapper _wrapper, int _userType)
	{
		super(_wrapper, _userType);

		/* Alustetaan muuttujat */
		projectiles = new ArrayList<ProjectileSpitfire>(25);
		for (int i = 0; i < 25; ++i) {
			projectiles.add(new ProjectileSpitfire(AbstractAi.LINEAR_PROJECTILE_AI, _userType));
		}
	}

    /**
     * Aktivoi ammukset. T‰st‰ eteenp‰in ammusten oma teko‰ly hoitaa niiden
     * p‰ivitt‰misen.
     * 
     * @param int Kohteen X-koordinaatti
     * @param int Kohteen Y-koordinaatti
     * @param int Ampujan X-koordinaatti
     * @param int Ampujan Y-koordinaatti
     */
	@Override
	public final void activate(float _targetX, float _targetY, float _startX, float _startY)
	{
		// K‰yd‰‰n l‰pi ammukset ja aktivoidaan ensimm‰inen ep‰aktiivinen
		for (int i = 0; i < 20; ++i) {
			if (projectiles.get(i).active == false) {
				
				// Aktivoidaan ammus ja asetetaan kohteen koordinaatit
				projectiles.get(i).activate(_targetX, _targetY, false, false, this, _startX, _startY);
				
				// Soitetaan ‰‰ni
				//SoundManager.playSound(3, 1);
				
				// Keskeytet‰‰n silmukka
				break;
			}
		}
	}
}
