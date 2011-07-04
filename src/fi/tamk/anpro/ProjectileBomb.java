package fi.tamk.anpro;

/**
 * Sisältää bomb-ammuksen tiedot ja toiminnot, kuten aktivoinnin, tekoälyn,
 * törmäystunnistuksen ja ajastukset.
 */
public class ProjectileBomb extends AbstractProjectile
{
    /**
     * Alustaa luokan muuttujat.
     *
     * @param int Tekoälyn tunnus
     * @param int Ammuksen käyttäjän tyyppi
     */
    public ProjectileBomb(int _ai, int _userType)
    {
        super(_ai, _userType);
        
        projectileId = 3;

        // Haetaan animaatioiden pituudet
        animationLength = new int[GLRenderer.AMOUNT_OF_PROJECTILE_ANIMATIONS];
        
        for (int i = 0; i < GLRenderer.AMOUNT_OF_PROJECTILE_ANIMATIONS; ++i) {
            if (GLRenderer.projectileAnimations[projectileId][i] != null) {
                animationLength[i] = GLRenderer.projectileAnimations[projectileId][i].length;
            }
        }

        // Määritetään ammuksen asetukset
        explodeOnTarget = true;
        collisionRadius = (int) (10 * Options.scale);
    }
}