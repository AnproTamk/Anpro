package fi.tamk.anpro;

import java.util.ArrayList;

/*
 * Luo uusi WeaponStorage-luokka. T‰h‰n luokkaan tulisi siirt‰‰ 
 * kaikki aseisiin liittyv‰t asiat, eli aselistat ja cooldownit. 
 * WeaponStorage tarvitsee myˆs initialize-funktion, 
 * joka XmlReaderin avulla hankkii tiedot aseista XML-tiedostosta.

 * Kun TouchEngine tulkitsee kosketuksen ampumiseksi, se kutsuu WeaponStoragen 
 * funkiota triggerShoot, joka tulkitsee, mit‰ asetta pit‰‰ k‰ytt‰‰. 
 * T‰st‰ syyst‰ myˆs currentWeapon-muuttuja siirret‰‰n Player-luokasta t‰nne.
 */

public class WeaponStorage
{
	private static WeaponStorage instance = null;

	private int currentWeapon;
	private int coolDown;

	public ArrayList<Integer> enemyWeapon    = null;
	public ArrayList<Integer> allyWeapon     = null;
	public ArrayList<Integer> survivalWeapon = null;

	public WeaponDefault weaponDefault       = null;
	public Weapon weapon                     = null;

	// T‰h‰n lis‰‰ aseita, kun valmistuvat:
	// public WeaponMagnet WeaponMagnet;
	// public WeaponCluster WeaponCluster;


	// WeaponStoragen rakentaja
	protected WeaponStorage()
    {
		enemyWeapon    = new ArrayList<Integer>();
		allyWeapon     = new ArrayList<Integer>();
		survivalWeapon = new ArrayList<Integer>();
		
    	// public WeaponMagnet WeaponMagnet;
		// public WeaponCluster WeaponCluster;

    }

    public static WeaponStorage getInstance()
    {
        if(instance == null){
            instance = new WeaponStorage();
        }
        return instance;
    }

    public void currentWeapon(int _currentWeapon){
    	_currentWeapon = currentWeapon;
    }
    
    public void triggerShoot(int _currentWeapon)
    {
    	_currentWeapon = currentWeapon;
    	int _coolDown =  coolDown;


    	if(_currentWeapon == 1){
    		// weaponDefault.activate();
    		_coolDown = 1;

    	}

    	else if(_currentWeapon == 2){

    		_coolDown = 2;
    	}
    	
    }
    
    public void initialize(int _id)
    {
       	survivalWeapon = context.getResources().getXml(R.xml.class.getField("weapon_"+_id).getInt(getClass()));
    	/*
    	Aseiden luominen latausvaiheessa (uusi mahdollinen suunnitelma)
    	1.	StoryMode- tai SurvivalMode-luokka kutsuu WeaponStoragen initialize-funktiota.
    	2.	Initialize lukee XmlReaderin avulla aseiden tiedot ja palauttaa ne WeaponStoragelle taulukossa.
    	3.	WeaponStorage luo tietojen perusteella olioita alemmista aseluokista asetaulukkoon ja v‰litt‰‰ tiedot parametreina.
		*/

    }

}
