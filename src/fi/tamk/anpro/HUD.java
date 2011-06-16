package fi.tamk.anpro;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;

/* 
 * - Pitää sisällään pelinaikaisten valikkojen toiminnallisuuden. 
 *   Grafiikat ovat renderöijässä, mutta HUD-luokka hallitsee kosketuksien 
 *   tarkistamisen, pistelaskurin päivittämisen ja cooldownien näyttämisen.
 *   
 * - 
 *   
 */

/*
 * 
 * - Pitää sisällään guiobjekteja, 
 *    tunnistaa niiden tapahtumat ja
 *    ohjaa niiden funktioihin.
 * 
 */

public class HUD
{
	/* Painikkeiden tunnukset */
	public static final int BUTTON_1  = 0;
	public static final int BUTTON_2  = 1;
	public static final int BUTTON_3  = 2;
	public static final int SPECIAL_1 = 2;
	public static final int SPECIAL_2 = 2;

	/* Painikkeisiin sijoitettujen aseiden tunnukset (viittaa WeaponManagerin
	   asetaulukoiden soluihin */
	public int[] weapons;
	
	/* Käyttöliittymän objektit */
	public ArrayList<GuiObject> guiObjects = null;

	/* Osoitin WeaponManageriin (HUDin tehtävänä on muuttaa käytössä olevaa
	   asetta WeaponManagerista) */
	private final WeaponManager weaponManager;
	
	private static HUD pointerToSelf;

	/**
	 * Alustaa luokan muuttujat ja kutsuu XmlReaderia.
	 */
	public HUD(Context _context)
	{
		weaponManager = WeaponManager.getConnection();
		weapons = new int[5];
		
		XmlReader reader = new XmlReader(_context );
		reader.readHUD(this);
	}

	/**
	 * Päivittää cooldownit (HUD:ssa näkyvät, ei "oikeita" cooldowneja).
	 */
	public final void updateCooldowns()
	{
		for(int i = 4; i >= 0; --i) {
			if(weaponManager.cooldownLeft[weapons[i]] > 0 ) {
				guiObjects.get(i).usedTexture = 1;
			}
			else {
				guiObjects.get(i).usedTexture = 0;
			}
		}
	}

	/**
	 * Käsittelee napin painalluksen ja asettaa uuden aseen käyttöön WeaponManageriin.
	 * 
	 * @param int Painetun napin tunnus
	 */
	public final void triggerClick(int _buttonId)
	{
		// Tarkistetaan, onko aseessa cooldownia jäljellä vai ei
		if (weaponManager.cooldownLeft[weapons[_buttonId]] <=0 ) {
			weaponManager.currentWeapon = weapons[_buttonId];
		}
	}
	
	public final static HUD getConnection()
	{
		return pointerToSelf;
	}
}
