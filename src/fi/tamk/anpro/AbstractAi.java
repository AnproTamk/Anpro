package fi.tamk.anpro;

/**
 * Sis‰lt‰‰ kaikille teko‰lyille yhteiset ominaisuudet.
 */
abstract public class AbstractAi
{
	/* Osoitin Wrapperiin */
    protected Wrapper wrapper;
    
    /* Objektin tunnus piirtolistalla ja sen tyyppi */
    protected int parentId;
    protected int type;
    
    /* Teko‰lyn tila (toistaiseksi ainoastaan ammusten teko‰lyt k‰ytt‰v‰t t‰t‰) */
	public boolean active = false;
    
    /* Ammusten teko‰lyt */
    public static final int NO_AI                  = 0;
    public static final int LINEAR_PROJECTILE_AI   = 1;
    public static final int TRACKING_PROJECTILE_AI = 2;
    public static final int MOTION_PROJECTILE_AI   = 3;
    
    /**
     * Alustaa luokan muuttujat.
     * 
     * @param int Objektin tunnus piirtolistalla
     * @param int Objektin tyyppi
     */
    public AbstractAi(int _id, int _type)
    {
        parentId = _id;
        type     = _type;
        
        wrapper = Wrapper.getInstance();
    }

    /**
     * Asettaa teko‰lyn aktiiviseksi.
     * 
     * @param int X-koordinaatti
     * @param int Y-koordinaatti
     */
    public void setActive(int _x, int _y) { }
    public void setActive(int _direction) { }
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
    protected int setDirection(int _x, int _y)
    {
    	int objectX = (int) wrapper.projectiles.get(parentId).x;
    	int objectY = (int) wrapper.projectiles.get(parentId).y;
    	
        return Utility.getAngle(objectX, objectY, _x, _y);
    }
    
    /**
     * K‰sittelee teko‰lyn.
     */
    public void handleAi() { }
    
    /**
     * Tarkistaa tˆrm‰yksen pelaajan kanssa.
     */
    protected final void checkCollisionWithPlayer() {
        Enemy enemyTemp = wrapper.enemies.get(parentId);
    	
    	// Tarkistetaan, onko vihollisen ja pelaajan v‰linen et‰isyys riitt‰v‰n pieni
    	// tarkkoja osumatarkistuksia varten
    	if (Math.abs(wrapper.player.x - enemyTemp.x) <= Wrapper.gridSize) {
        	if (Math.abs(wrapper.player.y - enemyTemp.y) <= Wrapper.gridSize) {
        
        		// Lasketaan tarkka et‰isyys
		        if (Utility.getDistance(enemyTemp.x, enemyTemp.y, wrapper.player.x, wrapper.player.y) - wrapper.player.collisionRadius - enemyTemp.collisionRadius <= 0) {
		            enemyTemp.triggerCollision(GameObject.COLLISION_WITH_PLAYER, 0, 0);
		            wrapper.player.triggerCollision(GameObject.COLLISION_WITH_ENEMY, enemyTemp.attack * 3, 0);
		        }
        	}
    	}
    }
}