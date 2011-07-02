package fi.tamk.anpro;

/**
 * Sisältää missile-ammuksen tiedot ja toiminnot, kuten aktivoinnin, tekoälyn,
 * törmäystunnistuksen ja ajastukset.
 */
public class ProjectileMissile extends AbstractProjectile
{
    /**
     * Alustaa luokan muuttujat.
     *
     * @param int Tekoälyn tunnus
     * @param int Ammuksen käyttäjän tyyppi
     */
    public ProjectileMissile(int _ai, int _userType)
    {
        super(_ai, _userType);
        
        projectileId = 4;

        // Haetaan animaatioiden pituudet
        animationLength = new int[GLRenderer.AMOUNT_OF_PROJECTILE_ANIMATIONS];
        
        for (int i = 0; i < GLRenderer.AMOUNT_OF_PROJECTILE_ANIMATIONS; ++i) {
            if (GLRenderer.projectileAnimations[projectileId][i] != null) {
                animationLength[i] = GLRenderer.projectileAnimations[projectileId][i].length;
            }
        }

        // Määritetään ammuksen asetukset
        damageOnExplode = 25;
        damageType      = EXPLODE_ON_TOUCH;
        
        collisionRadius = (int)(15 * Options.scale);
        explosionRadius = (int)(80 * Options.scale);
        
        movementAcceleration = 5;
        turningAcceleration  = 10;
    }
}
