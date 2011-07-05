package fi.tamk.anpro;


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
        int startCheckpoint = 0;
        
        checkpoints[0][0] =	(int) wrapper.player.x + 150;
        checkpoints[0][1] = (int) wrapper.player.y;
        	
        checkpoints[1][0] = (int) wrapper.player.x + 75;
        checkpoints[1][1] = (int) wrapper.player.y + 75;
        	
        checkpoints[2][0] = (int) wrapper.player.x;
        checkpoints[2][1] = (int) wrapper.player.y + 150;
        	
        checkpoints[3][0] = (int) wrapper.player.x - 75;
        checkpoints[3][1] = (int) wrapper.player.y + 75;
        
    	checkpoints[4][0] = (int) wrapper.player.x - 150;
        checkpoints[4][1] = (int) wrapper.player.y;
        	
        checkpoints[5][0] = (int) wrapper.player.x - 75;
        checkpoints[5][1] = (int) wrapper.player.y - 75;
        	
        checkpoints[6][0] = (int) wrapper.player.x;
        checkpoints[6][1] = (int) wrapper.player.y - 150;
        	
        checkpoints[7][0] = (int) wrapper.player.x + 75;
        checkpoints[7][1] = (int) wrapper.player.y - 75;
        
        // Määritetään vihollisen ja pelaajan välinen kulma
        double startAngle = Utility.getAngle((int) wrapper.enemies.get(parentId).x, (int) wrapper.enemies.get(parentId).y, (int) wrapper.player.x, (int) wrapper.player.y);
        
        if(startAngle >= 0 && startAngle < 45 ) {
        	startCheckpoint = 5;
        }
        
        else if(startAngle >= 45 && startAngle < 90) {
        	startCheckpoint = 6;
        }
        
        else if(startAngle >= 90 && startAngle < 135) {
        	startCheckpoint = 7;
        }
        
        else if(startAngle >= 135 && startAngle < 180) {
        	startCheckpoint = 0;
        }
        
        else if(startAngle >= 180 && startAngle < 225) {
        	startCheckpoint = 1;
        }
        
        else if(startAngle >= 225 && startAngle < 270) {
        	startCheckpoint = 2;
        }
        
        else if(startAngle >= 270 && startAngle < 315) {
        	startCheckpoint = 3;
        }
        
        else if(startAngle >= 315 && startAngle < 360) {
        	startCheckpoint = 4;
        }

        if((int) wrapper.enemies.get(parentId).x != checkpoints[startCheckpoint][0] && (int) wrapper.enemies.get(parentId).y != checkpoints[startCheckpoint][1]) {
        	
        	// Määritetään kääntymissuunta
        	double angle = Utility.getAngle((int) wrapper.enemies.get(parentId).x, (int) wrapper.enemies.get(parentId).y, checkpoints[startCheckpoint][0], checkpoints[startCheckpoint][1]);

        	wrapper.enemies.get(parentId).turningDirection = Utility.getTurningDirection(wrapper.enemies.get(parentId).direction, (int)angle);
        }
        
        if((int) wrapper.enemies.get(parentId).x == checkpoints[startCheckpoint][0] && (int) wrapper.enemies.get(parentId).y == checkpoints[startCheckpoint][1]){
        	
        	++startCheckpoint;
        	
        	if(startCheckpoint == 8) {
        		startCheckpoint = 0;
        	}
        	
        	double angle = Utility.getAngle((int) wrapper.enemies.get(parentId).x, (int) wrapper.enemies.get(parentId).y, checkpoints[startCheckpoint][0], checkpoints[startCheckpoint][1]);

        	wrapper.enemies.get(parentId).turningDirection = Utility.getTurningDirection(wrapper.enemies.get(parentId).direction, (int)angle);
        }
    }
}