package fi.tamk.anpro;

/**
 * Sis‰lt‰‰ HUDin nappien p‰‰lle asetettavien asekuvakkeiden tiedot ja toiminnot.
 */
public class Icon extends GuiObject
{
	/**
	 * Alustaa luokan muuttujat.
	 * 
	 * @param _x Kuvakkeen X-koordinaatti
	 * @param _y Kuvakkeen Y-koordinaatti
	 */
	public Icon(int _x, int _y)
	{
		super(_x, _y);
		
		usedTexture = GLRenderer.TEXTURE_BUTTON_SELECTED;
	}

    /**
     * Asettaa kuvakkeen valinnan (= k‰ytett‰v‰ tekstuuri) joko p‰‰lle tai pois
     * pelaajan valinnan mukaan.
     * 
     * @param _selected Valinta
     */
	public void setState(boolean _selected)
	{
		if(_selected) {
			usedTexture = GLRenderer.TEXTURE_BUTTON_SELECTED;
		}
		
		else {
			usedTexture = GLRenderer.TEXTURE_BUTTON_SELECTED;
		}
	}
}
	

