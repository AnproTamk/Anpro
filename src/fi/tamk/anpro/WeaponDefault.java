package fi.tamk.anpro;

import java.util.ArrayList;

import javax.microedition.khronos.opengles.GL10;

import android.content.Context;

public class WeaponDefault {
	private ArrayList<ProjectileLaser> projectileLaser;
	
	public WeaponDefault() {
		
		/*projectileLaser.add(new ProjectileLaser()); // Id 0
		projectileLaser.add(new ProjectileLaser()); // Id 1
		projectileLaser.add(new ProjectileLaser()); // Id 2*/
	}

	public void activate(int _xTouchPosition, int _yTouchPosition) {
		for (int i = 0; i <= 2; ++i) {
			if (projectileLaser.get(i).active == false) {
				projectileLaser.get(i).activate( _xTouchPosition, _yTouchPosition);
			}
		}
	}

}
