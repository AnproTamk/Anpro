package fi.tamk.anpro;

/**
 * Sisältää käyttöliittymän joystickin toiminnallisuudet.
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
        
        joystickActivated = true; // TODO: Muualla koodissa pitää tarkistaa, onko joystick käytössä vai ei
    }
}
