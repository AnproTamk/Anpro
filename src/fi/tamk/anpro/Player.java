package fi.tamk.anpro;

import java.util.ArrayList;

public class Player
{
	private int health;
	private int defence;
	private int currentWeapon;
	private int spawnPoint;
	private ArrayList<Integer> cooldownLeft;
	private ArrayList<Integer> cooldownTime;
	
	SurvivalMode survivalMode;
	
	
	// Luokan muuttujien rakentaja.
	public Player(int _health, int _defence, int _currentWeapon)
	{
		health = _health;
		defence = _defence;
		currentWeapon = _currentWeapon;
		
		survivalMode = SurvivalMode.getInstance();
		
		for (int i = survivalMode.cooldownTime.size()-1; i >= 0; i++) {
			cooldownTime.add(i, survivalMode.cooldownTime.get(i));
			
			cooldownLeft.add(i, 0);
		}
	}
	
	// Funktio pelaajan laukauksen toteuttamiseen.
	public void triggerShoot()
	{
		if (cooldownLeft.get(currentWeapon) == 0) {
			survivalMode.weapons.get(currentWeapon).activate();
			cooldownLeft.add(currentWeapon, cooldownTime.get(currentWeapon));
		}
	}
}
