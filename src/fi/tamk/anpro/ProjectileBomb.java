package fi.tamk.anpro;

import java.lang.Math;

/**
 * Sisältää bomb-ammuksen tiedot ja toiminnot, kuten aktivoinnin, tekoälyn,
 * törmäystunnistuksen ja ajastukset.
 */
public class ProjectileBomb extends AbstractProjectile
{
    /**
     * Alustaa luokan muuttujat.
     */
    public ProjectileBomb()
    {
        super();
        
        weaponId = 3;
    
        /* Haetaan animaatioiden pituudet */
        animationLength = new int[GLRenderer.AMOUNT_OF_PROJECTILE_ANIMATIONS];
        
        for (int i = 0; i < GLRenderer.AMOUNT_OF_PROJECTILE_ANIMATIONS; ++i) {
            if (GLRenderer.projectileAnimations[weaponId][i] != null) {
                animationLength[i] = GLRenderer.projectileAnimations[weaponId][i].length;
            }
        }
        
        explodeOnTarget = true;
        collisionRadius = 10;
    }

    /**
     * Määrittää ammuksen aloitussuunnan.
     */
    @Override
    protected final void setDirection()
    {
        // Valitaan suunta
        double xDiff = Math.abs((double)(x - targetX));
        double yDiff = Math.abs((double)(y - targetY));
        
        if (x < targetX) {
            if (y < targetY) {
                direction = (int) ((Math.atan(yDiff/xDiff)*180)/Math.PI);
            }
            else if (y > targetY) {
                direction = (int) (360 - (Math.atan(yDiff/xDiff)*180)/Math.PI);
            }
            else {
                direction = 0;
            }
        }
        else if (x > targetX) {
            if (y > targetY) {
                direction = (int) (180 + (Math.atan(yDiff/xDiff)*180)/Math.PI);
            }
            else if (y < targetY) {
                direction = (int) (180 - (Math.atan(yDiff/xDiff)*180)/Math.PI);
            }
            else {
                direction = 180;
            }
        }
        else {
            if (y > targetY) {
                direction = 270;
            }
            else {
                direction = 90;
            }
        }
    }

    /**
     * Etsii räjähdyksen vaikutusalueella olevia vihollisia ja kutsuu niiden triggerImpact-funktiota.
     */
    @Override
    protected final void causeExplosion()
    {
        // Tarkistetaan etäisyydet
        // Kutsutaan osumatarkistuksia tarvittaessa
        for (int i = wrapper.enemies.size(); i >= 0; --i) {
            if (wrapper.enemyStates.get(i) == 1 || wrapper.enemyStates.get(i) == 3) {
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
    	// ...
    }
}