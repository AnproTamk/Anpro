package fi.tamk.anpro;

import java.util.ArrayList;

import javax.microedition.khronos.opengles.GL10;

import android.content.Context;

public class Player extends GameObject
{
	public int health;
	public int defence;
	public int spawnPoint;
		
	SurvivalMode survivalMode;
	
	
	// Luokan muuttujien rakentaja.
	public Player(int _health, int _defence)
	{
		super();
		health  = _health;
		defence = _defence;

        animationLengths[0] = GLRenderer.playerAnimations.get(0).length;
        animationLengths[1] = GLRenderer.playerAnimations.get(1).length;
        animationLengths[2] = GLRenderer.playerAnimations.get(2).length;
	}



	public void draw()
	{
		// 1. Tarkistaa onko animaatio päällä.
		// 2. kutsuu animaatioita/tekstuureita
		if (usedAnimation >= 0){
			animations.get(usedAnimation).draw(xf, yf, direction, currentFrame);	
		}
		
		else{
			textures.get(usedTexture).draw(xf, yf, direction);
		}
	}


	public void setDrawables(ArrayList<Animation> _animations, ArrayList<Texture> _textures)
	{
		textures   = _textures;
		animations = _animations;
	}
}

