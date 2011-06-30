package fi.tamk.anpro;

/**
 * Toteutus toucheventill‰ luotua reitti‰ seuraavan reitinhaun teko‰lylle. Teko‰ly hakeutuu
 * kohteenseensa toucheventill‰ m‰‰r‰tty‰ reitti‰ pitkin ja reagoi ainoastaan t‰h‰n reittiin.
 * 
 * K‰ytet‰‰n ainoastaan ammuksille.
 * 
 * @extends AbstractAi
 */
public class MotionProjectileAi extends AbstractAi
{
	private int[][] optimizedPath;
	private int     pathLength;

	private int distanceToPlayer;
	private int distanceToTarget = 0;
	
	private int targetRadius = 20;
	private int target       = 0;
	
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
		target           = 0;
    }
    
    /**
     * K‰sittelee teko‰lyn.
     */
	@Override
	public void handleAi()
	{
		// Lasketaan ammuksen ja nykyisen kohteen v‰linen et‰isyys
		int angle = Utility.getAngle((int)wrapper.projectiles.get(parentId).x, (int)wrapper.projectiles.get(parentId).y, optimizedPath[target][0], optimizedPath[target][1]);
		
		// M‰‰ritet‰‰n k‰‰ntymissuunta
		wrapper.projectiles.get(parentId).turningDirection = Utility.getTurningDirection(wrapper.projectiles.get(parentId).direction, angle);
		
		// Lasketaan ammuksen et‰isyys kohteeseen
		if (distanceToTarget == 0) {
			distanceToTarget = Utility.getDistance((int)wrapper.projectiles.get(parentId).x, (int)wrapper.projectiles.get(parentId).y, optimizedPath[target][0], optimizedPath[target][1]);
		}
		else {
			int currentDistanceToTarget = Utility.getDistance((int)wrapper.projectiles.get(parentId).x,
															  (int)wrapper.projectiles.get(parentId).y,
															  optimizedPath[target][0],
															  optimizedPath[target][1]);
			
			// Tarkistetaan onko kohde saavutettu
			if (distanceToTarget <= targetRadius) {
				// M‰‰ritet‰‰n uusi kohde
				++target;
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
		distanceToPlayer = Utility.getDistance((int)wrapper.player.x, (int)wrapper.player.x, _path[0][0], _path[0][1]);
		
		// Luetaan loput reitist‰ ja verrataan kohteiden et‰isyytt‰ edellisiin
		// (vain pelaajasta loittonevat pisteet hyv‰ksyt‰‰n)
		pathLength = 1;
		int index  = 1;
		
		for (int i = 1; i < 9; ++i) {
			int distanceTemp = Utility.getDistance((int)wrapper.player.x, (int)wrapper.player.x, _path[i][0], _path[i][1]);
			
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
