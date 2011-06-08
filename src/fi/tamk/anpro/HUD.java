package fi.tamk.anpro;

import java.util.ArrayList;

import fi.tamk.anpro.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;


/* 
 * - Pit�� sis�ll��n pelinaikaisten valikkojen toiminnallisuuden. 
 *   Grafiikat ovat render�ij�ss�, mutta HUD-luokka hallitsee kosketuksien 
 *   tarkistamisen, pistelaskurin p�ivitt�misen ja cooldownien n�ytt�misen.
 *   
 * - 
 */

/*
 * 
 *	- Pit�� sis�ll��n guiobjekteja, 
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

	// Luo HUD:in, jos sellaista ei viel� ole tehty
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
		//   n�yt�n vai HUD:n alueella.
		//
		//   -> jos tunnistaa kosketuksen HUD:iin, ohjaa eteenp�in
		//
	
		/*
		 * 		if(){
		 * 			
		 * 		}
		 * 	
		 */
		
		
	}


}
