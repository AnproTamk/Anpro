package fi.tamk.anpro;

/**
 * Toteutus lineaarisen kohdetta seuraavan reitinhaun teko‰lylle. Teko‰ly hakeutuu
 * kohteenseensa mahdollisimman suoraa reitti‰ pitkin ja reagoi ainoastaan kohteen
 * liikkumiseen.
 * 
 * K‰ytet‰‰n ainoastaan ammuksille.
 * 
 * @extends AbstractAi
 */
public class TrackingProjectileAi extends AbstractAi
{
	private long    startTime;
	private boolean isTracking = false;
	
	private int   indexOfClosestEnemy = -1;
	private float distanceToEnemy     = -1;
	
	
	/**
	 * Alustaa luokan muuttujat.
	 * 
     * @param int Objektin tunnus piirtolistalla
     * @param int Objektin tyyppi
	 */
	public TrackingProjectileAi(int _id, int _type)
	{
		super(_id, _type);
	}

    /**
     * Asettaa teko‰lyn aktiiviseksi.
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
				if (indexOfClosestEnemy == -1 || (indexOfClosestEnemy > -1 && wrapper.enemyStates.get(indexOfClosestEnemy) != Wrapper.FULL_ACTIVITY)) {
					findClosestEnemy();
				}
				/* M‰‰ritet‰‰n k‰‰ntyminen */
				else {
			        // Verrataan kohteen sijaintia ammuksen sijaintiin
			        float xDiff = (float)Math.abs((double)(wrapper.projectiles.get(parentId).x - wrapper.enemies.get(indexOfClosestEnemy).x));
			        float yDiff = (float)Math.abs((double)(wrapper.projectiles.get(parentId).y - wrapper.enemies.get(indexOfClosestEnemy).y));
			        
			        float angle;
			        
			        // Jos ammus on kohteen vasemmalla puolella:
			        if (wrapper.projectiles.get(parentId).x < wrapper.enemies.get(indexOfClosestEnemy).x) {
			            // Jos vihollinen on pelaajan alapuolella:
			            if (wrapper.projectiles.get(parentId).y < wrapper.enemies.get(indexOfClosestEnemy).y) {
			                angle = (float)((Math.atan(yDiff/xDiff)*180)/Math.PI);
			            }
			            // Jos vihollinen on pelaajan yl‰puolella:
			            else if (wrapper.projectiles.get(parentId).y > wrapper.enemies.get(indexOfClosestEnemy).y) {
			                angle = (float)(360 - (Math.atan(yDiff/xDiff)*180)/Math.PI);
			            }
			            else {
			                angle = 0;
			            }
			        }
			        // Jos ammus on kohteen oikealla puolella:
			        else if (wrapper.projectiles.get(parentId).x > wrapper.enemies.get(indexOfClosestEnemy).x) {
			            // Jos vihollinen on pelaajan yl‰puolella:
			            if (wrapper.projectiles.get(parentId).y > wrapper.enemies.get(indexOfClosestEnemy).y) {
			                angle = (float)(180 + (Math.atan(yDiff/xDiff)*180)/Math.PI);
			            }
			            // Jos vihollinen on pelaajan alapuolella:
			            else if (wrapper.projectiles.get(parentId).y < wrapper.enemies.get(indexOfClosestEnemy).y) {
			                angle = (float)(180 - (Math.atan(yDiff/xDiff)*180)/Math.PI);
			            }
			            else {
			                angle = 180;
			            }
			        }
			        // Jos ammus on suoraan kohteen yl‰- tai alapuolella
			        else {
			            if (wrapper.projectiles.get(parentId).y > wrapper.enemies.get(indexOfClosestEnemy).y) {
			                angle = 270;
			            }
			            else {
			                angle = 90;
			            }
			        }
			        
			        // M‰‰ritet‰‰n k‰‰ntymissuunta
			        float angle2 = angle - wrapper.projectiles.get(parentId).direction;
		
			        if (angle == 0 || angle == 90 || angle == 180 || angle == 270) {
			        	wrapper.projectiles.get(parentId).turningDirection = 0;
			        }
			        else if (angle2 >= -10 && angle2 <= 10) {
			            wrapper.projectiles.get(parentId).turningDirection = 0;
			        }
			        else if (angle2 > 0 && angle2 <= 180) {
			            wrapper.projectiles.get(parentId).turningDirection = 1;
			        }
			        else {
			            wrapper.projectiles.get(parentId).turningDirection = 2;
			        }
				}
			}
			// Ammus on ollut tarpeeksi kauan kent‰ll‰, joten teko‰ly ohjaa sen ulos
			else {
				wrapper.projectiles.get(parentId).turningDirection = 0;
			}
		}
	}
	
	private void findClosestEnemy()
	{
		for (int i = wrapper.enemies.size()-1; i >= 0; --i) {
			if (wrapper.enemyStates.get(i) == Wrapper.FULL_ACTIVITY) {
				float distance = (float)(Math.sqrt(Math.pow(wrapper.enemies.get(i).x - wrapper.projectiles.get(parentId).x,2) +
						                           Math.pow(wrapper.enemies.get(i).y - wrapper.projectiles.get(parentId).y, 2)));
				
				if (indexOfClosestEnemy == -1) {
					indexOfClosestEnemy = i;
					distanceToEnemy     = distance;
				}
				else if (distance < distanceToEnemy) {
					indexOfClosestEnemy = i;
					distanceToEnemy     = distance;
				}
			}
		}
	}
}
