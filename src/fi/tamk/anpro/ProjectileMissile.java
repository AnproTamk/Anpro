package fi.tamk.anpro;

/**
 * Sis‰lt‰‰ missile-ammuksen tiedot ja toiminnot, kuten aktivoinnin, teko‰lyn,
 * tˆrm‰ystunnistuksen ja ajastukset.
 */
public class ProjectileMissile extends AbstractProjectile
{
    /**
     * Alustaa luokan muuttujat.
     *
     * @param int Teko‰lyn tunnus
     * @param int Ammuksen k‰ytt‰j‰n tyyppi
     */
    public ProjectileMissile(int _ai, int _userType)
    {
        super(_ai, _userType);
        
        /* Alustetaan muuttujat */
        // M‰‰ritet‰‰n ammuksen tunnus (k‰ytet‰‰n tekstuureja valittaessa)
        projectileId = 4;

        // Haetaan animaatioiden pituudet
        animationLength = new int[GLRenderer.AMOUNT_OF_PROJECTILE_ANIMATIONS];
        
        for (int i = 0; i < GLRenderer.AMOUNT_OF_PROJECTILE_ANIMATIONS; ++i) {
            if (GLRenderer.projectileAnimations[projectileId][i] != null) {
                animationLength[i] = GLRenderer.projectileAnimations[projectileId][i].length;
            }
        }

        // M‰‰ritet‰‰n asetukset
        damageOnExplode = 25;
        damageType      = EXPLODE_ON_TOUCH;
        
        collisionRadius = (int)(15 * Options.scale);
        explosionRadius = (int)(80 * Options.scale);
        
        movementAcceleration = 5;
        turningAcceleration  = 10;
    }
}
