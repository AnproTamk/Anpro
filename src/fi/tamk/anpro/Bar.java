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
		
		// laskee jotain
		
		usedTexture = 0;
	}
}
