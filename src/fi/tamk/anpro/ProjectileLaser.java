package fi.tamk.anpro;

import java.lang.Math;
import android.util.Log;

/**
 * Sis�lt�� laser-ammuksen tiedot ja toiminnot, kuten aktivoinnin, teko�lyn,
 * t�rm�ystunnistuksen ja ajastukset.
 */
public class ProjectileLaser extends AbstractProjectile
{
    /**
     * Alustaa luokan muuttujat.
     */
    public ProjectileLaser()
    {
        super();
    }
    
    /**
     * M��ritt�� ammuksen aloitussuunnan.
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
