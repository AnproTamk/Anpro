package fi.tamk.anpro;

import java.util.ArrayList;

public class WeaponDefault extends Weapon {
	// Ammukset ja niiden tilat
	private ArrayList<ProjectileLaser> projectiles;
	
	public WeaponDefault() {
		super();
		
		// Alustetaan ammukset
		projectiles = new ArrayList<ProjectileLaser>(AMOUNT_OF_PROJECTILES);

		for (int i = AMOUNT_OF_PROJECTILES-1; i >= 0; --i) {
			projectiles.add(new ProjectileLaser(this));
		}
		
		// Alustetaan ammusten tilat
		projectileStates = new int[AMOUNT_OF_PROJECTILES];
		
		for (int i = AMOUNT_OF_PROJECTILES-1; i >= 0; --i) {
			projectileStates[i] = 0;
		}
	}

	public void activate(int _xTouchPosition, int _yTouchPosition) {
		for (int i = AMOUNT_OF_PROJECTILES-1; i >= 0; --i) {
			if (projectileStates[i] == 0) {
				projectiles.get(i).activate( _xTouchPosition, _yTouchPosition);
				projectileStates[i] = 1;
			}
		}
	}
	
	
}
