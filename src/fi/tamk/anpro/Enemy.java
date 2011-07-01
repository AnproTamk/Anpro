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
    /* Vihollisen tiedot */
    public int attack;
    public int rank;
    
    /* Tekoäly */
    public AbstractAi ai;
    
    /* Muut tarvittavat oliot */
    private Wrapper wrapper;
    
    /* Vihollisen tila */
    private int listId;
    private int priority;

    /**
     * Alustaa luokan muuttujat.
     * 
     * @param int Elämät/kestävyys
     * @param int Puolustus
     * @param int Nopeus
     * @param int Hyökkäysvoima törmätessä pelaajaan
     * @param int Taso
     */
    public Enemy(int _health, int _defence, int _speed, int _attack, int _ai, int _rank, WeaponManager _weaponManager)
    {
        super(_speed);
        
        /* Tallennetaan tiedot */
        health     = _health;
        attack     = _attack;
        defence    = _defence;
        
        rank       = _rank;
        
        /* Asetetaan törmäysetäisyys */
        if (rank == 1) {
            collisionRadius = (int) (20 * Options.scale);
        }
        else if (rank == 2) {
            // TODO:
        }
    
        /* Haetaan animaatioiden pituudet */
        animationLength = new int[GLRenderer.AMOUNT_OF_ENEMY_ANIMATIONS];
        
        for (int i = 0; i < GLRenderer.AMOUNT_OF_ENEMY_ANIMATIONS; ++i) {
            if (GLRenderer.enemyAnimations[rank-1][i] != null) {
                animationLength[i] = GLRenderer.enemyAnimations[rank-1][i].length;
            }
        }
        
        /* Otetaan Wrapper käyttöön */
        wrapper = Wrapper.getInstance();
        
        /* Lisätään objekti piirtolistalle ja otetaan tekoäly käyttöön */
        if (_ai == 0) {
            listId = wrapper.addToList(this, Wrapper.CLASS_TYPE_ENEMY, 4);
            ai = new RotaryAi(listId, Wrapper.CLASS_TYPE_ENEMY);
        }
        /*
        else if (_ai == 2) {
            listId = wrapper.addToList(this, Wrapper.CLASS_TYPE_ENEMY, 4);
            ai = new SguigglyAi(listId);
        }
        else if (_ai == 3) {
            listId = wrapper.addToList(this, Wrapper.CLASS_TYPE_ENEMY, 3);
            ai = new ApproachAndStopAi(listId);
        }
        else if (_ai == 4) {
            listId = wrapper.addToList(this, Wrapper.CLASS_TYPE_ENEMY, 1);
            ai = new RotaryAi(listId);
        }
        */
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
        	triggerDestroyed();
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
            	triggerDestroyed();
            }
        }
        else if (_eventType == GameObject.COLLISION_WITH_PLAYER) {
        	triggerDestroyed();
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
        health     = _health;
        speed      = _speed;
        attack     = _attack;
        defence    = _defence;
        rank       = _rank;

        // Otetaan uusi tekoäly käyttöön
        ai = null;

        if (_ai == 1) {
            ai = new LinearAi(listId, Wrapper.CLASS_TYPE_ENEMY);
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

    /**
     * Käsittelee jonkin toiminnon päättymisen. Kutsutaan animaation loputtua, mikäli
     * actionActivated on TRUE.
     * 
     * (lue lisää GfxObject-luokasta!)
     */
    @Override
    protected void triggerEndOfAction()
    {
        /* Tuhotaan vihollinen */
        if (actionId == 1) {
        	--SurvivalMode.enemiesLeft;
        	SurvivalMode.updateScore(rank);
            setUnactive();
        }
        /* Aktivoidaan vihollinen (EMPin jälkeen) */
        else if (actionId == 2) {
        	wrapper.enemyStates.set(listId, 1);
        }
        
    	movementAcceleration = 0;
    	setMovementDelay(1.0f);
    	setMovementSpeed(1.0f);
    }

	public void triggerDisabled()
	{
    	wrapper.enemyStates.set(listId, 3);

    	movementAcceleration = -15;
    	turningDirection     = 0;
    	
        setAction(GLRenderer.ANIMATION_DISABLED, 1, 8, 2);
	}

	public void triggerDestroyed()
	{
    	wrapper.enemyStates.set(listId, 3);

    	movementAcceleration = -15;
    	
        setAction(GLRenderer.ANIMATION_DESTROY, 1, 1, 1);
	}
}
