package fi.tamk.anpro;

/**
 * Sis‰lt‰‰ Hudin napin toiminnallisuudet.
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
        
        // M‰‰ritet‰‰n aloitustekstuuri
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
     */
    @Override
    protected void triggerEndOfAction()
    {
    	if (actionId == 1) {
    		usedTexture = GLRenderer.TEXTURE_BUTTON_SELECTED;
    	}
    }
}
