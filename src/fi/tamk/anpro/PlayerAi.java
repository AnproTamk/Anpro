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

	/* =======================================================
	 * Perityt funktiot
	 * ======================================================= */
    /**
     * K‰sittelee teko‰lyn.
     */
    @Override
    public final void handleAi()
    {
        // M‰‰ritet‰‰n k‰‰ntymissuunta
        parentObject.turningDirection = Utility.getTurningDirection(parentObject.direction, parentObject.movementTargetDirection);
    }
}
