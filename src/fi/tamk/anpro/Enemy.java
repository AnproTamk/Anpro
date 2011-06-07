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
    
    Wrapper wrapper;
    int     listId;
    
    // Luokan muuttujien rakentaja.
    public Enemy(int _health, int _defence, int _speed, int _attack, int _rank)
    {
        super();
        attack   = _attack;
        speed    = _speed;
        defence  = _defence;
        health   = _health;
        rank     = _rank;
    
        animationLength = new int[3];

        int offset = rank *3;

        /*animationLength[0] = wrapper.renderer.playerAnimations.get(offset - 3).length;
        animationLength[1] = wrapper.renderer.playerAnimations.get(offset - 2).length;
        animationLength[2] = wrapper.renderer.playerAnimations.get(offset - 1).length;*/
        
        wrapper = Wrapper.getInstance();
        
        listId = wrapper.addToList(this);
		
		// TODO: Ota teko‰ly k‰yttˆˆn
    }

	// Funktio vihollisen "aktiivisuuden" toteuttamiseen.
	public void setActive()
	{
		wrapper.enemyStates.set(listId, 1);
		
		// TODO: Ota teko‰ly k‰yttˆˆn
	}

	// Funktio vihollisen "ep‰aktiivisuuden" toteuttamiseen.
	public void setUnactive()
	{
		wrapper.enemyStates.set(listId, 0);
		
		// TODO: Poista teko‰ly k‰ytˆst‰
	}
	
	public void draw(GL10 _gl)
	{
		// 1. Tarkistaa onko animaatio p‰‰ll‰.
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
	
	// K‰sitell‰‰n r‰j‰hdyksien aiheuttamat osumat
	public void triggerImpact(int _damage)
	{
		health -= (int)((float)_damage * (1 - 0.15 * (float)defence));
		
		if (health <= 0) {
			setUnactive();
		}
	}
	
	// K‰sitell‰‰n tˆrm‰ykset
	public void triggerCollision(int _eventType, int _damage, int _armorPiercing)
	{
		if (_eventType == GameObject.COLLISION_WITH_PROJECTILE) {
			health -= (int)((float)_damage * (1 - 0.15 * (float)defence + 0.1 * (float)_armorPiercing));
			
			if (health <= 0) {
				setUnactive();
			}
		}
		else if (_eventType == GameObject.COLLISION_WITH_PLAYER) {
			wrapper.players.get(0).health -= attack * 3;
			setUnactive();
		}
	}
}
