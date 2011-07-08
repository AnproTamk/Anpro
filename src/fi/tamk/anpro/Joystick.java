package fi.tamk.anpro;

/**
 * Sis�lt�� k�ytt�liittym�n joystickin toiminnallisuudet.
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
     * M��ritt�� pelaajan kulkusuunnan ollessaan k�yt�ss�
     * 
     * @param _xClickOffset Painalluksen X-koordinaatti
     * @param _yClickOffset Painalluksen Y-koordinaatti
     * @param _getX			Painalluksen alkuper�inen X-koordinaatti
     * @param _getY			Painalluksen alkuper�inen Y-koordinaatti
     * @param _wrapper		Wrapperi
     * 
     * @return Totuus
     */
    public static boolean useJoystick(int _xClickOffset, int _yClickOffset, int _getX, int _getY,
    						          Wrapper _wrapper)
    {
    	_xClickOffset = _getX - Options.screenWidth / 2;
        _yClickOffset = Options.screenHeight / 2 - _getY;
    	
    	// Verrataan sormen sijaintia joystickin sijaintiin
        double xDiff = Math.abs((double)(_xClickOffset - joystickX));
        double yDiff = Math.abs((double)(_yClickOffset - joystickY));
        
        // M��ritet��n sormen ja joystickin v�linen kulma
        int angle;
        
        // Jos sormi on joystickin oikealla puolella:
        if (_xClickOffset > joystickX) {
            // Jos sormi on joystickin alapuolella:
            if (_yClickOffset > joystickY) {
                angle = (int) ((Math.atan(yDiff/xDiff)*180)/Math.PI);
            }
            // Jos sormi on joystickin yl�puolella:
            else if (_yClickOffset < joystickY) {
                angle = (int) (360 - (Math.atan(yDiff/xDiff)*180)/Math.PI);
            }
            else {
                angle = 0;
            }
        }
        // Jos sormi on joystickin vasemmalla puolella:
        else if (_xClickOffset < joystickX) {
            // Jos sormi on joystickin yl�puolella:
            if (_yClickOffset < joystickY) {
                angle = (int) (180 + (Math.atan(yDiff/xDiff)*180)/Math.PI);
            }
            // Jos sormi on joystickin alapuolella:
            else if (_yClickOffset > joystickY) {
                angle = (int) (180 - (Math.atan(yDiff/xDiff)*180)/Math.PI);
            }
            else {
                angle = 180;
            }
        }
        // Jos sormi on suoraan joystickin yl�- tai alapuolella
        else {
            if (_yClickOffset > joystickY) {
                angle = 90;
            }
            else {
                angle = 270;
            }
        }

        _wrapper.player.direction = angle;
        
        return true;
    }
}
