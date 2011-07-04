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
    
	/* Osoitin Wrapperiin */
    protected Wrapper wrapper;
    
    /* Objektin tunnus piirtolistalla ja sen tyyppi */
    protected int parentId;
    protected int type;
    
    /* Teko‰lyn tila (toistaiseksi ainoastaan ammusten teko‰lyt k‰ytt‰v‰t t‰t‰) */
	public boolean active = false;
    
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
     * @param int Kohteen X-koordinaatti
     * @param int Kohteen Y-koordinaatti
     */
    public void setActive(int _x, int _y) { }

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
    protected int setDirection(int _x, int _y)
    {
        return Utility.getAngle((int)wrapper.projectiles.get(parentId).x, (int)wrapper.projectiles.get(parentId).y, _x, _y);
    }
    
    /**
     * K‰sittelee teko‰lyn.
     */
    public void handleAi() { }
    
    /**
     * Tarkistaa tˆrm‰yksen pelaajan kanssa.
     */
    protected final void checkCollisionWithPlayer()
    {
    	// Tarkistetaan, onko vihollisen ja pelaajan v‰linen et‰isyys riitt‰v‰n pieni
    	// tarkkoja osumatarkistuksia varten
    	if (Math.abs(wrapper.player.x - wrapper.enemies.get(parentId).x) <= Wrapper.gridSize) {
        	if (Math.abs(wrapper.player.y - wrapper.enemies.get(parentId).y) <= Wrapper.gridSize) {
        
        		// Tarkistetaan tˆrm‰ys
        		if (Utility.isColliding(wrapper.enemies.get(parentId), wrapper.player)) {
        			wrapper.enemies.get(parentId).triggerCollision(GameObject.COLLISION_WITH_PLAYER, 0, 0);
        			wrapper.player.triggerCollision(GameObject.COLLISION_WITH_ENEMY, wrapper.enemies.get(parentId).attack * 3, 0);
        		}
        	}
    	}
    }
}