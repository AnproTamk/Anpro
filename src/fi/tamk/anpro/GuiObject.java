package fi.tamk.anpro;

import javax.microedition.khronos.opengles.GL10;

/**
 * Sisältää yhden käyttöliittymäobjektin tiedot ja osan sen toiminnallisuudesta
 * (Hud hoitaa loput).
 * 
 * @extends GfxObject
 */
abstract public class GuiObject extends GfxObject
{
    /* Objektin tiedot*/
    private int type;
    
    /* Osoitin Wrapperiin */
    private Wrapper wrapper;
    
    /* Objektin tunnus piirtolistalla */
    private int listId;
    
    /**
     * Alustaa luokan muuttujat.
     * 
     * @param int Objektin X-koordinaatti
     * @param int Objektin Y-koordinaatti
     */
    public GuiObject(int _x, int _y)
    {
        x = _x;
        y = _y;
        
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

    /**
     * Käsittelee jonkin toiminnon päättymisen. Kutsutaan animaation loputtua, mikäli
     * actionActivated on TRUE.
     * 
     * (lue lisää GfxObject-luokasta!)
     */
    @Override
    protected void triggerEndOfAction()
    {
        // TODO:
    }
}
