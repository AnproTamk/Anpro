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
	public LinearAi(AiObject _parentObject, int _userType) 
	{
		super(_parentObject, _userType);
    }

	/* =======================================================
	 * Perityt funktiot
	 * ======================================================= */
    /**
     * K‰sittelee teko‰lyn.
     */
    @Override
    public final void handleAi()
    {
    	// M‰‰ritet‰‰n vihollisen ja pelaajan v‰linen kulma
    	double angle = Utility.getAngle((int) parentObject.x, (int) parentObject.y,(int) wrapper.player.x,(int) wrapper.player.y);
    	
        /* M‰‰ritet‰‰n k‰‰ntymissuunta */
        parentObject.turningDirection = Utility.getTurningDirection(parentObject.direction, (int)angle);
        
        /* Tarkistetaan tˆrm‰ykset pelaajan kanssa */
        checkCollisionWithPlayer();
    }
}


