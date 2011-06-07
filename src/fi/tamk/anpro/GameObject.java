package fi.tamk.anpro;

abstract public class GameObject extends GfxObject {
	// Vakioita suuntiin
	public static final int TO_THE_LEFT = 1;
	public static final int TO_THE_RIGHT = 2;
	
	// Vakioita törmäyksentunnistukseen
	public static final int NO_COLLISION = 0;
	public static final int CIRCLE_COLLISION = 1;
	
	public static final int COLLISION_WITH_PROJECTILE = 10;
	public static final int COLLISION_WITH_PLAYER = 11;
	public static final int COLLISION_WITH_ENEMY = 12;
	
	// Vakioita valintaan (ampumisessa)
	public static final int NO_SELECTION = 0;
	public static final int SELECTABLE = 1;
	
	// Törmäyksentunnistuksen muuttujat
	public int collisionType = 0;
	public int collisionRadius = 0;
	
	// Valinnan muuttujat
	public int selectionRadius = 0;
	
	// Liikkeen muuttujat
	private int movementSpeed = 2; // Kuinka monta yksikköä objekti liikkuu kerrallaan. Arvot välillä 0-5
	private int movementDelay = 20; // Arvot välillä 5-100(ms), mitä suurempi sitä hitaampi kiihtyvyys
	private int movementAcceleration = 0; // Liikkeen kiihtyminen ja hidastuminen
	
	// Suunnan ja kääntymisen muuttujat
	public  int direction = 0; // 0 on suoraan ylöspäin, 90 oikealle
	private int turningDelay = 200; // Arvot välillä 5-100(ms), mitä suurempi sitä hitaampi kääntyminen
	private int turningAcceleration = 0; // Kääntymisen kiihtyvyys
	public  int turningDirection = 0; // 0 ei käänny, 1 vasen, 2 oikea
	
	// Tallennetaan aika
	private long turningTime = 0;
	private long movementTime = 0;
	
	public GameObject() {
		super();
	}
	
	// Tekee osumalaskennat räjähdyksissä (ei kahden objektin osumisessa toisiinsa)
	abstract public void triggerImpact(int _damage);
	
	// Tekee osumalaskennat suorassa osumassa toiseen objektiin
	abstract public void triggerCollision(int _eventType, int _damage, int _armorPiercing);
	
	// Päivitetään liikkuminen
	public void updateMovement(long _time) {
		// Lasketaan kääntymisnopeus objektille
		if (_time - turningTime >= turningDelay) {
			turningTime = _time;
			// Jos objektin kääntymissuunta on vasemmalle
			if (turningDirection == TO_THE_LEFT) {
				--direction;
				// 
				turningDelay -= turningAcceleration;
				// Tekoäly käsittelee turningAccelerationin
			}
			// Jos objektin kääntymissuunta on oikealle
			else if (turningDirection == TO_THE_RIGHT) {
				++direction;
				// Tekoäly käsittelee turningAccelerationin
				turningDelay -= turningAcceleration;
			}
		}
		
		// Lasketaan liikkumisnopeus objektille
		// Mitä suurempi movementDelay sitä hitaammin objekti liikkuu
		if (_time - movementTime >= movementDelay) {
			movementTime = _time;
			// Jos objekti liikkuu eteenpäin
			if (movementSpeed > 0 ) {
				x += (movementSpeed * Math.cos(direction)); // Jos objekti liikkuu liian nopeasti -> movementSpeed*kerroin (esim. 0.1)
				y += (movementSpeed * Math.sin(direction));
				// Tekoäly käsittelee movementAccelerationin
				movementDelay = movementDelay - movementAcceleration;
			}
		}
	}
}

