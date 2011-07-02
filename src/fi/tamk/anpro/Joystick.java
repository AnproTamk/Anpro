package fi.tamk.anpro;

/**
 * Sis�lt�� k�ytt�liittym�n joystickin toiminnallisuudet.
 */
public class Joystick extends GuiObject
{
	private static boolean 	 joystickActivated;
	public  static int 		 joystickX;
	public  static int		 joystickY;
	
    /**
     * Alustaa luokan muuttujat.
     * 
     * @param int Objektin X-koordinaatti
     * @param int Objektin Y-koordinaatti
     */
    public Joystick(int _x, int _y)
    {
        super(_x, _y);
        
        joystickX         = _x;
        joystickY         = _y;
        joystickActivated = false;
        
        usedTexture = GLRenderer.TEXTURE_JOYSTICK;
    }
    
    /**
     * Ottaa Joystickin k�ytt��n ja tallentaa sen koordinaatit.
     */
    public static void initJoystick()
    {
        joystickActivated = true; // TODO: Muualla koodissa pit�� tarkistaa, onko joystick k�yt�ss� vai ei
    }
}
