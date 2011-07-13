package fi.tamk.anpro;

/**
 * Sis�lt�� k�ytt�liittym�n eri palkkien toiminnallisuudet.
 */
public class Bar extends GuiObject
{
	// Palkin maksimiarvo
	private int max;
	private int type;
	
	/**
	 * Alustaa luokan muuttujat.
	 * 
	 * @param int Objektin X-koordinaatti
	 * @param int Objektin Y-koordinaatti
	 */
	public Bar(int _x, int _y, int _type)
    {
        super(_x, _y);
        type = _type;
        
        // M��ritet��n aloitustekstuuri
        if (type == 1) {
        	usedTexture = GLRenderer.TEXTURE_HEALTH;
        }
        else if (type == 2) {
        	usedTexture = GLRenderer.TEXTURE_ARMOR;
        }
    }

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
	 * P�ivitt�� palkin k�yt�ss� olevan tekstuurin.
	 * 
	 * @param int Palkin uusi arvo
	 */
	public void updateValue(int _value)
	{
		// Lasketaan, paljonko pelaajalla on on healthia j�ljell�, mink� mukaan
		// piirret��n oikea healthBar-kuva ruudulle.
		if (type == 1) {
			usedTexture = GLRenderer.TEXTURE_HEALTH + (int)((1 - (float)_value / (float)max) * 10);
			
			if (usedTexture > GLRenderer.TEXTURE_HEALTH + 10) {
				usedTexture = GLRenderer.TEXTURE_HEALTH + 10;
			}
		}
		else if (type == 2) {
			usedTexture = GLRenderer.TEXTURE_ARMOR + (int)((1- (float)_value / (float)max) * 10);
			
			if (usedTexture > GLRenderer.TEXTURE_ARMOR + 10) {
				usedTexture = GLRenderer.TEXTURE_ARMOR + 10;
			}
		}
	}
}
