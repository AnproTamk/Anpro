package fi.tamk.anpro;

import android.util.Log;

public class PlayerAi extends AbstractAi
{
	public boolean autoPilotActivated = false;
	
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
    	if (!autoPilotActivated) {
	        // M‰‰ritet‰‰n k‰‰ntymissuunta
	        parentObject.turningDirection = Utility.getTurningDirection(parentObject.direction, parentObject.movementTargetDirection);
    	}
    	else {
    		if (parentObject.x < (GameMode.mapWidth - 200) && parentObject.x > -(GameMode.mapWidth + 200) &&
    			parentObject.y < (GameMode.mapHeight - 200) && parentObject.y > -(GameMode.mapHeight + 200)) {
    			parentObject.movementTargetDirection = parentObject.direction; 
    			deactivateAutoPilot();
    		}
    		
    		else {
    			parentObject.turningDirection = Utility.getTurningDirection(parentObject.direction,
    											Utility.getAngle(parentObject.x, parentObject.y, wrapper.mothership.x, wrapper.mothership.y));
    			parentObject.setMovementSpeed(1.0f);
    			parentObject.setMovementDelay(1.0f);
    		}
    	}
    }

    /**
     * K‰ynnist‰‰ autopilotin. K‰ytet‰‰n kun pelaaja menee kent‰n rajojen yli.
     */
    public final void activateAutoPilot()
    {
		autoPilotActivated = true;
		MessageManager.showAutoPilotOnMessage();
    }

    /**
     * Sammuttaa autopilotin. K‰ytet‰‰n kun pelaaja on palautunnut kent‰n rajojen sis‰puolelle.
     */
    public final void deactivateAutoPilot()
    {
    	parentObject.movementAcceleration = -10;
    	
    	autoPilotActivated = false;
		MessageManager.showAutoPilotOffMessage();
    }
}
