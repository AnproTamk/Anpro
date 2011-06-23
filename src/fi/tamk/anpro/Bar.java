package fi.tamk.anpro;

/**
 * Sisältää käyttöliittymän eri palkkien toiminnallisuudet.
 * 
 * @extends GuiObject
 */
public class Bar extends GuiObject {
	
	public Bar(int _x, int _y)
    {
        super(_x, _y);
        
        usedTexture = GLRenderer.TEXTURE_HEALTH;
    }
}
