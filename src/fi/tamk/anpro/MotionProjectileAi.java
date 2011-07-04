package fi.tamk.anpro;

/**
 * Toteutus toucheventill‰ luotua reitti‰ seuraavan reitinhaun teko‰lylle. Teko‰ly hakeutuu
 * kohteenseensa toucheventill‰ m‰‰r‰tty‰ reitti‰ pitkin ja reagoi ainoastaan t‰h‰n reittiin.
 * 
 * K‰ytet‰‰n ainoastaan ammuksille.
 */
public class MotionProjectileAi extends AbstractAi
{
	private int[][] optimizedPath;
	private int     pathLength;

	private int distanceToPlayer;
	private int distanceToTarget = 0;
	
	private int targetRadius = 20;
	private int targetIndex  = 0;
	
	private int fails = 0;
	
	/**
	 * Alustaa luokan muuttujat.
	 * 
     * @param int Objektin tunnus piirtolistalla
     * @param int Objektin tyyppi
	 */
	public MotionProjectileAi(int _id, int _type)
	{
		super(_id, _type);
	}

    /**
     * Asettaa teko‰lyn aktiiviseksi.
     * 
     * @param int[][] Ammuksen reitti
     */
	@Override
    public final void setActive(int[][] _path)
    {
		optimizePath(_path);
		
		active = true;
    }

    /**
     * Asettaa teko‰lyn ep‰aktiiviseksi.
     */
	@Override
    public final void setUnactive()
    {
		active = false;
		
		distanceToTarget = 0;
		fails            = 0;
		targetIndex      = 0;
    }
    
    /**
     * K‰sittelee teko‰lyn.
     */
	@Override
	public void handleAi()
	{
		// Lasketaan ammuksen ja nykyisen kohteen v‰linen et‰isyys
		int angle = Utility.getAngle((int)wrapper.projectiles.get(parentId).x, (int)wrapper.projectiles.get(parentId).y,
				                     optimizedPath[targetIndex][0], optimizedPath[targetIndex][1]);
		
		// M‰‰ritet‰‰n k‰‰ntymissuunta
		wrapper.projectiles.get(parentId).turningDirection = Utility.getTurningDirection(wrapper.projectiles.get(parentId).direction, angle);
		
		// Lasketaan ammuksen et‰isyys kohteeseen
		if (distanceToTarget == 0) {
			distanceToTarget = Utility.getDistance(wrapper.projectiles.get(parentId).x, wrapper.projectiles.get(parentId).y,
												   optimizedPath[targetIndex][0], optimizedPath[targetIndex][1]);
		}
		else {
			int currentDistanceToTarget = Utility.getDistance(wrapper.projectiles.get(parentId).x,
															  wrapper.projectiles.get(parentId).y,
															  optimizedPath[targetIndex][0],
															  optimizedPath[targetIndex][1]);
			
			// Tarkistetaan onko kohde saavutettu
			if (distanceToTarget <= targetRadius) {
				// M‰‰ritet‰‰n uusi kohde
				++targetIndex;
			}
			
			// Tarkistetaan onko teko‰ly ep‰onnistunut saavuttamaan seuraavan kohteen
			// (jos et‰isyys kohteeseen kasvaa liikaa, se tulkitaan ep‰onnistumiseksi)
			if (currentDistanceToTarget > distanceToTarget) {
				++fails;
				
				if (fails == 2) {
					wrapper.projectiles.get(parentId).turningDirection = 0;
					setUnactive();
				}
			}
			// Teko‰ly ei ole ep‰onnistunut, joten tallennetaan uusi et‰isyys
			else {
				distanceToTarget = currentDistanceToTarget;
			}
		}
		
		
	}
	
	/**
	 * Optimoi ammuksen kulkureitin.
	 * 
	 * @param int[][] Ammuksen kulkureitin koordinaatit
	 */
	private void optimizePath(int[][] _path)
	{
		optimizedPath = new int[10][2];
		
		// Tallennetaan ensimm‰inen kohde
		optimizedPath[0][0] = _path[0][0];
		optimizedPath[0][1] = _path[0][1];
		distanceToPlayer = Utility.getDistance(wrapper.player.x, wrapper.player.x, _path[0][0], _path[0][1]);
		
		// Luetaan loput reitist‰ ja verrataan kohteiden et‰isyytt‰ edellisiin
		// (vain pelaajasta loittonevat pisteet hyv‰ksyt‰‰n)
		pathLength = 1;
		int index  = 1;
		
		for (int i = 1; i < 9; ++i) {
			int distanceTemp = Utility.getDistance(wrapper.player.x, wrapper.player.x, _path[i][0], _path[i][1]);
			
			if (distanceTemp > distanceToPlayer) {
				optimizedPath[index][0] = _path[i][0];
				optimizedPath[index][1] = _path[i][1];
				
				distanceToPlayer = distanceTemp;
				
				++index;
				++pathLength;
			}
		}
	}
}
