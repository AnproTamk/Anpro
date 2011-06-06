package fi.tamk.anpro;

import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import java.lang.Math;

public class ProjectileLaser extends GameObject {
	public boolean active = false;
	
	private int speed;
	private int targetX;
	private int targetY;
	private int direction;

	public ProjectileLaser(GL10 _gl, Context _context, int _id){
		super(_gl, _context, _id); 
	}
	
	// Aktivoidaan ammus
	public void activate(int _xTouchPosition, int _yTouchPosition) {
		speed = 3;
		targetX = _xTouchPosition;
		targetY = _yTouchPosition;

		// Valitaan suunta (miss‰ kohde on pelaajaan n‰hden)
		// Jos vihollinen on pelaajasta katsottuna oikealla ja ylh‰‰ll‰
		if (_xTouchPosition > 0 && _yTouchPosition > 0){
			direction = (int) (Math.atan(_xTouchPosition / _yTouchPosition));
		}
		
		// Jos vihollinen on pelaajasta katsottuna oikealla ja alhaalla
		direction = (int) (Math.atan(_xTouchPosition / _yTouchPosition)+180);
		// Jos vihollinen on pelaajasta katsottuna vasemmalla ja ylh‰‰ll‰
		//direction = ();
		// Jos vihollinen on pelaajasta katsottuna vasemmalla ja alhaalla
		//direction = ();
		
	/*
	 * 1. valitse suunta (miss‰ kohde on pelaajaan n‰hden)
	 * 2. laske liikkumisnopeus ja kiihtyvyydet
	 * 3. m‰‰rit‰ collisionType ja collisionRadius (WEAPON LUOKKAAN!)
	 * 4. lis‰‰ t‰m‰ luokka globaaliin piirto- ja p‰ivityslistaan
	 */
	}
}
