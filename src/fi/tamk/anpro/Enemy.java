package fi.tamk.anpro;

import javax.microedition.khronos.opengles.GL10;

public class Enemy extends GameObject
{
    public int attackMax;
    public int speedMax;
    public int defenceMax;
    public int healthMax;
    
    public int attack;
    public int speed;
    public int defence;
    public int health;
    
    public int rank;
    
    public AbstractAi ai;
    
    //ArrayList<Animation> animations;
    //ArrayList<Texture> textures;
    
    private Wrapper wrapper;
    private int     listId;
    
    /*
     * Rakentaja
     */
    public Enemy(int _health, int _defence, int _speed, int _attack, int _rank)
    {
        super();
        healthMax  = _health;
        health     = _health;
        attackMax  = _attack;
        attack     = _attack;
        speedMax   = _speed;
        speed      = _speed;
        defenceMax = _defence;
        defence    = _defence;
        rank       = _rank;
        
        collisionRadius = 15;
    
        animationLength = new int[3];

        /*int offset = rank *3;

        animationLength[0] = wrapper.renderer.playerAnimations.get(offset - 3).length;
        animationLength[1] = wrapper.renderer.playerAnimations.get(offset - 2).length;
        animationLength[2] = wrapper.renderer.playerAnimations.get(offset - 1).length;*/
        
        wrapper = Wrapper.getInstance();
        
        listId = wrapper.addToList(this, Wrapper.CLASS_TYPE_ENEMY);
        
        ai = new LinearAi(listId);
    }

    /*
     * Aktivoi vihollisen
     */
    public final void setActive()
    {
        wrapper.enemyStates.set(listId, 1);
    }

    /*
     * Poistaa vihollisen käytöstä
     */
    public final void setUnactive()
    {
        wrapper.enemyStates.set(listId, 0);
    }
    
    /*
     * Piirtää vihollisen käytössä olevan tekstuurin tai animaation ruudulle
     */
    public final void draw(GL10 _gl)
    {
        // Tarkistaa onko animaatio päällä ja kutsuu oikeaa animaatiota tai tekstuuria
        if (usedAnimation >= 0){
            //GLRenderer.enemyAnimations.get(usedAnimation+3*(rank-1)).draw(_gl, x, y, direction, currentFrame);
        	GLRenderer.enemyAnimations.get(usedAnimation).draw(_gl, x, y, direction, currentFrame);
        }
        else{
            //GLRenderer.enemyTextures.get(usedTexture+2*(rank-1)).draw(_gl, x, y, direction);
        	GLRenderer.enemyTextures.get(usedTexture).draw(_gl, x, y, direction);
        }
    }
    
    /*
     * Käsittelee räjähdyksien aiheuttamat osumat
     */
    public final void triggerImpact(int _damage)
    {
        health -= (int)((float)_damage * (1 - 0.15 * (float)defence));
        
        if (health <= 0) {
            setUnactive();
        }
    }
    
    /*
     * Käsitelee törmäykset pelaajan ja ammusten kanssa
     */
    public final void triggerCollision(int _eventType, int _damage, int _armorPiercing)
    {
        if (_eventType == GameObject.COLLISION_WITH_PROJECTILE) {
            health -= (int)((float)_damage * (1 - 0.15 * (float)defence + 0.1 * (float)_armorPiercing));
            
            //if (health <= 0) {
                setUnactive();
            //}
        }
        else if (_eventType == GameObject.COLLISION_WITH_PLAYER) {
            setUnactive();
        }
    }

    /*
     * Asettaa tiedot
     */
    public final void setStats(int _health, int _speed, int _attack, int _defence, int _ai, int _rank) {
        // Tallennetaan uudet tiedot
        healthMax  = _health;
        health     = _health;
        speedMax   = _speed;
        speed      = _speed;
        attackMax  = _attack;
        attack     = _attack;
        defenceMax = _defence;
        defence    = _defence;
        rank       = _rank;

        // Otetaan uusi tekoäly käyttöön

        ai = null;

        if (_ai == 1) {
            ai = new LinearAi(listId);
        }
        /*
        else if (_ai == 2) {
            ai = new SguigglyAi(listId);
        }
        else if (_ai == 3) {
            ai = new ApproachAndStopAi(listId);
        }
        else if (_ai == 4) {
            ai = new RotaryAi(listId);
        }
        */
    }
}
