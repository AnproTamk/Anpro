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
    	float objectX;
    	float objectY;
    	
    	if (type == Wrapper.CLASS_TYPE_ENEMY) {
    		objectX = wrapper.enemies.get(parentId).x;
    		objectY = wrapper.enemies.get(parentId).y;
    	}
    	else {
    		objectX = wrapper.projectiles.get(parentId).x;
    		objectY = wrapper.projectiles.get(parentId).y;
    	}
    	
        // Valitaan suunta
        float xDiff = Math.abs((float)(objectX - _x));
        float yDiff = Math.abs((float)(objectY - _y));
        
        if (objectX < _x) {
            if (objectY < _y) {
                return (int) ((Math.atan(yDiff/xDiff)*180)/Math.PI);
            }
            else if (objectY > _y) {
            	return (int) (360 - (Math.atan(yDiff/xDiff)*180)/Math.PI);
            }
            else {
            	return 0;
            }
        }
        else if (objectX > _x) {
            if (objectY > _y) {
            	return (int) (180 + (Math.atan(yDiff/xDiff)*180)/Math.PI);
            }
            else if (objectY < _y) {
            	return (int) (180 - (Math.atan(yDiff/xDiff)*180)/Math.PI);
            }
            else {
            	return 180;
            }
        }
        else {
            if (objectY > _y) {
            	return 270;
            }
            else {
            	return 90;
            }
        }
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
        
        int distance = (int) Math.sqrt(Math.pow(enemyTemp.x - wrapper.player.x, 2) + Math.pow(enemyTemp.y - wrapper.player.y,2));
        
        if (distance - wrapper.player.collisionRadius - enemyTemp.collisionRadius <= 0) {
            enemyTemp.triggerCollision(GameObject.COLLISION_WITH_PLAYER, 0, 0);
            wrapper.player.triggerCollision(GameObject.COLLISION_WITH_ENEMY, enemyTemp.attack * 3, 0);
        }
    }
}