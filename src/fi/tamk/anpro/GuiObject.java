package fi.tamk.anpro;

import javax.microedition.khronos.opengles.GL10;

/**
 * Sis�lt�� yhden k�ytt�liittym�objektin tiedot ja osan sen toiminnallisuudesta
 * (Hud hoitaa loput).
 * 
 * @extends GfxObject
 */
public class GuiObject extends GfxObject
{
	/* Osoitin Wrapperiin */
    private Wrapper wrapper;
    
    /* Objektin tunnus piirtolistalla */
    private int listId;
    
    /**
     * Alustaa luokan muuttujat.
     */
    public GuiObject()
    {
        wrapper = Wrapper.getInstance();
        
        listId = wrapper.addToList(this, Wrapper.CLASS_TYPE_GUI);
    }

    /**
     * Piirt�� k�yt�ss� olevan tekstuurin ruudulle.
     * 
     * @param GL10 OpenGL-konteksti
     */
    public final void draw(GL10 _gl)
    {
        GLRenderer.hudTextures.get(usedTexture).draw(_gl, x, y, 0);
    }
}
