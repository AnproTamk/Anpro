package fi.tamk.anpro;

public class CollisionManager {

	private static CollisionManager instance;
	
	Wrapper wrapper;
	
	private GameObject gameObject;
	
	public static byte TRIGGER_COLLISION_ON_SELF 				= 1;
	public static byte TRIGGER_COLLISION_ON_TARGET 				= 2;
	public static byte TRIGGER_EXPLOSION_ON_SELF 				= 3;
	public static byte TRIGGER_EXPLOSION_ON_TARGET 				= 4;
	public static byte TRIGGER_SPECIAL_ON_SELF 					= 5;
	public static byte TRIGGER_SPECIAL_ON_TARGET 				= 6;
	public static byte TRIGGER_DISABLE_RULE_AFTER_COLLISION 	= 7;
	public static byte TRIGGER_CLUSTER 							= 8;
	public static byte TRIGGER_DESTROYED 						= 9;
	public static byte TRIGGER_DISABLED							= 10;
	
	public static boolean isEnabled = false;
	
	public byte HEALTH;
	public byte CURRENT_HEALTH;
	public byte ARMOR;
	public byte CURRENT_ARMOR;
	
	public CollisionManager(GameObject _gameObject) {
		gameObject = _gameObject;
	}
	
	/**
	 * Palauttaa osoittimen tähän luokkaan.
	 * 
	 */
	synchronized public static void getInstance()
	{

	}
	
	public static final CollisionManager requestCollisionDetection(GameObject _gameObject){
		return instance;
		
	}
	
	public static final CollisionManager setTargetRule(GameObject _gameObject) {
		return instance;
		
	}
	
	public static final CollisionManager setCollisionRule(byte _collisionRule) {
		return instance;
		
	}
	
	public static final CollisionManager setRelativeDamage(byte _health, float _multiplier) {
		return instance;
	}
	
	public static final CollisionManager setMethodToCall(byte _collisionRule) {
		return instance;
		
	}
	
	public static final void handleCollisions() {
		
	}
	
	public static final void removeCollisionRule(GameObject _gameObject) {
		
	}
	
	public static final void clearAllRules() {
		
	}
}
