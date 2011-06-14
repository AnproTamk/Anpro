package fi.tamk.anpro;

import java.util.ArrayList;

public class GfxObject {
	// Animaatiot ja tekstuurit
	//public ArrayList<Animation> animations;
	//public ArrayList<Texture>   textures;
	
    // Objektin sijainti
    public float x = 0.0f;
    public float y = 0.0f;
    public float z = 0.0f;
    
    // Käytössä oleva animaatio ja sen tiedot
    public int   usedAnimation = -1;
    public int[] animationLength;
    
    public  int currentFrame   = 0;
    private int currentLoop    = 0;
    private int animationLoops = 0;
    
    // Staattinen tekstuuri
    public int usedTexture = 0;
    
    /*
     * Rakentaja
     */
    public GfxObject() {
    	// ...
    }
    
    /*
     * Käynnistää animaation
     */
    public void startAnimation(int _animation, int _loops) {
    	usedAnimation  = _animation;
    	animationLoops = _loops;
		currentFrame   = 0;
    	
    	if (_loops > 0) {
    		currentLoop = 1;
    	}
    	else {
    		currentLoop = 0;
    	}
    }
    
    /*
     * Lopettaa animaation
     */
    public void stopAnimation(int _texture) {
    	usedAnimation = -1;
    	usedTexture   = _texture;
    }
    
    /*
     * Päivittää animaation
     */
    public void update() {
    	if (usedAnimation > -1) {
	    	if (animationLoops > 0) {
	    		if (currentFrame + 1 > animationLength[usedAnimation]) {
	    			currentFrame = 0;
	    			++currentLoop;
	    			if (currentLoop > animationLoops) {
	    				usedAnimation = -1;
	    				usedTexture   = 0;
	    			}
	    		}
	    		else {
	    			++currentFrame;
	    		}
	    	}
	    	else {
	    		if (currentFrame + 1 > animationLength[usedAnimation]) {
	    			currentFrame = 0;
	    		}
	    		else {
	    			++currentFrame;
	    		}
	    	}
    	}
    }
}
