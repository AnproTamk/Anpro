package fi.tamk.anpro;

import java.lang.Math;

import javax.microedition.khronos.opengles.GL10;

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
    public void activate(int _x, int _y) {
    	
    	//Log.v(TAG, "ProjectileLaser.activate()=" + _x + " " + _y);
    	
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
     * Käsitellään ammuksen tekoäly.
     */
    @Override
    public void handleAi() {
        // Tarkistetaan osumatyyppi ja etäisyydet ja kutsutaan osumatarkistuksia tarvittaessa
        for (int i = wrapper.enemies.size()-1; i >= 0; --i) {
            if (wrapper.enemyStates.get(i) == 1) {
	            double distance = Math.sqrt(Math.pow(x - wrapper.enemies.get(i).x,2) + Math.pow(y - wrapper.enemies.get(i).y, 2));
	            
	            if (distance - wrapper.enemies.get(i).collisionRadius - collisionRadius <= 0) {

	                // Osuma ja räjähdys
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
        
        // Tarkistetaan räjähdykset (ajastus)
        if (explodeTime > 0) {
            currentTime = android.os.SystemClock.uptimeMillis();
            
            if (currentTime - startTime >= explodeTime) {
                causeExplosion();
                setUnactive();
            }
        }
        
        // Tarkistetaan suunta ja kääntyminen
        //...
    }
    
    /*
     * Kutsutaan triggerImpact-funktiota muista objekteista, jotka ovat räjähdyksen vaikutusalueella.
     */
    public void causeExplosion() {
    	
		Log.v(TAG, "***** causeExplosion *****");
    	
        // Tarkistetaan etäisyydet
        // Kutsutaan osumatarkistuksia tarvittaessa
        for (int i = wrapper.enemies.size(); i >= 0; --i) {
        	if (wrapper.enemyStates.get(i) == 1) {
	            int distance = (int) Math.sqrt(((int)(x - wrapper.enemies.get(i).x))^2 + ((int)(y - wrapper.enemies.get(i).y))^2);
	            
	            if (distance - wrapper.enemies.get(i).collisionRadius - collisionRadius <= 0) {
	                // Osuma ja räjähdys
	                wrapper.enemies.get(i).triggerImpact(damageOnTouch);
	            }
        	}
        }
    }
    
    /*
     * Käsitellään räjähdykset
     */
    public void triggerImpact(int _damage) {
        // Räjähdykset eivät vaikuta tähän ammukseen
    }

    /*
     * Käsitellään osumat
     */
    public void triggerCollision(int _eventType, int _damage, int _armorPiercing) {
        // Osumat eivät vaikuta tähän ammukseen
    }
}
