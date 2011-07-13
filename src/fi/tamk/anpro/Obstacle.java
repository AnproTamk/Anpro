package fi.tamk.anpro;

import javax.microedition.khronos.opengles.GL10;

/**
 * 
 */
public class Obstacle extends GameObject
{
	// Kartan objektien tyypit
	public static final byte OBSTACLE_PLANET   = 0;
	public static final byte OBSTACLE_ASTEROID = 1;
	public static final byte OBSTACLE_STAR     = 2;
	
	// Objektin tyyppi
	private byte type;
	
	// Wrapper
	private Wrapper wrapper;
	
	/**
	 * Alustaa luokan muuttujat.
	 * 
	 * @param _type      Objektin tyyppi (tekstuurit määritellään tämän perusteella)
	 * @param _x         X-koordinaatti
	 * @param _y         Y-koordinaatti
	 * @param _speed     Liikkumisnopeus
	 * @param _direction Liikkumissuunta
	 */
	public Obstacle(int _type, int _x, int _y, int _speed, int _direction)
	{
		super(_speed);
		
		// Tallennetaan koordinaatit
		x = _x;
		y = _y;
		
		// Otetaan Wrapper käyttöön
		wrapper = Wrapper.getInstance();
		
		// Määritetään näytettävä tekstuuri
		//type = _type;
		
		// Määritellään liike
		if (type == OBSTACLE_PLANET || type == OBSTACLE_ASTEROID) {
			turningDirection = Utility.getRandom(1, 2);
		}
		
		// Määritellään törmäystunnistus
		if (type == OBSTACLE_PLANET) {
			collisionRadius = (int) (128 * Options.scale);
		}
		else if (type == OBSTACLE_ASTEROID) {
			collisionRadius = (int) (54 * Options.scale);
		}
		else if (type == OBSTACLE_STAR) {
			collisionRadius = (int) (245 * Options.scale);
		}
	}
    
    /**
     * Piirtää objektin käytössä olevan tekstuurin tai animaation ruudulle.
     * 
     * @param GL10 OpenGL-konteksti
     */
	@Override
	public void draw(GL10 _gl)
	{
        // Tarkistaa onko animaatio päällä ja kutsuu oikeaa animaatiota tai tekstuuria
        if (usedAnimation >= 0) {
            GLRenderer.obstacleAnimations[type][usedAnimation].draw(_gl, x, y, 0, currentFrame);
        }
        else {
            GLRenderer.obstacleTextures[type][usedTexture].draw(_gl, x, y, 0, 0);
        }
	}
    
    /**
     * Määrittää objektin aktiiviseksi.
     */
	@Override
	public void setActive()
	{
		wrapper.obstacleStates.set(listId, Wrapper.FULL_ACTIVITY);
	}

    /**
     * Määrittää objektin epäaktiiviseksi. Sammuttaa myös tekoälyn jos se on tarpeen.
     */
	@Override
	public void setUnactive()
	{
		wrapper.obstacleStates.set(listId, Wrapper.INACTIVE);
	}
	
	/**
     * Käsittelee objektin törmäystarkistukset.
     */
    public final void checkCollision()
    {
    	/* Tarkistaa törmäykset vihollisiin */
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
    	
    	/* Tarkistaa törmäykset ammuksiin */
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
    	
    	/* Tarkistaa törmäykset pelaajaan */
		if (Math.abs(x - wrapper.player.x) <= Wrapper.gridSize) {
        	if (Math.abs(y - wrapper.player.y) <= Wrapper.gridSize) {
        		
        		if (Utility.isColliding(wrapper.player, this)) {
        			wrapper.player.triggerCollision(GameObject.COLLISION_WITH_OBSTACLE, 10, 20);
        		}
        	}
		}
    }

    /**
     * Käsittelee jonkin toiminnon päättymisen. Kutsutaan animaation loputtua, mikäli
     * actionActivated on TRUE.
     * 
     * Käytetään esimerkiksi objektin tuhoutuessa. Objektille määritetään animaatioksi
     * sen tuhoutumisanimaatio, tilaksi Wrapperissa määritetään 2 (piirretään, mutta
     * päivitetään ainoastaan animaatio) ja asetetaan actionActivatedin arvoksi TRUE.
     * Tällöin GameThread päivittää objektin animaation, Renderer piirtää sen, ja kun
     * animaatio päättyy, kutsutaan objektin triggerEndOfAction-funktiota. Tässä
     * funktiossa objekti käsittelee tilansa. Tuhoutumisanimaation tapauksessa objekti
     * määrittää itsensä epäaktiiviseksi.
     * 
     * Jokainen objekti luo funktiosta oman toteutuksensa, sillä toimintoja voi olla
     * useita. Objekteilla on myös käytössään actionId-muuttuja, jolle voidaan asettaa
     * haluttu arvo. Tämä arvo kertoo objektille, minkä toiminnon se juuri suoritti.
     * 
     * Toimintojen vakiot löytyvät GfxObject-luokan alusta.
     */
	@Override
	protected void triggerEndOfAction()
	{
		// ...
	}
}
