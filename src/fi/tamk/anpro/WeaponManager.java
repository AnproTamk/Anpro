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
    
    /* Cooldownit */
    public int cooldownMax[];         // Maksimi cooldown
    public int cooldownLeft[];        // J�ljell� oleva cooldown

    /* K�yt�ss� oleva ase */
    public int     currentWeapon       = 0;    // K�yt�ss� oleva ase (viittaa alla olevien taulukoiden soluihin)
    public boolean isUsingMotionEvents = true; // K�ytt��k� k�yt�ss� oleva ase motioneventtej�
    
    /* Aseiden oliot */
    public ArrayList<AbstractWeapon> allyWeapons   = null;
    public ArrayList<AbstractWeapon> enemyWeapons  = null;
    public ArrayList<AbstractWeapon> playerWeapons = null;

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
     * @param int   Ampujan tyyppi
     * @param float Ampujan X-koordinaatti
     * @param float Ampujan Y-koordinaatti
     */
    public final void triggerShoot(int[] _coords, int _type, float _x, float _y)
    {
    	if(_type == Wrapper.CLASS_TYPE_PLAYER) {
	        if (cooldownLeft[currentWeapon] <= 0) {
	            playerWeapons.get(currentWeapon).activate(_coords[0], _coords[1], _x, _y);
	            
	            cooldownLeft[currentWeapon] = cooldownMax[currentWeapon];

	            // Asetetaan globaali cooldown
	            addGlobalCooldown();
	        }
    	}
    	else if(_type == Wrapper.CLASS_TYPE_ENEMY) {
    		enemyWeapons.get(0).activate(_coords[0], _coords[1], _x, _y);
    	}
    }
    
    /**
     * V�litt�� kutsupyynn�n Motion-teko�ly� k�ytt�v�lle valittuna olevalle aseelle.
     * P�ivitt�� my�s cooldownit.
     * 
     * @param int[] Kohteen koordinaatit
     * @param float Ampujan X-koordinaatti
     * @param float Ampujan Y-koordinaatti
     */
    public final void triggerMotionShoot(int[][] _path, float _x, float _y)
    {
    	// TODO: Motion-teko�ly� k�ytt�vien aseiden tunnukset pit�� tallentaa erikseen,
    	// jotta t�m� funktio osaa suoraan hakea niit� k�ym�tt� l�pi muita aseita.
        if (cooldownLeft[currentWeapon] <= 0) {
            playerWeapons.get(currentWeapon).activate(_path, _x, _y);
            
            cooldownLeft[currentWeapon] = cooldownMax[currentWeapon];

            // Asetetaan globaali cooldown
            addGlobalCooldown();
        }
    }

	/**
	 * Lis�� global cooldownin aseisiin.
	 */
	private final void addGlobalCooldown()
	{
        // Asetetaan globaali cooldown
        for (int i = 9; i >= 0; --i) {
            if (cooldownLeft[i] <= 0) {
                cooldownLeft[i] = 500;
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
            playerWeapons.add(new WeaponDefault(wrapper, Wrapper.CLASS_TYPE_PLAYER));
            //playerWeapons.add(new WeaponEmp(wrapper, Wrapper.CLASS_TYPE_PLAYER));
            //playerWeapons.add(new WeaponSpinningLaser(wrapper, Wrapper.CLASS_TYPE_PLAYER));
        	//playerWeapons.add(new WeaponCluster(wrapper, Wrapper.CLASS_TYPE_PLAYER));
        	//playerWeapons.add(new WeaponSwarm(wrapper, Wrapper.CLASS_TYPE_PLAYER));
        	//playerWeapons.add(new WeaponMissile(wrapper, Wrapper.CLASS_TYPE_PLAYER));

            enemyWeapons.add(new WeaponDefault(wrapper, Wrapper.CLASS_TYPE_ENEMY));
        }
        else if (_id == STORY_MODE_LEVEL_1) {
            playerWeapons.add(new WeaponDefault(wrapper, Wrapper.CLASS_TYPE_PLAYER));
        }
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
