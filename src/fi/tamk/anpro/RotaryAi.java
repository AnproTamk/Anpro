package fi.tamk.anpro;

import android.util.Log;


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
	
    private int[][] checkpoints;
	
	/**
	 * Alustaa luokan muuttujat.
	 * 
     * @param int Objektin tunnus piirtolistalla
     * @param int Objektin tyyppi
	 */
	public RotaryAi(AiObject _parentObject, int _userType, WeaponManager _weaponManager) 
	{
		super(_parentObject, _userType);
        
		/* Tallennetaan muuttujat */
		weaponManager = _weaponManager;
        
		/* Alustetaan muuttujat */
		checkpoints = new int[13][2];
	}

	/* =======================================================
	 * Perityt funktiot
	 * ======================================================= */
    /**
     * Käsittelee tekoälyn.
     */
    @Override
    public final void handleAi()
    {
    	// Alustetaan tarvittavat muuttujat
        int startCheckpoint = 0;
        
        // 12 "checkpointtia" ympyrän kaarella 30 asteen välein. Vihollisten on kuljettava näiden kautta.
        checkpoints[0][0] =	(int) (wrapper.player.x + 150 * Options.scaleX);
        checkpoints[0][1] = (int) wrapper.player.y;
        	
        checkpoints[1][0] = (int) (wrapper.player.x + 130 * Options.scaleX);
        checkpoints[1][1] = (int) (wrapper.player.y + 75 * Options.scaleY);
        	
        checkpoints[2][0] = (int) (wrapper.player.x + 75 * Options.scaleX);
        checkpoints[2][1] = (int) (wrapper.player.y + 130 * Options.scaleY);
        	
        checkpoints[3][0] = (int) wrapper.player.x;
        checkpoints[3][1] = (int) (wrapper.player.y + 150 * Options.scaleY);
        
    	checkpoints[4][0] = (int) (wrapper.player.x - 75 * Options.scaleX);
        checkpoints[4][1] = (int) (wrapper.player.y + 130 * Options.scaleY);
        	
        checkpoints[5][0] = (int) (wrapper.player.x - 130 * Options.scaleX);
        checkpoints[5][1] = (int) (wrapper.player.y + 75 * Options.scaleY);
        	
        checkpoints[6][0] = (int) (wrapper.player.x - 150 * Options.scaleX);
        checkpoints[6][1] = (int) wrapper.player.y;
        	
        checkpoints[7][0] = (int) (wrapper.player.x - 130 * Options.scaleX);
        checkpoints[7][1] = (int) (wrapper.player.y - 75 * Options.scaleY);
        
        checkpoints[8][0] = (int) (wrapper.player.x - 75 * Options.scaleX);
        checkpoints[8][1] = (int) (wrapper.player.y - 130 * Options.scaleY);
        
    	checkpoints[9][0] = (int) wrapper.player.x;
        checkpoints[9][1] = (int) (wrapper.player.y - 150 * Options.scaleY);
        	
        checkpoints[10][0] = (int) (wrapper.player.x + 75 * Options.scaleX);
        checkpoints[10][1] = (int) (wrapper.player.y - 130 * Options.scaleY);
        	
        checkpoints[11][0] = (int) (wrapper.player.x + 130 * Options.scaleX);
        checkpoints[11][1] = (int) (wrapper.player.y - 75 * Options.scaleY);

        // Määritetään vihollisen ja pelaajan välinen kulma
        double startAngle = Utility.getAngle((int) parentObject.x, (int) parentObject.y, (int) wrapper.player.x, (int) wrapper.player.y);
        
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
        double distance = Utility.getDistance(parentObject.x, parentObject.y, wrapper.player.x, wrapper.player.y);
        
        // TODO: SCALING (Options.scale)
        if(distance > 300 * Options.scale) {
        	
        	double angle = Utility.getAngle((int) parentObject.x, (int) parentObject.y, wrapper.player.x, wrapper.player.y);
        	
        	parentObject.turningDirection = Utility.getTurningDirection(parentObject.direction, (int)angle);
        }

        else {
	        // Määritetään kääntymissuuntaa aina kun vihollinen ei ole checkpointissa
	        if((int) parentObject.x != checkpoints[startCheckpoint][0] && (int) parentObject.y != checkpoints[startCheckpoint][1]) {
	        	
	        	// Määritetään kulma
	        	double angle = Utility.getAngle((int) parentObject.x, (int) parentObject.y, checkpoints[startCheckpoint][0], checkpoints[startCheckpoint][1]);
	
	        	// Määritetään kääntymissuunta
	        	parentObject.turningDirection = Utility.getTurningDirection(parentObject.direction, (int)angle);
	        }
	        
	        // Jos vihollinen on osunut checkponttiin, asetetaan kohteeksi seuraava checkpoint
	        if((int) parentObject.x == checkpoints[startCheckpoint][0] && (int) parentObject.y == checkpoints[startCheckpoint][1]){
	        	
	        	++startCheckpoint;
	        	
	        	if(startCheckpoint == 12) {
	        		startCheckpoint = 0;
	        	}
	        	
	        	// Määritetään kulma
	        	double angle = Utility.getAngle((int) parentObject.x, (int) parentObject.y, checkpoints[startCheckpoint][0], checkpoints[startCheckpoint][1]);
	
	        	// Määritetään kääntymissuunta
	        	parentObject.turningDirection = Utility.getTurningDirection(parentObject.direction, (int)angle);
	        }
	        
	        // Suoritetaan ampuminen
	        if (lastShootingTime == 0) {
	    		lastShootingTime = android.os.SystemClock.uptimeMillis();
	    		weaponManager.triggerEnemyShoot(parentObject.x, parentObject.y, WeaponManager.ENEMY_LASER);
	    	}
	    	else {
	    		long currentTime = android.os.SystemClock.uptimeMillis();
	        	
	        	if (currentTime - lastShootingTime >= 2000) {
	        		lastShootingTime = currentTime;
	        		weaponManager.triggerEnemyShoot(parentObject.x, parentObject.y, WeaponManager.ENEMY_LASER);
	        	}
	    	}
        }
        
        /* Tarkistetaan törmäykset pelaajan kanssa */
        checkCollisionWithPlayer();
    }	
}