package fi.tamk.anpro;

/**
 * Sis‰lt‰‰ #2 aseen, eli EMP:n toteutuksen.
 * 
 * @extends AbstractWeapon
 */
public class WeaponEmp extends AbstractWeapon
{
    // Ammus
    private ProjectileEmp projectile;

    /**
     * Alustaa luokan muuttujat ja luo tarvittavan m‰‰r‰n ammuksia.
     * 
     * @param Wrapper Osoitin Wrapperiin
     * @param int     Aseen k‰ytt‰j‰n tyyppi
     */
    public WeaponEmp(Wrapper _wrapper, int _userType)
    {
        super(_wrapper, _userType);
        
		/* Alustetaan muuttujat */
        projectile = new ProjectileEmp(AbstractAi.NO_AI, _userType);
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
        // Tarkistetaan onko ammus jo aktiivinen
        if (!projectile.active) {
            // Aktivoidaan ammus
            projectile.activate(_targetX, _targetY, false, true, this, _startX, _startY);
            
            // Soitetaan ‰‰ni
            SoundManager.playSound(SoundManager.SOUND_WEAPON_EMP, 1);
        }
    }
}
