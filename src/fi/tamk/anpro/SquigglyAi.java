package fi.tamk.anpro;

/**
 * Toteutus "v‰istelev‰lle" teko‰lylle. Teko‰ly seuraa l‰hes siniaallon
 * muotoista reitti‰ satunnaiseen suuntaan mutta kuitenkin lopulta pelaajaa kohti.
 * 
 * K‰ytet‰‰n ainoastaan vihollisille.
 */
public class SquigglyAi extends AbstractAi
{
	private long lastDirectionUpdate = 0;
	
	private long lastShootingTime = 0;
	
	private WeaponManager weaponManager;
	
	private double shootingAngle;
	
	/**
	 * Alustaa luokan muuttujat.
	 * 
     * @param int Objektin tunnus piirtolistalla
     * @param int Objektin tyyppi
	 */
	public SquigglyAi(AiObject _parentObject, int _userType, WeaponManager _weaponManager) 
	{
		super(_parentObject, _userType);
        
		/* Tallennetaan muuttujat */
		weaponManager = _weaponManager;
	}
    
    /**
     * K‰sittelee teko‰lyn.
     */
	@Override
    public final void handleAi()
	{ 
		// Jos vihollinen on kaukana pelaajasta, k‰ytet‰‰n v‰liaikaisesti LinearAi:n kaltaista toimintaa kunnes ollaan tarpeeksi l‰hell‰ pelaajaa
		double distance = Utility.getDistance(parentObject.x, parentObject.y, wrapper.player.x, wrapper.player.y);
		
		if(distance > 500) {
			
			double angle = Utility.getAngle((int) parentObject.x, (int) parentObject.y,(int) wrapper.player.x,(int) wrapper.player.y);
			
			parentObject.turningDirection = Utility.getTurningDirection(parentObject.direction, (int)angle);
		}
		
		else {
			
			// M‰‰ritet‰‰n vihollisen ja pelaajan v‰linen kulma
			double angle = Utility.getAngle((int) parentObject.x, (int) parentObject.y, (int) wrapper.player.x,(int) wrapper.player.y);
	    	
			// Vihollisen lentosuunta on samalla sen ammuntasuunta
			shootingAngle = parentObject.direction;
			
			if(lastDirectionUpdate == 0) {
				
				lastDirectionUpdate = android.os.SystemClock.uptimeMillis();
				
				
				/* M‰‰ritet‰‰n k‰‰ntymissuunta */
				parentObject.turningDirection = Utility.getTurningDirection(parentObject.direction, (int)angle);
				
				// Suoritetaan ampuminen
		    		lastShootingTime = android.os.SystemClock.uptimeMillis();
		    		weaponManager.triggerEnemyShootForward(shootingAngle, parentObject.x, parentObject.y, WeaponManager.ENEMY_SPITFIRE);
			}
			
			else {
				long currentTime = android.os.SystemClock.uptimeMillis();
		        	
		        if (currentTime - lastShootingTime >= 200) {
	        		lastShootingTime = currentTime;
	        		weaponManager.triggerEnemyShootForward(shootingAngle, parentObject.x, parentObject.y, WeaponManager.ENEMY_SPITFIRE);
	        	}
				
				if(currentTime - lastDirectionUpdate >= 1500 && currentTime - lastDirectionUpdate < 2000) {
					
					parentObject.turningDirection = 1;
				}
				
				else if(currentTime - lastDirectionUpdate >= 2000 && currentTime - lastDirectionUpdate < 2500) {
					
					parentObject.turningDirection = 0;
				}
				
				else if(currentTime - lastDirectionUpdate >= 2500 && currentTime - lastDirectionUpdate < 3000) {
					
					parentObject.turningDirection = 2;
				}
				
				else if(currentTime - lastDirectionUpdate >= 3000)
				{
					lastDirectionUpdate = currentTime;
				}
				
				else {
					// M‰‰ritet‰‰n vihollisen ja pelaajan v‰linen kulma
//					double angle = Utility.getAngle((int) wrapper.enemies.get(parentId).x, (int) wrapper.enemies.get(parentId).y,(int) wrapper.player.x,(int) wrapper.player.y);
		    	
					/* M‰‰ritet‰‰n k‰‰ntymissuunta */
					parentObject.turningDirection = Utility.getTurningDirection(parentObject.direction, (int)angle);
				}
			}
		}
		
        /* Tarkistetaan tˆrm‰ykset pelaajan kanssa */
        checkCollisionWithPlayer();
    }
}
