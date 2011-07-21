package fi.tamk.anpro;

/**
 * Sisältää EMP-ammuksen tiedot ja toiminnot, kuten aktivoinnin, tekoälyn,
 * törmäystunnistuksen ja ajastukset.
 */
public class ProjectileEmp extends AbstractProjectile
{
    /**
     * Alustaa luokan muuttujat.
     *
     * @param int Tekoälyn tunnus
     * @param int Ammuksen käyttäjän tyyppi
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
        
        // Määritetään ammuksen asetukset
        setMovementSpeed(0.0f);
        collisionRadius = (int)(200 * Options.scale);
        damageOnTouch   = 0;
    }
    
    /**
     * Aiheuttaa ammuksen erikoistoiminnon.
     */
    @Override
    public void triggerSpecialAction()
    {
        state = Wrapper.ONLY_ANIMATION;
        
        setAction(GLRenderer.ANIMATION_DESTROY, 1, 1, ACTION_DESTROYED, 0, 0);
        
        // Tarkistetaan etäisyydet
        for (int i = wrapper.enemies.size()-1; i >= 0; --i) {
            if (wrapper.enemies.get(i).state == Wrapper.FULL_ACTIVITY) {
            	if (Utility.isColliding(this, wrapper.enemies.get(i))) {
                	wrapper.enemies.get(i).triggerDisabled();
            	}
            }
        }
    }
}
