package fi.tamk.anpro;


abstract public class GenericAi {
	
	protected Wrapper wrapper;
	
	// Tekoälyä käyttävän vihollisen tunnus wrapperissa
	protected int parentId;
	
	/*
	 * Rakentaja
	 */
	public GenericAi(int _id) {
		parentId = _id;
		
		wrapper = Wrapper.getInstance();
	}
	
	/*
	 * Käsittelee tekoälyn
	 */
	abstract public void handleAi();
	
	/*
	 * Tarkistaa törmäyksen pelaajan kanssa
	 */
	protected void checkCollisionWithPlayer() {
		Enemy enemyTemp = wrapper.enemies.get(parentId);
		
		int distance = (int) Math.sqrt(Math.pow(enemyTemp.x - wrapper.player.x, 2) + Math.pow(enemyTemp.y - wrapper.player.y,2));
		
		if (distance - wrapper.player.collisionRadius - enemyTemp.collisionRadius <= 0) {
			enemyTemp.triggerCollision(GameObject.COLLISION_WITH_PLAYER, 0, 0);
		}
	}
}