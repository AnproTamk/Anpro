package fi.tamk.anpro;

/**
 * Sisältää käyttöliittymän eri palkkien toiminnallisuudet.
 */
public class Bar extends GuiObject
{
	// Palkin maksimiarvo
	private int max;
	
	/**
	 * Alustaa luokan muuttujat.
	 * 
	 * @param int Objektin X-koordinaatti
	 * @param int Objektin Y-koordinaatti
	 */
	public Bar(int _x, int _y, int _type)
    {
        super(_x, _y);
        
        // Määritetään aloitustekstuuri
        // TODO: Vakiot tyypeille
        if (_type == 1) {
        	usedTexture = GLRenderer.TEXTURE_HEALTH;
        }
        else if (_type == 2) {
        	usedTexture = GLRenderer.TEXTURE_ARMOR;
        }
    }
    
    /* =======================================================
     * Uudet funktiot
     * ======================================================= */
	/**
	 * Tallentaa palkin maksimiarvon.
	 * 
	 * @param int Palkin maksimiarvo
	 */
	public void initBar(int _max)
	{
		max = _max;
	}

	/**
	 * Päivittää palkin käytössä olevan tekstuurin.
	 * 
	 * @param int Palkin uusi arvo
	 */
	public void updateValue(int _value)
	{
		if (_value >= max) {
			currentFrame = 0;
		}
		else {
			currentFrame = (int)((1 - (float)_value / (float)max) * 10);
			if (currentFrame > 10) {
				currentFrame = 10;
			}
		}
	}
}
