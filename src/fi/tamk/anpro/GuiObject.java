package fi.tamk.anpro;

import javax.microedition.khronos.opengles.GL10;

/**
 * Sis‰lt‰‰ yhden k‰yttˆliittym‰objektin tiedot ja osan sen toiminnallisuudesta
 * (Hud hoitaa loput).
 */
abstract public class GuiObject extends GfxObject
{
    // Osoitin Wrapperiin
    protected Wrapper wrapper;

    /**
     * Alustaa luokan muuttujat.
     * 
     * @param int Objektin X-koordinaatti
     * @param int Objektin Y-koordinaatti
     */
    public GuiObject(int _x, int _y)
    {
    	/* Tallennetaan muuttujat */
        x = _x;
        y = _y;
        
        /* Alustetaan muuttujat */
        // M‰‰ritet‰‰n asetukset
        direction = 90;
    
        // Haetaan animaatioiden pituudet
        animationLength = new int[GLRenderer.AMOUNT_OF_HUD_ANIMATIONS];
        for (int i = 0; i < GLRenderer.AMOUNT_OF_HUD_ANIMATIONS; ++i) {
            if (GLRenderer.hudAnimations[i] != null) {
                animationLength[i] = GLRenderer.hudAnimations[i].length;
            }
        }

        /* Otetaan tarvittavat luokat k‰yttˆˆn */
        wrapper = Wrapper.getInstance();
        
        /* M‰‰ritet‰‰n objektin tila (piirtolista) */
        wrapper.addToDrawables(this);
    }

    /**
     * Piirt‰‰ k‰ytˆss‰ olevan tekstuurin ruudulle.
     * 
     * @param GL10 OpenGL-konteksti
     */
    public void draw(GL10 _gl)
    {
        // Tarkistaa onko animaatio p‰‰ll‰ ja kutsuu oikeaa animaatiota tai tekstuuria
        if (usedAnimation >= 0){
        	// TODO: Pluslasku on purkkaviritelm‰...?
            GLRenderer.hudAnimations[usedAnimation].draw(_gl, x + CameraManager.xTranslate,  y + CameraManager.yTranslate, direction, currentFrame);
        }
        else{
        	// TODO: Pluslasku on purkkaviritelm‰...?
        	GLRenderer.hudTextures[usedTexture].draw(_gl, x + CameraManager.xTranslate,  y + CameraManager.yTranslate, direction, currentFrame);
        }
        
    }

    /**
     * K‰sittelee jonkin toiminnon p‰‰ttymisen. Kutsutaan animaation loputtua, mik‰li
     * actionActivated on TRUE.<br /><br />
     * 
     * K‰ytet‰‰n seuraavasti:<br />
     * <ul>
     *   <li>1. Objekti kutsuu funktiota <b>setAction</b>, jolle annetaan parametreina haluttu animaatio,
     *     animaation toistokerrat, animaation nopeus, toiminnon tunnus (vakiot <b>GfxObject</b>issa).
     *     Toiminnon tunnus tallennetaan <i>actionId</i>-muuttujaan.
     *     		<ul><li>-> Lis‰ksi voi antaa myˆs jonkin animaation ruudun j‰rjestysnumeron (alkaen 0:sta)
     *     		   ja ajan, joka siin‰ ruudussa on tarkoitus odottaa.</li></ul></li>
     *  <li>2. <b>GfxObject</b>in <b>setAction</b>-funktio kutsuu startAnimation-funktiota (sis‰lt‰‰ myˆs
     *     <b>GfxObject</b>issa), joka k‰ynnist‰‰ animaation asettamalla <i>usedAnimation</i>-muuttujan arvoksi
     *     kohdassa 1 annetun animaation tunnuksen.</li>
     *  <li>3. <b>GLRenderer</b> p‰ivitt‰‰ animaatiota kutsumalla <b>GfxObject</b>in <b>update</b>-funktiota.</li>
     *  <li>4. Kun animaatio on loppunut, kutsuu <b>update</b>-funktio koko ketjun aloittaneen objektin
     *     <b>triggerEndOfAction</b>-funktiota (funktio on abstrakti, joten alaluokat luovat siit‰ aina
     *     oman toteutuksensa).</li>
     *  <li>5. <b>triggerEndOfAction</b>-funktio tulkitsee <i>actionId</i>-muuttujan arvoa, johon toiminnon tunnus
     *     tallennettiin, ja toimii sen mukaisesti.</li>
     * </ul>
     * 
     * Funktiota k‰ytet‰‰n esimerkiksi objektin tuhoutuessa, jolloin se voi asettaa itsens‰
     * "puoliaktiiviseen" tilaan (esimerkiksi 2, eli ONLY_ANIMATION) ja k‰ynnist‰‰ yll‰ esitetyn
     * tapahtumaketjun. Objekti tuhoutuu asettumalla tilaan 0 (INACTIVE) vasta ketjun p‰‰tytty‰.
     * Tuhoutuminen toteutettaisiin triggerEndOfAction-funktion sis‰ll‰.
     * 
     * Toimintojen vakiot lˆytyv‰t GfxObject-luokan alusta.
     */
    @Override
    protected void triggerEndOfAction()
    {
        // TODO: Nutin' to do... :(
    }
}
