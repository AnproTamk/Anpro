package fi.tamk.anpro;

/**
 * Sis‰lt‰‰ k‰yttˆliittym‰n joystickin toiminnallisuudet.
 */
public class Joystick extends GuiObject
{
	public static int 	  joystickX;
	public static int 	  joystickY;
	public static boolean joystickInUse;
	public static boolean joystickDown;
	
    /**
     * Alustaa luokan muuttujat.
     * 
     * @param int Objektin X-koordinaatti
     * @param int Objektin Y-koordinaatti
     */
    public Joystick(int _x, int _y)
    {
        super(_x, _y);
        
        joystickX = _x;
        joystickY = _y;
        joystickDown = false;
        joystickInUse = false;
        
        usedTexture = GLRenderer.TEXTURE_JOYSTICK;
    }
    
    /**
     * M‰‰ritt‰‰ pelaajan kulkusuunnan ollessaan k‰ytˆss‰
     * 
     * @param _xClickOffset Painalluksen X-koordinaatti
     * @param _yClickOffset Painalluksen Y-koordinaatti
     * @param _getX			Painalluksen alkuper‰inen X-koordinaatti
     * @param _getY			Painalluksen alkuper‰inen Y-koordinaatti
     * @param _wrapper		Wrapperi
     * 
     * @return Totuus
     */
    public static boolean useJoystick(int _xClickOffset, int _yClickOffset, int _getX, int _getY,
    						          Wrapper _wrapper)
    {
    	_xClickOffset = _getX - Options.screenWidth / 2;
        _yClickOffset = Options.screenHeight / 2 - _getY;
    	
        // M‰‰ritet‰‰n sormen ja joystickin v‰linen kulma
        int angle = Utility.getAngle(joystickX, joystickY, _xClickOffset, _yClickOffset);
        
        // Muutetaan pelaajan suunta ja nopeus
        _wrapper.player.movementTargetDirection = angle;
        _wrapper.player.movementAcceleration    = 0;
        _wrapper.player.setMovementSpeed(1.0f);
        _wrapper.player.setMovementDelay(1.0f);
        
        // N‰ytet‰‰n j‰lkipolttoefekti
        EffectManager.showPlayerTrailEffect(_wrapper.player);
        
        return true;
    }
}
