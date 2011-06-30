package fi.tamk.anpro;

import java.util.ArrayList;

import android.util.Log;

/**
 * Hallitsee aseiden cooldowneja, varastoi aseiden oliot ja välittää kutsupyynnöt
 * eri aseisiin.
 */
public class WeaponManager
{
    /* Pelitilat */
    public static final int SURVIVAL_MODE      = 1;
    public static final int STORY_MODE_LEVEL_1 = 1;
    
    /* Globaali cooldown, joka ammuttaessa lisätään jokaiseen aseeseen  */
    public static final int GLOBAL_COOLDOWN = 500;

    /* Käytössä oleva ase */
    public int currentWeapon = 0; // Viittaa alla olevien taulukoiden soluihin

    /* Aseiden oliot */
    public ArrayList<AbstractWeapon> allyWeapons   = null;
    public ArrayList<AbstractWeapon> enemyWeapons  = null;
    public ArrayList<AbstractWeapon> playerWeapons = null;
    
    /* Cooldownit */
    public int cooldownMax[];
    public int cooldownLeft[];

    /**
     * Alustaa luokan muuttujat.
     */
    public WeaponManager()
    {
        // Alustetaan taulukot
        playerWeapons = new ArrayList<AbstractWeapon>();
        enemyWeapons = new ArrayList<AbstractWeapon>();
        cooldownMax   = new int[10];
        cooldownLeft  = new int[10];
        
        // Määritetään cooldownit
        // TODO: Lue tiedostosta
        cooldownMax[0]  = 0;
        cooldownLeft[0] = 0;
    }
    
    /**
     * Välittää kutsupyynnön käytössä olevalle aseelle aktoiden sen ja lähettämällä
     * sille kohteen koordinaatit. Päivittää myös cooldownit.
     * 
     * @param int[] Kohteen koordinaatit
     */
    public final void triggerShoot(int[] _coords, int _type, float _x, float _y)
    {
    	if(_type == Wrapper.CLASS_TYPE_PLAYER) {
	        if (cooldownLeft[currentWeapon] <= 0) {
	            playerWeapons.get(currentWeapon).activate(_coords[0], _coords[1], _x, _y);
	            
	            cooldownLeft[currentWeapon] = cooldownMax[currentWeapon];
	            
	            // Asetetaan globaali cooldown
	            for (int i = 9; i >= 0; --i) {
	                if (cooldownLeft[i] <= 0) {
	                    cooldownLeft[i] = GLOBAL_COOLDOWN;
	                }
	            }
	        }
    	}
    	else if(_type == Wrapper.CLASS_TYPE_ENEMY) {
    		enemyWeapons.get(0).activate(_coords[0], _coords[1], _x, _y);
    	}
    }
    
    /**
     * Lataa aseet muistiin.
     * 
     * @param int Pelitilan ja tason tunnus
     */
    public final void initialize(int _id)
    {
    	Wrapper wrapper = Wrapper.getInstance();
    	
        // Ladataan tarvittavat aseluokat muistiin
        if (_id == SURVIVAL_MODE) {
            playerWeapons.add(new WeaponDefault(wrapper, Wrapper.CLASS_TYPE_PLAYER));
            //playerWeapons.add(new WeaponEmp(wrapper));
            //playerWeapons.add(new WeaponSpinningLaser(wrapper));
        	//playerWeapons.add(new WeaponCluster(wrapper));

            enemyWeapons.add(new WeaponDefault(wrapper, Wrapper.CLASS_TYPE_ENEMY));
        }
        else if (_id == STORY_MODE_LEVEL_1) {
            playerWeapons.add(new WeaponDefault(wrapper, Wrapper.CLASS_TYPE_PLAYER));
        }
    }

    /**
     * Päivittää cooldownit.
     */
    public final void updateCooldowns()
    {
        for (int i = 9; i>= 0; --i) {
            if (cooldownLeft[i] > 0) {
                cooldownLeft[i] -= 100;
            }
        }
    }
}
