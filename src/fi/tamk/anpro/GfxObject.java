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
     * Päivittää animaation
     */
    public void update() {
    	if (usedAnimation > -1) {
	    	if (animationLoops > 0) {
	    		if (currentFrame + 1 > animationLength[usedAnimation]) {
	    			currentFrame = 0;
	    			++currentLoop;
	    		}
	    		else {
	    			++currentFrame;
	    		}
	    	}
	    	else {
	    		if (currentFrame + 1 > animationLength[usedAnimation]) {
	    			
	    		}
	    	}
    	}
    }
}
