package fi.tamk.anpro;

import java.util.ArrayList;

import android.util.Log;

/**
 * Hallitsee aseiden cooldowneja, varastoi aseiden oliot ja välittää kutsupyynnöt
 * eri aseisiin.
 */
public class WeaponManager
{
    /* Vihollisten aseiden vakiot */
	public static int ENEMY_LASER = 0;
	public static int ENEMY_SPITFIRE = 1;
	
	/* Pelaajan aseiden vakiot */
	public static int WEAPON_LASER 		 	= 0;
	public static int WEAPON_CLUSTER		= 1;
	public static int WEAPON_MISSILE		= 2;
	public static int WEAPON_SWARM			= 3;
	public static int WEAPON_EMP   		    = 4;
	public static int WEAPON_SPINNING_LASER = 5;
	public static int WEAPON_SPITFIRE		= 6;
	
    
    /* Cooldownit */
    public int cooldownMax[];  // Maksimi cooldown
    public int cooldownLeft[]; // Jäljellä oleva cooldown

    /* Käytössä oleva ase */
    											  // 0:Laser 1:EMP 2:Spinning Laser 3:Cluster 4:Swarm 5:Missile 6:Spitfire
    public int       currentWeapon       = 0;     // Käytössä oleva ase (viittaa alla olevien taulukoiden soluihin)
    public boolean   isUsingMotionEvents = false; // Käyttääkö käytössä oleva ase motioneventtejä
    public boolean[] motioneventUsage;		      
    
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
        /* Otetaan tarvittavat luokat käyttöön käyttöön */
        wrapper = Wrapper.getInstance();
        
		/* Alustetaan muuttujat */
        playerWeapons    = new ArrayList<AbstractWeapon>();
        allyWeapons      = new ArrayList<AbstractWeapon>();
        enemyWeapons     = new ArrayList<AbstractWeapon>();
        cooldownMax      = new int[10];
        cooldownLeft     = new int[10];
        motioneventUsage = new boolean[7];
        
    	// TODO: Muuta aseiden järjestys loogisemmaksi
        // Ladataan aseet ja määritetään niiden cooldownit
        playerWeapons.add(new WeaponLaser(wrapper, Wrapper.CLASS_TYPE_PLAYER));
        cooldownMax[0] = 0;
        playerWeapons.add(new WeaponCluster(wrapper, Wrapper.CLASS_TYPE_PLAYER));
        cooldownMax[1] = 3000;
    	playerWeapons.add(new WeaponMissile(wrapper, Wrapper.CLASS_TYPE_PLAYER));
        cooldownMax[2] = 1000;
    	playerWeapons.add(new WeaponSwarm(wrapper, Wrapper.CLASS_TYPE_PLAYER));
        cooldownMax[3] = 15000;
        playerWeapons.add(new WeaponEmp(wrapper, Wrapper.CLASS_TYPE_PLAYER));
        cooldownMax[4] = 10000;
        playerWeapons.add(new WeaponSpinningLaser(wrapper, Wrapper.CLASS_TYPE_PLAYER));
        cooldownMax[5] = 8000;
        playerWeapons.add(new WeaponSpitfire(wrapper, Wrapper.CLASS_TYPE_PLAYER));
        cooldownMax[6] = 200;

        allyWeapons.add(new WeaponSpitfire(wrapper, Wrapper.CLASS_TYPE_ALLY));
            
        enemyWeapons.add(new WeaponLaser(wrapper, Wrapper.CLASS_TYPE_ENEMY));
        enemyWeapons.add(new WeaponSpitfire(wrapper, Wrapper.CLASS_TYPE_ENEMY));
    }

	/* =======================================================
	 * Uudet funktiot
	 * ======================================================= */
    /**
     * Välittää kutsupyynnön käytössä pelaajan aseelle aktivoiden sen ja lähettämällä
     * sille kohteen koordinaatit. Päivittää myös cooldownit.
     *
     * @param _targetX Kohteen X-koordinaatti
     * @param _targetY Kohteen Y-koordinaatti
     */
    public final void triggerPlayerShoot(float _targetX, float _targetY)
    {
		Log.e("TRIGGERSHOOT", String.valueOf(currentWeapon));
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
     * @param _startY Ampujan Y-koordinaatt
     * @param _weapon Vihollisen ase
     */
    public final void triggerEnemyShoot(float _startX, float _startY, int _weapon)
    {
    		enemyWeapons.get(_weapon).activate(wrapper.player.x, wrapper.player.y, _startX, _startY);
    }
    
    public final void triggerEnemyShootForward(double _direction, float _startX, float _startY, int _weapon) {
    	// Vihollinen ampuu suoraa omaan suuntaansa, jos käytössä on Spitfire, muutoin ampuminen tapahtuu pelaajan suuntaan
    	if (_weapon == WeaponManager.ENEMY_SPITFIRE) {
    		enemyWeapons.get(_weapon).activate(Utility.getAngleToCoordinates(_direction, _startX, _startY, 'x'), Utility.getAngleToCoordinates(_direction, _startX, _startY, 'y'), _startX, _startY);
    	}
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
                cooldownLeft[i] = 200;
            }
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
    	if(motioneventUsage[currentWeapon]) {
    		isUsingMotionEvents = true;
    	}
    	else {
    		isUsingMotionEvents = false;
    	}
    }
}
