package fi.tamk.anpro;

import javax.microedition.khronos.opengles.GL10;

/**
 * Sisältää yhden käyttöliittymäobjektin tiedot ja osan sen toiminnallisuudesta
 * (Hud hoitaa loput).
 * 
 * @extends GfxObject
 */
public class GuiObject extends GfxObject
{
    /* Objektin tiedot*/
    private int type;
    
    /* Osoitin Wrapperiin */
    private Wrapper wrapper;
    
    /* Objektin tunnus piirtolistalla */
    private int listId;
    
    /**
     * Alustaa luokan muuttujat.
     */
    public GuiObject(int _x, int _y, String _type)
    {
        x = _x;
        y = _y;
        
        if (_type.equals("weapon")) {
            type = 0;
        }
        else {
            type = 1;
        }
        
        wrapper = Wrapper.getInstance();

        listId = wrapper.addToList(this, Wrapper.CLASS_TYPE_GUI, 1);
    }

    /**
     * Piirtää käytössä olevan tekstuurin ruudulle.
     * 
     * @param GL10 OpenGL-konteksti
     */
    public final void draw(GL10 _gl)
    {
        GLRenderer.hudTextures[type][usedTexture].draw(_gl, x, y, 0);
    }
}
