package fi.tamk.anpro;

/**
 * Sis�lt�� kaikille vihollisten teko�lyille yhteiset ominaisuudet.
 */
abstract public class AbstractAi
{
	/* Osoitin Wrapperiin */
    protected Wrapper wrapper;
    
    /* Vihollisen tunnus piirtolistalla */
    protected int parentId;
    
    /**
     * Alustaa luokan muuttujat.
     * 
     * @param int Vihollisen tunnus piirtolistalla
     */
    public AbstractAi(int _id)
    {
        parentId = _id;
        
        wrapper = Wrapper.getInstance();
    }
    
    /**
     * K�sittelee teko�lyn.
     */
    abstract public void handleAi();
    
    /**
     * Tarkistaa t�rm�yksen pelaajan kanssa.
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