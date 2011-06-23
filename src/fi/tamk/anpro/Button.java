package fi.tamk.anpro;

/**
 * Sisältää käyttöliittymän napin toiminnallisuudet.
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
            usedTexture = GLRenderer.TEXTURE_BUTTON_SELECTED;
        }
        else {
            usedTexture = GLRenderer.TEXTURE_BUTTON_NOTSELECTED;
        }
    }

}
