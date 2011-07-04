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
	public Bar(int _x, int _y)
    {
        super(_x, _y);
        
        // Määritetään aloitustekstuuri
        usedTexture = GLRenderer.TEXTURE_HEALTH;
    }

	/**
	 * Tallentaa palkin maksimiarvon.
	 * 
	 * @param int Palkin maksimiarvo
	 */
	public void initHealthBar(int _max)
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
		usedTexture = GLRenderer.TEXTURE_HEALTH + (int)((1 - (float)_value / (float)max) * 10);
		
		if (usedTexture > 13) {
			usedTexture = 13;
		}
	}
}
