package fi.tamk.anpro;

/**
 * Yleinen apuluokka, joka sis‰lt‰‰ usein k‰ytettyj‰ toimintoja, kuten et‰isyyksien
 * ja kulmien laskemisen.
 */
public class Utility
{
	/**
	 * Laskee kahden pisteen v‰lisen et‰isyyden.
	 * 
	 * @param float Pisteen #1 X-koordinaatti
	 * @param float Pisteen #1 Y-koordinaatti
	 * @param float Pisteen #2 X-koordinaatti
	 * @param float Pisteen #2 Y-koordinaatti
	 * 
	 * @return int Et‰isyys
	 */
	public static int getDistance(float _firstX, float _firstY, float _secondX, float _secondY)
	{
		return (int) Math.sqrt(Math.pow(_firstX - _secondX, 2) + Math.pow(_firstY - _secondY, 2));
	}

	/**
	 * Laskee kahden pisteen v‰lisen kulman. Nollakulman osoittava vektori sijoitetaan l‰htem‰‰n
	 * ensimm‰isest‰ pisteest‰ oikealle.
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
	 * M‰‰ritt‰‰ k‰‰ntymissuunnan objektin sijainnin, sen katsomissuunnan ja kohteen
	 * sijainnin perusteella.
	 * 
	 * @param int Objektin katsomissuunta
	 * @param int Objektin ja kohteen v‰linen kulma
	 * 
	 * @return int K‰‰ntymissuunta (0 ei k‰‰nnyt‰, 1 vasen, 2 oikea)
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
