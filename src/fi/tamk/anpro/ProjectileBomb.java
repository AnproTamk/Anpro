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
        
        ai = new TrackingProjectileAi(listId);
        
        explodeOnTarget = true;
        collisionRadius = (int)(10 * Options.scale);
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
}