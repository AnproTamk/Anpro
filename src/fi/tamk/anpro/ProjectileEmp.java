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
     */
    public ProjectileEmp()
    {
        super();
    
        /* Haetaan animaatioiden pituudet */
        for (int i = 0; i < 4; ++i) {
            if (GLRenderer.projectileAnimations[1][i] != null) {
                animationLength[i] = GLRenderer.projectileAnimations[1][i].length;
            }
        }
        
        movementSpeed = 0;
        wrapper.projectileStates.set(listId, 2);
        setAction(GLRenderer.ANIMATION_DESTROY, 1, 1);
        causeExplosion();
    }
    
    /**
     * M��ritt�� ammuksen aloitussuunnan.
     */
    @Override
    protected final void setDirection()
    {
        // ...
    }
    
    /**
     * Etsii r�j�hdyksen vaikutusalueella olevia vihollisia ja kutsuu niiden triggerImpact-funktiota.
     */
    @Override
    protected final void causeExplosion()
    {
        // Tarkistetaan et�isyydet
        // Kutsutaan osumatarkistuksia tarvittaessa
        for (int i = wrapper.enemies.size(); i >= 0; --i) {
            if (wrapper.enemyStates.get(i) == 1) {
                int distance = (int) Math.sqrt(((int)(x - wrapper.enemies.get(i).x))^2 + ((int)(y - wrapper.enemies.get(i).y))^2);
                
                if (distance - wrapper.enemies.get(i).collisionRadius - collisionRadius <= 0) {
                    // Osuma ja r�j�hdys
                    wrapper.enemies.get(i).triggerImpact(damageOnTouch);
                }
            }
        }
    }
}
