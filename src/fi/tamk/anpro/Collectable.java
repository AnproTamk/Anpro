package fi.tamk.anpro;

import javax.microedition.khronos.opengles.GL10;

public class Collectable extends GameObject
{
	// Wrapper
	private Wrapper wrapper;
	
	/**
	 * Alustaa luokan muuttujat.
	 * 
	 * @param _x         X-koordinaatti
	 * @param _y         Y-koordinaatti
	 * @param _speed     Liikkumisnopeus
	 * @param _direction Liikkumissuunta
	 */
	public Collectable(int _x, int _y)
	{
		super(0);
		
		// Tallennetaan koordinaatit
		x = _x;
		y = _y;
		
		// Otetaan Wrapper k�ytt��n
		wrapper = Wrapper.getInstance();
		
		// M��ritell��n t�rm�ystunnistus
		collisionRadius = (int) (25 * Options.scale);
		
		// M��ritet��n tekstuuri
		usedTexture = 0;
    
        // Haetaan animaatioiden pituudet
        animationLength = new int[GLRenderer.AMOUNT_OF_COLLECTABLE_ANIMATIONS];
        
        for (int i = 0; i < GLRenderer.AMOUNT_OF_COLLECTABLE_ANIMATIONS; ++i) {
            if (GLRenderer.collectableAnimations[i] != null) {
                animationLength[i] = GLRenderer.collectableAnimations[i].length;
            }
        }

		// Lis�t��n objekti piirtolistalle
		listId = wrapper.addToList(this, Wrapper.CLASS_TYPE_COLLECTABLE, 0);
	}
    
    /**
     * Piirt�� objektin k�yt�ss� olevan tekstuurin tai animaation ruudulle.
     * 
     * @param GL10 OpenGL-konteksti
     */
	@Override
	public void draw(GL10 _gl)
	{
        // Tarkistaa onko animaatio p��ll� ja kutsuu oikeaa animaatiota tai tekstuuria
        if (usedAnimation >= 0) {
            GLRenderer.collectableAnimations[usedAnimation].draw(_gl, x, y, 0, currentFrame);
        }
        else {
            GLRenderer.collectableTextures[usedTexture].draw(_gl, x, y, direction, 0);
        }
	}
    
    /**
     * M��ritt�� objektin aktiiviseksi.
     */
	@Override
	public void setActive()
	{
		wrapper.collectableStates.set(listId, Wrapper.FULL_ACTIVITY);
		
		boolean isPlaced = false;
		
		while (!isPlaced) {
			x = Utility.getRandom(-GameMode.mapWidth, GameMode.mapWidth);
	        y = Utility.getRandom(-GameMode.mapHeight, GameMode.mapHeight);
			
	        for (int i = wrapper.obstacles.size()-1; i >= 0; --i) {
		        if (Math.abs(x - wrapper.obstacles.get(i).x) > Wrapper.gridSize + 150) {
		        	if (Math.abs(y - wrapper.obstacles.get(i).y) > Wrapper.gridSize + 150) {
		        		isPlaced = true;
		        	}
				}
	        }
        }
	}

    /**
     * M��ritt�� objektin ep�aktiiviseksi. Sammuttaa my�s teko�lyn jos se on tarpeen.
     */
	@Override
	public void setUnactive()
	{
		wrapper.collectableStates.set(listId, Wrapper.INACTIVE);
	}

    /**
     * K�sittelee t�rm�ykset.
     * 
     * @param int Osuman aiheuttama vahinko
     * @param int Osuman kyky l�p�ist� suojat (k�ytet��n, kun t�rm�ttiin ammukseen)
     */
    @Override
    public final void triggerCollision(int _damage, int _armorPiercing)
    {
    	wrapper.collectableStates.set(listId, Wrapper.ONLY_ANIMATION);
    	setAction(GLRenderer.ANIMATION_COLLECTED, 1, 1, GfxObject.ACTION_DESTROYED);
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
		if (actionId == GfxObject.ACTION_DESTROYED) {
			setActive();
		}
	}
}
