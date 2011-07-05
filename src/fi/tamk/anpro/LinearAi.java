package fi.tamk.anpro;


/**
 * Toteutus lineaarisen reitinhaun teko‰lylle. Teko‰ly hakeutuu kohteenseensa
 * mahdollisimman suoraa reitti‰ pitkin, eik‰ reagoi pelikent‰n tapahtumiin.
 * 
 * K‰ytet‰‰n ainoastaan vihollisille.
 */
public class LinearAi extends AbstractAi
{
	/**
	 * Alustaa luokan muuttujat.
	 * 
     * @param int Objektin tunnus piirtolistalla
     * @param int Objektin tyyppi
	 */
	public LinearAi(int _id, int _type) 
	{
		super(_id, _type);
    }
	
    /**
     * K‰sittelee teko‰lyn.
     */
    @Override
    public final void handleAi()
    {
    	// M‰‰ritet‰‰n vihollisen ja pelaajan v‰linen kulma
    	double angle = Utility.getAngle((int) wrapper.enemies.get(parentId).x, (int) wrapper.enemies.get(parentId).y,(int) wrapper.player.x,(int) wrapper.player.y);
    	
        /* M‰‰ritet‰‰n k‰‰ntymissuunta */
        wrapper.enemies.get(parentId).turningDirection = Utility.getTurningDirection(wrapper.enemies.get(parentId).direction, (int)angle);
        
        /* Tarkistetaan tˆrm‰ykset pelaajan kanssa */
        checkCollisionWithPlayer();
    }
}


