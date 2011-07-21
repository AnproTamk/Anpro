package fi.tamk.anpro;

/**
 * Sis‰lt‰‰ EMP-ammuksen tiedot ja toiminnot, kuten aktivoinnin, teko‰lyn,
 * tˆrm‰ystunnistuksen ja ajastukset.
 */
public class ProjectileEmp extends AbstractProjectile
{
    /**
     * Alustaa luokan muuttujat.
     *
     * @param int Teko‰lyn tunnus
     * @param int Ammuksen k‰ytt‰j‰n tyyppi
     */
    public ProjectileEmp(int _ai, int _userType)
    {
        super(_ai, _userType);
        
        /* Alustetaan muuttujat */
        // M‰‰ritet‰‰n ammuksen tunnus (k‰ytet‰‰n tekstuureja valittaessa)
        projectileId = 1;
        
        // Haetaan animaatioiden pituudet
        animationLength = new int[GLRenderer.AMOUNT_OF_PROJECTILE_ANIMATIONS];
        
        for (int i = 0; i < GLRenderer.AMOUNT_OF_PROJECTILE_ANIMATIONS; ++i) {
            if (GLRenderer.projectileAnimations[projectileId][i] != null) {
                animationLength[i] = GLRenderer.projectileAnimations[projectileId][i].length;
            }
        }
        
        // M‰‰ritet‰‰n ammuksen asetukset
        setMovementSpeed(0.0f);
        collisionRadius = (int)(200 * Options.scale);
        damageOnTouch   = 0;
    }

	/* =======================================================
	 * Perityt funktiot
	 * ======================================================= */
    /**
     * Aiheuttaa ammuksen erikoistoiminnon.
     */
    @Override
    public void triggerSpecialAction()
    {
        state = Wrapper.ONLY_ANIMATION;
        
        setAction(GLRenderer.ANIMATION_DESTROY, 1, 1, ACTION_DESTROYED, 0, 0);
        
        // Tarkistetaan et‰isyydet
        for (int i = wrapper.enemies.size()-1; i >= 0; --i) {
            if (wrapper.enemies.get(i).state == Wrapper.FULL_ACTIVITY) {
            	if (Utility.isColliding(this, wrapper.enemies.get(i))) {
                	wrapper.enemies.get(i).triggerDisabled();
            	}
            }
        }
    }
}
