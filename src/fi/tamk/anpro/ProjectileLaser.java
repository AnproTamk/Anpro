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
	public int damageOnTouch;
	public int damageOnExplode;
	
	public int damageType; // EXPLODE_ON_TIMER, EXPLODE_ON_TOUCH tai DAMAGE_ON_TOUCH
	
	public int armorPiercing;
	
	public boolean causePassiveDamage;
	public int     damageOnRadius;
	public int     damageRadius; // Passiiviselle AoE-vahingolle
	
	public int     explodeTime;
	
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

	// Funktio vihollisen "ep�aktiivisuuden" toteuttamiseen.
	public void setUnactive()
	{
		wrapper.projectileLaserStates.set(listId, 0);
		active = false;
	}
	
	// Aktivoidaan ammus
	public void activate(int _xTouchPosition, int _yTouchPosition) {
		targetX = _xTouchPosition;
		targetY = _yTouchPosition;

		// Valitaan suunta (miss� kohde on pelaajaan n�hden)
		// Jos vihollinen on pelaajasta katsottuna oikealla ja ylh��ll�
		if (_xTouchPosition > 0 && _yTouchPosition > 0){
			direction = (int) (Math.atan(_xTouchPosition / _yTouchPosition ));
		}
		// Jos vihollinen on pelaajasta katsottuna oikealla ja alhaalla
		if (_xTouchPosition > 0 && _yTouchPosition < 0){
			direction = (int) (Math.atan(_xTouchPosition / _yTouchPosition)) + 180;
		}
		// Jos vihollinen on pelaajasta katsottuna vasemmalla ja ylh��ll�
		if (_xTouchPosition < 0 && _yTouchPosition > 0){
			direction = (int) (Math.atan(_xTouchPosition / _yTouchPosition)) + 180;
		}
		// Jos vihollinen on pelaajasta katsottuna vasemmalla ja alhaalla
		if (_xTouchPosition < 0 && _yTouchPosition < 0){
			direction = (int) (Math.atan(_xTouchPosition / _yTouchPosition)) + 180;
		}
		else {
			direction = 0;
		}

		/*
		 * TEHTY 1. valitse suunta (miss� kohde on pelaajaan n�hden)
		 * 2. laske liikkumisnopeus ja kiihtyvyydet
		 * 3. m��rit� collisionType ja collisionRadius (WEAPON LUOKKAAN!)
		 * 4. lis�� t�m� luokka globaaliin piirto- ja p�ivityslistaan
		 */
	}
	
	public void handleAi() {
		// Tarkistetaan osumatyyppi ja et�isyydet
		// Kutsutaan osumatarkistuksia tarvittaessa
		for (int i = wrapper.enemies.size(); i >= 0; --i) {
			int distance = (int) Math.sqrt(((int)(x - wrapper.enemies.get(i).x))^2 + ((int)(y - wrapper.enemies.get(i).y))^2);
			if (distance - wrapper.enemies.get(i).collisionRadius - collisionRadius <= 0) {
				// Osuma ja r�j�hdys
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
		
		// Tarkistetaan r�j�hdykset (ajastus)
		//...
		
		// Tarkistetaan suunta ja k��ntyminen
		//...
	}
	
	public void causeExplosion() {
		// Tarkistetaan et�isyydet
		// Kutsutaan osumatarkistuksia tarvittaessa
		for (int i = wrapper.enemies.size(); i >= 0; --i) {
			int distance = (int) Math.sqrt(((int)(x - wrapper.enemies.get(i).x))^2 + ((int)(y - wrapper.enemies.get(i).y))^2);
			if (distance - wrapper.enemies.get(i).collisionRadius - collisionRadius <= 0) {
				// Osuma ja r�j�hdys
				wrapper.enemies.get(i).triggerImpact(damageOnTouch);
			}
		}
	}

	public void triggerImpact(int _damage) {
		// R�j�hdykset eiv�t vaikuta t�h�n ammukseen
	}

	public void triggerCollision(int _eventType, int _damage, int _armorPiercing) {
		// Osumat eiv�t vaikuta t�h�n ammukseen
	}
}
