package fi.tamk.anpro;

import java.lang.Math;

public class EnemyAI extends GenericAI {
	
	private Wrapper wrapper;
	
	public EnemyAI() {
		super();
		
		wrapper = Wrapper.getInstance();
	}

	// Activating AI
	public void activate() {
		/* Lineaarinen eteneminen
		 * 
		 * Etsitään pelaajan sijainti, verrataan omaan vihollisen omaan sijaintiin. X- ja Y-arvojen erotuksen itseisarvoilla
		 * voidaan laskea vihollisaluksen suunta:
		 * 
		 * alpha = arctan (y/x) 
		 * radiaaneina -> (PI*(arctan (y/x))/180)
		 * 
		 */
		
		for (int i = wrapper.enemies.size()-1; i >= 0; --i) {
			double _xDiff = Math.abs((double)(wrapper.enemies.get(i).x - wrapper.players.get(0).x));
			double _yDiff = Math.abs((double)(wrapper.enemies.get(i).y - wrapper.players.get(0).y));
			
			
			// Eliminoidaan nollalla jakaminen
			if(_xDiff == 0)
			{
				_xDiff = 1;
			}
			
			wrapper.enemies.get(i).direction = (int)((Math.PI*Math.atan(_yDiff/_xDiff))*720);
		
		}
	}
	
	public void unActivate() {
		
	}

	
	public void handleAI() {	
	
	}
}