package fi.tamk.anpro;

import javax.microedition.khronos.opengles.GL10;

import android.content.Context;

/**
 * Perii GLSpriten toiminnot, mutta on erikoistunut pelk‰st‰‰n tekstuureihin.
 */
public class Texture extends GLSprite
{
    /**
     * Alustaa luokan muuttujat.
     * 
     * @param _gl      OpenGL-konteksti
     * @param _context Prosessin konteksti
     * @param _id      Tekstuurin resurssitunnus
     */
    public Texture(GL10 _gl, Context _context, int _id)
    {
    	super();
    	
    	if (!GLRenderer.loadingFailed) {
	    	// Alustetaan kuva ja generoidaan tunnukset
	        sprites = new int[1];
	        _gl.glGenTextures(1, sprites, 0);
	        
	        // Luodaan tyhj‰ bitmap ja ladataan siihen tekstuuri resursseista
	        if (loadBitmap(_context, _id, _gl, 0)) {
	            // M‰‰ritet‰‰n tekstuurin vektorit
	            createVertices();
	            
	            // Luodaan vektoribufferit
	            createBuffers();
	        }        	
	        else {
	            GLRenderer.loadingFailed = true; 
	        }
    	}
    }
}
