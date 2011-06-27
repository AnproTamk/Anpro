package fi.tamk.anpro;

/**
 * Sisältää käyttöliittymän eri palkkien toiminnallisuudet.
 * 
 * @extends GuiObject
 */
public class Bar extends GuiObject
{
	private int max;
	private int value;
	
	public Bar(int _x, int _y)
    {
        super(_x, _y);
        
        usedTexture = GLRenderer.TEXTURE_HEALTH;
    }

	public void initHealthBar(int _max)
	{
		max = _max;
	}

	public void updateValue(int _value)
	{
		value = _value;
		
		// Lasketaan, paljonko pelaajalla on on healthia jäljellä, minkä mukaan
		// piirretään oikea healthBar-kuva ruudulle.
		/*for (int i = max; i >= 0; --i) {
			if (value == max - i)
				
		}*/
		
		usedTexture = GLRenderer.TEXTURE_HEALTH + (int)((1 - (float)value / (float)max) * 10);
		
		if (usedTexture > 13)
			usedTexture = 13;
	}
}
