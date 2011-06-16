package fi.tamk.anpro;

import javax.microedition.khronos.opengles.GL10;

abstract public class AbstractProjectile extends GameObject {
    /** Vakioita ammuksen tietoja varten */
    // Efektit
    public static final int MULTIPLY_ON_TIMER = 1;
    public static final int MULTIPLY_ON_TOUCH = 2;
    public static final int EXPLODE_ON_TIMER  = 3;
    public static final int EXPLODE_ON_TOUCH  = 4;
    public static final int DAMAGE_ON_TOUCH   = 5;
    
    // Aseiden tiedot
    public int damageOnTouch   = 2;
    public int damageOnExplode = 0;
    
    public int damageType = DAMAGE_ON_TOUCH; // EXPLODE_ON_TIMER, EXPLODE_ON_TOUCH tai DAMAGE_ON_TOUCH
    
    public int armorPiercing = 0;
    
    public boolean causePassiveDamage = false;
    public int     damageOnRadius     = 0;
    public int     damageRadius       = 0; // Passiiviselle AoE-vahingolle
    
    public int     explodeTime  = 0;
    public long    startTime    = 0;
    public long    currentTime  = 0;
    
    // Wrapper
    protected Wrapper wrapper;
    
    // Kohteen tiedot
    protected int targetX;
    protected int targetY;
    
    public boolean active = false;
    
    int listId;

    /*
     * Rakentaja
     */
    public AbstractProjectile() {
        super();
        
        wrapper = Wrapper.getInstance();
        
        listId = wrapper.addToList(this, Wrapper.CLASS_TYPE_PROJECTILE);
    }
    
    @Override
    final public void setActive() {
        wrapper.projectileStates.set(listId, 1);
        active = true;
    }
    
    @Override
    final public void setUnactive() {
        wrapper.projectileStates.set(listId, 0);
        active = false;
    }
    
    @Override
    public void triggerImpact(int _damage) {
        // TODO Auto-generated method stub
        
    }
    @Override
    public void triggerCollision(int _eventType, int _damage, int _armorPiercing) {
        // TODO Auto-generated method stub
        
    }

    public final void draw(GL10 _gl) {
        if (usedAnimation >= 0){
            GLRenderer.projectileAnimations.get(usedAnimation).draw(_gl, x, y, direction, currentFrame);
            //animations.get(usedAnimation).draw(_gl, x, y, direction, currentFrame);
        }
        else{
            GLRenderer.projectileTextures.get(usedTexture).draw(_gl, x, y, direction);
            //textures.get(usedTexture).draw(_gl, x, y, direction);
        }
    }
    
    abstract public void activate(int _x, int _y);
    
    abstract public void handleAi();
}
