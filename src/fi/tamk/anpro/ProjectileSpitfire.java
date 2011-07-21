package fi.tamk.anpro;

/**
 * Sis‰lt‰‰ "sarjatulilaser"-ammuksen tiedot ja toiminnot, kuten aktivoinnin, teko‰lyn,
 * tˆrm‰ystunnistuksen ja ajastukset.
 */
public class ProjectileSpitfire extends AbstractProjectile
{
    /**
     * Alustaa luokan muuttujat.
     *
     * @param int Teko‰lyn tunnus
     * @param int Ammuksen k‰ytt‰j‰n tyyppi
     */
    public ProjectileSpitfire(int _ai, int _userType)
    {
        super(_ai, _userType);
        
        /* Alustetaan muuttujat */
        // M‰‰ritet‰‰n ammuksen tunnus (k‰ytet‰‰n tekstuureja valittaessa)
        projectileId = 5;

        // Haetaan animaatioiden pituudet
        animationLength = new int[GLRenderer.AMOUNT_OF_PROJECTILE_ANIMATIONS];
        
        for (int i = 0; i < GLRenderer.AMOUNT_OF_PROJECTILE_ANIMATIONS; ++i) {
            if (GLRenderer.projectileAnimations[projectileId][i] != null) {
                animationLength[i] = GLRenderer.projectileAnimations[projectileId][i].length;
            }
        }
    }
}
