package fi.tamk.anpro;

import java.util.ArrayList;

/**
 * Hallitsee aseiden cooldowneja, varastoi aseiden oliot ja välittää kutsupyynnöt
 * eri aseisiin.
 */
public class WeaponManager
{
    /* Pelitilat */
    public static final int SURVIVAL_MODE      = 1;
    public static final int STORY_MODE_LEVEL_1 = 1;
    
    /* Cooldownit */
    public int cooldownMax[];  // Maksimi cooldown
    public int cooldownLeft[]; // Jäljellä oleva cooldown

    /* Käytössä oleva ase */
    public int       currentWeapon       = 0;     // Käytössä oleva ase (viittaa alla olevien taulukoiden soluihin)
    public boolean   isUsingMotionEvents = false; // Käyttääkö käytössä oleva ase motioneventtejä
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
        
        // Otetaan Wrapper käyttöön
        wrapper = Wrapper.getInstance();
    }
    
    /**
     * Välittää kutsupyynnön käytössä pelaajan aseelle aktivoiden sen ja lähettämällä
     * sille kohteen koordinaatit. Päivittää myös cooldownit.
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
     * Välittää kutsupyynnön vihollisen aseelle aktivoiden sen ja lähettämällä
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
     * Välittää kutsupyynnön liittolaisen aseelle aktivoiden sen ja lähettämällä
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
     * Välittää kutsupyynnön Motion-tekoälyä käyttävälle valittuna olevalle aseelle.
     * Päivittää myös cooldownit.
     * 
     * @param _path Ammuksen reitti
     */
    public final void triggerMotionShoot(int[][] _path)
    {
    	// TODO: Motion-tekoälyä käyttävien aseiden tunnukset pitää tallentaa erikseen,
    	// jotta tämä funktio osaa suoraan hakea niitä käymättä läpi muita aseita.
        if (cooldownLeft[currentWeapon] <= 0) {
            playerWeapons.get(currentWeapon).activate(_path, wrapper.player.x, wrapper.player.y);
            
            cooldownLeft[currentWeapon] = cooldownMax[currentWeapon];

            // Asetetaan globaali cooldown
            addGlobalCooldown();
        }
    }

	/**
	 * Lisää global cooldownin aseisiin.
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
    	// TODO: SurvivalModea varten aseille pitää määritellä erikseen, ovatko ne käytetttävissä
    	// vai ei, sillä aseita tulee käyttöön vain combojen ja achievementtien yhteydessä sekä
    	// StoryModen edetessä.
    	
    	// TODO: StoryModessa käytettävissä olevat aseet tulisi ladata kykypuun mukaisesti.
    	
        // Ladataan tarvittavat aseluokat muistiin
        if (_id == SURVIVAL_MODE) {
            // Ladataan aseet ja määritetään niiden cooldownit
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
     * Päivittää cooldownit (vähentää 100 ms jokaisesta cooldownista).
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
     * Asettaa käytössä olevan aseen ja tarkastaa sen tarpeen MotionEventille.
     * 
     * @param _selectedWeapon Valittu ase
     */
    public void setCurrentWeapon(int _selectedWeapon)
    {
    	// TODO: valitun aseen muuttaminen.
    	currentWeapon = _selectedWeapon;
    	
    	// Tarkastetaan tarvitseeko nykyinen ase MotionEventtiä
    	if(weaponLocation[currentWeapon]) {
    		isUsingMotionEvents = true;
    	}
    	else {
    		isUsingMotionEvents = false;
    	}
    }
}
