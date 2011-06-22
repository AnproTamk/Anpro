package fi.tamk.anpro;

/**
 * Sis‰lt‰‰ kaikkien peliobjektien (pelaaja, viholliset, liittolaiset, ammukset yms.)
 * yhteiset tiedot ja toiminnallisuudet.
 * 
 * @extends GfxObject
 */
abstract public class GameObject extends GfxObject
{
    /* Vakioita */
    // K‰‰ntymissuunnat
    public static final int TO_THE_LEFT  = 1;
    public static final int TO_THE_RIGHT = 2;
    
    // Tˆrm‰ystunnistus
    public static final int NO_COLLISION     = 0;
    public static final int CIRCLE_COLLISION = 1;
    
    // Tˆrm‰ystyypit
    public static final int COLLISION_WITH_PROJECTILE = 10;
    public static final int COLLISION_WITH_PLAYER     = 11;
    public static final int COLLISION_WITH_ENEMY      = 12;
    
    // Objektin valinta TouchEnginess‰
    public static final int NO_SELECTION = 0;
    public static final int SELECTABLE   = 1;
    
    /* Tˆrm‰ystunnistus */
    public int collisionType   = 0;
    public int collisionRadius = 0;
    
    /* Valinta */
    public int selectionRadius = 0;
    
    /* Lineaarinen liike */
    protected int movementSpeed        = 2;  // Kuinka monta yksikkˆ‰ objekti liikkuu kerrallaan. Arvot v‰lill‰ 0-5
    protected int movementDelay        = 20; // Arvot v‰lill‰ 5-100(ms), mit‰ suurempi sit‰ hitaampi kiihtyvyys
    protected int movementAcceleration = 0;  // Liikkeen kiihtyminen ja hidastuminen
    
    /* Suunta ja k‰‰ntyminen */
    public  int direction           = 0;  // 0 on suoraan ylˆsp‰in, 90 oikealle
    private int turningDelay        = 20; // Arvot v‰lill‰ 5-100(ms), mit‰ suurempi sit‰ hitaampi k‰‰ntyminen
    private int turningAcceleration = 0;  // K‰‰ntymisen kiihtyvyys
    public  int turningDirection    = 0;  // 0 ei k‰‰nny, 1 vasen, 2 oikea
    
    /* P‰ivitysajat */
    private long turningTime  = 0;
    private long movementTime = 0;
    
    /**
     * Alustaa luokan muuttujat.
     */
    public GameObject()
    {
        super();
    }
    
    /**
     * M‰‰ritt‰‰ objektin aktiiviseksi.
     */
    abstract public void setActive();
    
    /**
     * M‰‰ritt‰‰ objektin ep‰aktiiviseksi.
     */
    abstract public void setUnactive();

    /**
     * K‰sittelee r‰j‰hdyksien vaikutukset pelaajaan.
     * 
     * @param int R‰j‰hdyksen aiheuttama vahinko
     */
    abstract public void triggerImpact(int _damage);

    /**
     * K‰sittelee tˆrm‰yksien vaikutukset objektiin.
     * 
     * @param int Osuman tyyppi, eli mihin tˆrm‰ttiin (tyypit lˆytyv‰t GameObjectista)
     * @param int Osuman aiheuttama vahinko
     * @param int Osuman kyky l‰p‰ist‰ suojat (k‰ytet‰‰n, kun tˆrm‰ttiin ammukseen)
     */
    abstract public void triggerCollision(int _eventType, int _damage, int _armorPiercing);
    
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
            
            x += Math.cos((direction * Math.PI)/180) * movementSpeed * Options.scale;
            y += Math.sin((direction * Math.PI)/180) * movementSpeed * Options.scale;

            // P‰ivitet‰‰n nopeus kiihtyvyyden avulla
            movementDelay -= movementAcceleration;
        }
        
        // Lasketaan k‰‰ntymisnopeus objektille
        if (_time - turningTime >= turningDelay) {
            turningTime = _time;
            // Jos objektin k‰‰ntymissuunta on vasemmalle
            if (turningDirection == TO_THE_LEFT) {
                ++direction;
                if (direction == 360) {
                    direction = 0;
                }
            }
            // Jos objektin k‰‰ntymissuunta on oikealle
            else if (turningDirection == TO_THE_RIGHT) {
                --direction;
                if (direction < 0) {
                    direction = 359;
                }
            }
            
            // P‰ivitet‰‰n nopeus kiihtyvyyden avulla
            turningDelay -= turningAcceleration;
        }
    }
}

