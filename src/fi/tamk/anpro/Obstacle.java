package fi.tamk.anpro;

import javax.microedition.khronos.opengles.GL10;

/**
 * TODO: Lis‰‰ kommentit
 */
public class Obstacle extends GameObject
{
	// Kartan objektien tyypit
	public static final byte OBSTACLE_PLANET   = 0;
	public static final byte OBSTACLE_ASTEROID = 1;
	public static final byte OBSTACLE_STAR     = 2;

	// Objektien tarkemmin m‰‰ritelty tyyppi
	public static final byte PLANET_EARTH = 0;
	public static final byte PLANET_X     = 1;
	
	// Objektin "yleinen" tyyppi ja tarkempi m‰‰ritys
	private int type;
	private int specifiedType;
	
	// Wrapper
	private Wrapper wrapper;
	
	/**
	 * Alustaa luokan muuttujat.
	 * 
	 * @param _type          Objektin tyyppi                      (tekstuurit m‰‰ritell‰‰n t‰m‰n perusteella)
	 * @param _specifiedType Objektin tarkemmin m‰‰ritelty tyyppi (tekstuurit m‰‰ritell‰‰n t‰m‰n perusteella)
	 * @param _x         	 X-koordinaatti
	 * @param _y         	 Y-koordinaatti
	 * @param _speed     	 Liikkumisnopeus
	 * @param _direction 	 Liikkumissuunta
	 */
	public Obstacle(int _type, int _specifiedType, int _x, int _y, int _speed, int _direction)
	{
		super(_speed);
		
		// Tallennetaan koordinaatit
		x = _x;
		y = _y;
		
		// Otetaan Wrapper k‰yttˆˆn
		wrapper = Wrapper.getInstance();
		
		// Tallennetaan tyyppi ja m‰‰ritet‰‰n n‰ytett‰v‰ tekstuuri
		type          = _type;
		specifiedType = _specifiedType;
		usedTexture   = _specifiedType;
		
		// M‰‰ritell‰‰n liike
		if (type == OBSTACLE_PLANET || type == OBSTACLE_ASTEROID) {
			facingTurningDirection = Utility.getRandom(1, 2);
		}
		
		if (type == OBSTACLE_PLANET) {
			setFacingTurningSpeed(0.5f);
			setFacingTurningDelay(0.001f);
			setMovementSpeed(0.0f);
		}
		else if (type == OBSTACLE_ASTEROID) {
			setFacingTurningSpeed(1.0f);
			setFacingTurningDelay(0.25f);
			direction = Utility.getRandom(0, 359);
		}
		
		// M‰‰ritell‰‰n tˆrm‰ystunnistus
		if (type == OBSTACLE_PLANET) {
			collisionRadius = (int) (117 * Options.scale);
		}
		else if (type == OBSTACLE_ASTEROID) {
			collisionRadius = (int) (50 * Options.scale);
		}
		else if (type == OBSTACLE_STAR) {
			collisionRadius = (int) (240 * Options.scale);
		}
		
		// Lis‰t‰‰n objekti piirtolistalle
		listId = wrapper.addToList(this, Wrapper.CLASS_TYPE_OBSTACLE, 0);
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
            GLRenderer.obstacleAnimations[type][usedAnimation].draw(_gl, x, y, (int)facingDirection, currentFrame);
        }
        else {
            GLRenderer.obstacleTextures[type][usedTexture].draw(_gl, x, y, (int)facingDirection, 0);
        }
	}
    
    /**
     * M‰‰ritt‰‰ objektin aktiiviseksi.
     */
	@Override
	public void setActive()
	{
		wrapper.obstacleStates.set(listId, Wrapper.FULL_ACTIVITY);
	}

    /**
     * M‰‰ritt‰‰ objektin ep‰aktiiviseksi. Sammuttaa myˆs teko‰lyn jos se on tarpeen.
     */
	@Override
	public void setUnactive()
	{
		wrapper.obstacleStates.set(listId, Wrapper.INACTIVE);
	}
	
	/**
     * K‰sittelee objektin tˆrm‰ystarkistukset.
     */
    public final void checkCollision()
    {
    	/* Tarkistaa tˆrm‰ykset vihollisiin */
    	for (int i = wrapper.enemies.size() - 1; i >= 0; --i) {
    		
    		if (wrapper.enemyStates.get(i) == Wrapper.FULL_ACTIVITY) {
    			
    			if (Math.abs(x - wrapper.enemies.get(i).x) <= Wrapper.gridSize) {
    	        	if (Math.abs(y - wrapper.enemies.get(i).y) <= Wrapper.gridSize) {
    	        		
    	        		if (Utility.isColliding(wrapper.enemies.get(i), this)) {
    	        			wrapper.enemies.get(i).triggerCollision(GameObject.COLLISION_WITH_OBSTACLE, 0, 0);
    	        		}
    	        	}
    			}
    		}
    	}
    	
    	/* Tarkistaa tˆrm‰ykset ammuksiin */
    	for (int i = wrapper.projectiles.size() - 1; i >= 0; --i) {
    		
    		if (wrapper.projectileStates.get(i) == Wrapper.FULL_ACTIVITY) {
    			
    			if (Math.abs(x - wrapper.projectiles.get(i).x) <= Wrapper.gridSize) {
    	        	if (Math.abs(y - wrapper.projectiles.get(i).y) <= Wrapper.gridSize) {
    	        		
    	        		if (Utility.isColliding(wrapper.projectiles.get(i), this)) {
    	        			wrapper.projectiles.get(i).triggerCollision(GameObject.COLLISION_WITH_OBSTACLE, 0, 0);
    	        		}
    	        	}
    			}
    		}
    	}
    	
    	/* Tarkistaa tˆrm‰ykset pelaajaan */
		if (Math.abs(x - wrapper.player.x) <= Wrapper.gridSize) {
        	if (Math.abs(y - wrapper.player.y) <= Wrapper.gridSize) {
        		
        		if (Utility.isColliding(wrapper.player, this)) {
        			wrapper.player.triggerCollision(1000, 20);
        		}
        	}
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
