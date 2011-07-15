package fi.tamk.anpro;

/**
 * Sisältää käyttöliittymän eri palkkien toiminnallisuudet.
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
        
        // Määritetään aloitustekstuuri
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
	 * Päivittää palkin käytössä olevan tekstuurin.
	 * 
	 * @param int Palkin uusi arvo
	 */
	public void updateValue(int _value)
	{
		// Lasketaan, paljonko pelaajalla on on healthia jäljellä, minkä mukaan
		// piirretään oikea healthBar-kuva ruudulle.
		if (type == 1) {
			currentFrame = (int)((1 - (float)_value / (float)max) * 10);
			
			if (currentFrame > 10) {
				currentFrame = 10; // TODO: Käytä ruutuja, älä vaihda tekstuuria!
			}
		}
		else if (type == 2) {
			currentFrame = (int)((1- (float)_value / (float)max) * 10);
			
			if (currentFrame > 10) {
				currentFrame = 10; // TODO: Käytä ruutuja, älä vaihda tekstuuria!
			}
		}
	}
}
