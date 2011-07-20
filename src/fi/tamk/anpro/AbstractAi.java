package fi.tamk.anpro;

/**
 * Sis‰lt‰‰ kaikille teko‰lyille yhteiset ominaisuudet.
 */
abstract public class AbstractAi
{
    /* Ammusten teko‰lyt */
    public static final int NO_AI                  = 0;
    public static final int LINEAR_PROJECTILE_AI   = 1;
    public static final int TRACKING_PROJECTILE_AI = 2;
    public static final int MOTION_PROJECTILE_AI   = 3;
    
    /* Vihollisten teko‰lyt */
    public static final int LINEAR_ENEMY_AI          = 1;
    public static final int ROTARY_ENEMY_AI          = 2;
    public static final int SQUIGGLY_ENEMY_AI        = 3;
    public static final int APPROACHANDSTOP_ENEMY_AI = 4;
    
    /* Liittolaisten teko‰lyt */
    public static final int TURRET_AI = 1;
    
	/* Osoitin Wrapperiin */
    protected Wrapper wrapper;
    
    /* Objektin tunnus piirtolistalla ja sen tyyppi */
    protected AiObject parentObject;
    protected int      userType;
    
    /* Teko‰lyn tila (toistaiseksi ainoastaan ammusten teko‰lyt k‰ytt‰v‰t t‰t‰) */
	public boolean active = false;
	
	/* Vihollisen etsiminen */
	protected int   indexOfClosestEnemy = -1;
	protected float distanceToEnemy     = -1;
    
    /**
     * Alustaa luokan muuttujat.
     * 
     * @param int Objektin tunnus piirtolistalla
     * @param int Objektin tyyppi
     */
    public AbstractAi(AiObject _parentObject, int _userType)
    {
        parentObject = _parentObject;
        userType     = _userType;
        
        wrapper = Wrapper.getInstance();
    }

    /**
     * Asettaa teko‰lyn aktiiviseksi.
     * 
     * @param int Kohteen X-koordinaatti
     * @param int Kohteen Y-koordinaatti
     */
    public void setActive(float _x, float _y) { }

    /**
     * Asettaa teko‰lyn aktiiviseksi.
     * 
     * @param int Ammuksen suunta
     */
    public void setActive(int _direction) { }

    /**
     * Asettaa teko‰lyn aktiiviseksi.
     * 
     * @param int[][] Ammuksen reitti
     */
    public void setActive(int[][] _path) { }

    /**
     * Asettaa teko‰lyn ep‰aktiiviseksi.
     */
    public void setUnactive() { }

	/**
     * M‰‰ritt‰‰ objektin aloitussuunnan.
     * 
     * @param int X-koordinaatti
     * @param int Y-koordinaatti
     * 
     * @return int Objektin suunta
     */
    protected int setDirection(float _x, float _y)
    {
        return Utility.getAngle(parentObject.x, parentObject.y, _x, _y);
    }
    
    /**
     * K‰sittelee teko‰lyn.
     */
    public void handleAi() { }
    
    /**
     * Tarkistaa tˆrm‰yksen pelaajan kanssa.
     */
    // TODO: Siirr‰ t‰m‰ Enemy-luokkaan.
    protected final void checkCollisionWithPlayer()
    {
    	// Tarkistetaan, onko vihollisen ja pelaajan v‰linen et‰isyys riitt‰v‰n pieni
    	// tarkkoja osumatarkistuksia varten
    	if (Math.abs(wrapper.player.x - parentObject.x) <= Wrapper.gridSize) {
        	if (Math.abs(wrapper.player.y - parentObject.y) <= Wrapper.gridSize) {
        
        		wrapper.player.outOfBattleTime = android.os.SystemClock.uptimeMillis();
        		
        		// Tarkistetaan tˆrm‰ys
        		if (Utility.isColliding(parentObject, wrapper.player)) {
        			parentObject.triggerCollision(GameObject.COLLISION_WITH_PLAYER, 0, 0);
        			wrapper.player.triggerCollision(GameObject.COLLISION_WITH_ENEMY, parentObject.collisionDamage, 0);
        		}
        	}
    	}
    }
    
	/**
	 * Etsii l‰himm‰n vihollisen ja palauttaa sen indeksin (vastaa objektin listId-muuttujaa).
	 */
	protected final void findClosestEnemy(int _distance)
	{
		for (int i = wrapper.enemies.size()-1; i >= 0; --i) {
			if (wrapper.enemies.get(i).state == Wrapper.FULL_ACTIVITY) {
				
				float distance = Utility.getDistance(wrapper.enemies.get(i).x, wrapper.enemies.get(i).y,
													 parentObject.x, parentObject.y);
				
				// TODO: K‰yt‰ Wrapperin gridi‰
				if (_distance >= distance) {
					if (indexOfClosestEnemy == -1) {
						indexOfClosestEnemy = i;
						distanceToEnemy     = distance;
					}
					else if (distance < distanceToEnemy) {
						indexOfClosestEnemy = i;
						distanceToEnemy     = distance;
					}
				}
			}
		}
	}
}