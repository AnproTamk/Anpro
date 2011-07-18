package fi.tamk.anpro;

import javax.microedition.khronos.opengles.GL10;

/**
 * Sisältää pistelaskurin toiminnallisuudet.
 */
public class Counter extends GuiObject
{
	private int[] frames = {-1, -1, -1, -1, -1, -1};
	private int   tempX;

	/**
     * Alustaa luokan muuttujat.
     * 
     * @param int Objektin X-koordinaatti
     * @param int Objektin Y-koordinaatti
     * @param int Laskettavat arvot
     */
	public Counter(int _x, int _y) {
		super(_x, _y);
		
		usedTexture = GLRenderer.TEXTURE_COUNTER;
		
		wrapper.guiObjectStates.set(listId, Wrapper.INACTIVE);
	}
	
	/**
	 * Palastelee pisteet yksittäisiksi numeroiksi, joiden avulla
	 * piirretään yksittäiset numerot ruudulle pisteiden muodostamiseksi.
	 * 
	 * @param int Pisteet
	 */
	public void parseScore(long _score)
	{
		wrapper.guiObjectStates.set(listId, Wrapper.FULL_ACTIVITY);
		
		String temp = String.valueOf(_score);
		
		tempX = temp.length();
		
		for (int i = 0; i < tempX; ++i) {
			frames[i] = Integer.parseInt(String.valueOf(temp.charAt(i)));
		}
		
		tempX -= tempX * 13;
	}
	
	@Override
	public void draw(GL10 _gl)
	{
		for (int i = 0; i < 6; ++i) {
			if (frames[i] > -1) {
				GLRenderer.hudTextures[usedTexture].draw(_gl, (tempX+30*i) + cameraManager.xTranslate,  y + cameraManager.yTranslate, direction, frames[i]);
			}
		}
	}
}
