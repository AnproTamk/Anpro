package fi.tamk.anpro;

/**
 * Toteutus "v‰istelev‰lle" teko‰lylle. Teko‰ly seuraa l‰hes siniaallon
 * muotoista reitti‰ kohti pelaajaa.
 * 
 * K‰ytet‰‰n ainoastaan vihollisille.
 */
public class SquigglyAi extends AbstractAi
{
	private long lastDirectionUpdate;
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
            if(lastDirectionUpdate == 0) {
            	
            	lastDirectionUpdate = android.os.SystemClock.uptimeMillis();
            	
            	// M‰‰ritet‰‰n vihollisen ja pelaajan v‰linen kulma
            	double angle = Utility.getAngle((int) wrapper.enemies.get(parentId).x, (int) wrapper.enemies.get(parentId).y,(int) wrapper.player.x,(int) wrapper.player.y);
            	
            	// M‰‰ritet‰‰n vihollisen k‰‰ntymissuunta
                wrapper.enemies.get(parentId).turningDirection = Utility.getTurningDirection(wrapper.enemies.get(parentId).direction, (int)angle);
            }
            
            else {
        		long currentTime = android.os.SystemClock.uptimeMillis();
            	
            	if (currentTime - lastDirectionUpdate >= 1000) {
            		
            		lastDirectionUpdate = currentTime;
            		
            		// M‰‰ritet‰‰n vihollisen ja pelaajan v‰linen kulma
            		double angle = Utility.getAngle((int) wrapper.enemies.get(parentId).x, (int) wrapper.enemies.get(parentId).y,(int) wrapper.player.x,(int) wrapper.player.y);
            		
            		// M‰‰ritet‰‰n vihollisen k‰‰ntymissuunta
            		wrapper.enemies.get(parentId).turningDirection = Utility.getTurningDirection(wrapper.enemies.get(parentId).direction, (int)angle);
                }
            	
            }
            	
            /* Tarkistetaan tˆrm‰ykset pelaajan kanssa */
            checkCollisionWithPlayer();
    }
}
