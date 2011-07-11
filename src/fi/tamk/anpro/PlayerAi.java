package fi.tamk.anpro;

public class PlayerAi extends AbstractAi
{
	public PlayerAi(int _id, int _type)
	{
		super(_id, _type);
	}
	
    /**
     * K‰sittelee teko‰lyn.
     */
    @Override
    public final void handleAi()
    {
        // M‰‰ritet‰‰n k‰‰ntymissuunta
        wrapper.player.turningDirection = Utility.getTurningDirection(wrapper.player.direction, wrapper.player.movementTargetDirection);
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
