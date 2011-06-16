package fi.tamk.anpro;

import java.util.ArrayList;

public class WeaponManager
{
	// 
	public static final int SURVIVAL_MODE      = 1;
	public static final int STORY_MODE_LEVEL_1 = 1;
	
	public static final int GLOBAL_COOLDOWN = 500;

	// Käytössä oleva ase
	public int currentWeapon;

	// Aseet
	public ArrayList<Weapon> allyWeapons   = null;
	public ArrayList<Weapon> enemyWeapons  = null;
	public ArrayList<Weapon> playerWeapons = null;
	
	// Cooldownit
	public int cooldownMax[];
	public int cooldownLeft[];
	
	public static WeaponManager pointerToSelf;

	// WeaponStoragen rakentaja
	public WeaponManager()
    {
		playerWeapons = new ArrayList<Weapon>();
		cooldownMax   = new int[10];
		cooldownLeft  = new int[10];
		
		cooldownMax[0]  = 0;
		cooldownLeft[0] = 0;
		
		pointerToSelf = this;
    }
    
    public void triggerShoot(int[] _coords)
    {
	//	System.exit(0);
    	if (cooldownLeft[currentWeapon] <= 0) {
    		playerWeapons.get(currentWeapon).activate(_coords[0], _coords[1]);
    		
    		cooldownLeft[currentWeapon] = cooldownMax[currentWeapon];
    		
    		// Asetetaan globaali cooldown
    		for (int i = 9; i >= 0; --i) {
    			if (cooldownLeft[i] == 0) {
        			cooldownLeft[i] = GLOBAL_COOLDOWN;
    			}
    		}
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

    /*
     * Päivittää coolDownit.
     */
	public void updateCooldowns()
	{
		// Asetetaan globaali cooldown
		for (int i = 9; i>= 0; --i) {
			if (cooldownLeft[i] >= 0) {
				cooldownLeft[i] -= 100;
			}
		}
	}
	
	public static WeaponManager getConnection()
	{
		return pointerToSelf;
	}
}
