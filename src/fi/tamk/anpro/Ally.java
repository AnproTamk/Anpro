package fi.tamk.anpro;

import javax.microedition.khronos.opengles.GL10;

/**
 * Sis�lt�� liittolaisen omat ominaisuudet ja tiedot, kuten asettamisen aktiiviseksi ja
 * ep�aktiiviseksi, piirt�misen ja t�rm�yksenhallinnan (ei tunnistusta).
 */
public class Ally extends AiObject
{
	/* Liittolaisten tyypit */
	public static final byte ALLY_TURRET = 1;
	
    /* Liittolaisen tiedot */
    public int type;   // Liittolaisten tyypit vastaavat vihollisten tasoja
    
    /* Muut tarvittavat oliot */
    private Wrapper       wrapper;
    private WeaponManager weaponManager;

    /**
     * Alustaa luokan muuttujat.
     * 
     * @param int           El�m�t/kest�vyys
     * @param int           Puolustus
     * @param int           Nopeus
     * @param int           Hy�kk�ysvoima t�rm�tess� pelaajaan
     * @param int           Tyyppi
     * @param WeaponManager Osoitin WeaponManageriin
     */
    public Ally(int _health, int _armor, int _speed, int _attack, int _ai, byte _type, WeaponManager _weaponManager)
    {
        super(_speed);
        
        /* Tallennetaan muuttujat */
        health          = _health;
        currentHealth   = _health;
        collisionDamage = _attack;
        armor           = _armor;
        currentArmor    = _armor;
        type            = _type;
        weaponManager   = _weaponManager;
        
        /* Alustetaan muuttujat */
        // M��ritet��n t�rm�yset�isyys
        // TODO: Vakiot tyypeille?
        if (type == 1) {
            collisionRadius = (int) (20 * Options.scale);
        }
        else if (type == 2) {
        	collisionRadius = (int) (25 * Options.scale);
        }
    
        // Haetaan animaatioiden pituudet
        animationLength = new int[GLRenderer.AMOUNT_OF_ALLY_ANIMATIONS];
        
        for (int i = 0; i < GLRenderer.AMOUNT_OF_ALLY_ANIMATIONS; ++i) {
            if (GLRenderer.allyAnimations[type-1][i] != null) {
                animationLength[i] = GLRenderer.allyAnimations[type-1][i].length;
            }
        }
        
        /* Otetaan tarvittavat luokat k�ytt��n */
        wrapper = Wrapper.getInstance();

        /* M��ritet��n objektin tila (piirtolista ja teko�ly) */
        wrapper.addToDrawables(this);
        state = Wrapper.INACTIVE;
        
        if (_ai == AbstractAi.TURRET_AI) {
            ai = new TurretAllyAi(this, Wrapper.CLASS_TYPE_ALLY, _weaponManager);
        }
    }

    /**
     * M��ritt�� objektin aktiiviseksi.
     */
    @Override
    public final void setActive()
    {
        state = Wrapper.FULL_ACTIVITY;
        
    	currentHealth = health;
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
     * @param GL10 OpenGL-konteksti
     */
    public final void draw(GL10 _gl)
    {
        // Tarkistaa onko animaatio p��ll� ja kutsuu oikeaa animaatiota tai tekstuuria
        if (usedAnimation >= 0){
            GLRenderer.allyAnimations[type-1][usedAnimation].draw(_gl, x, y, direction, currentFrame);
        }
        else{
            GLRenderer.allyTextures[type-1][usedTexture].draw(_gl, x, y, direction, currentFrame);
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
        Utility.checkDamage(this, _damage, 0);
        
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
        	Utility.checkDamage(this, _damage, _armorPiercing);

            if (currentHealth <= 0) {
            	triggerDestroyed();
            }
        }
        else if (_eventType == GameObject.COLLISION_WITH_ENEMY) {
        	//triggerDestroyed();
        }
        else if (_eventType == GameObject.COLLISION_WITH_OBSTACLE) {
        	triggerDestroyed();
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
        // Tuhotaan liittolainen
        if (actionId == GfxObject.ACTION_DESTROYED) {
            setUnactive();
        }
        
    	movementAcceleration = 0;
    	setMovementDelay(1.0f);
    	setMovementSpeed(1.0f);
    }
    
    /**
     * Aiheuttaa objektin tuhoutumisen asettamalla toiminnon (ks. setAction GfxObject-luokasta)
     * ja hidastamalla objektia.
     */
	public void triggerDestroyed()
	{
    	state = Wrapper.ANIMATION_AND_MOVEMENT;

    	movementAcceleration = -15;
    	
        setAction(GLRenderer.ANIMATION_DESTROY, 1, 1, GfxObject.ACTION_DESTROYED, 0, 0);
	}
}
