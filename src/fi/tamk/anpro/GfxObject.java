package fi.tamk.anpro;

import java.lang.reflect.Array;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;

import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLUtils;

public class GfxObject {
    // Objektin sijainti
    public float _xf = 0.0f;
    public float _yf = 0.0f;
    public float _zf = 0.0f;
    
    // Käytössä oleva animaatio ja sen tiedot
    public int usedAnimation = -1;
    private int[] animationLengths;
    
    private int currentFrame   = 0;
    private int currentLoop    = 0;
    private int animationLoops = 0;
    
    // Staattinen tekstuuri
    private int usedTexture = 0;
    
    // Konteksti ja OpenGL-rajapinta
    private Context context;
    private GL10    gl;
    
    /** Rakentaja */
    public GfxObject() {
    	// ...
    }
    
    public void update() {
    	if (animationLoops > 0) {
    		if (currentFrame + 1 > animationLengths[usedAnimation]) {
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
