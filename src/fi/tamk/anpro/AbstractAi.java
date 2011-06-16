package fi.tamk.anpro;


abstract public class AbstractAi {
    
    protected Wrapper wrapper;
    
    // Teko�ly� k�ytt�v�n vihollisen tunnus wrapperissa
    protected int parentId;
    
    /*
     * Rakentaja
     */
    public AbstractAi(int _id) {
        parentId = _id;
        
        wrapper = Wrapper.getInstance();
    }
    
    /*
     * K�sittelee teko�lyn
     */
    abstract public void handleAi();
    
    /*
     * Tarkistaa t�rm�yksen pelaajan kanssa
     */
    protected final void checkCollisionWithPlayer() {
        Enemy enemyTemp = wrapper.enemies.get(parentId);
        
        int distance = (int) Math.sqrt(Math.pow(enemyTemp.x - wrapper.player.x, 2) + Math.pow(enemyTemp.y - wrapper.player.y,2));
        
        if (distance - wrapper.player.collisionRadius - enemyTemp.collisionRadius <= 0) {
            enemyTemp.triggerCollision(GameObject.COLLISION_WITH_PLAYER, 0, 0);
        }
    }
}