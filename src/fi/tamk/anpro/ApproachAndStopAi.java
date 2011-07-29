package fi.tamk.anpro;


/**
 * Toteutus hyökkäys-pysähdys -tekoälylle. Tekoäly hyökkää pelaajaa kohti
 * mahdollisimman suoraa reittiä pitkin, mutta pysähtyy ennen törmäystä pelaajaan
 * 
 * Käytetään ainoastaan vihollisille.
 */
public class ApproachAndStopAi extends AbstractAi
{
	// Osoitin WeaponManageriin
	private WeaponManager weaponManager;
	
	// Aika jolloin tekoäly viimeksi ampui
	private long lastShootingTime = 0;
  
	/**
	 * Alustaa luokan muuttujat.
	 * 
     * @param int           Objektin tunnus piirtolistalla
     * @param int           Objektin tyyppi
     * @param WeaponManager Osoitin WeaponManageriin aseiden käyttöä varten 
	 */
	public ApproachAndStopAi(AiObject _parentObject, int _userType, WeaponManager _weaponManager) 
	{
		super(_parentObject, _userType);
        
		/* Tallennetaan muuttujat */
		weaponManager = _weaponManager;
    }
    
    /* =======================================================
     * Perityt funktiot
     * ======================================================= */
    /**
     * Käsittelee tekoälyn.
     */
	@Override
    public final void handleAi()
    {
        // Lasketaan pelaajan ja vihollisen välinen etäisyys
        double distance = Utility.getDistance(parentObject.x, parentObject.y, wrapper.player.x, wrapper.player.y);
        
        // Vihollinen hidastaa tietyllä etäisyydellä pelaajasta
        // TODO: SCALING (Options.scale)
        if (distance <= 200 * Options.scale && distance >= 150 * Options.scale) {
        	parentObject.movementAcceleration = -5;	
        }
        
        
        // Vihollinen pysähtyy tietyllä etäisyydellä pelaajasta ja alkaa ampumaan pelaajaa kohti
        if (distance < 150 * Options.scale) {
        	parentObject.movementSpeed = 0;
        	
        	if (lastShootingTime == 0) {
        		lastShootingTime = android.os.SystemClock.uptimeMillis();
        		weaponManager.triggerEnemyShoot(parentObject.x, parentObject.y, WeaponManager.ENEMY_LASER, parentObject.direction, 16, 0, 0);
        	}
        	else {
        		long currentTime = android.os.SystemClock.uptimeMillis();
            	
            	if (currentTime - lastShootingTime >= 700) {
            		lastShootingTime = currentTime;
            		weaponManager.triggerEnemyShoot(parentObject.x, parentObject.y, WeaponManager.ENEMY_LASER, parentObject.direction, 16, 0, 0);
            	}
        	}
        }
        
        else {
        	
        	parentObject.movementAcceleration = 10;
        	parentObject.movementSpeed = parentObject.speed / 2;
        }
        
        
        // Määritetään vihollisen ja pelaajan välinen kulma
        double angle = Utility.getAngle((int) parentObject.x, (int) parentObject.y,(int) wrapper.player.x,(int) wrapper.player.y);
        
        // Määritetään vihollisen kääntymissuunta
        parentObject.turningDirection = Utility.getTurningDirection(parentObject.direction, (int)angle);
        
        /* Tarkistetaan törmäykset pelaajan kanssa */
        checkCollisionWithPlayer();
    }
}