package fi.tamk.anpro;

import java.lang.Math;

/**
 * Toteutus pelaajan ymp�rill� py�riv�lle teko�lylle. Teko�ly hakeutuu pelaajan l�helle
 * mahdollisimman suoraa reitti� pitkin kunnes tietyll� et�isyydell� hakeutuu pelaajaa
 * kiert�v�lle radalle ja py�rii pelaajan ymp�ri kunnes pelaaja tuhoaa vihollisen.
 * 
 * K�ytet��n ainoastaan vihollisille.
 * 
 * @extends AbstractAi
 */
public class RotaryAi extends AbstractAi
{
	/**
	 * Alustaa luokan muuttujat.
	 * 
     * @param int Objektin tunnus piirtolistalla
     * @param int Objektin tyyppi
	 */
	public RotaryAi(int _id, int _type) 
	{
		super(_id, _type);
	}
    
    /**
     * K�sittelee teko�lyn.
     * @return 
     */
    @Override
    public final void handleAi()
    {
        /* Verrataan pelaajan sijaintia vihollisen sijaintiin */
        double xDiff = Math.abs((double)(wrapper.enemies.get(parentId).x - wrapper.player.x));
        double yDiff = Math.abs((double)(wrapper.enemies.get(parentId).y - wrapper.player.y));
        double distance = Math.sqrt(Math.pow(xDiff,2) + Math.pow(yDiff,2));

        /* M��ritet��n objektien v�linen kulma */
        double angle;
        
        // Jos vihollinen on pelaajan vasemmalla puolella:
     
        if (wrapper.enemies.get(parentId).x < wrapper.player.x) {
            // Jos vihollinen on pelaajan alapuolella:
            if (wrapper.enemies.get(parentId).y < wrapper.player.y) {
                angle = (Math.atan(yDiff/xDiff)*180)/Math.PI;
            }
            // Jos vihollinen on pelaajan yl�puolella:
            else if (wrapper.enemies.get(parentId).y > wrapper.player.y) {
                angle = 360 - (Math.atan(yDiff/xDiff)*180)/Math.PI;
            }
            else {
                angle = 0;
            }
        }
        // Jos vihollinen on pelaajan oikealla puolella:
        else if (wrapper.enemies.get(parentId).x > wrapper.player.x) {
            // Jos vihollinen on pelaajan yl�puolella:
            if (wrapper.enemies.get(parentId).y > wrapper.player.y) {
                angle = 180 + (Math.atan(yDiff/xDiff)*180)/Math.PI;
            }
            // Jos vihollinen on pelaajan alapuolella:
            else if (wrapper.enemies.get(parentId).y < wrapper.player.y) {
                angle = 180 - (Math.atan(yDiff/xDiff)*180)/Math.PI;
            }
            else {
                angle = 180;
            }
        }
        // Jos vihollinen on suoraan pelaajan yl�- tai alapuolella
        else {
            if (wrapper.enemies.get(parentId).y > wrapper.player.y) {
                angle = 270;
            }
            else {
                angle = 90;
            }
        }
        
        
        if(distance <= 150) {
        	rotativeMovement(distance, angle);
        }
        
        /* M��ritet��n k��ntymissuunta */
        double angle2 = angle - wrapper.enemies.get(parentId).direction;

        if(angle == 0 || angle == 90 || angle == 180 || angle == 270) {
        	wrapper.enemies.get(parentId).turningDirection = 0;
        }
        
        if (angle2 >= -10 && angle2 <= 10) {
            wrapper.enemies.get(parentId).turningDirection = 0;
        }
        else if (angle2 > 0 && angle2 <= 180) {
            wrapper.enemies.get(parentId).turningDirection = 1;
        }
        else {
            wrapper.enemies.get(parentId).turningDirection = 2;
        }
    }

	private void rotativeMovement(double distance, double angle) {
		
		/*double radians = angle * Math.PI / 180;
		
		float new_x = (float) (distance * Math.cos(radians));
		float new_y = (float) (distance * Math.sin(radians));
		
		wrapper.enemies.get(parentId).x = - new_x;
		wrapper.enemies.get(parentId).y = - new_y;
		
		++angle;*/
	}
}