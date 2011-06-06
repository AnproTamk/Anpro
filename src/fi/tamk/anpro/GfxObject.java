package fi.tamk.anpro;

import java.util.ArrayList;

import javax.microedition.khronos.opengles.GL10;

import android.content.Context;

public class GfxObject {
	// Animaatiot ja tekstuurit
	public ArrayList<Animation> animations;
	public ArrayList<Texture>   textures;
	
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
    
    // Konteksti ja OpenGL-rajapinta
    private Context context;
    private GL10    gl;
    
    /** Rakentaja */
    public GfxObject() {
    	// ...
    }
    
    public void update() {
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
    		
    	}
    }
}
