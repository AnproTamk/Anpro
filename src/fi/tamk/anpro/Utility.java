package fi.tamk.anpro;

import java.util.Random;

/**
 * Yleinen apuluokka, joka sisältää usein käytettyjä toimintoja, kuten etäisyyksien
 * ja kulmien laskemisen.
 */
public class Utility
{
	private static Random randomGenerator = new Random();
	
	/**
	 * Laskee kahden pisteen välisen etäisyyden.
	 * 
	 * @param _firstX Pisteen #1 X-koordinaatti
	 * @param _firstY Pisteen #1 Y-koordinaatti
	 * @param _secondX Pisteen #2 X-koordinaatti
	 * @param _secondY Pisteen #2 Y-koordinaatti
	 * 
	 * @return Etäisyys
	 */
	public static int getDistance(float _firstX, float _firstY, float _secondX, float _secondY)
	{
		return (int) Math.sqrt(Math.pow(_firstX - _secondX, 2) + Math.pow(_firstY - _secondY, 2));
	}

	/**
	 * Laskee kahden pisteen välisen kulman. Nollakulman osoittava vektori sijoitetaan lähtemään
	 * ensimmäisestä pisteestä oikealle.
	 * 
	 * @param _firstX Pisteen #1 X-koordinaatti
	 * @param _firstY Pisteen #1 Y-koordinaatti
	 * @param _secondX Pisteen #2 X-koordinaatti
	 * @param _secondY Pisteen #2 Y-koordinaatti
	 * 
	 * @return Kulma
	 */
	public static int getAngle(float _firstX, float _firstY, float _secondX, float _secondY)
	{
		// TODO: Mahdollisuus lähettää float-muuttujia?
		
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
	 * Määrittää kääntymissuunnan objektin sijainnin, sen katsomissuunnan ja kohteen
	 * sijainnin perusteella.
	 * 
	 * @param _direction Objektin katsomissuunta
	 * @param _angle Objektin ja kohteen välinen kulma
	 * 
	 * @return Kääntymissuunta (0 ei käännytä, 1 vasen, 2 oikea)
	 */
	public static int getTurningDirection(int _direction, int _angle)
	{
        double angle2 = _angle - _direction;
        
        if (angle2 >= -10 && angle2 <= 10) {
            return 0;
        }
        else if (angle2 > 10 && angle2 <= 180 || (angle2 <= -180 && angle2 >= -359)) {
            return 1;
        }
        
        else if (angle2 <= -10 && angle2 >= -180) {
        	return 2;
        }
        
        else {
        	return 2;
        }
	}
    
    /**
     * Tarkastaa, tapahtuuko kahden objektin välillä törmäys.
     * 
     * @param _first Ensimmäinen objekti
     * @param _second Toinen objekti
     * 
     * @return Törmäävätkö objektit
     */
    public static final boolean isColliding(GameObject _first, GameObject _second)
    {
    	float distance = Utility.getDistance(_first.x, _first.y, _second.x, _second.y);
    	
        if (distance - _first.collisionRadius - _second.collisionRadius <= 0) {
           	return true;
        }
        else {
        	return false;
        }
    }
    
    /**
     * Tarkastaa, onko kohde vahingon aiheuttaja passiivisen vahingon vaikutusalueella.
     * 
     * @param _attacker Vahingon aiheuttaja
     * @param _target   Kohde
     * 
     * @return Vaikuttaako vahinko
     */
    public static final boolean isInDamageRadius(AbstractProjectile _attacker, GameObject _target)
    {
    	float distance = Utility.getDistance(_attacker.x, _attacker.y, _target.x, _target.y);
    	
        if (distance - _attacker.damageRadius - _target.collisionRadius <= 0) {
           	return true;
        }
        else {
        	return false;
        }
    }
    
    /**
     * Käsittelee aiheutuneen vahingon määrän suojiin ja kohteeseen.
     * 
     * @param _target Kohdeobjekti
     * @param _damage Vaurion määrä
     * @param _armorPiercing Läpäisykyky
     */
    public static final void checkDamage(GameObject _target, int _damage, int _armorPiercing)
    {
        _target.currentArmor -= calculateDamageToArmor(_damage, _armorPiercing);
        
        if (_target.currentArmor < 0) {
        	_target.currentHealth -= calculateDamageToHealth(_damage, _armorPiercing, Math.abs(_target.currentArmor));
        	_target.currentArmor = 0;
        }
        else {
        	_target.currentHealth -= calculateDamageToHealth(_damage, _armorPiercing, 0);
        }
    }

    /**
     * Laskee aiheutuneen vahingon määrän suojiin.
     * 
     * @param _damage Vaurion määrä
     * @param _armorPiercing Läpäisykyky
     * 
     * @return Aiheutunut vahinko
     */
    public static final int calculateDamageToArmor(int _damage, int _armorPiercing)
    {
    	return (int) ((float) _damage * (1 - (float) _armorPiercing * 0.05));
    }

    /**
     * Laskee aiheutuneen vahingon määrän kohteeseen.
     * 
     * @param _damage Vaurion määrä
     * @param _armorPiercing Läpäisykyky
     * @param _excessDamage "Ylijäämävahinko" suojasta
     * 
     * @return Aiheutunut vahinko
     */
    public static final int calculateDamageToHealth(int _damage, int _armorPiercing, int _excessDamage)
    {
    	return (int) ((float) _damage * (float) _armorPiercing * 0.05) + _excessDamage;
    }
    
    /**
     * Palauttaa satunnaisen kokonaisluvun halutulta alueelta.
     * 
     * @param _min Alaraja
     * @param _max Yläraja
     * 
     * @return Satunnainen kokonaisluku
     */
    public static final int getRandom(int _min, int _max)
    {
    	if (_min < 0 && _max < 0) {
    		return randomGenerator.nextInt(Math.abs(_min - _max)) + _min;
    	}
    	else if (_min < 0) {
    		return randomGenerator.nextInt(Math.abs(_min) + _max) + _min;
    	}
    	else {
    		return randomGenerator.nextInt(_max - _min) + _min;
    	}
    }
}
