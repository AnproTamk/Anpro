package fi.tamk.anpro;

public class BackgroundManager
{
	private BackgroundStar[] backgroundStars;
	
	public BackgroundManager(Wrapper _wrapper)
	{
		backgroundStars = new BackgroundStar[15];
		
    	for (int i = 0; i < 15; ++i) {
    		backgroundStars[i] = new BackgroundStar(Utility.getRandom(-(Options.scaledScreenWidth/2), Options.scaledScreenWidth/2),
    												Utility.getRandom(-(Options.scaledScreenHeight/2), Options.scaledScreenHeight/2), _wrapper);
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
