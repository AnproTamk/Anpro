package fi.tamk.anpro;

import java.lang.Math;

public class ProjectileLaser extends GameObject {
	/** Vakioita ammuksen tietoja varten */
	// Efektit
	public static final int MULTIPLY_ON_TIMER = 1;
	public static final int MULTIPLY_ON_TOUCH = 2;
	public static final int EXPLODE_ON_TIMER = 3;
	public static final int EXPLODE_ON_TOUCH = 4;
	public static final int DAMAGE_ON_TOUCH = 5;
	
	// Aseiden tiedot
	public int damageOnTouch = 2;
	public int damageOnExplode = 0;
	
	public int damageType = DAMAGE_ON_TOUCH; // EXPLODE_ON_TIMER, EXPLODE_ON_TOUCH tai DAMAGE_ON_TOUCH
	
	public int armorPiercing = 0;
	
	public boolean causePassiveDamage = false;
	public int     damageOnRadius = 0;
	public int     damageRadius = 0; // Passiiviselle AoE-vahingolle
	
	public int     explodeTime = 0;
	public long    startTime = 0;
	public long    currentTime;
	
	// Wrapper
	private Wrapper wrapper;
	
	// Ammuksen tiedot
	private int speed = 3;
	private int targetX;
	private int targetY;
	private int direction;
	
	public boolean active = false;
	
	int listId;

	public ProjectileLaser(){
		super();
		
		wrapper = Wrapper.getInstance();
        
        listId = wrapper.addToList(this);
	}

	// Funktio vihollisen "aktiivisuuden" toteuttamiseen.
	public void setActive()
	{
		wrapper.projectileLaserStates.set(listId, 1);
		active = true;
	}

	// Funktio vihollisen "epäaktiivisuuden" toteuttamiseen.
	public void setUnactive()
	{
		wrapper.projectileLaserStates.set(listId, 0);
		active = false;
	}
	
	// Aktivoidaan ammus
	public void activate(int _xTouchPosition, int _yTouchPosition) {
		// Tarkistetaan ajastus
		if (explodeTime > 0) {
			startTime = android.os.SystemClock.uptimeMillis();
		}
		
		// Tallennetaan koordinaatit
		targetX = _xTouchPosition;
		targetY = _yTouchPosition;

		// Valitaan suunta (missä kohde on pelaajaan nähden)
		// Jos vihollinen on pelaajasta katsottuna oikealla ja ylhäällä
		if (_xTouchPosition > 0 && _yTouchPosition > 0){
			direction = (int) (Math.atan(_xTouchPosition / _yTouchPosition ));
		}
		// Jos vihollinen on pelaajasta katsottuna oikealla ja alhaalla
		else if (_xTouchPosition > 0 && _yTouchPosition < 0){
			direction = (int) (Math.atan(_xTouchPosition / _yTouchPosition)) + 180;
		}
		// Jos vihollinen on pelaajasta katsottuna vasemmalla ja ylhäällä
		else if (_xTouchPosition < 0 && _yTouchPosition > 0){
			direction = (int) (Math.atan(_xTouchPosition / _yTouchPosition)) + 180;
		}
		// Jos vihollinen on pelaajasta katsottuna vasemmalla ja alhaalla
		else if (_xTouchPosition < 0 && _yTouchPosition < 0){
			direction = (int) (Math.atan(_xTouchPosition / _yTouchPosition)) + 180;
		}
		else {
			direction = 0;
		}
		
		// Aktivoidaan ammus
		wrapper.projectileLaserStates.set(listId, 1);
		active = true;
	}
	
	public void handleAi() {
		// Tarkistetaan osumatyyppi ja etäisyydet
		// Kutsutaan osumatarkistuksia tarvittaessa
		for (int i = wrapper.enemies.size(); i >= 0; --i) {
			int distance = (int) Math.sqrt(((int)(x - wrapper.enemies.get(i).x))^2 + ((int)(y - wrapper.enemies.get(i).y))^2);
			if (distance - wrapper.enemies.get(i).collisionRadius - collisionRadius <= 0) {
				// Osuma ja räjähdys
				if (damageType == ProjectileLaser.DAMAGE_ON_TOUCH) {
					wrapper.enemies.get(i).triggerCollision(GameObject.COLLISION_WITH_PROJECTILE, damageOnTouch, armorPiercing);
				}
				else if (damageType == ProjectileLaser.EXPLODE_ON_TOUCH) {
					causeExplosion();
				}
			}
			
			// Passiivinen vahinko
			if (distance - wrapper.enemies.get(i).collisionRadius - damageRadius <= 0) {
				wrapper.enemies.get(i).health -= (damageOnRadius * (1 - 0.15 * wrapper.enemies.get(i).defence));
			}
		}
		
		// Tarkistetaan räjähdykset (ajastus)
		if (explodeTime > 0) {
			currentTime = android.os.SystemClock.uptimeMillis();
			
			if (currentTime - startTime >= explodeTime) {
				causeExplosion();
			}
		}
		
		// Tarkistetaan suunta ja kääntyminen
		//...
	}
	
	public void causeExplosion() {
		// Tarkistetaan etäisyydet
		// Kutsutaan osumatarkistuksia tarvittaessa
		for (int i = wrapper.enemies.size(); i >= 0; --i) {
			int distance = (int) Math.sqrt(((int)(x - wrapper.enemies.get(i).x))^2 + ((int)(y - wrapper.enemies.get(i).y))^2);
			if (distance - wrapper.enemies.get(i).collisionRadius - collisionRadius <= 0) {
				// Osuma ja räjähdys
				wrapper.enemies.get(i).triggerImpact(damageOnTouch);
			}
		}
	}

	public void triggerImpact(int _damage) {
		// Räjähdykset eivät vaikuta tähän ammukseen
	}

	public void triggerCollision(int _eventType, int _damage, int _armorPiercing) {
		// Osumat eivät vaikuta tähän ammukseen
	}
}
