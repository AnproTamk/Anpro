package fi.tamk.anpro;

import java.util.ArrayList;

import javax.microedition.khronos.opengles.GL10;

import android.content.Context;

public class Player extends GameObject
{
	public int health;
	public int defence;
	private int currentWeapon;
	public int spawnPoint;
	private ArrayList<Integer> cooldownLeft;
	private ArrayList<Integer> cooldownTime;
	
	SurvivalMode survivalMode;
	
	
	// Luokan muuttujien rakentaja.
	public Player(GL10 _gl, Context _context, int _id, int _health, int _defence)
	{
		super(_gl, _context, _id);
		health = _health;
		defence = _defence;
		currentWeapon = -1;
		
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
