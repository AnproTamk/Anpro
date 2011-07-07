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
    	// Tallennetaan koordinaatit
        x = _x;
        y = _y;
        
        // Otetaan Wrapper k‰yttˆˆn
        wrapper = Wrapper.getInstance();
    
        // Haetaan animaatioiden pituudet
        animationLength = new int[GLRenderer.AMOUNT_OF_HUD_ANIMATIONS];
        
        for (int i = 0; i < GLRenderer.AMOUNT_OF_HUD_ANIMATIONS; ++i) {
            if (GLRenderer.hudAnimations[i] != null) {
                animationLength[i] = GLRenderer.hudAnimations[i].length;
            }
        }

        // Lis‰t‰‰n objekti piirtolistalle ja m‰‰ritet‰‰n tila
        listId = wrapper.addToList(this, Wrapper.CLASS_TYPE_GUI, Wrapper.FULL_ACTIVITY);
    }

    /**
     * Piirt‰‰ k‰ytˆss‰ olevan tekstuurin ruudulle.
     * 
     * @param GL10 OpenGL-konteksti
     */
    public final void draw(GL10 _gl)
    {
        // Tarkistaa onko animaatio p‰‰ll‰ ja kutsuu oikeaa animaatiota tai tekstuuria
        if (usedAnimation >= 0){
            GLRenderer.hudAnimations[usedAnimation].draw(_gl, x, y, 90, currentFrame);
        }
        else{
        	GLRenderer.hudTextures[usedTexture].draw(_gl, x, y, 90, 0);
        }
        
    }

    /**
     * K‰sittelee jonkin toiminnon p‰‰ttymisen. Kutsutaan animaation loputtua, mik‰li
     * actionActivated on TRUE.
     * 
     * K‰ytet‰‰n esimerkiksi objektin tuhoutuessa. Objektille m‰‰ritet‰‰n animaatioksi
     * sen tuhoutumisanimaatio, tilaksi Wrapperissa m‰‰ritet‰‰n 2 (piirret‰‰n, mutta
     * p‰ivitet‰‰n ainoastaan animaatio) ja asetetaan actionActivatedin arvoksi TRUE.
     * T‰llˆin GameThread p‰ivitt‰‰ objektin animaation, Renderer piirt‰‰ sen, ja kun
     * animaatio p‰‰ttyy, kutsutaan objektin triggerEndOfAction-funktiota. T‰ss‰
     * funktiossa objekti k‰sittelee tilansa. Tuhoutumisanimaation tapauksessa objekti
     * m‰‰ritt‰‰ itsens‰ ep‰aktiiviseksi.
     * 
     * Jokainen objekti luo funktiosta oman toteutuksensa, sill‰ toimintoja voi olla
     * useita. Objekteilla on myˆs k‰ytˆss‰‰n actionId-muuttuja, jolle voidaan asettaa
     * haluttu arvo. T‰m‰ arvo kertoo objektille, mink‰ toiminnon se juuri suoritti.
     * 
     * Toimintojen vakiot lˆytyv‰t GfxObject-luokan alusta.
     */
    @Override
    protected void triggerEndOfAction()
    {
        // TODO: Nutin' to do... :(
    }
}
