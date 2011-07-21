package fi.tamk.anpro;

import java.util.ArrayList;

public class CollisionRule {

	public GameObject objectChecked;
	
	public ArrayList<GameObject> targetObjects;
	
	public byte    selfType;
	public byte    targetType;
	public boolean triggerCollisionToSelf 		= false;
	public boolean triggerCollisionToTarget 	= true;
	public boolean triggerExplosionToSelf 		= false;
	public boolean triggerExplosionToTarget 	= false;
	public boolean triggerSpecialToSelf 		= false;
	public boolean triggerSpecialToTarget 		= false;
	public boolean disableRuleAfterCollision 	= false;
	
	public String field;
	public float  relativeDamageToSelf;
	public float  relativeDamageToTarget;
	
	public float targetX;
	public float targetY;
	public byte collisionRule;
	
	/**
	 * Luokan rakentaja
	 * 
	 * @param _gameObject Tarkasteltava peliobjekti
	 */
	public CollisionRule(float _targetX, float _targetY, GameObject _gameObject, byte _collisionRule){
		
		objectChecked = _gameObject;
		targetX = _gameObject.x;
		targetY = _gameObject.y;
		collisionRule = _collisionRule;
	}
	
	public final void runCollisionRule() {
		for (int i = targetObjects.size()-1; i >= 0; --i) {
			if (Math.abs(targetObjects.get(i).x - targetX) <= Wrapper.gridSize) {
				if (Math.abs(targetObjects.get(i).y - targetY) <= Wrapper.gridSize) {
        
					// Tarkistetaan osuma
					if (Utility.isColliding(targetObjects.get(i), objectChecked)) {
						
						if (collisionRule == CollisionManager.TRIGGER_COLLISION_ON_SELF) {
							int damage = targetObjects.get(i).collisionDamage;
							
							if (relativeDamageToSelf != -1) {
								try {
									damage = (int)(targetObjects.get(i).getClass().getField(field).getInt(getClass()) * relativeDamageToSelf);
								} catch (Exception e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								} 
							}
							
							objectChecked.triggerCollision(selfType, damage, targetObjects.get(i).armorPiercing);
						}
						
						else if (collisionRule == CollisionManager.TRIGGER_COLLISION_ON_TARGET) {
							int damage = objectChecked.collisionDamage;
							
							if(relativeDamageToTarget != -1) {
								damage = (int)(objectChecked.collisionDamage * relativeDamageToTarget);
							}
							
							targetObjects.get(i).triggerCollision(targetType, damage, objectChecked.armorPiercing);
						}
						
						else if (collisionRule == CollisionManager.TRIGGER_EXPLOSION_ON_SELF) {
							objectChecked.triggerExplosion();
						}
						
						else if (collisionRule == CollisionManager.TRIGGER_EXPLOSION_ON_TARGET) {
							targetObjects.get(i).triggerExplosion();
						}
						
						else if (collisionRule == CollisionManager.TRIGGER_SPECIAL_ON_SELF) {
							objectChecked.triggerSpecialAction();
						}
						
						else if (collisionRule == CollisionManager.TRIGGER_SPECIAL_ON_TARGET) {
							targetObjects.get(i).triggerSpecialAction();
						}
						
						else if (collisionRule == CollisionManager.TRIGGER_DISABLE_RULE_AFTER_COLLISION) {
							CollisionManager.isEnabled = false;
						}
					}
				}
			}
		}
	}
}
