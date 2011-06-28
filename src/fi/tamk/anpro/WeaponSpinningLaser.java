package fi.tamk.anpro;

/**
 * Sis�lt�� #3 aseen, eli Spinning Laserin toteutuksen.
 * 
 * @extends AbstractWeapon
 */
public class WeaponSpinningLaser extends AbstractWeapon
{
    /* Ammukset */
    private ProjectileSpinningLaser projectile;

    /**
     * Alustaa luokan muuttujat ja luo tarvittavan m��r�n ammuksia.
     */
    public WeaponSpinningLaser()
    {
        super();
        
        // Luodaan ammus
        projectile = new ProjectileSpinningLaser();
    }

    /**
     * Aktivoi ammukset. T�st� eteenp�in ammusten oma teko�ly hoitaa niiden
     * p�ivitt�misen.
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
            projectile.activate(_x, _y, true);
            
            // Soitetaan ��ni
            SoundManager.playSound(3, 1);
        }
    }
}