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
    public WeaponEmp(Wrapper _wrapper)
    {
        super(_wrapper);
        
        // Luodaan ammus
        projectile = new ProjectileEmp();
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
        // Tarkistetaan onko ammus jo aktiivinen
        if (!projectile.active) {
            // Aktivoidaan ammus
            projectile.activate(_x, _y, true, this, wrapper.player.x, wrapper.player.y);
            
            // Soitetaan ‰‰ni
            SoundManager.playSound(3, 1);
        }
    }
}
