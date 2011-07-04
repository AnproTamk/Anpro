package fi.tamk.anpro;

/**
 * Sis‰lt‰‰ kaikkien peliobjektien (pelaaja, viholliset, liittolaiset, ammukset yms.)
 * yhteiset tiedot ja toiminnallisuudet.
 * 
 * @extends GfxObject
 */
abstract public class GameObject extends GfxObject
{
    /* Objektin tiedot (kaikille) */
    public int speed;
    
    /* Objektin tiedot (pelaajalle, vihollisille ja liittolaisille) */
    public int defence;
    public int health;
    public int currentHealth;
    	
    /* Vakioita */
    // K‰‰ntymissuunnat
    public static final int TO_THE_LEFT  = 1;
    public static final int TO_THE_RIGHT = 2;
    
    // Tˆrm‰ystyypit
    public static final int COLLISION_WITH_PROJECTILE = 10;
    public static final int COLLISION_WITH_PLAYER     = 11;
    public static final int COLLISION_WITH_ENEMY      = 12;
    
    /* Tˆrm‰ystunnistus */
    public int collisionRadius = 0;
    
    /* Lineaarinen liike */
    protected int movementSpeed;            // Kuinka monta yksikkˆ‰ objekti liikkuu kerrallaan. Arvot v‰lill‰ 0-5
    protected int movementDelay;            // Arvot v‰lill‰ 5-100(ms), mit‰ suurempi sit‰ hitaampi kiihtyvyys
    protected int movementAcceleration = 0; // Liikkeen kiihtyminen ja hidastuminen
    
    /* Suunta ja k‰‰ntyminen */
    public    int direction           = 0; // 0 on suoraan ylˆsp‰in, 90 oikealle
    protected int turningSpeed;            // Montako astetta k‰‰nnyt‰‰n per p‰ivitys
    protected int turningDelay;            // Arvot v‰lill‰ 5-100(ms), mit‰ suurempi sit‰ hitaampi k‰‰ntyminen
    protected int turningAcceleration = 0; // K‰‰ntymisen kiihtyvyys
    public    int turningDirection    = 0; // 0 ei k‰‰nny, 1 vasen, 2 oikea
    
    /* P‰ivitysajat */
    private long turningTime  = 0;
    private long movementTime = 0;
    
    /**
     * Alustaa luokan muuttujat.
     */
    public GameObject(int _speed)
    {
        super();
        
        speed = _speed;
        
        setMovementSpeed(1.0f);
        setMovementDelay(1.0f);
            
        setTurningSpeed(1.0f);
        setTurningDelay(1.0f);
    }
    
    /**
     * M‰‰ritt‰‰ objektin aktiiviseksi.
     */
    abstract public void setActive();
    
    /**
     * M‰‰ritt‰‰ objektin ep‰aktiiviseksi. Sammuttaa myˆs teko‰lyn jos se on tarpeen.
     */
    abstract public void setUnactive();

    /**
     * K‰sittelee r‰j‰hdyksien vaikutukset objektiin.
     * 
     * @param int R‰j‰hdyksen aiheuttama vahinko
     */
    public void triggerImpact(int _damage) { }

    /**
     * K‰sittelee tˆrm‰yksien vaikutukset objektiin.
     * 
     * @param int Osuman tyyppi, eli mihin tˆrm‰ttiin (tyypit lˆytyv‰t GameObjectista)
     * @param int Osuman aiheuttama vahinko
     * @param int Osuman kyky l‰p‰ist‰ suojat (k‰ytet‰‰n, kun tˆrm‰ttiin ammukseen)
     */
    public void triggerCollision(int _eventType, int _damage, int _armorPiercing) { }
    
    /**
     * P‰ivitt‰‰ liikkumisen ja k‰‰ntymisen.
     * 
     * @param long T‰m‰n hetkinen aika
     */
    public void updateMovement(long _time)
    {
        // Lasketaan liikkumisnopeus objektille
        // Mit‰ suurempi movementDelay sit‰ hitaammin objekti liikkuu
        if (_time - movementTime >= movementDelay) {
            movementTime = _time;
            
            x += Math.cos((direction * Math.PI)/180) * movementSpeed * Options.scaleX;
            y += Math.sin((direction * Math.PI)/180) * movementSpeed * Options.scaleY;

            // P‰ivitet‰‰n nopeus kiihtyvyyden avulla
            movementDelay -= movementAcceleration;
            
            if (movementDelay < 0) {
                movementDelay = 0;
            }
        }
        
        // Lasketaan k‰‰ntymisnopeus objektille
        if (_time - turningTime >= turningDelay) {
            turningTime = _time;
            // Jos objektin k‰‰ntymissuunta on vasemmalle
            if (turningDirection == TO_THE_LEFT) {
                direction += turningSpeed;
                
                if (direction == 360) {
                    direction = 0;
                }
            }
            // Jos objektin k‰‰ntymissuunta on oikealle
            else if (turningDirection == TO_THE_RIGHT) {
                direction -= turningSpeed;
                
                if (direction < 0) {
                    direction = 359;
                }
            }
            
            // P‰ivitet‰‰n nopeus kiihtyvyyden avulla
            turningDelay -= turningAcceleration;
            
            if (turningDelay < 0) {
                turningDelay = 0;
            }
        }
    }
    
    /**
     * Laskee objektille "nopeuden" (pikselien m‰‰r‰ / liike).
     * 
     * @param float Nopeuden muutoskerroin
     */
    public final void setMovementSpeed(float _multiplier)
    {
    	movementSpeed = (int) (_multiplier * speed);
    }

    /**
     * Laskee objektille liikkeen viiveen. 
     * 
     * @param float Nopeuden muutoskerroin
     */
    public final void setMovementDelay(float _multiplier)
    {
    	movementDelay = (int) (80 / (_multiplier * speed));
    }
    
    /**
     * Laskee objektille "k‰‰ntymisnopeuden" (asteiden m‰‰r‰ / liike).
     * 
     * @param float Nopeuden muutoskerroin
     */
    public final void setTurningSpeed(float _multiplier)
    {
    	turningSpeed = (int) (_multiplier * speed);
    }

    /**
     * Laskee objektille k‰‰ntymisen viiveen. 
     * 
     * @param float Nopeuden muutoskerroin
     */
    public final void setTurningDelay(float _multiplier)
    {
    	turningDelay = (int) (60 / (_multiplier * speed));
    }
}

