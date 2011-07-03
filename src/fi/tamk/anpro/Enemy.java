package fi.tamk.anpro;

import javax.microedition.khronos.opengles.GL10;

/**
 * Sis�lt�� vihollisen omat ominaisuudet ja tiedot, kuten asettamisen aktiiviseksi ja
 * ep�aktiiviseksi, piirt�misen ja t�rm�yksenhallinnan (ei tunnistusta).
 */
public class Enemy extends GameObject
{
    /* Vihollisen tiedot */
    public int attack;
    public int rank;
    
    /* Teko�ly */
    public AbstractAi ai;
    
    /* Muut tarvittavat oliot */
    private Wrapper       wrapper;
    private WeaponManager weaponManager;
    
    /* Vihollisen tila */
    private int listId;

    /**
     * Alustaa luokan muuttujat.
     * 
     * @param int           El�m�t/kest�vyys
     * @param int           Puolustus
     * @param int           Nopeus
     * @param int           Hy�kk�ysvoima t�rm�tess� pelaajaan
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
        
        // Asetetaan t�rm�yset�isyys
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
        
        // Otetaan Wrapper k�ytt��n ja tallennetaan WeaponManagerin osoitin
        wrapper       = Wrapper.getInstance();
        weaponManager = _weaponManager;
        
        // Lis�t��n objekti piirtolistalle ja otetaan teko�ly k�ytt��n
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
     * M��ritt�� vihollisen aktiiviseksi.
     */
    @Override
    public final void setActive()
    {
        wrapper.enemyStates.set(listId, Wrapper.FULL_ACTIVITY);
        
    	currentHealth = health;
    }

    /**
     * M��ritt�� objektin ep�aktiiviseksi. Sammuttaa my�s teko�lyn jos se on tarpeen.
     */
    @Override
    public final void setUnactive()
    {
        wrapper.enemyStates.set(listId, Wrapper.INACTIVE);
    }
    
    /**
     * Piirt�� vihollisen k�yt�ss� olevan tekstuurin tai animaation ruudulle.
     * 
     * @param GL10 OpenGL-konteksti
     */
    public final void draw(GL10 _gl)
    {
        // Tarkistaa onko animaatio p��ll� ja kutsuu oikeaa animaatiota tai tekstuuria
        if (usedAnimation >= 0){
            GLRenderer.enemyAnimations[rank-1][usedAnimation].draw(_gl, x, y, direction, currentFrame);
        }
        else{
            GLRenderer.enemyTextures[rank-1][usedTexture].draw(_gl, x, y, direction);
        }
    }
    
    /**
     * K�sittelee r�j�hdyksien aiheuttamat osumat.
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
     * K�sitelee t�rm�ykset pelaajan ja ammusten kanssa.
     * 
     * @param int T�rm�ystyyppi
     * @param int Vahinko
     * @param int Panssarinl�p�isykyky
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
     * Asettaa vihollisen tiedot. K�ytet��n, kun vihollisen tasoa halutaan nostaa, ei vihollista
     * luodessa.
     * 
     * @param int El�m�t/kest�vyys
     * @param int Nopeus
     * @param int Hy�kk�ysvoima t�rm�tess� pelaajaan
     * @param int Puolustus
     * @param int Teko�lyn tunnus
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

        // Otetaan uusi teko�ly k�ytt��n
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
     * K�sittelee jonkin toiminnon p��ttymisen. Kutsutaan animaation loputtua, mik�li
     * actionActivated on TRUE.
     * 
     * K�ytet��n esimerkiksi objektin tuhoutuessa. Objektille m��ritet��n animaatioksi
     * sen tuhoutumisanimaatio, tilaksi Wrapperissa m��ritet��n 2 (piirret��n, mutta
     * p�ivitet��n ainoastaan animaatio) ja asetetaan actionActivatedin arvoksi TRUE.
     * T�ll�in GameThread p�ivitt�� objektin animaation, Renderer piirt�� sen, ja kun
     * animaatio p��ttyy, kutsutaan objektin triggerEndOfAction-funktiota. T�ss�
     * funktiossa objekti k�sittelee tilansa. Tuhoutumisanimaation tapauksessa objekti
     * m��ritt�� itsens� ep�aktiiviseksi.
     * 
     * Jokainen objekti luo funktiosta oman toteutuksensa, sill� toimintoja voi olla
     * useita. Objekteilla on my�s k�yt�ss��n actionId-muuttuja, jolle voidaan asettaa
     * haluttu arvo. T�m� arvo kertoo objektille, mink� toiminnon se juuri suoritti.
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
     * Aiheuttaa objektin muuttamisen ep�aktiiviseksi sit� vastaavan animaation ajaksi.
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
		// TODO: Pit�isik� samanlainen toteutus olla my�s ammuksilla?
		
    	wrapper.enemyStates.set(listId, 3);

    	movementAcceleration = -15;
    	
        setAction(GLRenderer.ANIMATION_DESTROY, 1, 1, 1);
	}
}
