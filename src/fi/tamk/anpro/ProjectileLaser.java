package fi.tamk.anpro;

import java.lang.Math;
import android.util.Log;

/**
 * Sis‰lt‰‰ laser-ammuksen tiedot ja toiminnot, kuten aktivoinnin, teko‰lyn,
 * tˆrm‰ystunnistuksen ja ajastukset.
 */
public class ProjectileLaser extends AbstractProjectile
{
    /**
     * Alustaa luokan muuttujat.
     */
    public ProjectileLaser()
    {
        super();
    }
    
    /**
     * Aktivoi ammuksen, eli m‰‰ritt‰‰ sen aloituspisteen, kohteen, suunnan ja lis‰‰
     * ammuksen Wrapperin piirtolistalle.
     * 
     * @param int Kohteen X-koordinaatti
     * @param int Kohteen Y-koordinaatti
     */
    @Override
    public final void activate(int _x, int _y)
    {
        // Ladataan aloitusaika, mik‰li ammuksen on r‰j‰hdett‰v‰ tietyn ajan kuluessa
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
    
    /**
     * K‰sittelee ammuksen teko‰lyn.
     */
    @Override
    public final void handleAi()
    {
        /* Tarkistetaan osumat vihollisiin */
        for (int i = wrapper.enemies.size()-1; i >= 0; --i) {
        	
        	// Tarkistetaan, onko vihollinen aktiivinen
            if (wrapper.enemyStates.get(i) == 1) {
            	
            	// Lasketaan et‰isyys pelaajaan
	            double distance = Math.sqrt(Math.pow(x - wrapper.enemies.get(i).x,2) + Math.pow(y - wrapper.enemies.get(i).y, 2));
	            
	            // Aiheutetaan osuma/r‰j‰hdys, mik‰li et‰isyys on tarpeeksi pieni
	            if (distance - wrapper.enemies.get(i).collisionRadius - collisionRadius <= 0) {
	                if (damageType == ProjectileLaser.DAMAGE_ON_TOUCH) {
	                    wrapper.enemies.get(i).triggerCollision(GameObject.COLLISION_WITH_PROJECTILE, damageOnTouch, armorPiercing);
	                }
	                else if (damageType == ProjectileLaser.EXPLODE_ON_TOUCH) {
	                    causeExplosion();
	                }
	
	                // Asetetaan ammus ep‰aktiiviseksi
	                setUnactive();
	                break;
	            }
	            
	            // K‰sitell‰‰n passiivinen vahinko
	            if (distance - wrapper.enemies.get(i).collisionRadius - damageRadius <= 0) {
	                wrapper.enemies.get(i).health -= (damageOnRadius * (1 - 0.15 * wrapper.enemies.get(i).defence));
	            }
            }
        }
        
        /* Tarkistetaan ajastetut r‰j‰hdykset */
        if (explodeTime > 0) {
            currentTime = android.os.SystemClock.uptimeMillis();
            
            if (currentTime - startTime >= explodeTime) {
                causeExplosion();
                setUnactive();
            }
        }
        
        /* Tarkistetaan suunta ja k‰‰ntyminen */
        //...
        
        /* K‰sitell‰‰n reuna-alueet panosten tuhoamiseksi */
        if (wrapper.player.x + x < -400 || wrapper.player.x + x > 400 ||
        	wrapper.player.y + y < -240 || wrapper.player.y + y > 240 ) {
        	setUnactive();
        }

    }
    
    /**
     * Etsii r‰j‰hdyksen vaikutusalueella olevia vihollisia ja kutsuu niiden triggerImpact-funktiota.
     */
    public final void causeExplosion()
    {
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
