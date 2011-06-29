package fi.tamk.anpro;

import java.lang.Math;
import android.util.Log;

/**
 * Sisältää laser-ammuksen tiedot ja toiminnot, kuten aktivoinnin, tekoälyn,
 * törmäystunnistuksen ja ajastukset.
 */
public class ProjectileLaser extends AbstractProjectile
{
    /**
     * Alustaa luokan muuttujat.
     */
    public ProjectileLaser(int _ai)
    {
        super(_ai);
        
        weaponId = 0;
    
        /* Haetaan animaatioiden pituudet */
        animationLength = new int[GLRenderer.AMOUNT_OF_PROJECTILE_ANIMATIONS];
        
        for (int i = 0; i < GLRenderer.AMOUNT_OF_PROJECTILE_ANIMATIONS; ++i) {
            if (GLRenderer.projectileAnimations[weaponId][i] != null) {
                animationLength[i] = GLRenderer.projectileAnimations[weaponId][i].length;
            }
        }
    }
}
