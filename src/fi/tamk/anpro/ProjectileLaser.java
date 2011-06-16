package fi.tamk.anpro;

import java.lang.Math;
import android.util.Log;

public class ProjectileLaser extends AbstractProjectile {

	private static final String TAG = "TouchEngine"; // Loggaus
	
    /*
     * Rakentaja
     */
    public ProjectileLaser() {
        super();
    }
    
    /*
     * Aktivoidaan ammus
     */
    @Override
    public final void activate(int _x, int _y) {
 
        // Tarkistetaan ajastus
        if (explodeTime > 0) {
            startTime = android.os.SystemClock.uptimeMillis();
        }
        
        // Asetetaan aloituspiste
        x = wrapper.player.x;
        y = wrapper.player.y;
        
        // Tallennetaan kohteen koordinaatit
        targetX = _x;
        targetY = _y;

        // Valitaan suunta
        double xDiff = Math.abs((double)(x - targetX));
        double yDiff = Math.abs((double)(y - targetY));
        
        if (x < targetX) {
            if (y < targetY) {
                direction = (int) ((Math.atan(yDiff/xDiff)*180)/Math.PI);
            }
            else if (y > targetY) {
                direction = (int) (360 - (Math.atan(yDiff/xDiff)*180)/Math.PI);
            }
            else {
                direction = 0;
            }
        }
        else if (x > targetX) {
            if (y > targetY) {
                direction = (int) (180 + (Math.atan(yDiff/xDiff)*180)/Math.PI);
            }
            else if (y < targetY) {
                direction = (int) (180 - (Math.atan(yDiff/xDiff)*180)/Math.PI);
            }
            else {
                direction = 180;
            }
        }
        else {
            if (y > targetY) {
                direction = 270;
            }
            else {
                direction = 90;
            }
        }
        
        // Aktivoidaan ammus
        wrapper.projectileStates.set(listId, 1);
        active = true;
    }
    
    /*
     * K‰sitell‰‰n ammuksen teko‰ly.
     */
    @Override
    public final void handleAi() {
        // Tarkistetaan osumatyyppi ja et‰isyydet ja kutsutaan osumatarkistuksia tarvittaessa
        for (int i = wrapper.enemies.size()-1; i >= 0; --i) {
            if (wrapper.enemyStates.get(i) == 1) {
	            double distance = Math.sqrt(Math.pow(x - wrapper.enemies.get(i).x,2) + Math.pow(y - wrapper.enemies.get(i).y, 2));
	            
	            if (distance - wrapper.enemies.get(i).collisionRadius - collisionRadius <= 0) {

	                // Osuma ja r‰j‰hdys
	                if (damageType == ProjectileLaser.DAMAGE_ON_TOUCH) {
	                    wrapper.enemies.get(i).triggerCollision(GameObject.COLLISION_WITH_PROJECTILE, damageOnTouch, armorPiercing);
	                }
	                else if (damageType == ProjectileLaser.EXPLODE_ON_TOUCH) {
	                    causeExplosion();
	                }
	
	                setUnactive();
	                break;
	            }
	            
	            // Passiivinen vahinko
	            if (distance - wrapper.enemies.get(i).collisionRadius - damageRadius <= 0) {
	                wrapper.enemies.get(i).health -= (damageOnRadius * (1 - 0.15 * wrapper.enemies.get(i).defence));
	            }
            }
        }
        
        // Tarkistetaan r‰j‰hdykset (ajastus)
        if (explodeTime > 0) {
            currentTime = android.os.SystemClock.uptimeMillis();
            
            if (currentTime - startTime >= explodeTime) {
                causeExplosion();
                setUnactive();
            }
        }
        
        // Tarkistetaan suunta ja k‰‰ntyminen
        //...
        

        // K‰sitell‰‰n reuna-alueet panosten tuhoamiseksi
        if (wrapper.player.x + x < -400 || wrapper.player.x + x > 400 ||
        	wrapper.player.y + y < -240 || wrapper.player.y + y > 240 ) {
        	setUnactive();
        }

    }
    
    /*
     * Kutsutaan triggerImpact-funktiota muista objekteista, jotka ovat r‰j‰hdyksen vaikutusalueella.
     */
    public final void causeExplosion() {
    	
		Log.v(TAG, "***** causeExplosion *****");
    	
        // Tarkistetaan et‰isyydet
        // Kutsutaan osumatarkistuksia tarvittaessa
        for (int i = wrapper.enemies.size(); i >= 0; --i) {
        	if (wrapper.enemyStates.get(i) == 1) {
	            int distance = (int) Math.sqrt(((int)(x - wrapper.enemies.get(i).x))^2 + ((int)(y - wrapper.enemies.get(i).y))^2);
	            
	            if (distance - wrapper.enemies.get(i).collisionRadius - collisionRadius <= 0) {
	                // Osuma ja r‰j‰hdys
	                wrapper.enemies.get(i).triggerImpact(damageOnTouch);
	            }
        	}
        }
    }
    
    /*
     * K‰sitell‰‰n r‰j‰hdykset
     */
    public final void triggerImpact(int _damage) {
        // R‰j‰hdykset eiv‰t vaikuta t‰h‰n ammukseen
    }

    /*
     * K‰sitell‰‰n osumat
     */
    public final void triggerCollision(int _eventType, int _damage, int _armorPiercing) {
        // Osumat eiv‰t vaikuta t‰h‰n ammukseen
    }
}
