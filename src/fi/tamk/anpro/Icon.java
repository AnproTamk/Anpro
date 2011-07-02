package fi.tamk.anpro;

/**
 * Sis‰lt‰‰ Hudin painikkeiden p‰‰lle asetettavien cooldown-mittareiden
 * toiminnallisuuden.
 */
public class Icon extends GuiObject
{
	/**
	 * Alustaa luokan muuttujat.
	 * 
	 * @param int Objektin X-koordinaatti
	 * @param int Objektin Y-koordinaatti
	 */
	public Icon(int _x, int _y)
	{
		super(_x, _y);
		
		// M‰‰ritet‰‰n aloitustekstuuri
		usedTexture = GLRenderer.TEXTURE_COOLDOWN;
	}
	
	/**
	 * P‰ivitt‰‰ n‰ytett‰v‰n tekstuurin.
	 * 
	 * @param int Aseen cooldown
	 */
	public void updateCooldownIcon(int _cooldown)
	{
		// M‰‰ritet‰‰n k‰ytett‰v‰ tekstuuri (arvot v‰lilt‰ 13-23)
		usedTexture = GLRenderer.TEXTURE_COOLDOWN + (int)((((float)_cooldown / 1000) * 20) + 1);
		
		if (_cooldown <= 0) {
			usedTexture = GLRenderer.TEXTURE_COOLDOWN;
		}
	}

}
