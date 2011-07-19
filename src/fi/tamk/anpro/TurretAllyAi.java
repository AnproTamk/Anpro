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
	public TurretAllyAi(int _id, int _type, WeaponManager _weaponManager) 
	{
		super(_id, _type);
		
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
    	
    	if (indexOfClosestEnemy != -1 && wrapper.enemyStates.get(indexOfClosestEnemy) == Wrapper.FULL_ACTIVITY) {
	    	wrapper.allies.get(parentId).direction = Utility.getAngle(wrapper.allies.get(parentId).x, wrapper.allies.get(parentId).y,
	    															  wrapper.enemies.get(indexOfClosestEnemy).x, wrapper.enemies.get(indexOfClosestEnemy).y);
	    	
	        // Suoritetaan ampuminen
	    	long currentTime = android.os.SystemClock.uptimeMillis();
	        if (lastShootingTime == 0 || currentTime - lastShootingTime >= 400) {
	    		lastShootingTime = currentTime;
	    		weaponManager.triggerAllyShoot(wrapper.enemies.get(indexOfClosestEnemy).x, wrapper.enemies.get(indexOfClosestEnemy).y,
	    									   wrapper.allies.get(parentId).x, wrapper.allies.get(parentId).y);
	    	}
    	}
    }
}