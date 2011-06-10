package fi.tamk.anpro;

import java.util.ArrayList;

/*
 * TODO:
 *  - Pitää ottaa huomioon kykypuun tiedot aseita luodessa.
 */

public class WeaponStorage
{
	public static final int SURVIVAL_MODE      = 1;
	public static final int STORY_MODE_LEVEL_1 = 1;
	
	private static WeaponStorage instance = null;

	// Käytössä oleva ase
	public int currentWeapon;

	// Aseet
	public ArrayList<Weapon> allyWeapons   = null;
	public ArrayList<Weapon> enemyWeapons  = null;
	public ArrayList<Weapon> playerWeapons = null;
	
	// Cooldownit
	public int cooldownMax[];
	public int cooldownLeft[];

	// WeaponStoragen rakentaja
	protected WeaponStorage()
    {
		playerWeapons = new ArrayList<Weapon>();
		cooldownMax   = new int[10];
		cooldownLeft  = new int[10];
    }

	// Lataa pointteri tähän luokkaan
    public static WeaponStorage getInstance()
    {
        if(instance == null){
            instance = new WeaponStorage();
        }
        return instance;
    }
    
    public void triggerShoot(int _xTouchPosition, int _yTouchPosition)
    {
    	if (cooldownLeft[currentWeapon] == 0) {
    		playerWeapons.get(currentWeapon).activate(_xTouchPosition, _yTouchPosition);
    		
    		cooldownLeft[currentWeapon] = cooldownMax[currentWeapon];
    	}
    }
    
    public void initialize(int _id)
    {
    	// Ladataan tarvittavat aseluokat muistiin
    	if (_id == SURVIVAL_MODE) {
    		playerWeapons.add(new WeaponDefault());
    	}
    	else if (_id == STORY_MODE_LEVEL_1) {
    		playerWeapons.add(new WeaponDefault());
    	}
    }
}
