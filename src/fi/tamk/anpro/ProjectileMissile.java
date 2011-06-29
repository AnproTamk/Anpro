package fi.tamk.anpro;

/**
 * Sisältää missile-ammuksen tiedot ja toiminnot, kuten aktivoinnin, tekoälyn,
 * törmäystunnistuksen ja ajastukset.
 */
public class ProjectileMissile extends AbstractProjectile
{
    /**
     * Alustaa luokan muuttujat.
     */
    public ProjectileMissile(int _ai)
    {
        super(_ai);
        
        weaponId = 4;
    
        /* Haetaan animaatioiden pituudet */
        animationLength = new int[GLRenderer.AMOUNT_OF_PROJECTILE_ANIMATIONS];
        
        for (int i = 0; i < GLRenderer.AMOUNT_OF_PROJECTILE_ANIMATIONS; ++i) {
            if (GLRenderer.projectileAnimations[weaponId][i] != null) {
                animationLength[i] = GLRenderer.projectileAnimations[weaponId][i].length;
            }
        }
        
        damageOnExplode = 25;
        damageType      = EXPLODE_ON_TOUCH;
        
        collisionRadius = (int)(15 * Options.scale);
        explosionRadius = (int)(80 * Options.scale);
        
        movementDelay        = 10;
        movementSpeed        = (int)(3 * Options.scale);
        movementAcceleration = 5;
        
        turningDelay         = 10;
        turningSpeed         = (int)(2 * Options.scale);
        turningAcceleration  = 10;
    }
}
