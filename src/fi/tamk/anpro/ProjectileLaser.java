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
	
	// Is�nt�objekti
	private Object parent;
	
	// Wrapper
	private Wrapper wrapper;
	
	// Ammuksen tiedot
	private int speed = 3;
	private int targetX;
	private int targetY;
	private int direction;

	public ProjectileLaser(Object _parent){
		super();
		
		wrapper = Wrapper.getInstance();
		
		if (_parent instanceof WeaponDefault) {
			parent = _parent;
		}
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
		// ...
	}
}
