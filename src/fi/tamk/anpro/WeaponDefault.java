package fi.tamk.anpro;

import java.util.ArrayList;

public class WeaponDefault extends Weapon {
	// Ammukset ja niiden tilat
	private ArrayList<ProjectileLaser> projectiles;
	
	public WeaponDefault() {
		super();
		
		// M‰‰ritet‰‰n aseen sijainti HUDissa
		locationOnHUD = 1;
		
		// Alustetaan ammukset
		projectiles = new ArrayList<ProjectileLaser>(AMOUNT_OF_PROJECTILES);

		for (int i = AMOUNT_OF_PROJECTILES-1; i >= 0; --i) {
			projectiles.add(new ProjectileLaser());
		}
	}

	public void activate(int _xTouchPosition, int _yTouchPosition) {
		for (int i = AMOUNT_OF_PROJECTILES-1; i >= 0; --i) {
			if (projectiles.get(i).active == false) {
				projectiles.get(i).activate( _xTouchPosition, _yTouchPosition);
				projectiles.get(i).active = true;
			}
		}
	}
}
