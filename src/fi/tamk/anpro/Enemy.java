package fi.tamk.anpro;

import javax.microedition.khronos.opengles.GL10;

/**
 * Sisältää vihollisen omat ominaisuudet ja tiedot, kuten asettamisen aktiiviseksi ja
 * epäaktiiviseksi, piirtämisen ja törmäyksenhallinnan (ei tunnistusta).
 * 
 * @extends GameObject
 */
public class Enemy extends GameObject
{
    /* Alkuperäiset tiedot (maksimit) */
    public int attackMax;
    public int speedMax;
    public int defenceMax;
    public int healthMax;
    
    /* Nykyiset tiedot */
    public int attack;
    public int speed;
    public int defence;
    public int health;
    
    public int rank;
    
    /* Tekoäly */
    public AbstractAi ai;
    
    /* Muut tarvittavat oliot */
    private Wrapper wrapper;
    
    /* Vihollisen tila */
    private int     listId;

    /**
     * Alustaa luokan muuttujat.
     * 
     * @param int Elämät/kestävyys
     * @param int Puolustus
     * @param int Nopeus
     * @param int Hyökkäysvoima törmätessä pelaajaan
     * @param int Taso
     */
    public Enemy(int _health, int _defence, int _speed, int _attack, int _ai, int _rank)
    {
        super();
        
        /* Tallennetaan tiedot */
        healthMax  = _health;
        health     = _health;
        attackMax  = _attack;
        attack     = _attack;
        speedMax   = _speed;
        speed      = _speed;
        defenceMax = _defence;
        defence    = _defence;
        
        rank       = _rank;
        
        /* Asetetaan törmäysetäisyys */
        if (rank == 1) {
            collisionRadius = 20;
        }
        else if (rank == 2) {
            
        }
    
        /* Haetaan animaatioiden pituudet */
        animationLength = new int[3];

        try {
        	for (int i = 0; i < 4; ++i) {
        		animationLength[i] = GLRenderer.enemyAnimations[rank-1][i].length;
        	}
        }
        catch (Exception e) {
            // Animaatioita ei oltu luotu. Jatketaan eteenpäin.
        }
        
        /* Otetaan Wrapper käyttöön */
        wrapper = Wrapper.getInstance();
        
        /* Lisätään objekti piirtolistalle ja otetaan tekoäly käyttöön */
        listId = wrapper.addToList(this, Wrapper.CLASS_TYPE_ENEMY);
        
        if (_ai == 0) {
        	ai = new LinearAi(listId);
        }
        else {
        	ai = new LinearAi(listId);
        }
    }

    /**
     * Määrittää vihollisen aktiiviseksi.
     */
    @Override
    public final void setActive()
    {
        wrapper.enemyStates.set(listId, 1);
    }

    /**
     * Määrittää vihollisen epäaktiiviseksi.
     */
    @Override
    public final void setUnactive()
    {
        wrapper.enemyStates.set(listId, 0);
    }
    
    /**
     * Piirtää vihollisen käytössä olevan tekstuurin tai animaation ruudulle.
     * 
     * @param GL10 OpenGL-konteksti
     */
    public final void draw(GL10 _gl)
    {
        // Tarkistaa onko animaatio päällä ja kutsuu oikeaa animaatiota tai tekstuuria
        if (usedAnimation >= 0){
            GLRenderer.enemyAnimations[rank-1][usedAnimation].draw(_gl, x, y, direction, currentFrame);
        }
        else{
            GLRenderer.enemyTextures[rank-1][usedTexture].draw(_gl, x, y, direction);
        }
    }
    
    /**
     * Käsittelee räjähdyksien aiheuttamat osumat.
     * 
     * @param int Vahinko
     */
    @Override
    public final void triggerImpact(int _damage)
    {
        health -= (int)((float)_damage * (1 - 0.15 * (float)defence));
        
        if (health <= 0) {
            setUnactive();
        }
    }
    
    /**
     * Käsitelee törmäykset pelaajan ja ammusten kanssa.
     * 
     * @param int Törmäystyyppi
     * @param int Vahinko
     * @param int Panssarinläpäisykyky
     */
    @Override
    public final void triggerCollision(int _eventType, int _damage, int _armorPiercing)
    {
        if (_eventType == GameObject.COLLISION_WITH_PROJECTILE) {
            health -= (int)((float)_damage * (1 - 0.15 * (float)defence + 0.1 * (float)_armorPiercing));
            
            if (health <= 0) {
                setUnactive();
            }
        }
        else if (_eventType == GameObject.COLLISION_WITH_PLAYER) {
            setUnactive();
        }
    }

    /**
     * Asettaa vihollisen tiedot. Käytetään, kun vihollisen tasoa halutaan nostaa, ei vihollista
     * luodessa.
     * 
     * @param int Elämät/kestävyys
     * @param int Nopeus
     * @param int Hyökkäysvoima törmätessä pelaajaan
     * @param int Puolustus
     * @param int Tekoälyn tunnus
     * @param int Taso
     */
    public final void setStats(int _health, int _speed, int _attack, int _defence, int _ai, int _rank)
    {
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
