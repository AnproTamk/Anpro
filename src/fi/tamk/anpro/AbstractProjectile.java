package fi.tamk.anpro;

import javax.microedition.khronos.opengles.GL10;

/**
 * Sis�lt�� ammusten yhteiset ominaisuudet.
 * 
 * @extends GameObject
 */
abstract public class AbstractProjectile extends GameObject
{
    /* Ammusten toimintatavat */
    public static final int MULTIPLY_ON_TIMER = 1;
    public static final int MULTIPLY_ON_TOUCH = 2;
    public static final int EXPLODE_ON_TIMER  = 3;
    public static final int EXPLODE_ON_TOUCH  = 4;
    public static final int DAMAGE_ON_TOUCH   = 5;
    
    /* Aseen tiedot */
    protected int weaponId;
    
    // Vahinko ja sen tyyppi
    public int damageOnTouch   = 2;
    public int damageOnExplode = 0;
    public int damageType      = DAMAGE_ON_TOUCH; // EXPLODE_ON_TIMER, EXPLODE_ON_TOUCH tai DAMAGE_ON_TOUCH
    
    // R�j�ht�minen kohteen p��ll�
    public boolean explodeOnTarget = false;
    
    // Panssarien l�p�isykyky
    public int armorPiercing = 0;
    
    // Passiivinen AoE-vahinko
    public boolean causePassiveDamage = false;
    public int     damageOnRadius     = 0;
    public int     damageRadius       = 0;
    
    // R�j�hdyksen ajastus
    private int  explodeTime  = 0;
    private long startTime    = 0;
    private long currentTime  = 0;
    
    // R�j�hdyksen vaikutusalue
    protected int explosionRadius = 0;
    
    /* Muut tarvittavat oliot */
    protected Wrapper wrapper;
    protected AbstractWeapon parent;
    
    /* Kohteen tiedot */
    protected int targetX;
    protected int targetY;
    
    /* Ammuksen tila */
    public    boolean active = false; // Aktiivisuusmuuttuja aseluokkia varten
    protected int     listId;         // Tunnus Wrapperin piirtolistalla
    private   int     priority;
    protected int     userType;
    
    /* Ammuksen teko�ly */
    protected AbstractAi ai;

    /**
     * Alustaa luokan muuttujat ja lis�� ammuksen piirtolistalle.
     */
    public AbstractProjectile(int _ai, int _userType)
    {
        super();
        
        userType = _userType;
        
        wrapper = Wrapper.getInstance();
        
        priority = 1; // TODO: T�m� pit�� tarkistaa AI:n perusteella ja jokaiselle ammukselle erikseen!
        listId = wrapper.addToList(this, Wrapper.CLASS_TYPE_PROJECTILE, priority);
        
        if (_ai == AbstractAi.NO_AI) {
        	ai = null;
        }
        else if (_ai == AbstractAi.LINEAR_PROJECTILE_AI) {
        	ai = new LinearProjectileAi(listId, Wrapper.CLASS_TYPE_PROJECTILE);
        }
        else if (_ai == AbstractAi.TRACKING_PROJECTILE_AI) {
        	ai = new TrackingProjectileAi(listId, Wrapper.CLASS_TYPE_PROJECTILE);
        }
    }
    
    /**
     * M��ritt�� ammuksen aktiiviseksi.
     */
    @Override
    final public void setActive()
    {
        wrapper.projectileStates.set(listId, Wrapper.FULL_ACTIVITY);
        active = true;
    }
    
    /**
     * M��ritt�� ammuksen ep�aktiiviseksi.
     */
    @Override
    final public void setUnactive()
    {
        wrapper.projectileStates.set(listId, Wrapper.INACTIVE);
        active = false;
        
        ai.setUnactive();
    }
    
    /**
     * K�sittelee r�j�hdyksien vaikutukset.
     * 
     * @param int Vahinko
     */
    @Override
    public void triggerImpact(int _damage)
    {
        // R�j�hdykset eiv�t toistaiseksi vaikuta ammuksiin
    }
    
    /**
     * K�sittelee t�rm�yksien vaikutukset.
     * 
     * @param int T�rm�ystyyppi
     * @param int Vahinko
     * @param int Panssarinl�p�isykyky
     */
    @Override
    public void triggerCollision(int _eventType, int _damage, int _armorPiercing)
    {
        // Aseiden teko�ly tarkistaa t�rm�ykset muihin objekteihin ja kutsuu niiden
        // triggerCollision-funktioita. Teko�ly my�s poistaa ammuksen heti k�yt�st�,
        // jolloin t�t� funktiota ei tarvitse kutsua.
    }

    /**
     * Piirt�� ammuksen ruudulle.
     * 
     * @param GL10 OpenGL-konteksti
     */
    public final void draw(GL10 _gl)
    {
        if (usedAnimation >= 0) {
            GLRenderer.projectileAnimations[weaponId][usedAnimation].draw(_gl, x, y, direction, currentFrame);
        }
        else {
            GLRenderer.projectileTextures[weaponId][usedTexture].draw(_gl, x, y, direction);
        }
    }
    
    /**
     * Aktivoi ammuksen, eli m��ritt�� sen aloituspisteen, kohteen, suunnan ja lis��
     * ammuksen Wrapperin piirtolistalle.
     * 
     * @param int     Kohteen X-koordinaatti
     * @param int     Kohteen Y-koordinaatti
     * @param boolean Onko ammuksen tarkoitus aktivoida erikoistoiminto heti (esim. EMP)?
     */
    public final void activate(int _x, int _y, boolean _explodeOnTarget, boolean _autoSpecial, AbstractWeapon _parent, float _startX, float _startY)
    {
        // Ladataan aloitusaika, mik�li ammuksen on r�j�hdett�v� tietyn ajan kuluessa
        if (explodeTime > 0) {
            startTime = android.os.SystemClock.uptimeMillis();
        }
        
        // Asetetaan aloituspiste
        x = _startX;
        y = _startY;
        
        // Tallennetaan is�nt�luokan osoitisn 
        parent = _parent;
        
        // Tallennetaan kohteen koordinaatit
        targetX = _x;
        targetY = _y;
        
        // Otetaan kohteessa r�j�ht�minen k�ytt��n
        explodeOnTarget = _explodeOnTarget;
        
        // Aktivoidaan ammus
        wrapper.projectileStates.set(listId, 1);
        active = true;
        ai.setActive(_x, _y);
        
        // Aktivoidaan erikoistoiminto
        if (_autoSpecial) {
        	triggerSpecialAction();
        }
    }
    
    public final void activate(int _direction, boolean _explodeOnTarget, boolean _autoSpecial, AbstractWeapon _parent, float _startX, float _startY)
    {
        // Ladataan aloitusaika, mik�li ammuksen on r�j�hdett�v� tietyn ajan kuluessa
        if (explodeTime > 0) {
            startTime = android.os.SystemClock.uptimeMillis();
        }
        
        // Asetetaan aloituspiste
        x = _startX;
        y = _startY;
        
        // Tallennetaan is�nt�luokan osoitisn 
        parent = _parent;
        
        // M��ritet��n aloitussuunta
        direction = _direction;
        
        // Poistetaan kohteessa r�j�ht�minen k�yt�st�
        explodeOnTarget = _explodeOnTarget;
        
        // Aktivoidaan ammus
        wrapper.projectileStates.set(listId, Wrapper.FULL_ACTIVITY);
        active = true;
        ai.setActive(_direction);
        
        // Aktivoidaan erikoistoiminto
        if (_autoSpecial) {
        	triggerSpecialAction();
        }
    }
    
    /**
     * K�sittelee ammuksen t�rm�ystarkistukset.
     */
    public final void checkCollision()
    {
    	/* Tarkistetaan r�j�hdys kohteessa */
    	if (explodeOnTarget) {
    		double distance = Math.sqrt(Math.pow(x - targetX, 2) + Math.pow(y - targetY, 2));
    		
    		if (distance - collisionRadius - 20 <= 0) {
    			setUnactive();
	    	    active = false;
	    		parent.triggerCluster(8, x, y);
    		}
    	}
    		
        /* Tarkistetaan osumat vihollisiin */
        for (int i = wrapper.enemies.size()-1; i >= 0; --i) {
            
            // Tarkistetaan, onko vihollinen aktiivinen
            if (wrapper.enemyStates.get(i) == Wrapper.FULL_ACTIVITY) {
                
                // Lasketaan et�isyys pelaajaan
                double distance = Math.sqrt(Math.pow(x - wrapper.enemies.get(i).x,2) + Math.pow(y - wrapper.enemies.get(i).y, 2));
                
                // Aiheutetaan osuma/r�j�hdys, mik�li et�isyys on tarpeeksi pieni
                if (distance - wrapper.enemies.get(i).collisionRadius - collisionRadius <= 0) {
                    wrapper.projectileStates.set(listId, 2);
                    
                    if (damageType == ProjectileLaser.DAMAGE_ON_TOUCH) {
                        wrapper.enemies.get(i).triggerCollision(GameObject.COLLISION_WITH_PROJECTILE, damageOnTouch, armorPiercing);

                    	if (explodeOnTarget) {
                		    setUnactive();
            	    	    active = false;
            	    		parent.triggerCluster(8, x, y);
                    	}
                    	
                        setAction(GLRenderer.ANIMATION_DESTROY, 1, 1, 1);
                    }
                    else if (damageType == ProjectileLaser.EXPLODE_ON_TOUCH) {
                        setAction(GLRenderer.ANIMATION_DESTROY, 1, 1, 1);

                        causeExplosion();
                    }
                    
                    break;
                }
                
                // K�sitell��n passiivinen vahinko
                if (distance - wrapper.enemies.get(i).collisionRadius - damageRadius <= 0) {
                    wrapper.enemies.get(i).health -= (damageOnRadius * (1 - 0.15 * wrapper.enemies.get(i).defence));
                }
            }
        }
        
        /* Tarkistetaan ajastetut r�j�hdykset */
        if (explodeTime > 0) {
            currentTime = android.os.SystemClock.uptimeMillis();
            
            if (currentTime - startTime >= explodeTime) {
                wrapper.projectileStates.set(listId, 2);
                setAction(GLRenderer.ANIMATION_DESTROY, 1, 1, 1);
                
                causeExplosion();
            }
        }
        
        /* Tarkistetaan suunta ja k��ntyminen */
        //...
        
        /* K�sitell��n reuna-alueet panosten tuhoamiseksi */
        if (wrapper.player.x + x < -Options.scaledScreenWidth || wrapper.player.x + x > Options.scaledScreenWidth ||
            wrapper.player.y + y < -Options.scaledScreenHeight || wrapper.player.y + y > Options.scaledScreenHeight ) {
            setUnactive();
        }
    }

    /**
     * K�sittelee jonkin toiminnon p��ttymisen. Kutsutaan animaation loputtua, mik�li
     * actionActivated on TRUE.
     * 
     * (lue lis�� GfxObject-luokasta!)
     */
    @Override
    protected void triggerEndOfAction()
    {
        /* Tuhoutuminen */
        if (actionId == 1) {
            setUnactive();
            active = false;
        }
    }
    
    /**
     * Etsii r�j�hdyksen vaikutusalueella olevia vihollisia ja kutsuu niiden triggerImpact-funktiota.
     */
    private final void causeExplosion()
    {
        // Tarkistetaan et�isyydet
        // Kutsutaan osumatarkistuksia tarvittaessa
        for (int i = wrapper.enemies.size() - 1; i >= 0; --i) {
            if (wrapper.enemyStates.get(i) == 1 || wrapper.enemyStates.get(i) == 3) {
                int distance = (int) Math.sqrt(Math.pow(x - wrapper.enemies.get(i).x, 2) + Math.pow(y - wrapper.enemies.get(i).y, 2));

                if (distance - wrapper.enemies.get(i).collisionRadius - explosionRadius <= 0) {
                    // Osuma ja r�j�hdys
                    wrapper.enemies.get(i).triggerImpact(damageOnExplode);
                }
            }
        }
    }
    
    /**
     * Aiheuttaa ammuksen erikoistoiminnon.
     */
    protected void triggerSpecialAction() { }
}
