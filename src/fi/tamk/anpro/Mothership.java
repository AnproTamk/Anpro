package fi.tamk.anpro;

import javax.microedition.khronos.opengles.GL10;

public class Mothership extends GameObject
{
	private Wrapper wrapper;
	
	public Mothership(int _speed)
	{
		super(0);
		
		// Otetaan Wrapper käyttöön
		wrapper = Wrapper.getInstance();
		
		// Lisätään emoalus piirtolistalle
		listId = wrapper.addToList(this, Wrapper.CLASS_TYPE_MOTHERSHIP, 0); // Tärkeydellä ei väliä, vakiona aina 2
		

	    
        // Haetaan animaatioiden pituudet
        animationLength = new int[GLRenderer.AMOUNT_OF_MOTHERSHIP_ANIMATIONS];
        
        for (int i = 0; i < GLRenderer.AMOUNT_OF_MOTHERSHIP_ANIMATIONS; ++i) {
            if (GLRenderer.mothershipAnimations[i] != null) {
                animationLength[i] = GLRenderer.mothershipAnimations[i].length;
            }
        }
		
        /*// Käynnistetään animaatio
		usedAnimation  = 0;
		animationSpeed = 2;*/
	}

	@Override
	public void draw(GL10 _gl)
	{
        // Tarkistaa onko animaatio päällä ja kutsuu oikeaa animaatiota tai tekstuuria
        if (usedAnimation >= 0){
            GLRenderer.mothershipAnimations[usedAnimation].draw(_gl, x, y, direction, currentFrame);
        }
        else{
            GLRenderer.mothershipTextures[usedTexture].draw(_gl, x, y, direction, 0);
        }
	}

	@Override
	protected void triggerEndOfAction()
	{
		// ...
	}

}
