package fi.tamk.anpro;


/**
 * Toteutus hy�kk�ys-pys�hdys -teko�lylle. Teko�ly hy�kk�� pelaajaa kohti
 * mahdollisimman suoraa reitti� pitkin, mutta pys�htyy ennen t�rm�yst� pelaajaan
 * 
 * K�ytet��n ainoastaan vihollisille.
 */
public class ApproachAndStopAi extends AbstractAi
{
	// Osoitin WeaponManageriin
	private WeaponManager weaponManager;
	
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
        // Lasketaan pelaajan ja vihollisen v�linen et�isyys
        double distance = Utility.getDistance(wrapper.enemies.get(parentId).x, wrapper.enemies.get(parentId).y, wrapper.player.x, wrapper.player.y);
        
        // Vihollinen hidastaa tietyll� et�isyydell� pelaajasta
        if (distance <= 200 && distance >= 150) {
        	 wrapper.enemies.get(parentId).movementAcceleration = -5;	
        }
        
        
        // Vihollinen pys�htyy tietyll� et�isyydell� pelaajasta ja alkaa ampumaan pelaajaa kohti
        if (distance < 150) {
        	wrapper.enemies.get(parentId).movementSpeed = 0;
        	
        	if (lastShootingTime == 0) {
        		lastShootingTime = android.os.SystemClock.uptimeMillis();
        		weaponManager.triggerEnemyShoot(wrapper.enemies.get(parentId).x, wrapper.enemies.get(parentId).y, WeaponManager.ENEMY_LASER);
        	}
        	else {
        		long currentTime = android.os.SystemClock.uptimeMillis();
            	
            	if (currentTime - lastShootingTime >= 700) {
            		lastShootingTime = currentTime;
            		weaponManager.triggerEnemyShoot(wrapper.enemies.get(parentId).x, wrapper.enemies.get(parentId).y, WeaponManager.ENEMY_LASER);
            	}
        	}
        }
        
        else {
        	
        	wrapper.enemies.get(parentId).movementAcceleration = 10;
        	wrapper.enemies.get(parentId).movementSpeed = wrapper.enemies.get(parentId).speed / 2;
        }
        
        
        // M��ritet��n vihollisen ja pelaajan v�linen kulma
        double angle = Utility.getAngle((int) wrapper.enemies.get(parentId).x, (int) wrapper.enemies.get(parentId).y,(int) wrapper.player.x,(int) wrapper.player.y);
        
        // M��ritet��n vihollisen k��ntymissuunta
        wrapper.enemies.get(parentId).turningDirection = Utility.getTurningDirection(wrapper.enemies.get(parentId).direction, (int)angle);
        
        /* Tarkistetaan t�rm�ykset pelaajan kanssa */
        checkCollisionWithPlayer();
    }
}