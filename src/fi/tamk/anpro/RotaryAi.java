package fi.tamk.anpro;

import java.lang.Math;

/**
 * Toteutus pelaajan ympärillä pyörivälle tekoälylle. Tekoäly hakeutuu pelaajan lähelle
 * mahdollisimman suoraa reittiä pitkin kunnes tietyllä etäisyydellä hakeutuu pelaajaa
 * kiertävälle radalle ja pyörii pelaajan ympäri kunnes pelaaja tuhoaa vihollisen.
 * 
 * Käytetään ainoastaan vihollisille.
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
     * Käsittelee tekoälyn.
     */
    @Override
    public final void handleAi()
    {
        int checkpoints[][] = new int[9][2];
        
        checkpoints[0][0] =	150;
        checkpoints[0][1] = 0;
        	
        checkpoints[1][0] = 150;
        checkpoints[1][1] = 150;
        	
        checkpoints[2][0] = 0;
        checkpoints[2][1] = 150;
        	
        checkpoints[3][0] = -150;
        checkpoints[3][1] = 150;
        
    	checkpoints[4][0] = -150;
        checkpoints[4][1] = 0;
        	
        checkpoints[5][0] = -150;
        checkpoints[5][1] = -150;
        	
        checkpoints[6][0] = 0;
        checkpoints[6][1] = -150;
        	
        checkpoints[7][0] = 150;
        checkpoints[7][1] = -150;
        
        /* Määritetään objektien välinen kulma */
        double angle;
        
        // Jos vihollinen on pelaajan vasemmalla puolella:
        for(int i = 1; i < 9; ++i)
        {
        	double xDiff = Math.abs((double)(wrapper.enemies.get(parentId).x - checkpoints[i][0]));
            double yDiff = Math.abs((double)(wrapper.enemies.get(parentId).y - checkpoints[i][1]));
            //double distance = Math.sqrt(Math.pow(xDiff,2) + Math.pow(yDiff,2));
            
	        if (wrapper.enemies.get(parentId).x < checkpoints[i][0]) {
	            // Jos vihollinen on pelaajan alapuolella:
	            if (wrapper.enemies.get(parentId).y < checkpoints[i][1]) {
	                angle = (Math.atan(yDiff/xDiff)*180)/Math.PI;
	            }
	            // Jos vihollinen on pelaajan yläpuolella:
	            else if (wrapper.enemies.get(parentId).y > checkpoints[i][1]) {
	                angle = 360 - (Math.atan(yDiff/xDiff)*180)/Math.PI;
	            }
	            else {
	                angle = 0;
	            }
	        }
	        // Jos vihollinen on pelaajan oikealla puolella:
	        else if (wrapper.enemies.get(parentId).x > checkpoints[i][0]) {
	            // Jos vihollinen on pelaajan yläpuolella:
	            if (wrapper.enemies.get(parentId).y > checkpoints[i][1]) {
	                angle = 180 + (Math.atan(yDiff/xDiff)*180)/Math.PI;
	            }
	            // Jos vihollinen on pelaajan alapuolella:
	            else if (wrapper.enemies.get(parentId).y < checkpoints[i][1]) {
	                angle = 180 - (Math.atan(yDiff/xDiff)*180)/Math.PI;
	            }
	            else {
	                angle = 180;
	            }
	        }
	        // Jos vihollinen on suoraan pelaajan ylä- tai alapuolella
	        else {
	            if (wrapper.enemies.get(parentId).y > checkpoints[i][1]) {
	                angle = 270;
	            }
	            else {
	                angle = 90;
	            }
	        }
	        
	        /* Määritetään kääntymissuunta */
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

    }
}