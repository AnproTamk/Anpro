package fi.tamk.anpro;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;

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
	/* Painikkeiden tunnukset */
	public static final int BUTTON_1  = 0;
	public static final int BUTTON_2  = 1;
	public static final int BUTTON_3  = 2;
	public static final int SPECIAL_1 = 2;
	public static final int SPECIAL_2 = 2;

	/* Painikkeisiin sijoitettujen aseiden tunnukset (viittaa WeaponManagerin
	   asetaulukoiden soluihin */
	public int[] weapons;
	
	/* K�ytt�liittym�n objektit */
	public ArrayList<GuiObject> guiObjects = null;

	/* Osoitin WeaponManageriin (HUDin teht�v�n� on muuttaa k�yt�ss� olevaa
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
	 * P�ivitt�� cooldownit (HUD:ssa n�kyv�t, ei "oikeita" cooldowneja).
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
	 * K�sittelee napin painalluksen ja asettaa uuden aseen k�ytt��n WeaponManageriin.
	 * 
	 * @param int Painetun napin tunnus
	 */
	public final void triggerClick(int _buttonId)
	{
		// Tarkistetaan, onko aseessa cooldownia j�ljell� vai ei
		if (weaponManager.cooldownLeft[weapons[_buttonId]] <=0 ) {
			weaponManager.currentWeapon = weapons[_buttonId];
		}
	}
	
	public final static HUD getConnection()
	{
		return pointerToSelf;
	}
}
