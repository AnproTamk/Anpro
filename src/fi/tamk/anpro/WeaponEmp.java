package fi.tamk.anpro;

/**
 * Sis‰lt‰‰ #2 aseen, eli EMP:n toteutuksen.
 * 
 * @extends AbstractWeapon
 */
public class WeaponEmp extends AbstractWeapon
{
    /* Ammukset */
    private ProjectileEmp projectile;

    /**
     * Alustaa luokan muuttujat ja luo tarvittavan m‰‰r‰n ammuksia.
     */
    public WeaponEmp(Wrapper _wrapper, int _userType)
    {
        super(_wrapper, _userType);
        
        // Luodaan ammus
        projectile = new ProjectileEmp(AbstractAi.NO_AI, _userType);
    }

    /**
     * Aktivoi ammukset. T‰st‰ eteenp‰in ammusten oma teko‰ly hoitaa niiden
     * p‰ivitt‰misen.
     * 
     * @param int Kohteen X-koordinaatti
     * @param int Kohteen Y-koordinaatti
     */
    @Override
    public final void activate(int _targetX, int _targetY, float _startX, float _startY)
    {
        // Tarkistetaan onko ammus jo aktiivinen
        if (!projectile.active) {
            // Aktivoidaan ammus
            projectile.activate(_targetX, _targetY, false, true, this, _startX, _startY);
            
            // Soitetaan ‰‰ni
            SoundManager.playSound(3, 1);
        }
    }
}
