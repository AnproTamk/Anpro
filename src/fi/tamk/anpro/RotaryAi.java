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
	private WeaponManager weaponManager;
	
	private long lastShootingTime = 0;
	
	/**
	 * Alustaa luokan muuttujat.
	 * 
     * @param int Objektin tunnus piirtolistalla
     * @param int Objektin tyyppi
	 */
	public RotaryAi(int _id, int _type, WeaponManager _weaponManager) 
	{
		super(_id, _type);
		
		weaponManager = _weaponManager;
	}
    
    /**
     * Käsittelee tekoälyn.
     */
    @Override
    public final void handleAi()
    {
    	// Alustetaan tarvittavat muuttujat
        int checkpoints[][] = new int[13][2];
        int startCheckpoint = 0;
        
        
        // 12 "checkpointtia" ympyrän kaarella 30 asteen välein. Vihollisten on kuljettava näiden kautta.
        checkpoints[0][0] =	(int) wrapper.player.x + 150;
        checkpoints[0][1] = (int) wrapper.player.y;
        	
        checkpoints[1][0] = (int) wrapper.player.x + 130;
        checkpoints[1][1] = (int) wrapper.player.y + 75;
        	
        checkpoints[2][0] = (int) wrapper.player.x + 75;
        checkpoints[2][1] = (int) wrapper.player.y + 130;
        	
        checkpoints[3][0] = (int) wrapper.player.x;
        checkpoints[3][1] = (int) wrapper.player.y + 150;
        
    	checkpoints[4][0] = (int) wrapper.player.x - 75;
        checkpoints[4][1] = (int) wrapper.player.y + 130;
        	
        checkpoints[5][0] = (int) wrapper.player.x - 130;
        checkpoints[5][1] = (int) wrapper.player.y + 75;
        	
        checkpoints[6][0] = (int) wrapper.player.x - 150;
        checkpoints[6][1] = (int) wrapper.player.y;
        	
        checkpoints[7][0] = (int) wrapper.player.x - 130;
        checkpoints[7][1] = (int) wrapper.player.y - 75;
        
        checkpoints[8][0] = (int) wrapper.player.x - 75;
        checkpoints[8][1] = (int) wrapper.player.y - 130;
        
    	checkpoints[9][0] = (int) wrapper.player.x;
        checkpoints[9][1] = (int) wrapper.player.y - 150;
        	
        checkpoints[10][0] = (int) wrapper.player.x + 75;
        checkpoints[10][1] = (int) wrapper.player.y - 130;
        	
        checkpoints[11][0] = (int) wrapper.player.x + 130;
        checkpoints[11][1] = (int) wrapper.player.y - 75;

        // Määritetään vihollisen ja pelaajan välinen kulma
        double startAngle = Utility.getAngle((int) wrapper.enemies.get(parentId).x, (int) wrapper.enemies.get(parentId).y, (int) wrapper.player.x, (int) wrapper.player.y);
        
        // Vihollisen ja pelaajan välisellä kulmalla määritetään kunkin vihollisen ensimmäinen "checkpoint", josta ympyräliike alkaa
        if(startAngle >= 0 && startAngle < 30 ) {
        	startCheckpoint = 7;
        }
        
        else if(startAngle >= 30 && startAngle < 60) {
        	startCheckpoint = 8;
        }
        
        else if(startAngle >= 60 && startAngle < 90) {
        	startCheckpoint = 9;
        }
        
        else if(startAngle >= 90 && startAngle < 120) {
        	startCheckpoint = 10;
        }
        
        else if(startAngle >= 120 && startAngle < 150) {
        	startCheckpoint = 11;
        }
        
        else if(startAngle >= 150 && startAngle < 180) {
        	startCheckpoint = 0;
        }
        
        else if(startAngle >= 180 && startAngle < 210) {
        	startCheckpoint = 1;
        }
        
        else if(startAngle >= 210 && startAngle < 240) {
        	startCheckpoint = 2;
        }
        
        else if(startAngle >= 240 && startAngle < 270) {
        	startCheckpoint = 3;
        }
        
        else if(startAngle >= 270 && startAngle < 300) {
        	startCheckpoint = 4;
        }
        
        else if(startAngle >= 300 && startAngle < 330) {
        	startCheckpoint = 5;
        }
        
        else if(startAngle >= 330 && startAngle < 360) {
        	startCheckpoint = 6;
        }

        // Jos vihollinen on kaukana pelaajasta, vihollinen käyttää LinearAi:ta väliaikaisesti kunnes on tarpeeksi lähellä
        double distance = Utility.getDistance(wrapper.enemies.get(parentId).x, wrapper.enemies.get(parentId).y, wrapper.player.x, wrapper.player.y);
        
        if(distance > 300) {
        	
        	double angle = Utility.getAngle((int) wrapper.enemies.get(parentId).x, (int) wrapper.enemies.get(parentId).y, wrapper.player.x, wrapper.player.y);
        	
        	wrapper.enemies.get(parentId).turningDirection = Utility.getTurningDirection(wrapper.enemies.get(parentId).direction, (int)angle);
        }

        else {
	        // Määritetään kääntymissuuntaa aina kun vihollinen ei ole checkpointissa
	        if((int) wrapper.enemies.get(parentId).x != checkpoints[startCheckpoint][0] && (int) wrapper.enemies.get(parentId).y != checkpoints[startCheckpoint][1]) {
	        	
	        	// Määritetään kulma
	        	double angle = Utility.getAngle((int) wrapper.enemies.get(parentId).x, (int) wrapper.enemies.get(parentId).y, checkpoints[startCheckpoint][0], checkpoints[startCheckpoint][1]);
	
	        	// Määritetään kääntymissuunta
	        	wrapper.enemies.get(parentId).turningDirection = Utility.getTurningDirection(wrapper.enemies.get(parentId).direction, (int)angle);
	        }
	        
	        // Jos vihollinen on osunut checkponttiin, asetetaan kohteeksi seuraava checkpoint
	        if((int) wrapper.enemies.get(parentId).x == checkpoints[startCheckpoint][0] && (int) wrapper.enemies.get(parentId).y == checkpoints[startCheckpoint][1]){
	        	
	        	++startCheckpoint;
	        	
	        	if(startCheckpoint == 12) {
	        		startCheckpoint = 0;
	        	}
	        	
	        	// Määritetään kulma
	        	double angle = Utility.getAngle((int) wrapper.enemies.get(parentId).x, (int) wrapper.enemies.get(parentId).y, checkpoints[startCheckpoint][0], checkpoints[startCheckpoint][1]);
	
	        	// Määritetään kääntymissuunta
	        	wrapper.enemies.get(parentId).turningDirection = Utility.getTurningDirection(wrapper.enemies.get(parentId).direction, (int)angle);
	        }
	        
	        // Suoritetaan ampuminen
	        if (lastShootingTime == 0) {
	    		lastShootingTime = android.os.SystemClock.uptimeMillis();
	    		weaponManager.triggerEnemyShoot(wrapper.enemies.get(parentId).x, wrapper.enemies.get(parentId).y);
	    	}
	    	else {
	    		long currentTime = android.os.SystemClock.uptimeMillis();
	        	
	        	if (currentTime - lastShootingTime >= 2000) {
	        		lastShootingTime = currentTime;
	        		weaponManager.triggerEnemyShoot(wrapper.enemies.get(parentId).x, wrapper.enemies.get(parentId).y);
	        	}
	    	}
        }
        
        /* Tarkistetaan törmäykset pelaajan kanssa */
        checkCollisionWithPlayer();
    }	
}