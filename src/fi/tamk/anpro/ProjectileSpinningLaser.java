package fi.tamk.anpro;

import java.lang.Math;

/**
 * Sisältää Spinning Laser -ammuksen tiedot ja toiminnot, kuten aktivoinnin, tekoälyn,
 * törmäystunnistuksen ja ajastukset.
 */
public class ProjectileSpinningLaser extends AbstractProjectile
{
    /**
     * Alustaa luokan muuttujat.
     */
    public ProjectileSpinningLaser()
    {
        super();
    
        weaponId = 2;
        
        /* Haetaan animaatioiden pituudet */
        animationLength = new int[GLRenderer.AMOUNT_OF_PROJECTILE_ANIMATIONS];
        
        for (int i = 0; i < GLRenderer.AMOUNT_OF_PROJECTILE_ANIMATIONS; ++i) {
            if (GLRenderer.projectileAnimations[weaponId][i] != null) {
                animationLength[i] = GLRenderer.projectileAnimations[weaponId][i].length;
            }
        }
        
        movementSpeed    = 0;
        collisionRadius  = 200;
        damageOnTouch    = 40;
        turningDirection = 1;
        turningSpeed     = 8;
    }
    
    /**
     * Määrittää ammuksen aloitussuunnan.
     */
    @Override
    protected final void setDirection()
    {
        // ...
    }
    
    /**
     * Etsii räjähdyksen vaikutusalueella olevia vihollisia ja kutsuu niiden triggerImpact-funktiota.
     */
    @Override
    protected final void causeExplosion()
    {
        // Tarkistetaan etäisyydet
        // Kutsutaan osumatarkistuksia tarvittaessa
        for (int i = wrapper.enemies.size()-1; i >= 0; --i) {
            if (wrapper.enemyStates.get(i) == 1) {
                int distance = (int) Math.sqrt(Math.pow(x - wrapper.enemies.get(i).x, 2) + Math.pow(y - wrapper.enemies.get(i).y, 2));
                
                if (distance - wrapper.enemies.get(i).collisionRadius - collisionRadius <= 0) {
                    // Osuma ja räjähdys
                    wrapper.enemies.get(i).triggerImpact(damageOnTouch);
                }
            }
        }
    }
    
    /**
     * Aiheuttaa ammuksen erikoistoiminnon.
     */
    @Override
    protected void triggerSpecialAction()
    {
        wrapper.projectileStates.set(listId, 3);
        
        setAction(GLRenderer.ANIMATION_DESTROY, 1, 1, 1);
        
        // Tarkistetaan etäisyydet
        for (int i = wrapper.enemies.size()-1; i >= 0; --i) {
            if (wrapper.enemyStates.get(i) == 1) {
                int distance = (int) Math.sqrt(Math.pow(x - wrapper.enemies.get(i).x, 2) + Math.pow(y - wrapper.enemies.get(i).y, 2));
                
                if (distance - wrapper.enemies.get(i).collisionRadius - collisionRadius <= 0) {
                    wrapper.enemyStates.set(i, 2);
                    wrapper.enemies.get(i).setAction(GLRenderer.ANIMATION_DESTROY, 1, 1, 1);
                }
            }
        }
    }
}
