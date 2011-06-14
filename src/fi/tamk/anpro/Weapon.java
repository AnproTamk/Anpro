package fi.tamk.anpro;

abstract public class Weapon {
	// Ammusten m‰‰r‰
	public static final int AMOUNT_OF_PROJECTILES = 3;
	
	// Sijainti HUDissa
	public int locationOnHUD = 0;
	
	/*
	 * Rakentaja
	 */
	public Weapon() { }
	
	/*
	 * Aktivoidaan ammukset
	 */
	abstract public void activate(int _x, int _y);
}
