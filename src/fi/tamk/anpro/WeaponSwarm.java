package fi.tamk.anpro;

import java.util.ArrayList;

/**
 * Sis‰lt‰‰ #5 aseen, eli "Swarm Missile":n toteutuksen.
 * 
 * @extends AbstractWeapon
 */
public class WeaponSwarm extends AbstractWeapon
{
	/* Ammukset */
	private ArrayList<ProjectileMissile> projectiles;

    /**
     * Alustaa luokan muuttujat ja luo tarvittavan m‰‰r‰n ammuksia.
     */
	public WeaponSwarm(Wrapper _wrapper)
	{
		super(_wrapper);
		
		// Alustetaan ammukset
		projectiles = new ArrayList<ProjectileMissile>(10);
		
		// Luodaan tarvittava m‰‰r‰ ammuksia
		for (int i = 0; i < 10; ++i) {
			projectiles.add(new ProjectileMissile(AbstractAi.TRACKING_PROJECTILE_AI));
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
		// Soitetaan ‰‰ni
		SoundManager.playSound(3, 1);
		
        for (int index = 9; index >= 0; --index) {
        	// Aktivoidaan ammus ja asetetaan ammuksen suunta
			projectiles.get(index).activate(36 * index, false, false, this, wrapper.player.x, wrapper.player.y);
		}
	}
}
