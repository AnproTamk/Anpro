package fi.tamk.anpro;

import java.util.ArrayList;

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
    public int cooldownMax[];  // Maksimi cooldown
    public int cooldownLeft[]; // J�ljell� oleva cooldown

    /* K�yt�ss� oleva ase */
    public int       currentWeapon       = 0;     // K�yt�ss� oleva ase (viittaa alla olevien taulukoiden soluihin)
    public boolean   isUsingMotionEvents = false; // K�ytt��k� k�yt�ss� oleva ase motioneventtej�
    public boolean[] weaponLocation;		      // Aseen
    
    /* Aseiden oliot */
    private ArrayList<AbstractWeapon> allyWeapons   = null;
    private ArrayList<AbstractWeapon> enemyWeapons  = null;
    private ArrayList<AbstractWeapon> playerWeapons = null;
    
    /* Osoitin Wrapperiin */
    private Wrapper wrapper;

    /**
     * Alustaa luokan muuttujat.
     */
    public WeaponManager()
    {
        // Alustetaan taulukot
        playerWeapons = new ArrayList<AbstractWeapon>();
        allyWeapons   = new ArrayList<AbstractWeapon>();
        enemyWeapons  = new ArrayList<AbstractWeapon>();
        cooldownMax   = new int[10];
        cooldownLeft  = new int[10];
        
        // Otetaan Wrapper k�ytt��n
        wrapper = Wrapper.getInstance();
    }
    
    /**
     * V�litt�� kutsupyynn�n k�yt�ss� pelaajan aseelle aktivoiden sen ja l�hett�m�ll�
     * sille kohteen koordinaatit. P�ivitt�� my�s cooldownit.
     *
     * @param _targetX Kohteen X-koordinaatti
     * @param _targetY Kohteen Y-koordinaatti
     */
    public final void triggerPlayerShoot(float _targetX, float _targetY)
    {
		if (cooldownLeft[currentWeapon] <= 0) {
            playerWeapons.get(currentWeapon).activate(_targetX + wrapper.player.x, _targetY + wrapper.player.y,
            										  wrapper.player.x, wrapper.player.y);

            cooldownLeft[currentWeapon] = cooldownMax[currentWeapon];

            // Asetetaan globaali cooldown
            addGlobalCooldown();
        }
    }
    
    /**
     * V�litt�� kutsupyynn�n vihollisen aseelle aktivoiden sen ja l�hett�m�ll�
     * sille kohteen koordinaatit.
     * 
     * @param _startX Ampujan X-koordinaatti
     * @param _startY Ampujan Y-koordinaatti
     */
    public final void triggerEnemyShoot(float _startX, float _startY)
    {
   		enemyWeapons.get(0).activate(wrapper.player.x, wrapper.player.y, _startX, _startY);
    }
    
    /**
     * V�litt�� kutsupyynn�n liittolaisen aseelle aktivoiden sen ja l�hett�m�ll�
     * sille kohteen koordinaatit.
     * 
     * @param _targetX Kohteen X-koordinaatti
     * @param _targetY Kohteen Y-koordinaatti
     * @param _startX Ampujan X-koordinaatti
     * @param _startY Ampujan Y-koordinaatti
     */
    public final void triggerAllyShoot(float _targetX, float _targetY, float _startX, float _startY)
    {
   		allyWeapons.get(0).activate(_targetX, _targetY, _startX, _startY);
    }
    
    /**
     * V�litt�� kutsupyynn�n Motion-teko�ly� k�ytt�v�lle valittuna olevalle aseelle.
     * P�ivitt�� my�s cooldownit.
     * 
     * @param _path Ammuksen reitti
     */
    public final void triggerMotionShoot(int[][] _path)
    {
    	// TODO: Motion-teko�ly� k�ytt�vien aseiden tunnukset pit�� tallentaa erikseen,
    	// jotta t�m� funktio osaa suoraan hakea niit� k�ym�tt� l�pi muita aseita.
        if (cooldownLeft[currentWeapon] <= 0) {
            playerWeapons.get(currentWeapon).activate(_path, wrapper.player.x, wrapper.player.y);
            
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
                cooldownLeft[i] = 0;
            }
        }
	}
    
    /**
     * Lataa aseet muistiin.
     * 
     * @param _id Pelitilan ja tason tunnus
     */
    public final void initialize(int _id)
    {
    	// TODO: SurvivalModea varten aseille pit�� m��ritell� erikseen, ovatko ne k�ytettt�viss�
    	// vai ei, sill� aseita tulee k�ytt��n vain combojen ja achievementtien yhteydess� sek�
    	// StoryModen edetess�.
    	
    	// TODO: StoryModessa k�ytett�viss� olevat aseet tulisi ladata kykypuun mukaisesti.
    	
        // Ladataan tarvittavat aseluokat muistiin
        if (_id == SURVIVAL_MODE) {
            // Ladataan aseet ja m��ritet��n niiden cooldownit
            playerWeapons.add(new WeaponSpitfire(wrapper, Wrapper.CLASS_TYPE_PLAYER));
            cooldownMax[0] = 0;
            
            //playerWeapons.add(new WeaponEmp(wrapper, Wrapper.CLASS_TYPE_PLAYER));
            //playerWeapons.add(new WeaponSpinningLaser(wrapper, Wrapper.CLASS_TYPE_PLAYER));
        	//playerWeapons.add(new WeaponCluster(wrapper, Wrapper.CLASS_TYPE_PLAYER));
        	//playerWeapons.add(new WeaponSwarm(wrapper, Wrapper.CLASS_TYPE_PLAYER));
        	//playerWeapons.add(new WeaponMissile(wrapper, Wrapper.CLASS_TYPE_PLAYER));

            enemyWeapons.add(new WeaponDefault(wrapper, Wrapper.CLASS_TYPE_ENEMY));

            allyWeapons.add(new WeaponSpitfire(wrapper, Wrapper.CLASS_TYPE_ALLY));
        }
        else if (_id == STORY_MODE_LEVEL_1) {
            playerWeapons.add(new WeaponDefault(wrapper, Wrapper.CLASS_TYPE_PLAYER));
            cooldownMax[0] = 0;
        }
    }

    /**
     * P�ivitt�� cooldownit (v�hent�� 100 ms jokaisesta cooldownista).
     */
    public final void updateCooldowns()
    {
        for (int i = 9; i>= 0; --i) {
            if (cooldownLeft[i] > 0) {
                cooldownLeft[i] -= 100;
            }
        }
    }

    /**
     * Asettaa k�yt�ss� olevan aseen ja tarkastaa sen tarpeen MotionEventille.
     * 
     * @param _selectedWeapon Valittu ase
     */
    public void setCurrentWeapon(int _selectedWeapon)
    {
    	// TODO: valitun aseen muuttaminen.
    	currentWeapon = _selectedWeapon;
    	
    	// Tarkastetaan tarvitseeko nykyinen ase MotionEventti�
    	if(weaponLocation[currentWeapon]) {
    		isUsingMotionEvents = true;
    	}
    	else {
    		isUsingMotionEvents = false;
    	}
    }
}
