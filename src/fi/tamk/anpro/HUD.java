package fi.tamk.anpro;

import java.util.ArrayList;

import fi.tamk.anpro.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
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
	public static final int BUTTON_DEFAULT_WEAPON = 1;
	public static final int BUTTON_1 			  = 2;
	public static final int BUTTON_2 			  = 3;
	
	private static HUD instance   = null;
	public int[] weapons;

	public GuiObject gameScore    = null;
	public GuiObject cooldownBar1 = null;
	public GuiObject gunButton1   = null;
	public GuiObject gunButton2   = null;
	
	WeaponStorage weaponStorage  = null;
	
	// HUD:in rakentaja
	public HUD()
	{

	}

	public static HUD getInstance()
	{
	    if(instance == null){
	       instance = new HUD();
	    }
	    return instance;
	}
	// HUD:in rakentajan loppu



	public void upDateCooldowns(WeaponStorage _weaponStorage)
	{
		_weaponStorage = weaponStorage;
		int coolConsume    = weaponStorage.cooldownLeft[0];
		int coolLeft   = weaponStorage.cooldownLeft[0];
		long coolTime;
		
		// jos cooldown > 0, cooldown laskee
		for(coolLeft = coolConsume; coolConsume>0; coolConsume--){
	        --coolLeft;
	        	        
	        // odottaa ajastetusti, koska laskee cooldownia
	        //coolTime = android.os.SystemClock.uptimeMillis();
		}
		
		// kun cooldown = 0, ilmoitetaan WeaponStorageen, että kyky on taas käytössä
		if(coolLeft == 0){
			++coolLeft;
		}
	}

	
	 
	public void triggerClick(int _buttonId)
	{
		
		if(_buttonId == BUTTON_DEFAULT_WEAPON){
			weaponStorage.currentWeapon = 0;
		}

		else if(_buttonId == BUTTON_1){
			weaponStorage.currentWeapon = 1;
		}

		else if(_buttonId == BUTTON_2){
			weaponStorage.currentWeapon = 2;
		}

		// - Tunnistaa painamistapahtuman ja tulkitsee, tapahtuuko se 
		//   näytön vai HUD:n alueella.

		//
		//   -> jos tunnistaa kosketuksen HUD:iin, ohjaa eteenpäin
		//
	}

}
