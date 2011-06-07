package fi.tamk.anpro;

import java.util.ArrayList;

import javax.microedition.khronos.opengles.GL10;

public class Player extends GameObject
{
    public int health;
    public int defence;
    public int spawnPoint;
        
    SurvivalMode survivalMode;
    
    Wrapper wrapper;
    int     listId;
    
    // Luokan muuttujien rakentaja.
    public Player(int _health, int _defence)
    {
        super();
        health  = _health;
        defence = _defence;
        
        /*animationLength[0] = wrapper.renderer.playerAnimations.get(0).length;
        animationLength[1] = wrapper.renderer.playerAnimations.get(1).length;
        animationLength[2] = wrapper.renderer.playerAnimations.get(2).length;*/
        
        wrapper = Wrapper.getInstance();
        
        listId = wrapper.addToList(this);
    }


    // Funktio vihollisen "aktiivisuuden" toteuttamiseen.
    public void setActive()
    {
        if (health > 0) {
            wrapper.playerStates.set(listId, 1);
        }
    }

    // Funktio vihollisen "epäaktiivisuuden" toteuttamiseen.
    public void setUnactive()
    {
        if (health == 0) {
            wrapper.playerStates.set(listId, 1);
        }
    }

    public void draw(GL10 _gl)
    {
        if (usedAnimation >= 0){
            //animations.get(usedAnimation).draw(_gl, x, y, direction, currentFrame);	
        }
        else{
            textures.get(usedTexture).draw(_gl, x, y, direction);
        }
    }


    public void setDrawables(ArrayList<Animation> _animations, ArrayList<Texture> _textures)
    {
        animations = _animations;
        textures   = _textures;
    }

	public void triggerImpact(int _damage, int _armorPiercing) {
		// TODO Auto-generated method stub
		
	}
	
	public void triggerCollision(int _eventType, int _damage, int _armorPiercing) {
		// TODO Auto-generated method stub
		
	}
}

