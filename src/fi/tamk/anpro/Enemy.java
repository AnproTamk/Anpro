package fi.tamk.anpro;

import javax.microedition.khronos.opengles.GL10;

/**
 * Sisältää vihollisen omat ominaisuudet ja tiedot, kuten asettamisen aktiiviseksi ja
 * epäaktiiviseksi, piirtämisen ja törmäyksenhallinnan (ei tunnistusta).
 */
public class Enemy extends GameObject
{
    /* Vihollisen tiedot */
    public int attack;
    public int rank;
    
    /* Tekoäly */
    public AbstractAi ai;
    
    /* Muut tarvittavat oliot */
    private Wrapper       wrapper;
    private WeaponManager weaponManager;
    
    /* Vihollisen tila */
    private int listId;

    /**
     * Alustaa luokan muuttujat.
     * 
     * @param int           Elämät/kestävyys
     * @param int           Puolustus
     * @param int           Nopeus
     * @param int           Hyökkäysvoima törmätessä pelaajaan
     * @param int           Taso
     * @param WeaponManager Osoitin WeaponManageriin
     */
    public Enemy(int _health, int _defence, int _speed, int _attack, int _ai, int _rank, WeaponManager _weaponManager)
    {
        super(_speed);
        
        // Tallennetaan tiedot
        health        = _health;
        currentHealth = _health;
        attack        = _attack;
        defence       = _defence;
        rank          = _rank;
        
        // Asetetaan törmäysetäisyys
        if (rank == 1) {
            collisionRadius = (int) (20 * Options.scale);
        }
        else if (rank == 2) {
        	collisionRadius = (int) (25 * Options.scale);
        }
    
        // Haetaan animaatioiden pituudet
        animationLength = new int[GLRenderer.AMOUNT_OF_ENEMY_ANIMATIONS];
        
        for (int i = 0; i < GLRenderer.AMOUNT_OF_ENEMY_ANIMATIONS; ++i) {
            if (GLRenderer.enemyAnimations[rank-1][i] != null) {
                animationLength[i] = GLRenderer.enemyAnimations[rank-1][i].length;
            }
        }
        
        // Otetaan Wrapper käyttöön ja tallennetaan WeaponManagerin osoitin
        wrapper       = Wrapper.getInstance();
        weaponManager = _weaponManager;
        
        // Lisätään objekti piirtolistalle ja otetaan tekoäly käyttöön
        if (_ai == 0) {
            listId = wrapper.addToList(this, Wrapper.CLASS_TYPE_ENEMY, 4);
            ai = new LinearAi(listId, Wrapper.CLASS_TYPE_ENEMY);
        }
        else if (_ai == 1) {
            listId = wrapper.addToList(this, Wrapper.CLASS_TYPE_ENEMY, 4);
            ai = new ApproachAndStopAi(listId, Wrapper.CLASS_TYPE_ENEMY, _weaponManager);
        }
        /*
        else if (_ai == 2) {
            listId = wrapper.addToList(this, Wrapper.CLASS_TYPE_ENEMY, 3);
            ai = new SguigglyAi(listId, Wrapper.CLASS_TYPE_ENEMY);
        }
        else if (_ai == 3) {
            listId = wrapper.addToList(this, Wrapper.CLASS_TYPE_ENEMY, 1);
            ai = new RotaryAi(listId, Wrapper.CLASS_TYPE_ENEMY);
        }
        */
    }

    /**
     * Määrittää vihollisen aktiiviseksi.
     */
    @Override
    public final void setActive()
    {
        wrapper.enemyStates.set(listId, Wrapper.FULL_ACTIVITY);
        
    	currentHealth = health;
    }

    /**
     * Määrittää objektin epäaktiiviseksi. Sammuttaa myös tekoälyn jos se on tarpeen.
     */
    @Override
    public final void setUnactive()
    {
        wrapper.enemyStates.set(listId, Wrapper.INACTIVE);
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
        currentHealth -= (int)((float)_damage * (1 - 0.15 * (float)defence));
        
        if (currentHealth <= 0) {
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
        
        if (_ai == 0) {
            ai = new RotaryAi(listId, Wrapper.CLASS_TYPE_ENEMY);
        }
        else if (_ai == 1) {
            ai = new ApproachAndStopAi(listId, Wrapper.CLASS_TYPE_ENEMY, weaponManager);
        }
        /*
        else if (_ai == 2) {
            ai = new SguigglyAi(listId, Wrapper.CLASS_TYPE_ENEMY);
        }
        else if (_ai == 3) {
            ai = new RotaryAi(listId, Wrapper.CLASS_TYPE_ENEMY);
        }
        */
    }

    /**
     * Käsittelee jonkin toiminnon päättymisen. Kutsutaan animaation loputtua, mikäli
     * actionActivated on TRUE.
     * 
     * Käytetään esimerkiksi objektin tuhoutuessa. Objektille määritetään animaatioksi
     * sen tuhoutumisanimaatio, tilaksi Wrapperissa määritetään 2 (piirretään, mutta
     * päivitetään ainoastaan animaatio) ja asetetaan actionActivatedin arvoksi TRUE.
     * Tällöin GameThread päivittää objektin animaation, Renderer piirtää sen, ja kun
     * animaatio päättyy, kutsutaan objektin triggerEndOfAction-funktiota. Tässä
     * funktiossa objekti käsittelee tilansa. Tuhoutumisanimaation tapauksessa objekti
     * määrittää itsensä epäaktiiviseksi.
     * 
     * Jokainen objekti luo funktiosta oman toteutuksensa, sillä toimintoja voi olla
     * useita. Objekteilla on myös käytössään actionId-muuttuja, jolle voidaan asettaa
     * haluttu arvo. Tämä arvo kertoo objektille, minkä toiminnon se juuri suoritti.
     */
    @Override
    protected void triggerEndOfAction()
    {
        // Tuhotaan vihollinen
        if (actionId == 1) {
        	--SurvivalMode.enemiesLeft;
        	SurvivalMode.updateScore(rank);
            setUnactive();
        }
        // Aktivoidaan vihollinen
        else if (actionId == 2) {
        	wrapper.enemyStates.set(listId, 1);
        }
        
    	movementAcceleration = 0;
    	setMovementDelay(1.0f);
    	setMovementSpeed(1.0f);
    }
    
    /**
     * Aiheuttaa objektin muuttamisen epäaktiiviseksi sitä vastaavan animaation ajaksi.
     */
	public void triggerDisabled()
	{
    	wrapper.enemyStates.set(listId, 3);

    	movementAcceleration = -15;
    	turningDirection     = 0;
    	
        setAction(GLRenderer.ANIMATION_DISABLED, 1, 8, 2);
	}
    
    /**
     * Aiheuttaa objektin tuhoutumisen asettamalla toiminnon (ks. setAction GfxObject-luokasta)
     * ja hidastamalla objektia.
     */
	public void triggerDestroyed()
	{
		// TODO: Pitäisikö samanlainen toteutus olla myös ammuksilla?
		
    	wrapper.enemyStates.set(listId, 3);

    	movementAcceleration = -15;
    	
        setAction(GLRenderer.ANIMATION_DESTROY, 1, 1, 1);
	}
}
