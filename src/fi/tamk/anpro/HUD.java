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
 */

/*
 * 
 *	- Pitää sisällään guiobjekteja, 
 *    tunnistaa niiden tapahtumat ja
 *    ohjaa niiden funktioihin.
 *    
 */


public class HUD
{

	
	// Muuttujat
	private static HUD instance = null;





	// HUD:in rakentaja
	protected HUD()
	{
		
	}
	// HUD:in rakentajan loppu

	// Luo HUD:in, jos sellaista ei vielä ole tehty
	public static HUD getInstance()
	{
	    if(instance == null) {
	       instance = new HUD();
	    }
	    return instance;
	}


	public void handleTouch (int x, int y)
	{
		// - Tunnistaa painamistapahtuman ja tulkitsee, tapahtuuko se 
		//   näytön vai HUD:n alueella.
		//
		//   -> jos tunnistaa kosketuksen HUD:iin, ohjaa eteenpäin
		//
	
		/*
		 * 		if(){
		 * 			
		 * 		}
		 * 	
		 */
		
		
	}


}
