package fi.tamk.anpro;

public class GameObject extends GfxObject {
	// Vakioita törmäyksentunnistukseen
	public static final int NO_COLLISION = 0;
	public static final int CIRCLE_COLLISION = 1;
	public static final int COLLISION_WITH_PROJECTILE = 10;
	public static final int COLLISION_WITH_PLAYER = 11;
	
	// Vakioita valintaan (ampumisessa)
	public static final int NO_SELECTION = 0;
	public static final int SELECTABLE = 1;
	
	// Törmäyksentunnistuksen muuttujat
	public int collisionType = 0;
	public int collisionRadius = 0;
	
	// Valinnan muuttujat
	public int selectionRadius = 0;
	
	// Liikkeen muuttujat
	private int movementSpeed = 0; // Kuinka monta yksikköä objekti liikkuu kerrallaan. Arvot välillä 0-5
	private int movementDelay = 0; // Arvot välillä 5-100(ms), mitä suurempi sitä hitaampi kiihtyvyys
	private int movementAcceleration = 0; // Liikkeen kiihtyminen ja hidastuminen
	
	// Suunnan ja kääntymisen muuttujat
	public  int direction = 0; // 0 on suoraan ylöspäin, 90 oikealle
	private int turningDelay = 0; // Arvot välillä 5-100(ms), mitä suurempi sitä hitaampi kääntyminen
	private int turningAcceleration = 0; // Kääntymisen kiihtyvyys
	private int turningDirection = 0; // 0 ei käänny, 1 vasen, 2 oikea
	
	// Tallennetaan aika
	private long time;
	
	/*
	 public GameObject() {
		super();
	}
	*/	
	
	// Tekee osumalaskennat räjähdyksissä (ei kahden objektin osumisessa toisiinsa)
	public void triggerImpact(int _damage, int _armorPiercing) {
		// VIRTUAALINEN
	}
	
	// Tekee osumalaskennat suorassa osumassa toiseen objektiin
	public void handleCollision(int _eventType, int _damage, int _armorPiercing) {
		// VIRTUAALINEN
	}
	
	// 
	public void updateMovement(long _time) {
		// Lasketaan kääntymisnopeus objektille
		if (_time - time >= turningDelay) {
			// Jos objektin kääntymissuunta on ei mihinkään
			if (turningDirection == 0) {
				// ÄLÄ TEE MITÄÄN
			}
			// Jos objektin kääntymissuunta on vasemmalle
			else if (turningDirection == 1) {
				--direction;
				// 
				turningDelay = turningDelay - turningAcceleration;
				// Tekoäly käsittelee turningAccelerationin
			}
			// Jos objektin kääntymissuunta on oikealle
			else if (turningDirection == 2) {
				++direction;
				// Tekoäly käsittelee turningAccelerationin
				turningDelay = turningDelay - turningAcceleration;
			}
		}
		
		// Lasketaan liikkumisnopeus objektille
		// Mitä suurempi movementDelay sitä hitaammin objekti liikkuu
		if (_time - time >= movementDelay) {
			// Jos objekti liikkuu eteenpäin
			if (movementSpeed > 0 ) {
				x += (movementSpeed * Math.cos(direction)); // Jos objekti liikkuu liian nopeasti -> movementSpeed*kerroin (esim. 0.1)
				y += (movementSpeed * Math.sin(direction));
				// Tekoäly käsittelee movementAccelerationin
				movementDelay = movementDelay - movementAcceleration;
			}
			// Jos objekti ei liiku
			else {
				// ÄLÄ TEE MITÄÄN
			}
		}
	}
}

