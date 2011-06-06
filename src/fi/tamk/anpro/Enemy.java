package fi.tamk.anpro;

import javax.microedition.khronos.opengles.GL10;
import java.util.ArrayList;

public class Enemy extends GameObject
{
    public int attack;
    public int speed;
    public int defence;
    public int health;
    public int rank;
    
    ArrayList<Animation> animations;
    ArrayList<Texture> textures;
    
    public boolean active;
    
    Wrapper wrapper;
    int     listId;
    
    // Luokan muuttujien rakentaja.
    public Enemy(int _health, int _defence, int _speed, int _attack, boolean _active, int _rank)
    {
        super();
        attack   = _attack;
        speed    = _speed;
        defence  = _defence;
        health   = _health;
        active   = _active;
        rank     = _rank;
    
        animationLength = new int[3];

        int offset = rank *3;

        animationLength[0] = wrapper.renderer.playerAnimations.get(offset - 3).length;
        animationLength[1] = wrapper.renderer.playerAnimations.get(offset - 2).length;
        animationLength[2] = wrapper.renderer.playerAnimations.get(offset - 1).length;
        
        wrapper = Wrapper.getInstance();
        
        listId = wrapper.addToList(this);
    }


	// Funktio vihollisen "aktiivisuuden" toteuttamiseen.
	public void setActive()
	{
		if (health > 0) {
			active = true;
		
			//int[]   objectStatuses;
			//Enemy[] enemies;
			
			wrapper.enemyStates.set(listId, 1);
		}
	}

	// Funktio vihollisen "epäaktiivisuuden" toteuttamiseen.
	public void setUnactive()
	{
		if (health == 0) {
			active = false;

			wrapper.enemyStates.set(listId, 1);
		}
		// hanki pointteri Wrapper-luokasta
		// poista tästä luokasta pointteri taulukoista,
		// molemmista siis!!----^
		// poista "vihollisen aloituspiste"

	}

	/*
	Vihollisalus olisi luultavasti parasta siirtää vain pois näkyvistä sen tuhoutuessa. Se tulisi
	myös määrittää tuhotuksi, mikä estäisi tekoälyn toiminnan ja poistaisi sen piirtotaulukosta
	(tarvitaan siis jokin taulukko, johon määritetään objektit, jotka tulisi piirtää seuraavassa
	framessa).
	*/
	
	
	public void draw(GL10 _gl)
	{
		// 1. Tarkistaa onko animaatio päällä.
		// 2. kutsuu animaatioita/tekstuureita
		if (usedAnimation >= 0){
			animations.get(usedAnimation).draw(_gl, x, y, direction, currentFrame);	
			}
		
		else{
			textures.get(usedTexture).draw(_gl, x, y, direction);
		}
	}
	
	
	public void setDrawables(ArrayList<Animation> _animations, ArrayList<Texture> _textures)
	{
		textures   = _textures;
		animations = _animations;
	}
	
	public void triggerImpact(int _damage, int _armorPiercing)
	{
		
	}
	
	
	public void triggerCollision(int _eventType, int _damage, int _armorPiercing)
	{
		
	}

	
}
