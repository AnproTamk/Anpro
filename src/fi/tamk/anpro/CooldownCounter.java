package fi.tamk.anpro;

/**
 * Sis�lt�� Hudin painikkeiden p��lle asetettavien cooldown-mittareiden
 * toiminnallisuuden.
 */
public class CooldownCounter extends GuiObject
{
	private int originalCooldown = 0;
	
	/**
	 * Alustaa luokan muuttujat.
	 * 
	 * @param int Objektin X-koordinaatti
	 * @param int Objektin Y-koordinaatti
	 */
	public CooldownCounter(int _x, int _y)
	{
		super(_x, _y);
		
		// M��ritet��n tekstuuri
		usedTexture = GLRenderer.TEXTURE_COOLDOWN;
		
		state = Wrapper.INACTIVE;
	}
	    
    /* =======================================================
     * Uudet funktiot
     * ======================================================= */
	/**
	 * P�ivitt�� n�ytett�v�n tekstuurin.
	 * 
	 * @param int Aseen cooldown
	 */
	public void updateCounter(int _cooldown)
	{
		if (_cooldown > 0) {
			// Tallenna alkuper�inen cooldown, jos se ei ole viel� tallessa
			if (originalCooldown == 0) {
				originalCooldown = _cooldown;
			}
			float temp = ((float)_cooldown / (float)originalCooldown) * 7;
			
			if (temp >= 1) {
				currentFrame = (int)temp;
			}
			else {
				currentFrame = 0;
			}
			
			state = Wrapper.FULL_ACTIVITY;
		}
		else {
			originalCooldown = 0;
			
			state = Wrapper.INACTIVE;
		}
	}

}
