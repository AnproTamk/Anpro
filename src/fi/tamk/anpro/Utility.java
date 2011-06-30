package fi.tamk.anpro;

public class Utility
{
	/**
	 * Laskee kahden pisteen välisen etäisyyden.
	 * 
	 * @param int Pisteen #1 X-koordinaatti
	 * @param int Pisteen #1 Y-koordinaatti
	 * @param int Pisteen #2 X-koordinaatti
	 * @param int Pisteen #2 Y-koordinaatti
	 * 
	 * @return int Etäisyys
	 */
	public static int getDistance(int _firstX, int _firstY, int _secondX, int _secondY)
	{
		return (int) Math.sqrt(Math.pow(_firstX - _secondX, 2) + Math.pow(_firstY - _secondY, 2));
	}

	/**
	 * Laskee kahden pisteen välisen kulman. Nollakulman osoittava vektori sijoitetaan ensimmäisen
	 * koordinaatin kohdalle.
	 * 
	 * @param int Pisteen #1 X-koordinaatti
	 * @param int Pisteen #1 Y-koordinaatti
	 * @param int Pisteen #2 X-koordinaatti
	 * @param int Pisteen #2 Y-koordinaatti
	 * 
	 * @return int Kulma
	 */
	public static int getAngle(int _firstX, int _firstY, int _secondX, int _secondY)
	{
		// Valitaan suunta
        float xDiff = Math.abs((float)(_firstX - _secondX));
        float yDiff = Math.abs((float)(_firstY - _secondY));
        
        if (_firstX < _secondX) {
            if (_firstY < _secondY) {
                return (int) ((Math.atan(yDiff/xDiff)*180)/Math.PI);
            }
            else if (_firstY > _secondY) {
            	return (int) (360 - (Math.atan(yDiff/xDiff)*180)/Math.PI);
            }
            else {
            	return 0;
            }
        }
        else if (_firstX > _secondX) {
            if (_firstY > _secondY) {
            	return (int) (180 + (Math.atan(yDiff/xDiff)*180)/Math.PI);
            }
            else if (_firstY < _secondY) {
            	return (int) (180 - (Math.atan(yDiff/xDiff)*180)/Math.PI);
            }
            else {
            	return 180;
            }
        }
        else {
            if (_firstY > _secondY) {
            	return 270;
            }
            else {
            	return 90;
            }
        }
	}
	
	/**
	 * Määrittää kääntymissuunnan objektin ja kohteen välisen kulman perusteella.
	 * 
	 * @param int Objektin katsomissuunta
	 * @param int Objektin ja kohteen välinen kulma
	 * 
	 * @return int Kääntymissuunta (0 ei käännytä, 1 vasen, 2 oikea)
	 */
	public static int getTurningDirection(int _direction, int _angle)
	{
        double angle2 = _angle - _direction;
        
        if (angle2 >= -10 && angle2 <= 10) {
            return 0;
        }
        else if (angle2 > 0 && angle2 <= 180) {
            return 1;
        }
        else {
            return 2;
        }
	}
}
