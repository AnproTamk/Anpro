package fi.tamk.anpro;

import javax.microedition.khronos.opengles.GL10;

import android.util.Log;

/**
 * Sis�lt�� yhden taustalle generoitavan t�hden tiedot ja toiminnot. N�it� t�hti�
 * generoidaan taustalle pelaajan liikkuessa.
 */
public class BackgroundStar extends GfxObject
{
	/**
	 * Alustaa luokan muuttujat.
	 * 
	 * @param _x T�hden X-koordinaatti
	 * @param _y T�hden Y-koordinaatti
	 */
	public BackgroundStar(int _x, int _y, Wrapper _wrapper)
	{
		/* Tallennetaan muuttujat */
		x = _x;
		y = _y;
		
		/* Alustetaan muuttujat */
		z = 9;

        /* M��ritet��n objektin tila (piirtolista) */
		_wrapper.addToDrawables(this);
	}

    /* =======================================================
     * Perityt funktiot
     * ======================================================= */
	/**
	 * Tarkistetaan t�hden sijainti ja siirret��n, mik�li se on ulkona kuvasta.
	 */
	public void updatePosition()
	{
		int tempWidth  = (int)((float)Options.screenWidth / 2);
		int tempHeight = (int)((float)Options.screenHeight / 2);
		
		if (x < CameraManager.xTranslate - tempWidth - Options.scaledScreenWidth) {
			x = Utility.getRandom((int)CameraManager.xTranslate + tempWidth + 10,
								  (int)CameraManager.xTranslate + tempWidth + Options.scaledScreenWidth - 30);
			y = Utility.getRandom((int)CameraManager.yTranslate - tempHeight - Options.scaledScreenHeight + 20,
		  			  			  (int)CameraManager.yTranslate + tempHeight + Options.scaledScreenHeight - 20);
		}
		else if (x > CameraManager.xTranslate + tempWidth + Options.scaledScreenWidth) {
			x = Utility.getRandom((int)CameraManager.xTranslate - tempWidth - Options.scaledScreenWidth + 30,
								  (int)CameraManager.xTranslate - tempWidth - 10);
			y = Utility.getRandom((int)CameraManager.yTranslate - tempHeight - Options.scaledScreenHeight + 20,
		  			  			  (int)CameraManager.yTranslate + tempHeight + Options.scaledScreenHeight - 20);
		}
		else if (y < CameraManager.yTranslate - tempHeight - Options.scaledScreenHeight) {
			x = Utility.getRandom((int)CameraManager.xTranslate - tempWidth - Options.scaledScreenWidth + 30,
					  			  (int)CameraManager.xTranslate + tempWidth + Options.scaledScreenWidth - 30);
			y = Utility.getRandom((int)CameraManager.yTranslate + tempHeight + 10,
		  			  			  (int)CameraManager.yTranslate + tempHeight + Options.scaledScreenHeight - 20);
		}
		else if (y > CameraManager.yTranslate + tempHeight + Options.scaledScreenHeight) {
			x = Utility.getRandom((int)CameraManager.xTranslate - tempWidth - Options.scaledScreenWidth + 30,
								  (int)CameraManager.xTranslate + tempWidth + Options.scaledScreenWidth - 30);
			y = Utility.getRandom((int)CameraManager.yTranslate - tempHeight - Options.scaledScreenHeight + 20,
								  (int)CameraManager.yTranslate - tempHeight - 10);
		}
	}
    
    /**
     * Piirt�� objektin k�yt�ss� olevan tekstuurin tai animaation ruudulle.
     * 
     * @param GL10 OpenGL-konteksti
     */
	@Override
	public void draw(GL10 _gl)
	{
		GLRenderer.starBackgroundTexture.draw(_gl, x, y, direction, currentFrame);
	}

    /**
     * K�sittelee jonkin toiminnon p��ttymisen. Kutsutaan animaation loputtua, mik�li
     * <i>actionActivated</i> on TRUE.<br /><br />
     * 
     * K�ytet��n seuraavasti:<br />
     * <ul>
     *   <li>1. Objekti kutsuu funktiota <b>setAction</b>, jolle annetaan parametreina haluttu animaatio,
     *     animaation toistokerrat, animaation nopeus, toiminnon tunnus (vakiot <b>GfxObject</b>issa).
     *     Toiminnon tunnus tallennetaan <i>actionId</i>-muuttujaan.
     *     		<ul><li>-> Lis�ksi voi antaa my�s jonkin animaation ruudun j�rjestysnumeron (alkaen 0:sta)
     *     		   ja ajan, joka siin� ruudussa on tarkoitus odottaa.</li></ul></li>
     *  <li>2. <b>GfxObject</b>in <b>setAction</b>-funktio kutsuu startAnimation-funktiota (sis�lt�� my�s
     *     <b>GfxObject</b>issa), joka k�ynnist�� animaation asettamalla <i>usedAnimation</i>-muuttujan arvoksi
     *     kohdassa 1 annetun animaation tunnuksen.</li>
     *  <li>3. <b>GLRenderer</b> p�ivitt�� animaatiota kutsumalla <b>GfxObject</b>in <b>update</b>-funktiota.</li>
     *  <li>4. Kun animaatio on loppunut, kutsuu <b>update</b>-funktio koko ketjun aloittaneen objektin
     *     <b>triggerEndOfAction</b>-funktiota (funktio on abstrakti, joten alaluokat luovat siit� aina
     *     oman toteutuksensa).</li>
     *  <li>5. <b>triggerEndOfAction</b>-funktio tulkitsee <i>actionId</i>-muuttujan arvoa, johon toiminnon tunnus
     *     tallennettiin, ja toimii sen mukaisesti.</li>
     * </ul>
     * 
     * Funktiota k�ytet��n esimerkiksi objektin tuhoutuessa, jolloin se voi asettaa itsens�
     * "puoliaktiiviseen" tilaan (esimerkiksi 2, eli ONLY_ANIMATION) ja k�ynnist�� yll� esitetyn
     * tapahtumaketjun. Objekti tuhoutuu asettumalla tilaan 0 (INACTIVE) vasta ketjun p��tytty�.
     * Tuhoutuminen toteutettaisiin triggerEndOfAction-funktion sis�ll�.
     * 
     * Toimintojen vakiot l�ytyv�t GfxObject-luokan alusta.
     */
	@Override
	protected void triggerEndOfAction()
	{
		// ...
	}
}
