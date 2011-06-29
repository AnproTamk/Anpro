package fi.tamk.anpro;

import java.lang.Math;

/**
 * Sis�lt�� bomb-ammuksen tiedot ja toiminnot, kuten aktivoinnin, teko�lyn,
 * t�rm�ystunnistuksen ja ajastukset.
 */
public class ProjectileBomb extends AbstractProjectile
{
    /**
     * Alustaa luokan muuttujat.
     */
    public ProjectileBomb(int _ai)
    {
        super(_ai);
        
        weaponId = 3;
    
        /* Haetaan animaatioiden pituudet */
        animationLength = new int[GLRenderer.AMOUNT_OF_PROJECTILE_ANIMATIONS];
        
        for (int i = 0; i < GLRenderer.AMOUNT_OF_PROJECTILE_ANIMATIONS; ++i) {
            if (GLRenderer.projectileAnimations[weaponId][i] != null) {
                animationLength[i] = GLRenderer.projectileAnimations[weaponId][i].length;
            }
        }
        
        explodeOnTarget = true;
        collisionRadius = (int)(10 * Options.scale);
    }
}