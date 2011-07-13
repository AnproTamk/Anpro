package fi.tamk.anpro;

/**
 * Sis�lt�� EMP-ammuksen tiedot ja toiminnot, kuten aktivoinnin, teko�lyn,
 * t�rm�ystunnistuksen ja ajastukset.
 */
public class ProjectileEmp extends AbstractProjectile
{
    /**
     * Alustaa luokan muuttujat.
     *
     * @param int Teko�lyn tunnus
     * @param int Ammuksen k�ytt�j�n tyyppi
     */
    public ProjectileEmp(int _ai, int _userType)
    {
        super(_ai, _userType);
    
        projectileId = 1;
        
        // Haetaan animaatioiden pituudet
        animationLength = new int[GLRenderer.AMOUNT_OF_PROJECTILE_ANIMATIONS];
        
        for (int i = 0; i < GLRenderer.AMOUNT_OF_PROJECTILE_ANIMATIONS; ++i) {
            if (GLRenderer.projectileAnimations[projectileId][i] != null) {
                animationLength[i] = GLRenderer.projectileAnimations[projectileId][i].length;
            }
        }
        
        // M��ritet��n ammuksen asetukset
        setMovementSpeed(0.0f);
        collisionRadius = (int)(200 * Options.scale);
        damageOnTouch   = 0;
    }
    
    /**
     * Aiheuttaa ammuksen erikoistoiminnon.
     */
    @Override
    protected void triggerSpecialAction()
    {
        wrapper.projectileStates.set(listId, Wrapper.ONLY_ANIMATION);
        
        setAction(GLRenderer.ANIMATION_DESTROY, 1, 1, 1);
        
        // Tarkistetaan et�isyydet
        for (int i = wrapper.enemies.size()-1; i >= 0; --i) {
            if (wrapper.enemyStates.get(i) == Wrapper.FULL_ACTIVITY) {
            	if (Utility.isColliding(this, wrapper.enemies.get(i))) {
                	wrapper.enemies.get(i).triggerDisabled();
            	}
            }
        }
    }
}
