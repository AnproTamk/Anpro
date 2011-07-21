package fi.tamk.anpro;

import javax.microedition.khronos.opengles.GL10;

/**
 * Sis�lt�� vihollisen omat ominaisuudet ja tiedot, kuten asettamisen aktiiviseksi ja
 * ep�aktiiviseksi, piirt�misen ja t�rm�yksenhallinnan (ei tunnistusta).
 */
public class Enemy extends AiObject
{
    /* Vihollisen tiedot */
    public int rank;
    
    /* Muut tarvittavat oliot */
    private Wrapper       wrapper;
    private WeaponManager weaponManager;

    /**
     * Alustaa luokan muuttujat.
     * 
     * @param _int           El�m�t/kest�vyys
     * @param _int           Puolustus
     * @param _int           Nopeus
     * @param _int           Hy�kk�ysvoima t�rm�tess� pelaajaan
     * @param _int           Taso
     * @param _weaponManager Osoitin WeaponManageriin
     */
    public Enemy(int _health, int _armor, int _speed, int _attack, int _ai, int _rank, WeaponManager _weaponManager)
    {
        super(_speed);
        
        /* Tallennetaan muuttujat */
        health          = _health;
        currentHealth   = _health;
        collisionDamage = _attack;
        armor           = _armor;
        currentArmor    = _armor;
        rank            = _rank;
        weaponManager   = _weaponManager;

		/* Alustetaan muuttujat */
        // M��ritet��n t�rm�yset�isyys tason perusteella
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

        /* Haetaan tarvittavat luokat k�ytt��n */
        wrapper       = Wrapper.getInstance();

        /* M��ritet��n objektin tila (piirtolista ja teko�ly) */
        wrapper.addToDrawables(this);
        state = Wrapper.INACTIVE;
        
        if (_ai == AbstractAi.LINEAR_ENEMY_AI) {
            ai = new LinearAi(this, Wrapper.CLASS_TYPE_ENEMY);
        }
        else if (_ai == AbstractAi.ROTARY_ENEMY_AI) {
            ai = new RotaryAi(this, Wrapper.CLASS_TYPE_ENEMY, _weaponManager);
        }
        else if (_ai == AbstractAi.SQUIGGLY_ENEMY_AI) {
            ai = new SquigglyAi(this, Wrapper.CLASS_TYPE_ENEMY, _weaponManager);
        }
        else if (_ai == AbstractAi.APPROACHANDSTOP_ENEMY_AI) {
            ai = new ApproachAndStopAi(this, Wrapper.CLASS_TYPE_ENEMY, _weaponManager);
        }
    }
    
    /* =======================================================
     * Perityt funktiot
     * ======================================================= */
    /**
     * M��ritt�� objektin aktiiviseksi.
     */
    @Override
    public final void setActive()
    {
        state = Wrapper.FULL_ACTIVITY;
        
    	movementAcceleration = 0;
    	setMovementDelay(1.0f);
    	setMovementSpeed(1.0f);
    	
    	
    	currentHealth = health;
    	currentArmor  = armor;
    }

    /**
     * M��ritt�� objektin ep�aktiiviseksi. Sammuttaa my�s teko�lyn jos se on tarpeen.
     */
    @Override
    public final void setUnactive()
    {
        state = Wrapper.INACTIVE;
    }
    
    /**
     * Piirt�� vihollisen k�yt�ss� olevan tekstuurin tai animaation ruudulle.
     * 
     * @param _gl OpenGL-konteksti
     */
    public final void draw(GL10 _gl)
    {
        // Tarkistaa onko animaatio p��ll� ja kutsuu oikeaa animaatiota tai tekstuuria
        if (usedAnimation >= 0){
            GLRenderer.enemyAnimations[rank-1][usedAnimation].draw(_gl, x, y, direction, currentFrame);
        }
        else{
            GLRenderer.enemyTextures[rank-1][usedTexture].draw(_gl, x, y, direction, currentFrame);
        }
    }
    
    /**
     * K�sittelee r�j�hdyksien aiheuttamat osumat.
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
     * K�sittelee t�rm�ykset.
     * 
     * @param _eventType T�rm�ystyyppi
     * @param _damage Vahinko
     * @param _armorPiercing Panssarinl�p�isykyky
     */
    @Override
    public final void triggerCollision(int _eventType, int _damage, int _armorPiercing)
    {
        if (_eventType == GameObject.COLLISION_WITH_PLAYERPROJECTILE) {
        	if (currentArmor > 0) {
        		EffectManager.showEnemyArmorEffect(this);
        	}
        	
            Utility.checkDamage(this, _damage, _armorPiercing);
            
            if (currentHealth <= 0) {
        		GameMode.updateScore(rank, x, y);
            	triggerDestroyed();
            }
        }
        else if (_eventType == GameObject.COLLISION_WITH_ALLYPROJECTILE) {
        	if (currentArmor > 0) {
        		EffectManager.showEnemyArmorEffect(this);
        	}
        	
            Utility.checkDamage(this, _damage, _armorPiercing);
            
            if (currentHealth <= 0) {
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
    
    /* =======================================================
     * Uudet funktiot
     * ======================================================= */
    /**
     * Asettaa vihollisen tiedot. K�ytet��n, kun vihollisen tasoa halutaan nostaa, ei vihollista
     * luodessa.
     * 
     * @param _health El�m�t/kest�vyys
     * @param _armor Puolustus
     * @param _speed Nopeus
     * @param _attack Hy�kk�ysvoima t�rm�tess� pelaajaan
     * @param _ai Teko�lyn tunnus
     * @param _rank Taso
     */
    public final void setStats(int _health, int _armor, int _speed, int _attack, int _ai, int _rank)
    {
        // Tallennetaan uudet tiedot
        health          = _health;
        speed           = _speed;
        collisionDamage = _attack;
        armor           = _armor;
        rank            = _rank;

        
        // Otetaan uusi teko�ly k�ytt��n
        ai = null;
        
        // Lis�t��n objekti piirtolistalle ja otetaan teko�ly k�ytt��n
        if (_ai == AbstractAi.LINEAR_ENEMY_AI) {
            ai = new LinearAi(this, Wrapper.CLASS_TYPE_ENEMY);
        }
        else if (_ai == AbstractAi.ROTARY_ENEMY_AI) {
            ai = new RotaryAi(this, Wrapper.CLASS_TYPE_ENEMY, weaponManager);
        }
        else if (_ai == AbstractAi.SQUIGGLY_ENEMY_AI) {
            ai = new SquigglyAi(this, Wrapper.CLASS_TYPE_ENEMY, weaponManager);
        }
        else if (_ai == AbstractAi.APPROACHANDSTOP_ENEMY_AI) {
            ai = new ApproachAndStopAi(this, Wrapper.CLASS_TYPE_ENEMY, weaponManager);
        }
    }

    /**
     * K�sittelee jonkin toiminnon p��ttymisen. Kutsutaan animaation loputtua, mik�li
     * <i>actionActivated</i> on TRUE.<br /><br />
     * 
     * K�ytet��n seuraavasti:<br />
     * <ul>
     *   <li>1. Objekti kutsuu funktiota <b>setAction</b>, jolle annetaan parametreina haluttu animaatio,
     *     animaation toistokerrat, animaation nopeus, toiminnon tunnus (vakiot <b>GfxObject</b>issa).
     *     Toiminnon tunnus tallennetaan <i>actionId</i>-muuttujaan.
     *     		<ul><li>-> Lis�ksi voi antaa my�s jonkin animaation ruudun j�rjestysnumeron (alkaen 0:sta)
     *     		   ja ajan, joka siin� ruudussa on tarkoitus odottaa.</li></ul></li>
     *  <li>2. <b>GfxObject</b>in <b>setAction</b>-funktio kutsuu startAnimation-funktiota (sis�lt�� my�s
     *     <b>GfxObject</b>issa), joka k�ynnist�� animaation asettamalla <i>usedAnimation</i>-muuttujan arvoksi
     *     kohdassa 1 annetun animaation tunnuksen.</li>
     *  <li>3. <b>GLRenderer</b> p�ivitt�� animaatiota kutsumalla <b>GfxObject</b>in <b>update</b>-funktiota.</li>
     *  <li>4. Kun animaatio on loppunut, kutsuu <b>update</b>-funktio koko ketjun aloittaneen objektin
     *     <b>triggerEndOfAction</b>-funktiota (funktio on abstrakti, joten alaluokat luovat siit� aina
     *     oman toteutuksensa).</li>
     *  <li>5. <b>triggerEndOfAction</b>-funktio tulkitsee <i>actionId</i>-muuttujan arvoa, johon toiminnon tunnus
     *     tallennettiin, ja toimii sen mukaisesti.</li>
     * </ul>
     * 
     * Funktiota k�ytet��n esimerkiksi objektin tuhoutuessa, jolloin se voi asettaa itsens�
     * "puoliaktiiviseen" tilaan (esimerkiksi 2, eli ONLY_ANIMATION) ja k�ynnist�� yll� esitetyn
     * tapahtumaketjun. Objekti tuhoutuu asettumalla tilaan 0 (INACTIVE) vasta ketjun p��tytty�.
     * Tuhoutuminen toteutettaisiin triggerEndOfAction-funktion sis�ll�.
     * 
     * Toimintojen vakiot l�ytyv�t GfxObject-luokan alusta.
     */
    @Override
    protected void triggerEndOfAction()
    {
        // Tuhotaan vihollinen
        if (actionId == GfxObject.ACTION_DESTROYED) {
        	--GameMode.enemiesLeft;
            setUnactive();
        }
        // Aktivoidaan vihollinen (esim. EMPin j�lkeen)
        else if (actionId == GfxObject.ACTION_ENABLED) {
        	state = Wrapper.FULL_ACTIVITY;
        }
    }
    
    /**
     * Aiheuttaa objektin muuttamisen ep�aktiiviseksi sit� vastaavan animaation ajaksi.
     */
    public void triggerDisabled()
	{
    	state = Wrapper.ANIMATION_AND_MOVEMENT;

    	movementAcceleration = -15;
    	turningDirection     = 0;

    	EffectManager.showQuestionMarkBalloon(this);
    	
        setAction(GLRenderer.ANIMATION_DISABLED, 1, 8, GfxObject.ACTION_ENABLED, 0, 0);
	}
    
    /**
     * Aiheuttaa objektin tuhoutumisen asettamalla toiminnon (ks. setAction GfxObject-luokasta)
     * ja hidastamalla objektia.
     */
    public void triggerDestroyed()
	{
		// TODO: Pit�isik� samanlainen toteutus olla my�s ammuksilla?
		
    	state = Wrapper.ANIMATION_AND_MOVEMENT;

    	movementAcceleration = -15;
    	turningDirection     = 0;
    	
        setAction(GLRenderer.ANIMATION_DESTROY, 1, 1, GfxObject.ACTION_DESTROYED, 0, 0);
	}
}
