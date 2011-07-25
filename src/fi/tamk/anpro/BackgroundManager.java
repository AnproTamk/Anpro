package fi.tamk.anpro;

public class BackgroundManager
{
	private BackgroundStar[] backgroundStars;
	
	public BackgroundManager(Wrapper _wrapper)
	{
		backgroundStars = new BackgroundStar[15];
		
    	for (int i = 0; i < 15; ++i) {
    		backgroundStars[i] = new BackgroundStar(Utility.getRandom(-(Options.scaledScreenWidth), Options.scaledScreenWidth),
    												Utility.getRandom(-(Options.scaledScreenHeight), Options.scaledScreenHeight), _wrapper);
    		
    		backgroundStars[i].direction = Utility.getRandom(0, 359);
    	}
	}
	
    /* =======================================================
     * Uudet funktiot
     * ======================================================= */
	public final void updatePositions()
	{
		for (int i = 0; i < 15; ++i) {
			backgroundStars[i].updatePosition();
		}
	}
}
