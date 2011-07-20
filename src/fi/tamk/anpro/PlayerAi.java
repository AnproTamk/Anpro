package fi.tamk.anpro;

import android.util.Log;

public class PlayerAi extends AbstractAi
{
	/**
	 * Alustaa luokan muuttujat.
	 * 
	 * @param _id   Objektin tunnus piirtolistalla
	 * @param _type Objektin tyyppi
	 */
	public PlayerAi(AiObject _parentObject, int _userType)
	{
		super(_parentObject, _userType);
	}
	
    /**
     * K‰sittelee teko‰lyn.
     */
    @Override
    public final void handleAi()
    {
        // M‰‰ritet‰‰n k‰‰ntymissuunta
        parentObject.turningDirection = Utility.getTurningDirection(parentObject.direction, parentObject.movementTargetDirection);
    }
    
    /**
     * K‰ynnist‰‰ autopilotin. K‰ytet‰‰n kun pelaaja menee kent‰n rajojen yli.
     */
    public static final void activateAutoPilot()
    {
    	// TODO: Tee toteutus
    }

    /**
     * Sammuttaa autopilotin. K‰ytet‰‰n kun pelaaja on palautunnut kent‰n rajojen sis‰puolelle.
     */
    public static final void deactivateAutoPilot()
    {
    	// TODO: Tee toteutus
    }
        
    /**
     * K‰sittelee autopilotin teko‰lyn.
     */
    public final void handleAutoPilot()
    {
    	// TODO: Tee toteutus
    }
}
