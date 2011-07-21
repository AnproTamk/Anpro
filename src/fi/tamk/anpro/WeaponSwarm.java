package fi.tamk.anpro;

import java.util.ArrayList;

/**
 * Sis‰lt‰‰ #5 aseen, eli "Swarm Missile":n toteutuksen.
 */
public class WeaponSwarm extends AbstractWeapon
{
	// Ammukset
	private ArrayList<ProjectileMissile> projectiles;

    /**
     * Alustaa luokan muuttujat ja luo tarvittavan m‰‰r‰n ammuksia.
     * 
     * @param Wrapper Osoitin Wrapperiin
     * @param int     Aseen k‰ytt‰j‰n tyyppi
     */
	public WeaponSwarm(Wrapper _wrapper, int _userType)
	{
		super(_wrapper, _userType);
		
		/* Alustetaan muuttujat */
		projectiles = new ArrayList<ProjectileMissile>(10);
		for (int i = 0; i < 10; ++i) {
			projectiles.add(new ProjectileMissile(AbstractAi.TRACKING_PROJECTILE_AI, _userType));
		}
	}

	/* =======================================================
	 * Perityt funktiot
	 * ======================================================= */
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
		// Soitetaan ‰‰ni
		SoundManager.playSound(3, 1);
		
		// K‰yd‰‰n ammukset l‰pi
        for (int index = 9; index >= 0; --index) {
        	// Aktivoidaan ammus ja asetetaan ammuksen suunta
			projectiles.get(index).activate(36 * index, false, false, this, _startX, _startY);
		}
	}
}
