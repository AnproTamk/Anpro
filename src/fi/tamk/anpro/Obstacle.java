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
	 * @param _type      Objektin tyyppi (tekstuurit m��ritell��n t�m�n perusteella)
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
		
		// Otetaan Wrapper k�ytt��n
		wrapper = Wrapper.getInstance();
		
		// M��ritet��n n�ytett�v� tekstuuri
		//type = _type;
		
		// M��ritell��n liike
		if (type == OBSTACLE_PLANET || type == OBSTACLE_ASTEROID) {
			turningDirection = Utility.getRandom(1, 2);
		}
		
		// M��ritell��n t�rm�ystunnistus
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
     * Piirt�� objektin k�yt�ss� olevan tekstuurin tai animaation ruudulle.
     * 
     * @param GL10 OpenGL-konteksti
     */
	@Override
	public void draw(GL10 _gl)
	{
        // Tarkistaa onko animaatio p��ll� ja kutsuu oikeaa animaatiota tai tekstuuria
        if (usedAnimation >= 0) {
            GLRenderer.obstacleAnimations[type][usedAnimation].draw(_gl, x, y, 0, currentFrame);
        }
        else {
            GLRenderer.obstacleTextures[type][usedTexture].draw(_gl, x, y, 0, 0);
        }
	}
    
    /**
     * M��ritt�� objektin aktiiviseksi.
     */
	@Override
	public void setActive()
	{
		wrapper.obstacleStates.set(listId, Wrapper.FULL_ACTIVITY);
	}

    /**
     * M��ritt�� objektin ep�aktiiviseksi. Sammuttaa my�s teko�lyn jos se on tarpeen.
     */
	@Override
	public void setUnactive()
	{
		wrapper.obstacleStates.set(listId, Wrapper.INACTIVE);
	}
	
	/**
     * K�sittelee objektin t�rm�ystarkistukset.
     */
    public final void checkCollision()
    {
    	/* Tarkistaa t�rm�ykset vihollisiin */
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
    	
    	/* Tarkistaa t�rm�ykset ammuksiin */
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
    	
    	/* Tarkistaa t�rm�ykset pelaajaan */
		if (Math.abs(x - wrapper.player.x) <= Wrapper.gridSize) {
        	if (Math.abs(y - wrapper.player.y) <= Wrapper.gridSize) {
        		
        		if (Utility.isColliding(wrapper.player, this)) {
        			wrapper.player.triggerCollision(GameObject.COLLISION_WITH_OBSTACLE, 10, 20);
        		}
        	}
		}
    }

    /**
     * K�sittelee jonkin toiminnon p��ttymisen. Kutsutaan animaation loputtua, mik�li
     * actionActivated on TRUE.
     * 
     * K�ytet��n esimerkiksi objektin tuhoutuessa. Objektille m��ritet��n animaatioksi
     * sen tuhoutumisanimaatio, tilaksi Wrapperissa m��ritet��n 2 (piirret��n, mutta
     * p�ivitet��n ainoastaan animaatio) ja asetetaan actionActivatedin arvoksi TRUE.
     * T�ll�in GameThread p�ivitt�� objektin animaation, Renderer piirt�� sen, ja kun
     * animaatio p��ttyy, kutsutaan objektin triggerEndOfAction-funktiota. T�ss�
     * funktiossa objekti k�sittelee tilansa. Tuhoutumisanimaation tapauksessa objekti
     * m��ritt�� itsens� ep�aktiiviseksi.
     * 
     * Jokainen objekti luo funktiosta oman toteutuksensa, sill� toimintoja voi olla
     * useita. Objekteilla on my�s k�yt�ss��n actionId-muuttuja, jolle voidaan asettaa
     * haluttu arvo. T�m� arvo kertoo objektille, mink� toiminnon se juuri suoritti.
     * 
     * Toimintojen vakiot l�ytyv�t GfxObject-luokan alusta.
     */
	@Override
	protected void triggerEndOfAction()
	{
		// ...
	}
}
