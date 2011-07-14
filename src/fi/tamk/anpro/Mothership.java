package fi.tamk.anpro;

import javax.microedition.khronos.opengles.GL10;

public class Mothership extends GameObject
{
	private Wrapper wrapper;
	
	public Mothership(int _speed)
	{
		super(0);
		
		// Otetaan Wrapper k‰yttˆˆn
		wrapper = Wrapper.getInstance();
		
		// Lis‰t‰‰n emoalus piirtolistalle
		listId = wrapper.addToList(this, Wrapper.CLASS_TYPE_MOTHERSHIP, 0); // T‰rkeydell‰ ei v‰li‰, vakiona aina 2
		
		// M‰‰ritet‰‰n emoaluksen tiedot
		direction = 160;
		x         = 100;
		y         = 90;
	    
        // Haetaan animaatioiden pituudet
        animationLength = new int[GLRenderer.AMOUNT_OF_MOTHERSHIP_ANIMATIONS];
        
        for (int i = 0; i < GLRenderer.AMOUNT_OF_MOTHERSHIP_ANIMATIONS; ++i) {
            if (GLRenderer.mothershipAnimations[i] != null) {
                animationLength[i] = GLRenderer.mothershipAnimations[i].length;
            }
        }
		
        /*// K‰ynnistet‰‰n animaatio
		usedAnimation  = 0;
		animationSpeed = 2;*/
	}
    
    /**
     * K‰sittelee ammuksen tˆrm‰ystarkistukset.
     */
    public final void checkCollision()
    {
    	/* Tarkistetaan osumat pelaajaan */
    	// Tarkistetaan, onko emoaluksen ja pelaajan v‰linen et‰isyys riitt‰v‰n pieni
    	// tarkkoja osumatarkistuksia varten
    	if (Math.abs(wrapper.player.x - x) <= Wrapper.gridSize) {
        	if (Math.abs(wrapper.player.y - y) <= Wrapper.gridSize) {
        
                // Tarkistetaan osuma
        		if (Utility.isColliding(wrapper.player, this)) {
        			
        			// Siirryt‰‰n emoaluksen valikkoon
        			// TODO: Lis‰‰ siirtyminen toiseen activityyn
        		}
        	}
    	}
    }
    
    /**
     * Piirt‰‰ objektin k‰ytˆss‰ olevan tekstuurin tai animaation ruudulle.
     * 
     * @param GL10 OpenGL-konteksti
     */
	@Override
	public void draw(GL10 _gl)
	{
        // Tarkistaa onko animaatio p‰‰ll‰ ja kutsuu oikeaa animaatiota tai tekstuuria
        if (usedAnimation >= 0){
            GLRenderer.mothershipAnimations[usedAnimation].draw(_gl, x, y, direction, currentFrame);
        }
        else{
            GLRenderer.mothershipTextures[usedTexture].draw(_gl, x, y, direction, 0);
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
		// ...
	}

}
