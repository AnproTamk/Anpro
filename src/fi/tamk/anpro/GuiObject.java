package fi.tamk.anpro;

import javax.microedition.khronos.opengles.GL10;

public class GuiObject extends GfxObject {
    private Wrapper wrapper;
    
    private int listId;
    
    /*
     * Rakentaja
     */
    public GuiObject() {
        wrapper = Wrapper.getInstance();
        
        listId = wrapper.addToList(this, Wrapper.CLASS_TYPE_GUI);
    }

    /*
     * Piirtää käytössä olevan tekstuurin ruudulle
     */
    public void draw(GL10 _gl)
    {
        GLRenderer.hudTextures.get(usedTexture).draw(_gl, x, y, 0);
    }
    
    /*
     * Asettaa luokan tekstuurit
     */
    /*public void setDrawables(StringTexture _texture)
    {
        stringTexture = _texture;
    }*/
}
