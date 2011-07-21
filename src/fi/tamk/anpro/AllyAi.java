package fi.tamk.anpro;


/**
 * Toteutus pelaajan ymp�rill� py�riv�lle teko�lylle. Teko�ly hakeutuu pelaajan l�helle
 * mahdollisimman suoraa reitti� pitkin kunnes tietyll� et�isyydell� hakeutuu pelaajaa
 * kiert�v�lle radalle ja py�rii pelaajan ymp�ri.
 * 
 * K�ytet��n liittolaiselle
 */
public class AllyAi extends AbstractAi
{
	private WeaponManager weaponManager;
	
	private long lastShootingTime = 0;
	
	/**
	 * Alustaa luokan muuttujat.
	 * 
     * @param int Objektin tunnus piirtolistalla
     * @param int Objektin tyyppi
	 */
	public AllyAi(AiObject _parentObject, int _userType, WeaponManager _weaponManager) 
	{
		super(_parentObject, _userType);
		
		weaponManager = _weaponManager;
	}
    
    /* =======================================================
     * Perityt funktiot
     * ======================================================= */
    /**
     * K�sittelee teko�lyn.
     */
    @Override
    public final void handleAi()
    {
    	// Alustetaan tarvittavat muuttujat
        int checkpoints[][] = new int[13][2];
        int startCheckpoint = 0;
        
        
        // 12 "checkpointtia" ympyr�n kaarella 30 asteen v�lein.
        checkpoints[0][0] =	(int) wrapper.player.x + 50;
        checkpoints[0][1] = (int) wrapper.player.y;
        	
        checkpoints[1][0] = (int) wrapper.player.x + 43;
        checkpoints[1][1] = (int) wrapper.player.y + 25;
        	
        checkpoints[2][0] = (int) wrapper.player.x + 25;
        checkpoints[2][1] = (int) wrapper.player.y + 43;
        	
        checkpoints[3][0] = (int) wrapper.player.x;
        checkpoints[3][1] = (int) wrapper.player.y + 50;
        
    	checkpoints[4][0] = (int) wrapper.player.x - 25;
        checkpoints[4][1] = (int) wrapper.player.y + 43;
        	
        checkpoints[5][0] = (int) wrapper.player.x - 43;
        checkpoints[5][1] = (int) wrapper.player.y + 25;
        	
        checkpoints[6][0] = (int) wrapper.player.x - 50;
        checkpoints[6][1] = (int) wrapper.player.y;
        	
        checkpoints[7][0] = (int) wrapper.player.x - 43;
        checkpoints[7][1] = (int) wrapper.player.y - 25;
        
        checkpoints[8][0] = (int) wrapper.player.x - 25;
        checkpoints[8][1] = (int) wrapper.player.y - 43;
        
    	checkpoints[9][0] = (int) wrapper.player.x;
        checkpoints[9][1] = (int) wrapper.player.y - 50;
        	
        checkpoints[10][0] = (int) wrapper.player.x + 25;
        checkpoints[10][1] = (int) wrapper.player.y - 43;
        	
        checkpoints[11][0] = (int) wrapper.player.x + 43;
        checkpoints[11][1] = (int) wrapper.player.y - 25;

        // M��ritet��n vihollisen ja pelaajan v�linen kulma
        double startAngle = Utility.getAngle((int) parentObject.x, (int) parentObject.y, (int) wrapper.player.x, (int) wrapper.player.y);
        
        // Liittolainene ja pelaajan v�lisell� kulmalla m��ritet��n kunkin vihollisen ensimm�inen "checkpoint", josta ympyr�liike alkaa
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

        // Jos liittolainen on kaukana pelaajasta, liittolainen k�ytt�� LinearAi:ta v�liaikaisesti kunnes on tarpeeksi l�hell�
        double distance = Utility.getDistance(parentObject.x, parentObject.y, wrapper.player.x, wrapper.player.y);
        
        if(distance > 300) {
        	
        	double angle = Utility.getAngle((int) parentObject.x, (int) parentObject.y, wrapper.player.x, wrapper.player.y);
        	
        	parentObject.turningDirection = Utility.getTurningDirection(parentObject.direction, (int)angle);
        }

        else {
	        // M��ritet��n k��ntymissuuntaa aina kun liittolainen ei ole checkpointissa
	        if((int) parentObject.x != checkpoints[startCheckpoint][0] && (int) parentObject.y != checkpoints[startCheckpoint][1]) {
	        	
	        	// M��ritet��n kulma
	        	double angle = Utility.getAngle((int) parentObject.x, (int) parentObject.y, checkpoints[startCheckpoint][0], checkpoints[startCheckpoint][1]);
	
	        	// M��ritet��n k��ntymissuunta
	        	parentObject.turningDirection = Utility.getTurningDirection(parentObject.direction, (int)angle);
	        }
	        
	        // Jos liittolainen on osunut checkponttiin, asetetaan kohteeksi seuraava checkpoint
	        if((int) parentObject.x == checkpoints[startCheckpoint][0] && (int) parentObject.y == checkpoints[startCheckpoint][1]){
	        	
	        	++startCheckpoint;
	        	
	        	if(startCheckpoint == 12) {
	        		startCheckpoint = 0;
	        	}
	        	
	        	// M��ritet��n kulma
	        	double angle = Utility.getAngle((int) parentObject.x, (int) parentObject.y, checkpoints[startCheckpoint][0], checkpoints[startCheckpoint][1]);
	
	        	// M��ritet��n k��ntymissuunta
	        	parentObject.turningDirection = Utility.getTurningDirection(parentObject.direction, (int)angle);
	        }
	        
	        // Suoritetaan ampuminen
	        if (lastShootingTime == 0) {
	    		lastShootingTime = android.os.SystemClock.uptimeMillis();
	    		weaponManager.triggerAllyShoot(parentObject.x, parentObject.y, parentObject.x, parentObject.y);
	    	}
	    	else {
	    		long currentTime = android.os.SystemClock.uptimeMillis();
	        	
	        	if (currentTime - lastShootingTime >= 2000) {
	        		lastShootingTime = currentTime;
		    		weaponManager.triggerAllyShoot(parentObject.x, parentObject.y, parentObject.x, parentObject.y);
	        	}
	    	}
        }
    }	
}