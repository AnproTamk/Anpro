package fi.tamk.anpro;

import javax.microedition.khronos.opengles.GL10;

public class Mothership extends GameObject
{
	private Wrapper wrapper;
	
	public Mothership(int _speed)
	{
		super(0);
		
		// Otetaan Wrapper k‰yttˆˆn
		wrapper = Wrapper.getInstance();
		
		// Lis‰t‰‰n emoalus piirtolistalle
		listId = wrapper.addToList(this, Wrapper.CLASS_TYPE_MOTHERSHIP, 0); // T‰rkeydell‰ ei v‰li‰, vakiona aina 2
		
		// M‰‰ritet‰‰n emoaluksen tiedot
		direction = 160;
		x         = 100;
		y         = 90;
	    
        // Haetaan animaatioiden pituudet
        animationLength = new int[GLRenderer.AMOUNT_OF_MOTHERSHIP_ANIMATIONS];
        
        for (int i = 0; i < GLRenderer.AMOUNT_OF_MOTHERSHIP_ANIMATIONS; ++i) {
            if (GLRenderer.mothershipAnimations[i] != null) {
                animationLength[i] = GLRenderer.mothershipAnimations[i].length;
            }
        }
		
        /*// K‰ynnistet‰‰n animaatio
		usedAnimation  = 0;
		animationSpeed = 2;*/
	}
    
    /**
     * K‰sittelee ammuksen tˆrm‰ystarkistukset.
     */
    public final void checkCollision()
    {
    	/* Tarkistetaan osumat pelaajaan */
    	// Tarkistetaan, onko emoaluksen ja pelaajan v‰linen et‰isyys riitt‰v‰n pieni
    	// tarkkoja osumatarkistuksia varten
    	if (Math.abs(wrapper.player.x - x) <= Wrapper.gridSize) {
        	if (Math.abs(wrapper.player.y - y) <= Wrapper.gridSize) {
        
                // Tarkistetaan osuma
        		if (Utility.isColliding(wrapper.player, this)) {
        			
        			// Siirryt‰‰n emoaluksen valikkoon
        			// TODO: Lis‰‰ siirtyminen toiseen activityyn
        		}
        	}
    	}
    }

	@Override
	public void draw(GL10 _gl)
	{
        // Tarkistaa onko animaatio p‰‰ll‰ ja kutsuu oikeaa animaatiota tai tekstuuria
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
