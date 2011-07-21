package fi.tamk.anpro;

import javax.microedition.khronos.opengles.GL10;

/**
 * Sis‰lt‰‰ pistelaskurin toiminnallisuudet.
 */
public class Counter extends GuiObject
{
	private int[] frames = {-1, -1, -1, -1, -1, -1};
	private int   tempX;

	/**
     * Alustaa luokan muuttujat.
     * 
     * @param _x Objektin X-koordinaatti
     * @param _y Objektin Y-koordinaatti
     */
	public Counter(int _x, int _y) {
		super(_x, _y);
		
		// M‰‰ritet‰‰n objektin tila
		state = Wrapper.INACTIVE;
		
		// M‰‰ritet‰‰n k‰ytett‰v‰ tekstuuri
		usedTexture = GLRenderer.TEXTURE_COUNTER;
	}

	/* =======================================================
	* Perityt funktiot
	* ======================================================= */
	@Override
	public void draw(GL10 _gl)
	{
		for (int i = 0; i < 6; ++i) {
			if (frames[i] > -1) {
				GLRenderer.hudTextures[usedTexture].draw(_gl, (tempX+30*i) + CameraManager.xTranslate,  y + CameraManager.yTranslate, direction, frames[i]);
			}
		}
	}
	
	/* =======================================================
	* Uudet funktiot
	* ======================================================= */
	/**
	 * Palastelee pisteet yksitt‰isiksi numeroiksi, joiden avulla
	 * piirret‰‰n yksitt‰iset numerot ruudulle pisteiden muodostamiseksi.
	 * 
	 * @param _score Pisteet
	 */
	public void parseScore(long _score)
	{
		state = Wrapper.FULL_ACTIVITY;
		
		String temp = String.valueOf(_score);
		
		tempX = temp.length();
		
		for (int i = 0; i < tempX; ++i) {
			frames[i] = Integer.parseInt(String.valueOf(temp.charAt(i)));
		}
		
		tempX -= tempX * 13;
	}
}
