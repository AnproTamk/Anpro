package fi.tamk.anpro;

/**
 * Sis‰lt‰‰ Spinning Laser -ammuksen tiedot ja toiminnot, kuten aktivoinnin, teko‰lyn,
 * tˆrm‰ystunnistuksen ja ajastukset.
 */
public class ProjectileSpinningLaser extends AbstractProjectile
{
    /**
     * Alustaa luokan muuttujat.
     *
     * @param int Teko‰lyn tunnus
     * @param int Ammuksen k‰ytt‰j‰n tyyppi
     */
    public ProjectileSpinningLaser(int _ai, int _userType)
    {
        super(_ai, _userType);
        
        /* Alustetaan muuttujat */
        // M‰‰ritet‰‰n ammuksen tunnus (k‰ytet‰‰n tekstuureja valittaessa)
        projectileId = 2;

        // Haetaan animaatioiden pituudet
        animationLength = new int[GLRenderer.AMOUNT_OF_PROJECTILE_ANIMATIONS];

        for (int i = 0; i < GLRenderer.AMOUNT_OF_PROJECTILE_ANIMATIONS; ++i) {
            if (GLRenderer.projectileAnimations[projectileId][i] != null) {
                animationLength[i] = GLRenderer.projectileAnimations[projectileId][i].length;
            }
        }

        // M‰‰ritet‰‰n asetukset
        setMovementSpeed(0.0f);
        // TODO: SCALING (Options.scale)
        collisionRadius  = (int)(200 * Options.scale);
        damageOnTouch    = 40;
        turningDirection = 1;
    }

	/* =======================================================
	 * Perityt funktiot
	 * ======================================================= */
    /**
     * K‰ynnist‰‰ ammuksen erikoistoiminnon.
     */
    @Override
    public void triggerSpecialAction()
    {
        state = Wrapper.ANIMATION_AND_MOVEMENT;

        setAction(GLRenderer.ANIMATION_DESTROY, 1, 1, ACTION_DESTROYED, 0, 0, 0, 1000);

        // Tarkistetaan et‰isyydet
        for (int i = wrapper.enemies.size()-1; i >= 0; --i) {
            if (wrapper.enemies.get(i).state == Wrapper.FULL_ACTIVITY) {
            	if (Utility.isColliding(this, wrapper.enemies.get(i))) {
                    wrapper.enemies.get(i).triggerDestroyed();
            	}
            }
        }
    }
}
