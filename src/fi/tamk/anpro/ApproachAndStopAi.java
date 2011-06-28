package fi.tamk.anpro;

import java.lang.Math;

/**
 * Toteutus hy�kk�ys-pys�hdys -teko�lylle. Teko�ly hy�kk�� pelaajaa kohti
 * mahdollisimman suoraa reitti� pitkin, mutta pys�htyy ennen t�rm�yst� pelaajaan
 * 
 * K�ytet��n ainoastaan vihollisille.
 * 
 * @extends AbstractAi
 */
public class ApproachAndStopAi extends AbstractAi
{
    /**
     * Alustaa luokan muuttujat.
     * 
     * @param int Vihollisen tunnus piirtolistalla
     */
    public ApproachAndStopAi(int _id)
    {
        super(_id);
    }
    
    /**
     * K�sittelee teko�lyn.
     */
    @Override
    public final void handleAi()
    {
        /* Verrataan pelaajan sijaintia vihollisen sijaintiin */
        double xDiff = Math.abs((double)(wrapper.enemies.get(parentId).x - wrapper.player.x));
        double yDiff = Math.abs((double)(wrapper.enemies.get(parentId).y - wrapper.player.y));
        
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
        
        /* M��ritet��n k��ntymissuunta */
        
        if(angle == 0 || angle == 90 || angle == 270) {
        	wrapper.enemies.get(parentId).turningDirection = 0;
        }
        
        double angle2 = angle -wrapper.enemies.get(parentId).direction;

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