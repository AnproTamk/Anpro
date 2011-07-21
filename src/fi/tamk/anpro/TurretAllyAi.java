package fi.tamk.anpro;

/**
 * Toteutus emoaluksen tykkien tekoälylle.
 * 
 * Käytetään ainoastaan liittolaisille.
 */
public class TurretAllyAi extends AbstractAi
{
	private WeaponManager weaponManager;
	
	private long lastShootingTime = 0;
	
	/**
	 * Alustaa luokan muuttujat.
	 * 
     * @param int Objektin tunnus piirtolistalla
     * @param int Objektin tyyppi
	 */
	public TurretAllyAi(AiObject _parentObject, int _userType, WeaponManager _weaponManager) 
	{
		super(_parentObject, _userType);
        
		/* Tallennetaan muuttujat */
		weaponManager = _weaponManager;
	}
    
    /**
     * Käsittelee tekoälyn.
     */
    @Override
    public final void handleAi()
    {
		indexOfClosestEnemy = -1;
		distanceToEnemy     = 0;
		
    	findClosestEnemy(300);
    	
    	if (indexOfClosestEnemy != -1 && wrapper.enemies.get(indexOfClosestEnemy).state == Wrapper.FULL_ACTIVITY) {
    		parentObject.direction = Utility.getAngle(parentObject.x, parentObject.y,
	    											  wrapper.enemies.get(indexOfClosestEnemy).x, wrapper.enemies.get(indexOfClosestEnemy).y);
	    	
	        // Suoritetaan ampuminen
	    	long currentTime = android.os.SystemClock.uptimeMillis();
	        if (lastShootingTime == 0 || currentTime - lastShootingTime >= 400) {
	    		lastShootingTime = currentTime;
	    		weaponManager.triggerAllyShoot(wrapper.enemies.get(indexOfClosestEnemy).x, wrapper.enemies.get(indexOfClosestEnemy).y,
	    									   parentObject.x, parentObject.y);
	    	}
    	}
    }
}