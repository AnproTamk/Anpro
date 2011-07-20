package fi.tamk.anpro;

/**
 * Sisältää Spinning Laser -ammuksen tiedot ja toiminnot, kuten aktivoinnin, tekoälyn,
 * törmäystunnistuksen ja ajastukset.
 */
public class ProjectileSpinningLaser extends AbstractProjectile
{
    /**
     * Alustaa luokan muuttujat.
     *
     * @param int Tekoälyn tunnus
     * @param int Ammuksen käyttäjän tyyppi
     */
    public ProjectileSpinningLaser(int _ai, int _userType)
    {
        super(_ai, _userType);

        projectileId = 2;

        // Haetaan animaatioiden pituudet
        animationLength = new int[GLRenderer.AMOUNT_OF_PROJECTILE_ANIMATIONS];

        for (int i = 0; i < GLRenderer.AMOUNT_OF_PROJECTILE_ANIMATIONS; ++i) {
            if (GLRenderer.projectileAnimations[projectileId][i] != null) {
                animationLength[i] = GLRenderer.projectileAnimations[projectileId][i].length;
            }
        }

        // Määritetään ammuksen asetukset
        setMovementSpeed(0.0f);
        collisionRadius  = (int)(200 * Options.scale);
        damageOnTouch    = 40;
        turningDirection = 1;
    }

    /**
     * Käynnistää ammuksen erikoistoiminnon.
     */
    @Override
    protected void triggerSpecialAction()
    {
        state = Wrapper.ANIMATION_AND_MOVEMENT;

        setAction(GLRenderer.ANIMATION_DESTROY, 1, 1, 1, 0, 0);

        // Tarkistetaan etäisyydet
        for (int i = wrapper.enemies.size()-1; i >= 0; --i) {
            if (wrapper.enemies.get(i).state == Wrapper.FULL_ACTIVITY) {
            	if (Utility.isColliding(this, wrapper.enemies.get(i))) {
                    wrapper.enemies.get(i).triggerDestroyed();
            	}
            }
        }
    }
}
