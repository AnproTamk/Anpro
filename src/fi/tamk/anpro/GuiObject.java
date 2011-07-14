package fi.tamk.anpro;

import javax.microedition.khronos.opengles.GL10;

/**
 * Sis�lt�� yhden k�ytt�liittym�objektin tiedot ja osan sen toiminnallisuudesta
 * (Hud hoitaa loput).
 */
abstract public class GuiObject extends GfxObject
{
    // Osoitin Wrapperiin
    protected Wrapper wrapper;
    
    // Osoitin CameraManageriin
    protected CameraManager cameraManager;

    /**
     * Alustaa luokan muuttujat.
     * 
     * @param int Objektin X-koordinaatti
     * @param int Objektin Y-koordinaatti
     */
    public GuiObject(int _x, int _y)
    {
    	// Tallennetaan koordinaatit
        x = _x;
        y = _y;
        
        // M��ritet��n kulma
        direction = 90;
        
        // Otetaan Wrapper ja CameraManager k�ytt��n
        wrapper       = Wrapper.getInstance();
        cameraManager = CameraManager.getInstance();
    
        // Haetaan animaatioiden pituudet
        animationLength = new int[GLRenderer.AMOUNT_OF_HUD_ANIMATIONS];
        
        for (int i = 0; i < GLRenderer.AMOUNT_OF_HUD_ANIMATIONS; ++i) {
            if (GLRenderer.hudAnimations[i] != null) {
                animationLength[i] = GLRenderer.hudAnimations[i].length;
            }
        }

        // Lis�t��n objekti piirtolistalle ja m��ritet��n tila
        listId = wrapper.addToList(this, Wrapper.CLASS_TYPE_GUI, Wrapper.FULL_ACTIVITY);
    }

    /**
     * Piirt�� k�yt�ss� olevan tekstuurin ruudulle.
     * 
     * @param GL10 OpenGL-konteksti
     */
    public void draw(GL10 _gl)
    {
        // Tarkistaa onko animaatio p��ll� ja kutsuu oikeaa animaatiota tai tekstuuria
        if (usedAnimation >= 0){
        	// TODO: Pluslasku on purkkaviritelm�...?
            GLRenderer.hudAnimations[usedAnimation].draw(_gl, x + cameraManager.xTranslate,  y + cameraManager.yTranslate, direction, currentFrame);
        }
        else{
        	// TODO: Pluslasku on purkkaviritelm�...?
        	GLRenderer.hudTextures[usedTexture].draw(_gl, x + cameraManager.xTranslate,  y + cameraManager.yTranslate, direction, 0);
        }
        
    }

    /**
     * K�sittelee jonkin toiminnon p��ttymisen. Kutsutaan animaation loputtua, mik�li
     * actionActivated on TRUE.<br /><br />
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
        // TODO: Nutin' to do... :(
    }
}
