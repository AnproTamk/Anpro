package fi.tamk.anpro;

import javax.microedition.khronos.opengles.GL10;

import android.content.Context;

public class Enemy extends GameObject
{
    public int attack;
    public int speed;
    public int defence;
    public int health;
    public boolean active;
    
    // Luokan muuttujien rakentaja.
    public Enemy(GL10 _gl, Context _context, int _id, int _health, int _defence, int _speed, int _attack, boolean _active)
    {
        super(_gl, _context, _id);
        attack = _attack;
        speed = _speed;
        defence = _defence;
        health = _health;
        active = _active;
    }
	
	
	// Funktio vihollisen "aktiivisuuden" toteuttamiseen.
	public void setActive()
	{
		if (health < 0) {
			active = true;
		}
	}
	
	// Funktio vihollisen "epäaktiivisuuden" toteuttamiseen.
	public void setUnactive()
	{
		if (health == 0) {
			active = false;
		}
	}
	
	@Override
	public void triggerImpact(int _damage, int _armorPiercing)
	{
		
	}
	
	@Override
	public void triggerCollision(int _eventType, int _damage, int _armorPiercing)
	{
		
	}
	
}
