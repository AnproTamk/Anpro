package fi.tamk.anpro;

/**
 * Sis‰lt‰‰ Hudin napin toiminnallisuudet.
 */
public class Button extends GuiObject
{
    /**
     * Alustaa luokan muuttujat.
	 * 
	 * @param int Objektin X-koordinaatti
	 * @param int Objektin Y-koordinaatti
     */
    public Button(int _x, int _y)
    {
        super(_x, _y);
        
        // M‰‰ritet‰‰n aloitustekstuuri
        usedTexture = GLRenderer.TEXTURE_BUTTON_NOTSELECTED;
    }

    /**
     * Asettaa napin valituksi tai poistaa valinnan.
     * 
     * @param boolean Valinta
     */
    public void setSelected(boolean _selected)
    {
        if (_selected) {
            setAction(GLRenderer.ANIMATION_CLICK, 1, 1, 1, 0, 0);
        }
        else {
            usedTexture = GLRenderer.TEXTURE_BUTTON_NOTSELECTED;
        }
    }

    /**
     * K‰sittelee jonkin toiminnon p‰‰ttymisen. Kutsutaan animaation loputtua, mik‰li
     * <i>actionActivated</i> on TRUE.<br /><br />
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
    	if (actionId == 1) {
    		usedTexture = GLRenderer.TEXTURE_BUTTON_SELECTED;
    	}
    }
}
