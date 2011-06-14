package fi.tamk.anpro;

import javax.microedition.khronos.opengles.GL10;

public class Player extends GameObject
{
    public int health;
    public int defence;
    public int spawnPoint;
    
    Wrapper wrapper;
    
    // Luokan muuttujien rakentaja.
    public Player(int _health, int _defence)
    {
        super();
        
        health  = _health;
        defence = _defence;
        
        collisionRadius = 25;
        
        /*animationLength[0] = GLRenderer.playerAnimations.get(0).length;
        animationLength[1] = GLRenderer.playerAnimations.get(1).length;
        animationLength[2] = GLRenderer.playerAnimations.get(2).length;*/
        
        wrapper = Wrapper.getInstance();
        
        wrapper.addToList(this);
    }


    // Funktio vihollisen "aktiivisuuden" toteuttamiseen.
    public void setActive()
    {
    	wrapper.playerState = 0;
    }

    // Funktio vihollisen "ep‰aktiivisuuden" toteuttamiseen.
    public void setUnactive()
    {
    	wrapper.playerState = 1;
    }

    public void draw(GL10 _gl)
    {
        if (usedAnimation >= 0){
        	GLRenderer.playerAnimations.get(usedAnimation).draw(_gl, x, y, direction, currentFrame);
            //animations.get(usedAnimation).draw(_gl, x, y, direction, currentFrame);
        }
        else{
        	GLRenderer.playerTextures.get(usedTexture).draw(_gl, x, y, direction);
            //textures.get(usedTexture).draw(_gl, x, y, direction);
        }
    }

	public void triggerImpact(int _damage) {
		// R‰j‰hdykset eiv‰t vaikuta pelaajaan
	}
	
	public void triggerCollision(int _eventType, int _damage, int _armorPiercing) {
		// Osumat playeriin k‰sitell‰‰n Enemy-luokassa
	}
}

