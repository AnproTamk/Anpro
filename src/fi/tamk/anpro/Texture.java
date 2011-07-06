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
     * @param GL10    OpenGL-konteksti
     * @param Context Prosessin konteksti
     * @param int     Tekstuurin resurssitunnus
     */
    public Texture(GL10 _gl, Context _context, int _id)
    {
    	// Alustetaan kuva ja generoidaan tunnukset
        sprites = new int[1];
        _gl.glGenTextures(1, sprites, 0);
        
        // Luodaan tyhj‰ bitmap ja ladataan siihen tekstuuri resursseista
        loadBitmap(_context, _id, _gl, 0);
        
        // M‰‰ritet‰‰n tekstuurin vektorit
        createVertices();
        
        // Luodaan vektoribufferit
        createBuffers();
    }
}
