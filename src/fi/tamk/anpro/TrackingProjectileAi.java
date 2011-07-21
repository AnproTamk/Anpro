package fi.tamk.anpro;

/**
 * Toteutus lineaarisen kohdetta seuraavan reitinhaun teko‰lylle. Teko‰ly hakeutuu
 * kohteenseensa mahdollisimman suoraa reitti‰ pitkin ja reagoi ainoastaan kohteen
 * liikkumiseen.
 * 
 * K‰ytet‰‰n ainoastaan ammuksille.
 */
public class TrackingProjectileAi extends AbstractAi
{
	private long    startTime;
	private boolean isTracking = false;
		
	/**
	 * Alustaa luokan muuttujat.
	 * 
     * @param int Objektin tunnus piirtolistalla
     * @param int Objektin tyyppi
	 */
	public TrackingProjectileAi(AiObject _parentObject, int _userType)
	{
		super(_parentObject, _userType);
	}

    /**
     * Asettaa teko‰lyn aktiiviseksi.
     * 
     * @param int Ammuksen suunta
     */
	@Override
    public final void setActive(int _direction)
    {
		startTime = android.os.SystemClock.uptimeMillis();
		
		active = true;
    }

    /**
     * Asettaa teko‰lyn ep‰aktiiviseksi.
     */
	@Override
    public final void setUnactive()
    {
		isTracking = false;
		active     = false;
    }
    
    /**
     * K‰sittelee teko‰lyn.
     */
	@Override
	public void handleAi()
	{
		long currentTime = android.os.SystemClock.uptimeMillis();
		
		/* Odotetaan hetki ennen teko‰lyn aktivoimista */
		if (!isTracking) {
			if (currentTime - startTime >= 150) {
				isTracking = true;
			}
		}
		/* K‰sitell‰‰n teko‰ly */
		else {
			// Tarkistetaan kauanko ammus on ollut kent‰ll‰
			if (currentTime - startTime < 5000) {
				/* Etsit‰‰n l‰hin vihollinen */
				if (indexOfClosestEnemy == -1 || (indexOfClosestEnemy > -1 && wrapper.enemies.get(indexOfClosestEnemy).state != Wrapper.FULL_ACTIVITY)) {
					findClosestEnemy(500);
				}
				/* M‰‰ritet‰‰n k‰‰ntyminen */
				else {
					// M‰‰ritet‰‰n objektien v‰linen kulma
					int angle = Utility.getAngle(parentObject.x, parentObject.y,
												 wrapper.enemies.get(indexOfClosestEnemy).x, wrapper.enemies.get(indexOfClosestEnemy).y);
					
					parentObject.turningDirection = Utility.getTurningDirection(parentObject.direction, angle);
				}
			}
			// Ammus on ollut tarpeeksi kauan kent‰ll‰, joten teko‰ly ohjaa sen ulos
			else {
				parentObject.turningDirection = 0;
			}
		}
	}
}
