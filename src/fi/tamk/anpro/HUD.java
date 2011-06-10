package fi.tamk.anpro;

/* 
 * - Pit�� sis�ll��n pelinaikaisten valikkojen toiminnallisuuden. 
 *   Grafiikat ovat render�ij�ss�, mutta HUD-luokka hallitsee kosketuksien 
 *   tarkistamisen, pistelaskurin p�ivitt�misen ja cooldownien n�ytt�misen.
 *   
 * - 
 *   
 */

/*
 * 
 * - Pit�� sis�ll��n guiobjekteja, 
 *    tunnistaa niiden tapahtumat ja
 *    ohjaa niiden funktioihin.
 * 
 */

public class HUD
{
	public static final int BUTTON_DEFAULT_WEAPON = 1;
	public static final int BUTTON_1 			  = 2;
	public static final int BUTTON_2 			  = 3;
	
	private static HUD instance = null;
	
	public int[] weapons;

	public GuiObject gameScore    = null;
	public GuiObject cooldownBar1 = null;
	public GuiObject gunButton1   = null;
	public GuiObject gunButton2   = null;
	
	private WeaponStorage weaponStorage;
	
	/*
	 * Rakentaja
	 */
	public HUD()
	{
		weaponStorage = WeaponStorage.getInstance();
	}

	/*
	 * Palauttaa pointterin t�h�n luokkaan
	 */
	public static HUD getInstance()
	{
	    if(instance == null){
	       instance = new HUD();
	    }
	    return instance;
	}
	
	/*
	 * P�ivitt�� cooldownit (HUD:ssa n�kyv�t, ei "oikeita" cooldowneja)
	 */
	public void updateCooldowns()
	{
		int coolConsume = weaponStorage.cooldownLeft[0];
		int coolLeft    = weaponStorage.cooldownLeft[0];
		long coolTime;
		
		// jos cooldown > 0, cooldown laskee
		for(coolLeft = coolConsume; coolConsume>0; coolConsume--){
	        --coolLeft;
	        	        
	        // odottaa ajastetusti, koska laskee cooldownia
	        coolTime = android.os.SystemClock.uptimeMillis();
		}
		
		// kun cooldown = 0, ilmoitetaan WeaponStorageen, ett� kyky on taas k�yt�ss�
		if(coolLeft == 0){
			++coolLeft;
		}
	}

	/*
	 * K�sittelee napin painalluksen
	 */
	public void triggerClick(int _buttonId)
	{
		// Asettaa WeaponStorage-luokkaan perusaseen ja tallentaa tiedon my�s HUD:iin.
		if(_buttonId == BUTTON_DEFAULT_WEAPON){
			weaponStorage.currentWeapon = 0;
			weapons[0] = BUTTON_DEFAULT_WEAPON;
		}

		// Asettaa WeaponStorage-luokkaan 2. aseen ja tallentaa tiedon my�s HUD:iin.
		else if(_buttonId == BUTTON_1){
			weaponStorage.currentWeapon = 1;
			weapons[1] = BUTTON_1;
		}

		// Asettaa WeaponStorage-luokkaan 3. aseen ja tallentaa tiedon my�s HUD:iin.
		else if(_buttonId == BUTTON_2){
			weaponStorage.currentWeapon = 2;
			weapons[2] = BUTTON_2;
		}
	}

}
