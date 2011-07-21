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
	public ApproachAndStopAi(AiObject _parentObject, int _userType, WeaponManager _weaponManager) 
	{
		super(_parentObject, _userType);
        
		weaponManager = _weaponManager;
    }
    
    /* =======================================================
     * Perityt funktiot
     * ======================================================= */
    /**
     * K�sittelee teko�lyn.
     */
	@Override
    public final void handleAi()
    {
        // Lasketaan pelaajan ja vihollisen v�linen et�isyys
        double distance = Utility.getDistance(parentObject.x, parentObject.y, wrapper.player.x, wrapper.player.y);
        
        // Vihollinen hidastaa tietyll� et�isyydell� pelaajasta
        if (distance <= 200 && distance >= 150) {
        	parentObject.movementAcceleration = -5;	
        }
        
        
        // Vihollinen pys�htyy tietyll� et�isyydell� pelaajasta ja alkaa ampumaan pelaajaa kohti
        if (distance < 150) {
        	parentObject.movementSpeed = 0;
        	
        	if (lastShootingTime == 0) {
        		lastShootingTime = android.os.SystemClock.uptimeMillis();
        		weaponManager.triggerEnemyShoot(parentObject.x, parentObject.y, WeaponManager.ENEMY_LASER);
        	}
        	else {
        		long currentTime = android.os.SystemClock.uptimeMillis();
            	
            	if (currentTime - lastShootingTime >= 700) {
            		lastShootingTime = currentTime;
            		weaponManager.triggerEnemyShoot(parentObject.x, parentObject.y, WeaponManager.ENEMY_LASER);
            	}
        	}
        }
        
        else {
        	
        	parentObject.movementAcceleration = 10;
        	parentObject.movementSpeed = parentObject.speed / 2;
        }
        
        
        // M��ritet��n vihollisen ja pelaajan v�linen kulma
        double angle = Utility.getAngle((int) parentObject.x, (int) parentObject.y,(int) wrapper.player.x,(int) wrapper.player.y);
        
        // M��ritet��n vihollisen k��ntymissuunta
        parentObject.turningDirection = Utility.getTurningDirection(parentObject.direction, (int)angle);
        
        /* Tarkistetaan t�rm�ykset pelaajan kanssa */
        checkCollisionWithPlayer();
    }
}