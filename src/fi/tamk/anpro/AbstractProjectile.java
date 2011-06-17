package fi.tamk.anpro;

import javax.microedition.khronos.opengles.GL10;

/**
 * Sis‰lt‰‰ ammusten yhteiset ominaisuudet.
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
    public int damageOnTouch   = 2;
    public int damageOnExplode = 0;
    
    public int damageType = DAMAGE_ON_TOUCH; // EXPLODE_ON_TIMER, EXPLODE_ON_TOUCH tai DAMAGE_ON_TOUCH
    
    public int armorPiercing = 0;
    
    // Passiivinen AoE-vahinko
    public boolean causePassiveDamage = false;
    public int     damageOnRadius     = 0;
    public int     damageRadius       = 0;
    
    // R‰j‰hdyksen ajastus
    public int     explodeTime  = 0;
    public long    startTime    = 0;
    public long    currentTime  = 0;
    
    /* Muut tarvittavat oliot */
    protected Wrapper wrapper;
    
    /* Kohteen tiedot */
    protected int targetX;
    protected int targetY;
    
    /* Ammuksen tila */
    public boolean active = false;
    int listId;                    // Tunnus Wrapperin piirtolistalla

    /**
     * Alustaa luokan muuttujat ja lis‰‰ ammuksen piirtolistalle.
     */
    public AbstractProjectile()
    {
        super();
        
        wrapper = Wrapper.getInstance();
        
        listId = wrapper.addToList(this, Wrapper.CLASS_TYPE_PROJECTILE);
    }
    
    /**
     * M‰‰ritt‰‰ ammuksen aktiiviseksi.
     */
    @Override
    final public void setActive()
    {
        wrapper.projectileStates.set(listId, 1);
        active = true;
    }
    
    /**
     * M‰‰ritt‰‰ ammuksen ep‰aktiiviseksi.
     */
    @Override
    final public void setUnactive()
    {
        wrapper.projectileStates.set(listId, 0);
        active = false;
    }
    
    /**
     * K‰sittelee r‰j‰hdyksien vaikutukset.
     * 
     * @param int Vahinko
     */
    @Override
    public void triggerImpact(int _damage)
    {
        // R‰j‰hdykset eiv‰t toistaiseksi vaikuta ammuksiin
    }
    
    /**
     * K‰sittelee tˆrm‰yksien vaikutukset.
     * 
     * @param int Tˆrm‰ystyyppi
     * @param int Vahinko
     * @param int Panssarinl‰p‰isykyky
     */
    @Override
    public void triggerCollision(int _eventType, int _damage, int _armorPiercing)
    {
        // Aseiden teko‰ly tarkistaa tˆrm‰ykset muihin objekteihin ja kutsuu niiden
    	// triggerCollision-funktioita. Teko‰ly myˆs poistaa ammuksen heti k‰ytˆst‰,
    	// jolloin t‰t‰ funktiota ei tarvitse kutsua.
    }

    /**
     * Piirt‰‰ ammuksen ruudulle.
     * 
     * @param GL10 OpenGL-konteksti
     */
    public final void draw(GL10 _gl)
    {
        if (usedAnimation >= 0){
            GLRenderer.projectileAnimations.get(usedAnimation).draw(_gl, x, y, direction, currentFrame);
            //animations.get(usedAnimation).draw(_gl, x, y, direction, currentFrame);
        }
        else{
            GLRenderer.projectileTextures.get(usedTexture).draw(_gl, x, y, direction);
            //textures.get(usedTexture).draw(_gl, x, y, direction);
        }
    }
    
    /**
     * Aktivoi ammuksen, eli m‰‰ritt‰‰ sen aloituspisteen, kohteen, suunnan ja lis‰‰
     * ammuksen Wrapperin piirtolistalle.
     * 
     * @param int Kohteen X-koordinaatti
     * @param int Kohteen Y-koordinaatti
     */
    public final void activate(int _x, int _y)
    {
        // Ladataan aloitusaika, mik‰li ammuksen on r‰j‰hdett‰v‰ tietyn ajan kuluessa
        if (explodeTime > 0) {
            startTime = android.os.SystemClock.uptimeMillis();
        }
        
        // Asetetaan aloituspiste
        x = wrapper.player.x;
        y = wrapper.player.y;
        
        // Tallennetaan kohteen koordinaatit
        targetX = _x;
        targetY = _y;
        
        // M‰‰ritet‰‰n aloitussuunta
        setDirection();
        
        // Aktivoidaan ammus
        wrapper.projectileStates.set(listId, 1);
        active = true;
    }
    
    /**
     * K‰sittelee ammuksen teko‰lyn.
     */
    public final void handleAi()
    {
        /* Tarkistetaan osumat vihollisiin */
        for (int i = wrapper.enemies.size()-1; i >= 0; --i) {
        	
        	// Tarkistetaan, onko vihollinen aktiivinen
            if (wrapper.enemyStates.get(i) == 1) {
            	
            	// Lasketaan et‰isyys pelaajaan
	            double distance = Math.sqrt(Math.pow(x - wrapper.enemies.get(i).x,2) + Math.pow(y - wrapper.enemies.get(i).y, 2));
	            
	            // Aiheutetaan osuma/r‰j‰hdys, mik‰li et‰isyys on tarpeeksi pieni
	            if (distance - wrapper.enemies.get(i).collisionRadius - collisionRadius <= 0) {
	                if (damageType == ProjectileLaser.DAMAGE_ON_TOUCH) {
	                    wrapper.enemies.get(i).triggerCollision(GameObject.COLLISION_WITH_PROJECTILE, damageOnTouch, armorPiercing);
	                }
	                else if (damageType == ProjectileLaser.EXPLODE_ON_TOUCH) {
	                    causeExplosion();
	                }
	
	                // Asetetaan ammus ep‰aktiiviseksi
	                setUnactive();
	                break;
	            }
	            
	            // K‰sitell‰‰n passiivinen vahinko
	            if (distance - wrapper.enemies.get(i).collisionRadius - damageRadius <= 0) {
	                wrapper.enemies.get(i).health -= (damageOnRadius * (1 - 0.15 * wrapper.enemies.get(i).defence));
	            }
            }
        }
        
        /* Tarkistetaan ajastetut r‰j‰hdykset */
        if (explodeTime > 0) {
            currentTime = android.os.SystemClock.uptimeMillis();
            
            if (currentTime - startTime >= explodeTime) {
                causeExplosion();
                setUnactive();
            }
        }
        
        /* Tarkistetaan suunta ja k‰‰ntyminen */
        //...
        
        /* K‰sitell‰‰n reuna-alueet panosten tuhoamiseksi */
        if (wrapper.player.x + x < -400 || wrapper.player.x + x > 400 ||
        	wrapper.player.y + y < -240 || wrapper.player.y + y > 240 ) {
        	setUnactive();
        }
    }

    /**
     * M‰‰ritt‰‰ ammuksen aloitussuunnan.
     */
    abstract protected void setDirection();
    
    /**
     * Etsii r‰j‰hdyksen vaikutusalueella olevia vihollisia ja kutsuu niiden triggerImpact-funktiota.
     */
    abstract protected void causeExplosion();
}
