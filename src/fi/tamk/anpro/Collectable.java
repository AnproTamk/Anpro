package fi.tamk.anpro;

import javax.microedition.khronos.opengles.GL10;

import android.util.Log;

public class Collectable extends GameObject
{
	// Collectablen "rank" (t‰ll‰ m‰‰ritett‰‰n ker‰yksest‰ ansaitut pisteet)
	protected int COLLECTABLE_RANK = 5;
	
	// Wrapper
	private Wrapper wrapper;
	
	/**
	 * Alustaa luokan muuttujat.
	 * 
	 * @param _x         X-koordinaatti
	 * @param _y         Y-koordinaatti
	 */
	public Collectable(int _x, int _y)
	{
		super(0);
		
		/* Tallennetaan muuttujat */
		x = _x;
		y = _y;
		z = 7;
		
		/* Otetaan tarvittavat luokat k‰yttˆˆn */
		wrapper = Wrapper.getInstance();
		
		/* Alustetaan muuttujat */
		// M‰‰ritet‰‰n tˆrm‰ystunnistus
		collisionRadius = (int) (25 * Options.scale);
		
		// M‰‰ritet‰‰n k‰ytett‰v‰ tekstuuri
		usedTexture = 0;
    
        // Haetaan animaatioiden pituudet
        animationLength = new int[GLRenderer.AMOUNT_OF_COLLECTABLE_ANIMATIONS];
        
        for (int i = 0; i < GLRenderer.AMOUNT_OF_COLLECTABLE_ANIMATIONS; ++i) {
            if (GLRenderer.collectableAnimations[i] != null) {
                animationLength[i] = GLRenderer.collectableAnimations[i].length;
            }
        }

        /* M‰‰ritet‰‰n objektin tila (piirtolista) */
		wrapper.addToDrawables(this);
	}
    
    /**
     * Piirt‰‰ objektin k‰ytˆss‰ olevan tekstuurin tai animaation ruudulle.
     * 
     * @param _gl OpenGL-konteksti
     */
	@Override
	public void draw(GL10 _gl)
	{
        // Tarkistaa onko animaatio p‰‰ll‰ ja kutsuu oikeaa animaatiota tai tekstuuria
        if (usedAnimation >= 0) {
            GLRenderer.collectableAnimations[usedAnimation].draw(_gl, x, y, 0, currentFrame);
        }
        else {
            GLRenderer.collectableTextures[usedTexture].draw(_gl, x, y, direction, currentFrame);
        }
	}
    
    /**
     * M‰‰ritt‰‰ objektin aktiiviseksi.
     */
	@Override
	public void setActive()
	{
		state = Wrapper.FULL_ACTIVITY;
		
		boolean isPlaced = false;
		
		while (!isPlaced) {
			x = Utility.getRandom(-GameMode.mapWidth, GameMode.mapWidth);
	        y = Utility.getRandom(-GameMode.mapHeight, GameMode.mapHeight);

	        for (int i = wrapper.obstacles.size()-1; i >= 0; --i) {
		        if ((Math.abs(x - wrapper.obstacles.get(i).x) > (Wrapper.gridSize + 300)) && (Math.abs(x - wrapper.mothership.x) > Wrapper.gridSize + 50 && Math.abs(x - wrapper.player.x) > 250 &&
		        	 Math.abs(y - wrapper.obstacles.get(i).y) > (Wrapper.gridSize + 300)) && (Math.abs(y - wrapper.mothership.y) > Wrapper.gridSize + 50 && Math.abs(y - wrapper.player.y) > 500)) {
	        		isPlaced = true;
	        		break;
				}
	        }
        }
	}

    /**
     * M‰‰ritt‰‰ objektin ep‰aktiiviseksi. Sammuttaa myˆs teko‰lyn jos se on tarpeen.
     */
	@Override
	public void setUnactive()
	{
		state = Wrapper.INACTIVE;
	}

    /**
     * K‰sittelee tˆrm‰ykset.
     * 
     * @param _damage Osuman aiheuttama vahinko
     * @param _armorPiercing Osuman kyky l‰p‰ist‰ suojat (k‰ytet‰‰n, kun tˆrm‰ttiin ammukseen)
     */
    @Override
    public final void triggerCollision(int _eventType, int _damage, int _armorPiercing)
    {
    	GameMode.updateScore(COLLECTABLE_RANK, x, y);
    	
    	state = Wrapper.ONLY_ANIMATION;
    	setAction(GLRenderer.ANIMATION_COLLECTED, 1, 1, GfxObject.ACTION_DESTROYED, 0, 0);
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
		if (actionId == GfxObject.ACTION_DESTROYED) {
			setActive();
		}
	}
}
