package fi.tamk.anpro;

import java.lang.Math;

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
    
        weaponId = 1;
        
        /* Haetaan animaatioiden pituudet */
        animationLength = new int[GLRenderer.AMOUNT_OF_PROJECTILE_ANIMATIONS];
        
        for (int i = 0; i < GLRenderer.AMOUNT_OF_PROJECTILE_ANIMATIONS; ++i) {
            if (GLRenderer.projectileAnimations[weaponId][i] != null) {
                animationLength[i] = GLRenderer.projectileAnimations[weaponId][i].length;
            }
        }
        
        movementSpeed   = 0;
        collisionRadius = (int)(200 * Options.scale);
        damageOnTouch   = 0;
    }
    
    /**
     * Aiheuttaa ammuksen erikoistoiminnon.
     */
    @Override
    protected void triggerSpecialAction()
    {
        wrapper.projectileStates.set(listId, 2);
        
        setAction(GLRenderer.ANIMATION_DESTROY, 1, 1, 1);
        
        // Tarkistetaan et�isyydet
        for (int i = wrapper.enemies.size()-1; i >= 0; --i) {
            if (wrapper.enemyStates.get(i) == 1) {
                int distance = (int) Math.sqrt(Math.pow(x - wrapper.enemies.get(i).x, 2) + Math.pow(y - wrapper.enemies.get(i).y, 2));
                
                if (distance - wrapper.enemies.get(i).collisionRadius - collisionRadius <= 0) {
                	wrapper.enemies.get(i).triggerDisabled();
                }
            }
        }
    }
}
