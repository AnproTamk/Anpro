package fi.tamk.anpro;

import java.util.ArrayList;

import android.util.Log;

/**
 * Hallitsee aseiden cooldowneja, varastoi aseiden oliot ja v�litt�� kutsupyynn�t
 * eri aseisiin.
 */
public class WeaponManager
{
    /* Pelitilat */
    public static final int SURVIVAL_MODE      = 1;
    public static final int STORY_MODE_LEVEL_1 = 1;
    
    /* Globaali cooldown, joka ammuttaessa lis�t��n jokaiseen aseeseen  */
    public static final int GLOBAL_COOLDOWN = 500;

    /* K�yt�ss� oleva ase */
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
        cooldownMax   = new int[10];
        cooldownLeft  = new int[10];
        
        // M��ritet��n cooldownit
        // TODO: Lue tiedostosta
        cooldownMax[0]  = 0;
        cooldownLeft[0] = 0;
    }
    
    /**
     * V�litt�� kutsupyynn�n k�yt�ss� olevalle aseelle aktoiden sen ja l�hett�m�ll�
     * sille kohteen koordinaatit. P�ivitt�� my�s cooldownit.
     * 
     * @param int[] Kohteen koordinaatit
     */
    public final void triggerShoot(int[] _coords)
    {
        if (cooldownLeft[currentWeapon] <= 0) {
            playerWeapons.get(currentWeapon).activate(_coords[0], _coords[1]);
            
            cooldownLeft[currentWeapon] = cooldownMax[currentWeapon];
            
            // Asetetaan globaali cooldown
            for (int i = 9; i >= 0; --i) {
                if (cooldownLeft[i] <= 0) {
                    cooldownLeft[i] = GLOBAL_COOLDOWN;
                }
            }
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
            playerWeapons.add(new WeaponDefault(wrapper));
            //playerWeapons.add(new WeaponEmp(wrapper));
            //playerWeapons.add(new WeaponSpinningLaser(wrapper));
        	//playerWeapons.add(new WeaponCluster(wrapper));
        }
        else if (_id == STORY_MODE_LEVEL_1) {
            playerWeapons.add(new WeaponDefault(wrapper));
        }
        int a = 0;
    }

    /**
     * P�ivitt�� cooldownit.
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
