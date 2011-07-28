package fi.tamk.anpro;

/**
 * Sisältää Hudin painikkeiden päälle asetettavien cooldown-mittareiden
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
		
		// Määritetään tekstuuri
		usedTexture = GLRenderer.TEXTURE_COOLDOWN;
		
		state = Wrapper.INACTIVE;
	}
	    
    /* =======================================================
     * Uudet funktiot
     * ======================================================= */
	/**
	 * Päivittää näytettävän tekstuurin.
	 * 
	 * @param int Aseen cooldown
	 */
	public void updateCounter(int _cooldown)
	{
		if (_cooldown > 0) {
			// Tallenna alkuperäinen cooldown, jos se ei ole vielä tallessa
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
