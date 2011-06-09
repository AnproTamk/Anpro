package fi.tamk.anpro;

abstract public class GameObject extends GfxObject {
	// Vakioita suuntiin
	public static final int TO_THE_LEFT = 1;
	public static final int TO_THE_RIGHT = 2;
	
	// Vakioita tˆrm‰yksentunnistukseen
	public static final int NO_COLLISION = 0;
	public static final int CIRCLE_COLLISION = 1;
	
	public static final int COLLISION_WITH_PROJECTILE = 10;
	public static final int COLLISION_WITH_PLAYER = 11;
	public static final int COLLISION_WITH_ENEMY = 12;
	
	// Vakioita valintaan (ampumisessa)
	public static final int NO_SELECTION = 0;
	public static final int SELECTABLE = 1;
	
	// Tˆrm‰yksentunnistuksen muuttujat
	public int collisionType = 0;
	public int collisionRadius = 0;
	
	// Valinnan muuttujat
	public int selectionRadius = 0;
	
	// Liikkeen muuttujat
	private int movementSpeed = 1; // Kuinka monta yksikkˆ‰ objekti liikkuu kerrallaan. Arvot v‰lill‰ 0-5
	private int movementDelay = 20; // Arvot v‰lill‰ 5-100(ms), mit‰ suurempi sit‰ hitaampi kiihtyvyys
	private int movementAcceleration = 0; // Liikkeen kiihtyminen ja hidastuminen
	
	// Suunnan ja k‰‰ntymisen muuttujat
	public  int direction = 0; // 0 on suoraan ylˆsp‰in, 90 oikealle
	private int turningDelay = 100; // Arvot v‰lill‰ 5-100(ms), mit‰ suurempi sit‰ hitaampi k‰‰ntyminen
	private int turningAcceleration = 0; // K‰‰ntymisen kiihtyvyys
	public  int turningDirection = 0; // 0 ei k‰‰nny, 1 vasen, 2 oikea
	
	// Tallennetaan aika
	private long turningTime = 0;
	private long movementTime = 0;
	
	public GameObject() {
		super();
	}
	
	// Tekee osumalaskennat r‰j‰hdyksiss‰ (ei kahden objektin osumisessa toisiinsa)
	abstract public void triggerImpact(int _damage);
	
	// Tekee osumalaskennat suorassa osumassa toiseen objektiin
	abstract public void triggerCollision(int _eventType, int _damage, int _armorPiercing);
	
	// P‰ivitet‰‰n liikkuminen
	public void updateMovement(long _time) {
		// Lasketaan liikkumisnopeus objektille
		// Mit‰ suurempi movementDelay sit‰ hitaammin objekti liikkuu
		if (_time - movementTime >= movementDelay) {
			movementTime = _time;
			
			x += (Math.cos((direction * Math.PI)/180) * movementSpeed);
			y += (Math.sin((direction * Math.PI)/180) * movementSpeed);
		}
		
		// Lasketaan k‰‰ntymisnopeus objektille
		if (_time - turningTime >= turningDelay) {
			turningTime = _time;
			// Jos objektin k‰‰ntymissuunta on vasemmalle
			if (turningDirection == TO_THE_LEFT) {
				--direction;
				if (direction < 0) {
					direction = 359;
				}
				// 
				turningDelay -= turningAcceleration;
				// Teko‰ly k‰sittelee turningAccelerationin
			}
			// Jos objektin k‰‰ntymissuunta on oikealle
			else if (turningDirection == TO_THE_RIGHT) {
				++direction;
				if (direction == 360) {
					direction = 0;
				}
				// Teko‰ly k‰sittelee turningAccelerationin
				turningDelay -= turningAcceleration;
			}
		}
	}
}

