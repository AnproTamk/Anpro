package fi.tamk.anpro;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;

/* 
 * - Pit‰‰ sis‰ll‰‰n pelinaikaisten valikkojen toiminnallisuuden. 
 *   Grafiikat ovat renderˆij‰ss‰, mutta HUD-luokka hallitsee kosketuksien 
 *   tarkistamisen, pistelaskurin p‰ivitt‰misen ja cooldownien n‰ytt‰misen.
 *   
 * - 
 *   
 */

/*
 * 
 * - Pit‰‰ sis‰ll‰‰n guiobjekteja, 
 *    tunnistaa niiden tapahtumat ja
 *    ohjaa niiden funktioihin.
 * 
 */

public class HUD
{
	public static final int BUTTON_1  = 0;
	public static final int BUTTON_2  = 1;
	public static final int BUTTON_3  = 2;
	public static final int SPECIAL_1 = 2;
	public static final int SPECIAL_2 = 2;

	private static HUD instance = null;

	public int[] weapons;

	public ArrayList<GuiObject> guiObjects = null;

	private WeaponStorage weaponStorage;

	/*
	 * Rakentaja
	 */
	public HUD()
	{
		weaponStorage = WeaponStorage.getInstance();
		weapons = new int[5];
	}

	/*
	 * Palauttaa pointterin t‰h‰n luokkaan
	 */
	public static HUD getInstance()
	{
	    if(instance == null){
	       instance = new HUD();
	    }
	    return instance;
	}

	public void loadHud(Context _context)
	{
		XmlReader reader = new XmlReader(_context );
		//reader.readHUD(this);
	}

	/*
	 * P‰ivitt‰‰ cooldownit (HUD:ssa n‰kyv‰t, ei "oikeita" cooldowneja)
	 */
	public void updateCooldowns()
	{
		// TODO: Animaation lis‰‰minen 
		
		for(int i = 4; i >= 0; --i) {
			if(weaponStorage.cooldownLeft[weapons[i]] > 0 ) {
				guiObjects.get(i).usedTexture = 1;
			}
			else {
				guiObjects.get(i).usedTexture = 0;
			}
		}
	}


	/*
	 * K‰sittelee napin painalluksen & Asettaa WeaponStorage-luokkaan aseen.
	 */
	public void triggerClick(int _buttonId)
	{
		// Tarkistus, jolla estet‰‰n cooldownereiden valinta
		if (weaponStorage.cooldownLeft[weapons[_buttonId]] <=0 ) {
			weaponStorage.currentWeapon = weapons[_buttonId];
		}
	}
}
