package fi.tamk.anpro;

import java.util.ArrayList;

abstract public class Weapon {
	// Ammusten m‰‰r‰
	public static final int AMOUNT_OF_PROJECTILES = 3;
	
	// Ammusten tiedot
	public int[] projectileStates;
	
	public Weapon() { }
	
	abstract public void activate(int _x, int _y);
}
