package fi.tamk.anpro;

/**
 * Sisältää käyttöliittymän joystickin toiminnallisuudet.
 * 
 * @extends GuiObject
 */
public class Joystick extends GuiObject
{

    /**
     * Alustaa luokan muuttujat.
     * 
     * @param int Objektin X-koordinaatti
     * @param int Objektin Y-koordinaatti
     */
    public Joystick(int _x, int _y)
    {
        super(_x, _y);
        
        usedTexture = GLRenderer.TEXTURE_JOYSTICK;
        
        TouchManager.initJoystick(x, y);
    }

}
