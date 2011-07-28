package fi.tamk.anpro;

/**
 * Sis‰lt‰‰ HUDin nappien p‰‰lle asetettavien asekuvakkeiden tiedot ja toiminnot.
 */
public class Icon extends GuiObject
{
	private int iconType;
	
	/**
	 * Alustaa luokan muuttujat.
	 * 
	 * @param _x Kuvakkeen X-koordinaatti
	 * @param _y Kuvakkeen Y-koordinaatti
	 * @param _type 
	 */
	public Icon(int _x, int _y, int _type)
	{
		super(_x, _y);
		
		iconType = _type;
		
		// Asetetaan sijainti syvyystopologiassa (0-10, jossa 0 on p‰‰limm‰isen‰ ja 10 alimmaisena)
		z = 1;
		
		// M‰‰ritet‰‰n k‰ytett‰v‰ tekstuuri (ei v‰li‰ mik‰ tekstuuri, sill‰ tekstuuri
		// m‰‰ritet‰‰n uudestaan kun pelaaja ker‰‰ aseita)
		usedTexture = GLRenderer.TEXTURE_LASER_ICON;
	}

	/* =======================================================
	 * Uudet funktiot
	 * ======================================================= */
    /**
     * Asettaa kuvakkeen valinnan (= k‰ytett‰v‰ tekstuuri) joko p‰‰lle tai pois
     * pelaajan valinnan mukaan.
     * 
     * @param _selected Valinta
     */
	public void setState(boolean _selected)
	{
		// TODO:
		if(_selected) {
			if (iconType == 0) {
				usedTexture  = GLRenderer.TEXTURE_LASER_ICON;
			}
			else {
				usedTexture = GLRenderer.TEXTURE_LASER_ICON + Hud.collectedWeapon;
			}
			currentFrame = 1;
		}
		
		else {
			if (iconType == 0) {
				usedTexture  = GLRenderer.TEXTURE_LASER_ICON;
			}
			else {
				usedTexture = GLRenderer.TEXTURE_LASER_ICON + Hud.collectedWeapon;
			}
			currentFrame = 0;
		}
	}
}
	

