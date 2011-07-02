package fi.tamk.anpro;

import java.lang.Math;

/**
 * Toteutus hy�kk�ys-pys�hdys -teko�lylle. Teko�ly hy�kk�� pelaajaa kohti
 * mahdollisimman suoraa reitti� pitkin, mutta pys�htyy ennen t�rm�yst� pelaajaan
 * 
 * K�ytet��n ainoastaan vihollisille.
 */
public class ApproachAndStopAi extends AbstractAi
{
	//private boolean running = false;
	
	WeaponManager weaponManager;
	
	// Aika jolloin teko�ly viimeksi ampui
	private long lastShootingTime = 0;
  
	/**
	 * Alustaa luokan muuttujat.
	 * 
     * @param int           Objektin tunnus piirtolistalla
     * @param int           Objektin tyyppi
     * @param WeaponManager Osoitin WeaponManageriin aseiden k�ytt�� varten 
	 */
	public ApproachAndStopAi(int _id, int _type, WeaponManager _weaponManager) 
	{
		super(_id, _type);
        
		weaponManager = _weaponManager;
    }
    
    /**
     * K�sittelee teko�lyn.
     */
	@Override
    public final void handleAi()
    {
        /* Verrataan pelaajan sijaintia vihollisen sijaintiin */
        double xDiff = Math.abs((double)(wrapper.enemies.get(parentId).x - wrapper.player.x));
        double yDiff = Math.abs((double)(wrapper.enemies.get(parentId).y - wrapper.player.y));
        
        // Lasketaan pelaajan ja vihollisen v�linen et�isyys
        double distance = Math.sqrt(Math.pow(xDiff,2) + Math.pow(yDiff,2));
        
        // Vihollinen hidastaa tietyll� et�isyydell� pelaajasta
        if (distance <= 200 && distance >= 150) {
        	 wrapper.enemies.get(parentId).movementAcceleration = -5;	
        }
        
        
        // Vihollinen pys�htyy tietyll� et�isyydell� pelaajasta
        if (distance < 150) {
        	wrapper.enemies.get(parentId).movementSpeed = 0;
        	int coords[] = {(int) wrapper.player.x,(int) wrapper.player.y};
        	
        	if (lastShootingTime == 0) {
        		lastShootingTime = android.os.SystemClock.uptimeMillis();
        		weaponManager.triggerShoot(coords, Wrapper.CLASS_TYPE_ENEMY, wrapper.enemies.get(parentId).x, wrapper.enemies.get(parentId).y);
        	}
        	else {
        		long currentTime = android.os.SystemClock.uptimeMillis();
            	
            	if (currentTime - lastShootingTime >= 700) {
            		lastShootingTime = currentTime;
            		weaponManager.triggerShoot(coords, Wrapper.CLASS_TYPE_ENEMY, wrapper.enemies.get(parentId).x, wrapper.enemies.get(parentId).y);
            	}
        	}
        }
        /* M��ritet��n objektien v�linen kulma */
        double angle;
        
        // Jos vihollinen on pelaajan vasemmalla puolella:
        if (wrapper.enemies.get(parentId).x < wrapper.player.x) {
            // Jos vihollinen on pelaajan alapuolella:
            if (wrapper.enemies.get(parentId).y < wrapper.player.y) {
                angle = (Math.atan(yDiff/xDiff)*180)/Math.PI;
            }
            // Jos vihollinen on pelaajan yl�puolella:
            else if (wrapper.enemies.get(parentId).y > wrapper.player.y) {
                angle = 360 - (Math.atan(yDiff/xDiff)*180)/Math.PI;
            }
            else {
                angle = 0;
            }
        }
        // Jos vihollinen on pelaajan oikealla puolella:
        else if (wrapper.enemies.get(parentId).x > wrapper.player.x) {
            // Jos vihollinen on pelaajan yl�puolella:
            if (wrapper.enemies.get(parentId).y > wrapper.player.y) {
                angle = 180 + (Math.atan(yDiff/xDiff)*180)/Math.PI;
            }
            // Jos vihollinen on pelaajan alapuolella:
            else if (wrapper.enemies.get(parentId).y < wrapper.player.y) {
                angle = 180 - (Math.atan(yDiff/xDiff)*180)/Math.PI;
            }
            else {
                angle = 180;
            }
        }
        // Jos vihollinen on suoraan pelaajan yl�- tai alapuolella
        else {
            if (wrapper.enemies.get(parentId).y > wrapper.player.y) {
                angle = 270;
            }
            else {
                angle = 90;
            }
        }
        
        /* M��ritet��n k��ntymissuunta */
        double angle2 = angle -wrapper.enemies.get(parentId).direction;

        if(angle == 0 || angle == 90 || angle == 180 || angle == 270) {
        	wrapper.enemies.get(parentId).turningDirection = 0;
        }

        if (angle2 >= -10 && angle2 <= 10) {
            wrapper.enemies.get(parentId).turningDirection = 0;
        }
        else if (angle2 > 0 && angle2 <= 180) {
            wrapper.enemies.get(parentId).turningDirection = 1;
        }
        else {
            wrapper.enemies.get(parentId).turningDirection = 2;
        }
    }
}