package fi.tamk.anpro;

import java.lang.Math;

public class LinearAi extends GenericAi {
	
	private Wrapper wrapper;
	
	// Tekoälyä käyttävän vihollisen tunnus wrapperissa
	private int parentId;
	
	/*
	 * Rakentaja
	 */
	public LinearAi(int _id) {
		super(_id);
		
		parentId = _id;
		
		wrapper = Wrapper.getInstance();
	}

	/*
	 * Käsittelee tekoälyn
	 */
	public void handleAi() {
		/* Lineaarinen eteneminen
		 * 
		 * Etsitään pelaajan sijainti, verrataan omaan vihollisen omaan sijaintiin. X- ja Y-arvojen erotuksen itseisarvoilla
		 * voidaan laskea vihollisaluksen suunta:
		 * 
		 * alpha = arctan (y/x) 
		 * radiaaneina -> (PI*(arctan (y/x))/180)
		 * 
		 */
		
		double xDiff = Math.abs((double)(wrapper.enemies.get(parentId).x - wrapper.player.x));
		double yDiff = Math.abs((double)(wrapper.enemies.get(parentId).y - wrapper.player.y));
		
		
		// Eliminoidaan nollalla jakaminen
		if(xDiff == 0) {
			xDiff = 1;
		}
		
		wrapper.enemies.get(parentId).direction = (int)((Math.PI*Math.atan(yDiff/xDiff))*360);
		
		/*double angleAH;
		double angleBH;
		double angleAB; // <-- lopullinen
		double vectorA_x;
		double vectorA_y;
		double vectorB_x;
		double vectorB_y;
		double hypotenusa;
		
		if (wrapper.enemies.get(parentId).y > wrapper.player.y) {
			vectorA_x = (Math.cos((wrapper.enemies.get(parentId).direction * Math.PI)/180));
			vectorA_y = (Math.sin((wrapper.enemies.get(parentId).direction * Math.PI)/180));
			
			vectorB_x = wrapper.player.x - wrapper.enemies.get(parentId).x;
			vectorB_y = wrapper.player.y - wrapper.enemies.get(parentId).y;
			
			//hypotenusa = Math.sqrt(Math.pow((Math.sqrt(Math.pow(vectorA_x, 2) + Math.pow(vectorA_y, 2))), 2) + 
			//		               Math.pow((Math.sqrt(Math.pow(vectorB_x, 2) + Math.pow(vectorB_y, 2))), 2));
			
			angleAH = Math.atan(Math.pow((Math.sqrt(Math.pow(vectorB_x, 2) + Math.pow(vectorB_y, 2))), 2)/Math.pow((Math.sqrt(Math.pow(vectorA_x, 2) + Math.pow(vectorA_y, 2))), 2));
			angleBH = Math.atan(Math.pow((Math.sqrt(Math.pow(vectorA_x, 2) + Math.pow(vectorA_y, 2))), 2)/Math.pow((Math.sqrt(Math.pow(vectorB_x, 2) + Math.pow(vectorB_y, 2))), 2));
			
			angleAB = 180 - angleAH - angleBH;
			
			if (angleAB > 0) {
				wrapper.enemies.get(parentId).turningDirection = 2;
			}
		}*/
		
		/*double refVectorLength = Math.sqrt(Math.pow(wrapper.player.x - wrapper.enemies.get(parentId).x, 2) +
										   Math.pow(wrapper.player.y - wrapper.enemies.get(parentId).y, 2));
		
		double sightPointX = (Math.cos((wrapper.enemies.get(parentId).direction * Math.PI)/180)) * refVectorLength;
		double sightPointY = (Math.sin((wrapper.enemies.get(parentId).direction * Math.PI)/180)) * refVectorLength;
		
		//double angle = ((Math.atan2(wrapper.player.y - sightPointY, wrapper.player.x - sightPointX)) * 180) / Math.PI;
		double angle = ((Math.atan2(wrapper.player.y - sightPointY, wrapper.player.x - sightPointX)) * 180) / Math.PI;
		
		if (angle >= 180) {
			//wrapper.enemies.get(parentId).turningDirection = 1;
		}
		else if (angle < 180 && angle > 0) {
			wrapper.enemies.get(parentId).turningDirection = 2;
		}*/
	}
}