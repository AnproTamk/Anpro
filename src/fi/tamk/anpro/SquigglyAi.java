package fi.tamk.anpro;

/**
 * Toteutus "v‰istelev‰lle" teko‰lylle. Teko‰ly seuraa l‰hes siniaallon
 * muotoista reitti‰ satunnaiseen suuntaan mutta kuitenkin lopulta pelaajaa kohti.
 * 
 * K‰ytet‰‰n ainoastaan vihollisille.
 */
public class SquigglyAi extends AbstractAi
{
	private long lastDirectionUpdate = 0;
	
	/**
	 * Alustaa luokan muuttujat.
	 * 
     * @param int Objektin tunnus piirtolistalla
     * @param int Objektin tyyppi
	 */
	public SquigglyAi(int _id, int _type) 
	{
		super(_id, _type);
	}
    
    /**
     * K‰sittelee teko‰lyn.
     */
	@Override
    public final void handleAi()
	{ 
		// Jos vihollinen on kaukana pelaajasta, k‰ytet‰‰n v‰liaikaisesti LinearAi:n kaltaista toimintaa kunnes ollaan tarpeeksi l‰hell‰ pelaajaa
		double distance = Utility.getDistance(wrapper.enemies.get(parentId).x, wrapper.enemies.get(parentId).y, wrapper.player.x, wrapper.player.y);
		
		if(distance > 500) {
			
			double angle = Utility.getAngle((int) wrapper.enemies.get(parentId).x, (int) wrapper.enemies.get(parentId).y,(int) wrapper.player.x,(int) wrapper.player.y);
			
			wrapper.enemies.get(parentId).turningDirection = Utility.getTurningDirection(wrapper.enemies.get(parentId).direction, (int)angle);

		}
		
		else {
			if(lastDirectionUpdate == 0) {
				
				lastDirectionUpdate = android.os.SystemClock.uptimeMillis();
				
				// M‰‰ritet‰‰n vihollisen ja pelaajan v‰linen kulma
				double angle = Utility.getAngle((int) wrapper.enemies.get(parentId).x, (int) wrapper.enemies.get(parentId).y,(int) wrapper.player.x,(int) wrapper.player.y);
	    	
				/* M‰‰ritet‰‰n k‰‰ntymissuunta */
				wrapper.enemies.get(parentId).turningDirection = Utility.getTurningDirection(wrapper.enemies.get(parentId).direction, (int)angle);
			}
			
			else {
				
				long currentTime = android.os.SystemClock.uptimeMillis();
				
				if(currentTime - lastDirectionUpdate >= 1500 && currentTime - lastDirectionUpdate < 2000) {
					
					wrapper.enemies.get(parentId).turningDirection = 1;
				}
				
				else if(currentTime - lastDirectionUpdate >= 2000 && currentTime - lastDirectionUpdate < 2500) {
					
					wrapper.enemies.get(parentId).turningDirection = 0;
				}
				
				else if(currentTime - lastDirectionUpdate >= 2500 && currentTime - lastDirectionUpdate < 3000) {
					
					wrapper.enemies.get(parentId).turningDirection = 2;
				}
				
				else if(currentTime - lastDirectionUpdate >= 3000)
				{
					lastDirectionUpdate = currentTime;
				}
				
				else {
					// M‰‰ritet‰‰n vihollisen ja pelaajan v‰linen kulma
					double angle = Utility.getAngle((int) wrapper.enemies.get(parentId).x, (int) wrapper.enemies.get(parentId).y,(int) wrapper.player.x,(int) wrapper.player.y);
		    	
					/* M‰‰ritet‰‰n k‰‰ntymissuunta */
					wrapper.enemies.get(parentId).turningDirection = Utility.getTurningDirection(wrapper.enemies.get(parentId).direction, (int)angle);
				}
			}
		}
		
        /* Tarkistetaan tˆrm‰ykset pelaajan kanssa */
        checkCollisionWithPlayer();
    }
}
