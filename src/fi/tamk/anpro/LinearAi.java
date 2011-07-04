package fi.tamk.anpro;

import java.lang.Math;

/**
 * Toteutus lineaarisen reitinhaun teko‰lylle. Teko‰ly hakeutuu kohteenseensa
 * mahdollisimman suoraa reitti‰ pitkin, eik‰ reagoi pelikent‰n tapahtumiin.
 * 
 * K‰ytet‰‰n ainoastaan vihollisille.
 */
public class LinearAi extends AbstractAi
{
	/**
	 * Alustaa luokan muuttujat.
	 * 
     * @param int Objektin tunnus piirtolistalla
     * @param int Objektin tyyppi
	 */
	public LinearAi(int _id, int _type) 
	{
		super(_id, _type);
    }
	
    /**
     * K‰sittelee teko‰lyn.
     */
    @Override
    public final void handleAi()
    {
        /* Verrataan pelaajan sijaintia vihollisen sijaintiin */
        double xDiff = Math.abs((double)(wrapper.enemies.get(parentId).x - wrapper.player.x));
        double yDiff = Math.abs((double)(wrapper.enemies.get(parentId).y - wrapper.player.y));
        
        /* M‰‰ritet‰‰n objektien v‰linen kulma */
        double angle;
        
        // Jos vihollinen on pelaajan vasemmalla puolella:
        if (wrapper.enemies.get(parentId).x < wrapper.player.x) {
            // Jos vihollinen on pelaajan alapuolella:
            if (wrapper.enemies.get(parentId).y < wrapper.player.y) {
                angle = (Math.atan(yDiff/xDiff)*180)/Math.PI;
            }
            // Jos vihollinen on pelaajan yl‰puolella:
            else if (wrapper.enemies.get(parentId).y > wrapper.player.y) {
                angle = 360 - (Math.atan(yDiff/xDiff)*180)/Math.PI;
            }
            else {
                angle = 0;
            }
        }
        // Jos vihollinen on pelaajan oikealla puolella:
        else if (wrapper.enemies.get(parentId).x > wrapper.player.x) {
            // Jos vihollinen on pelaajan yl‰puolella:
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
        // Jos vihollinen on suoraan pelaajan yl‰- tai alapuolella
        else {
            if (wrapper.enemies.get(parentId).y > wrapper.player.y) {
                angle = 270;
            }
            else {
                angle = 90;
            }
        }
        
        /* M‰‰ritet‰‰n k‰‰ntymissuunta */
        wrapper.enemies.get(parentId).turningDirection = Utility.getTurningDirection(wrapper.enemies.get(parentId).direction, (int)angle);
        
        /* Tarkistetaan tˆrm‰ykset pelaajan kanssa */
        checkCollisionWithPlayer();
    }
}


