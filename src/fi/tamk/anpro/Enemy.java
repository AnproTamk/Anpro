package fi.tamk.anpro;

import javax.microedition.khronos.opengles.GL10;

import android.util.Log;

/**
 * Sisältää vihollisen omat ominaisuudet ja tiedot, kuten asettamisen aktiiviseksi ja
 * epäaktiiviseksi, piirtämisen ja törmäyksenhallinnan (ei tunnistusta).
 */
public class Enemy extends GameObject
{
    /* Vihollisen tiedot */
	public int attack; // TODO: Siirrä checkCollisionWithPlayer AbstractAi:sta Enemy-luokkaan,
					   // jolloin tämä voisi olla "protected"
    public int rank;
    
    /* Tekoäly */
    public AbstractAi ai;
    
    /* Muut tarvittavat oliot */
    private Wrapper       wrapper;
    private WeaponManager weaponManager;

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
    public Enemy(int _health, int _armor, int _speed, int _attack, int _ai, int _rank, WeaponManager _weaponManager)
    {
        super(_speed);
        
        // Tallennetaan tiedot
        health        = _health;
        currentHealth = _health;
        attack        = _attack;
        armor         = _armor;
        currentArmor  = _armor;
        rank          = _rank;
        
        // Asetetaan törmäysetäisyys
        if (rank == 1) {
            collisionRadius = (int) (20 * Options.scale);
        }
        else if (rank == 2) {
        	collisionRadius = (int) (25 * Options.scale);
        }
        else if (rank == 3) {
        	collisionRadius = (int) (25 * Options.scale);
        }
        else if (rank == 4) {
        	collisionRadius = (int) (50 * Options.scale);
        }
        else if (rank == 5) {
        	collisionRadius = (int) (50 * Options.scale);
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
        if (_ai == AbstractAi.LINEAR_ENEMY_AI) {
            listId = wrapper.addToList(this, Wrapper.CLASS_TYPE_ENEMY, 4);
            ai = new LinearAi(listId, Wrapper.CLASS_TYPE_ENEMY);
        }
        else if (_ai == AbstractAi.ROTARY_ENEMY_AI) {
            listId = wrapper.addToList(this, Wrapper.CLASS_TYPE_ENEMY, 4);
            ai = new RotaryAi(listId, Wrapper.CLASS_TYPE_ENEMY, _weaponManager);
        }
        else if (_ai == AbstractAi.SQUIGGLY_ENEMY_AI) {
            listId = wrapper.addToList(this, Wrapper.CLASS_TYPE_ENEMY, 3);
            ai = new SquigglyAi(listId, Wrapper.CLASS_TYPE_ENEMY);
        }
        else if (_ai == AbstractAi.APPROACHANDSTOP_ENEMY_AI) {
            listId = wrapper.addToList(this, Wrapper.CLASS_TYPE_ENEMY, 1);
            ai = new ApproachAndStopAi(listId, Wrapper.CLASS_TYPE_ENEMY, _weaponManager);
        }
    }

    /**
     * Määrittää objektin aktiiviseksi.
     */
    @Override
    public final void setActive()
    {
        wrapper.enemyStates.set(listId, Wrapper.FULL_ACTIVITY);
        
    	movementAcceleration = 0;
    	setMovementDelay(1.0f);
    	setMovementSpeed(1.0f);
    	
    	
    	currentHealth = health;
    	currentArmor  = armor;
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
     * @param _gl OpenGL-konteksti
     */
    public final void draw(GL10 _gl)
    {
        // Tarkistaa onko animaatio päällä ja kutsuu oikeaa animaatiota tai tekstuuria
        if (usedAnimation >= 0){
            GLRenderer.enemyAnimations[rank-1][usedAnimation].draw(_gl, x, y, direction, currentFrame);
        }
        else{
            GLRenderer.enemyTextures[rank-1][usedTexture].draw(_gl, x, y, direction, 0);
        }
    }
    
    /**
     * Käsittelee räjähdyksien aiheuttamat osumat.
     * 
     * @param _damage Vahinko
     */
    @Override
    public final void triggerImpact(int _damage)
    {
        Utility.checkDamage(this, _damage, 0);
        
        if (currentHealth <= 0) {
        	triggerDestroyed();
        }
    }
    
    /**
     * Käsittelee törmäykset.
     * 
     * @param _eventType Törmäystyyppi
     * @param _damage Vahinko
     * @param _armorPiercing Panssarinläpäisykyky
     */
    @Override
    public final void triggerCollision(int _eventType, int _damage, int _armorPiercing)
    {
        if (_eventType == GameObject.COLLISION_WITH_PROJECTILE) {
            
        	if (currentArmor > 0) {
        		EffectManager.showEnemyArmorEffect(this);
        	}
        	
            Utility.checkDamage(this, _damage, _armorPiercing);
            
            if (currentHealth <= 0) {
        		GameMode.updateScore(rank, x, y);
            	triggerDestroyed();
            }
        }
        else if (_eventType == GameObject.COLLISION_WITH_PLAYER) {
        	triggerDestroyed();
        }
        if (_eventType == GameObject.COLLISION_WITH_OBSTACLE) {
        	triggerDestroyed();
        }
    }

    /**
     * Asettaa vihollisen tiedot. Käytetään, kun vihollisen tasoa halutaan nostaa, ei vihollista
     * luodessa.
     * 
     * @param _health Elämät/kestävyys
     * @param _armor Puolustus
     * @param _speed Nopeus
     * @param _attack Hyökkäysvoima törmätessä pelaajaan
     * @param _ai Tekoälyn tunnus
     * @param _rank Taso
     */
    public final void setStats(int _health, int _armor, int _speed, int _attack, int _ai, int _rank)
    {
        // Tallennetaan uudet tiedot
        health     = _health;
        speed      = _speed;
        attack     = _attack;
        armor      = _armor;
        rank       = _rank;

        
        // Otetaan uusi tekoäly käyttöön
        ai = null;
        
        // Lisätään objekti piirtolistalle ja otetaan tekoäly käyttöön
        if (_ai == AbstractAi.LINEAR_ENEMY_AI) {
            ai = new LinearAi(listId, Wrapper.CLASS_TYPE_ENEMY);
        }
        else if (_ai == AbstractAi.ROTARY_ENEMY_AI) {
            ai = new RotaryAi(listId, Wrapper.CLASS_TYPE_ENEMY, weaponManager);
        }
        else if (_ai == AbstractAi.SQUIGGLY_ENEMY_AI) {
            ai = new SquigglyAi(listId, Wrapper.CLASS_TYPE_ENEMY);
        }
        else if (_ai == AbstractAi.APPROACHANDSTOP_ENEMY_AI) {
            ai = new ApproachAndStopAi(listId, Wrapper.CLASS_TYPE_ENEMY, weaponManager);
        }
    }

    /**
     * Käsittelee jonkin toiminnon päättymisen. Kutsutaan animaation loputtua, mikäli
     * <i>actionActivated</i> on TRUE.<br /><br />
     * 
     * Käytetään seuraavasti:<br />
     * <ul>
     *   <li>1. Objekti kutsuu funktiota <b>setAction</b>, jolle annetaan parametreina haluttu animaatio,
     *     animaation toistokerrat, animaation nopeus, toiminnon tunnus (vakiot <b>GfxObject</b>issa).
     *     Toiminnon tunnus tallennetaan <i>actionId</i>-muuttujaan.
     *     		<ul><li>-> Lisäksi voi antaa myös jonkin animaation ruudun järjestysnumeron (alkaen 0:sta)
     *     		   ja ajan, joka siinä ruudussa on tarkoitus odottaa.</li></ul></li>
     *  <li>2. <b>GfxObject</b>in <b>setAction</b>-funktio kutsuu startAnimation-funktiota (sisältää myös
     *     <b>GfxObject</b>issa), joka käynnistää animaation asettamalla <i>usedAnimation</i>-muuttujan arvoksi
     *     kohdassa 1 annetun animaation tunnuksen.</li>
     *  <li>3. <b>GLRenderer</b> päivittää animaatiota kutsumalla <b>GfxObject</b>in <b>update</b>-funktiota.</li>
     *  <li>4. Kun animaatio on loppunut, kutsuu <b>update</b>-funktio koko ketjun aloittaneen objektin
     *     <b>triggerEndOfAction</b>-funktiota (funktio on abstrakti, joten alaluokat luovat siitä aina
     *     oman toteutuksensa).</li>
     *  <li>5. <b>triggerEndOfAction</b>-funktio tulkitsee <i>actionId</i>-muuttujan arvoa, johon toiminnon tunnus
     *     tallennettiin, ja toimii sen mukaisesti.</li>
     * </ul>
     * 
     * Funktiota käytetään esimerkiksi objektin tuhoutuessa, jolloin se voi asettaa itsensä
     * "puoliaktiiviseen" tilaan (esimerkiksi 2, eli ONLY_ANIMATION) ja käynnistää yllä esitetyn
     * tapahtumaketjun. Objekti tuhoutuu asettumalla tilaan 0 (INACTIVE) vasta ketjun päätyttyä.
     * Tuhoutuminen toteutettaisiin triggerEndOfAction-funktion sisällä.
     * 
     * Toimintojen vakiot löytyvät GfxObject-luokan alusta.
     */
    @Override
    protected void triggerEndOfAction()
    {
        // Tuhotaan vihollinen
        if (actionId == GfxObject.ACTION_DESTROYED) {
        	--GameMode.enemiesLeft;
            setUnactive();
        }
        // Aktivoidaan vihollinen (esim. EMPin jälkeen)
        else if (actionId == GfxObject.ACTION_ENABLED) {
        	wrapper.enemyStates.set(listId, Wrapper.FULL_ACTIVITY);
        }
    }
    
    /**
     * Aiheuttaa objektin muuttamisen epäaktiiviseksi sitä vastaavan animaation ajaksi.
     */
    public void triggerDisabled()
	{
    	wrapper.enemyStates.set(listId, Wrapper.ANIMATION_AND_MOVEMENT);

    	movementAcceleration = -15;
    	turningDirection     = 0;

    	EffectManager.showQuestionMarkBalloon(this);
    	
        setAction(GLRenderer.ANIMATION_DISABLED, 1, 8, 2);
	}
    
    /**
     * Aiheuttaa objektin tuhoutumisen asettamalla toiminnon (ks. setAction GfxObject-luokasta)
     * ja hidastamalla objektia.
     */
    public void triggerDestroyed()
	{
		// TODO: Pitäisikö samanlainen toteutus olla myös ammuksilla?
		
    	wrapper.enemyStates.set(listId, Wrapper.ANIMATION_AND_MOVEMENT);

    	movementAcceleration = -15;
    	turningDirection     = 0;
    	
        setAction(GLRenderer.ANIMATION_DESTROY, 1, 1, 1);
	}
}
