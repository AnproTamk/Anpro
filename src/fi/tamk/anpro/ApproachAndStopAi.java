package fi.tamk.anpro;

import java.lang.Math;

/**
 * Toteutus hyökkäys-pysähdys -tekoälylle. Tekoäly hyökkää pelaajaa kohti
 * mahdollisimman suoraa reittiä pitkin, mutta pysähtyy ennen törmäystä pelaajaan
 * 
 * Käytetään ainoastaan vihollisille.
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
     * Käsittelee tekoälyn.
     */
    @Override
    public final void handleAi()
    {
        /* Verrataan pelaajan sijaintia vihollisen sijaintiin */
        double xDiff = Math.abs((double)(wrapper.enemies.get(parentId).x - wrapper.player.x));
        double yDiff = Math.abs((double)(wrapper.enemies.get(parentId).y - wrapper.player.y));
        
        
        // Lasketaan pelaajan ja vihollisen välinen etäisyys
        double distance = Math.sqrt(Math.pow(xDiff,2) + Math.pow(yDiff,2));
        
        // Vihollinen pysähtyy tietylle etäisyydelle pelaajasta
        if(distance <= 150)
        {
        	wrapper.enemies.get(parentId).movementSpeed = 0;
        }
        
        /* Määritetään objektien välinen kulma */
        double angle;
        
        // Jos vihollinen on pelaajan vasemmalla puolella:
        if (wrapper.enemies.get(parentId).x < wrapper.player.x) {
            // Jos vihollinen on pelaajan alapuolella:
            if (wrapper.enemies.get(parentId).y < wrapper.player.y) {
                angle = (Math.atan(yDiff/xDiff)*180)/Math.PI;
            }
            // Jos vihollinen on pelaajan yläpuolella:
            else if (wrapper.enemies.get(parentId).y > wrapper.player.y) {
                angle = 360 - (Math.atan(yDiff/xDiff)*180)/Math.PI;
            }
            else {
                angle = 0;
            }
        }
        // Jos vihollinen on pelaajan oikealla puolella:
        else if (wrapper.enemies.get(parentId).x > wrapper.player.x) {
            // Jos vihollinen on pelaajan yläpuolella:
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
        // Jos vihollinen on suoraan pelaajan ylä- tai alapuolella
        else {
            if (wrapper.enemies.get(parentId).y > wrapper.player.y) {
                angle = 270;
            }
            else {
                angle = 90;
            }
        }
        
        /* Määritetään kääntymissuunta */
        double angle2 = angle -wrapper.enemies.get(parentId).direction;

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