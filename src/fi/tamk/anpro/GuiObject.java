package fi.tamk.anpro;

import javax.microedition.khronos.opengles.GL10;

/**
 * Sis‰lt‰‰ yhden k‰yttˆliittym‰objektin tiedot ja osan sen toiminnallisuudesta
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
    
        /* Haetaan animaatioiden pituudet */
        for (int i = 0; i < 2; ++i) {
            if (GLRenderer.hudAnimations[0][i] != null) {
                animationLength[i] = GLRenderer.hudAnimations[0][i].length;
            }
        }
    }

    /**
     * Piirt‰‰ k‰ytˆss‰ olevan tekstuurin ruudulle.
     * 
     * @param GL10 OpenGL-konteksti
     */
    public final void draw(GL10 _gl)
    {
        // Tarkistaa onko animaatio p‰‰ll‰ ja kutsuu oikeaa animaatiota tai tekstuuria
        if (usedAnimation >= 0){
            GLRenderer.hudAnimations[0][usedAnimation].draw(_gl, x, y, 90, currentFrame);
        }
        else{
        	GLRenderer.hudTextures[0][usedTexture].draw(_gl, x, y, 90);
        }
        
    }

    /**
     * K‰sittelee jonkin toiminnon p‰‰ttymisen. Kutsutaan animaation loputtua, mik‰li
     * actionActivated on TRUE.
     * 
     * (lue lis‰‰ GfxObject-luokasta!)
     */
    @Override
    protected void triggerEndOfAction()
    {
        // TODO:
    }
}
