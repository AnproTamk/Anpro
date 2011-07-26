package fi.tamk.anpro;

/**
 * Sis‰lt‰‰ Hudin painikkeiden p‰‰lle asetettavien cooldown-mittareiden
 * toiminnallisuuden.
 */
public class CooldownCounter extends GuiObject
{
	private int originalCooldown;
	
	/**
	 * Alustaa luokan muuttujat.
	 * 
	 * @param int Objektin X-koordinaatti
	 * @param int Objektin Y-koordinaatti
	 */
	public CooldownCounter(int _x, int _y)
	{
		super(_x, _y);
		
		// M‰‰ritet‰‰n aloitustekstuuri
		usedTexture = GLRenderer.TEXTURE_COOLDOWN;
	}
	    
    /* =======================================================
     * Uudet funktiot
     * ======================================================= */
	/**
	 * P‰ivitt‰‰ n‰ytett‰v‰n tekstuurin.
	 * 
	 * @param int Aseen cooldown
	 */
	public void updateCounter(int _cooldown)
	{
		// Tallenna alkuper‰inen cooldown, jos se ei ole viel‰ tallessa
		
		if (originalCooldown <= 0 && _cooldown > 0) {
			originalCooldown = _cooldown;
		}
		else if (originalCooldown > 0) {
			// M‰‰ritet‰‰n k‰ytett‰v‰ tekstuuri (arvot v‰lilt‰ 14-23)
			currentFrame = 9 - (int)((1 - ((float)_cooldown / (float)originalCooldown)) * 9); // TODO: K‰yt‰ ruutuja, ‰l‰ vaihda tekstuuria!
		}
		if (_cooldown <= 0) {
			usedTexture = GLRenderer.TEXTURE_COOLDOWN;
			originalCooldown = 0;
		}
	}

}
