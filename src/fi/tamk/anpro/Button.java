package fi.tamk.anpro;

/**
 * Sis‰lt‰‰ k‰yttˆliittym‰n napin toiminnallisuudet.
 * 
 * @extends GuiObject
 */
public class Button extends GuiObject
{

    /**
     * Alustaa luokan muuttujat.
     * 
     * @param int Objektin X-koordinaatti
     * @param int Objektin Y-koordinaatti
     */
    public Button(int _x, int _y)
    {
        super(_x, _y);
        
        usedTexture = GLRenderer.TEXTURE_BUTTON_NOTSELECTED;
    }

    /**
     * Asettaa napin valituksi tai poistaa valinnan.
     * 
     * @param boolean Valinta
     */
    public void setSelected(boolean _selected)
    {
        if (_selected) {
            setAction(GLRenderer.ANIMATION_CLICK, 1, 1, 1);
        }
        else {
            usedTexture = GLRenderer.TEXTURE_BUTTON_NOTSELECTED;
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

    	if (actionId == 1) {
    		usedTexture = GLRenderer.TEXTURE_BUTTON_SELECTED;
    	}
    }

}
