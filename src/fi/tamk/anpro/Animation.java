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
     * @param GL10      OpenGL-konteksti
     * @param Context   Ohjelman konteksti
     * @param Resources Ohjelman resurssit
     * @param String    Tekstuurin tunnus
     * @paran int       Animaation pituus
     */
    public Animation(GL10 _gl, Context _context, Resources _resources, String _id, int _length)
    {
    	// Alustetaan kuva ja generoidaan tunnukset
        sprites = new int[_length];
        length  = _length - 1;
        _gl.glGenTextures(_length, sprites, 0);

        // Ladataan tekstuurit
        for (int i = 0; i < _length; ++i) {
        	loadBitmap(_context, _resources.getIdentifier(_id+"_anim_"+i, "drawable", "fi.tamk.anpro"), _gl, i);
        }
        
        // M‰‰ritet‰‰n tekstuurin vektorit
        createVertices();
        
        // Luodaan vektoribufferit
        createBuffers();
    }
}
