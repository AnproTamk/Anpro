package fi.tamk.anpro;

import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.content.res.Resources;

/**
 * Perii GLSpriten toiminnot, mutta on erikoistunut pelk‰st‰‰n animaatioihin.
 */
public class Animation extends GLSprite
{
	// Animaation pituus
    public  int   length;

    /**
     * Alustaa luokan muuttujat ja kutsuu loadFramea jokaista ruutua varten.
     * 
     * @param _gl        OpenGL-konteksti
     * @param _context   Ohjelman konteksti
     * @param _resources Ohjelman resurssit
     * @param _id        Tekstuurin tunnus
     * @param _length    Animaation pituus
     */
    public Animation(GL10 _gl, Context _context, Resources _resources, String _id, int _length)
    {
    	if (!GLRenderer.loadingFailed) {
	    	// Alustetaan kuva ja generoidaan tunnukset
	        sprites = new int[_length];
	        length  = _length - 1;
	        _gl.glGenTextures(_length, sprites, 0);
	
	        // Ladataan tekstuurit
	        for (int i = 0; i < _length; ++i) {
	        	if (!loadBitmap(_context, _resources.getIdentifier(_id+"_anim_"+i, "drawable", "fi.tamk.anpro"), _gl, i)) {
	                GLRenderer.loadingFailed = true;
	        		break;
	        	}
	        }
	        
	        if (!GLRenderer.loadingFailed) {
	            // M‰‰ritet‰‰n tekstuurin vektorit
	            createVertices();
	            
	            // Luodaan vektoribufferit
	            createBuffers();
	        }
    	}
    }
}
