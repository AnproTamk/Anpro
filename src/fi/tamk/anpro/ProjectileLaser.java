package fi.tamk.anpro;

import java.lang.Math;

public class ProjectileLaser extends GameObject {
	public boolean active = false;
	
	private int speed;
	private int targetX;
	private int targetY;
	private int direction;

	public ProjectileLaser(){
		super(); 
	}
	
	// Aktivoidaan ammus
	public void activate(int _xTouchPosition, int _yTouchPosition) {
		speed = 3;
		targetX = _xTouchPosition;
		targetY = _yTouchPosition;

		// Valitaan suunta (missä kohde on pelaajaan nähden)
		// Jos vihollinen on pelaajasta katsottuna oikealla ja ylhäällä
		if (_xTouchPosition > 0 && _yTouchPosition > 0){
			direction = (int) (Math.atan(_xTouchPosition / _yTouchPosition ));
		}
		
		// Jos vihollinen on pelaajasta katsottuna oikealla ja alhaalla
		if (_xTouchPosition > 0 && _yTouchPosition < 0){
			direction = (int) (Math.atan(_xTouchPosition / _yTouchPosition)) + 180;
		}
		// Jos vihollinen on pelaajasta katsottuna vasemmalla ja ylhäällä
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
	 * TEHTY 1. valitse suunta (missä kohde on pelaajaan nähden)
	 * 2. laske liikkumisnopeus ja kiihtyvyydet
	 * 3. määritä collisionType ja collisionRadius (WEAPON LUOKKAAN!)
	 * 4. lisää tämä luokka globaaliin piirto- ja päivityslistaan
	 */
	}
}
