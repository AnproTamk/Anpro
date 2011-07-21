package fi.tamk.anpro;

import javax.microedition.khronos.opengles.GL10;

/**
 * Sisältää liittolaisen omat ominaisuudet ja tiedot, kuten asettamisen aktiiviseksi ja
 * epäaktiiviseksi, piirtämisen ja törmäyksenhallinnan (ei tunnistusta).
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
     * @param int           Elämät/kestävyys
     * @param int           Puolustus
     * @param int           Nopeus
     * @param int           Hyökkäysvoima törmätessä pelaajaan
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
        // Määritetään törmäysetäisyys
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
        
        /* Otetaan tarvittavat luokat käyttöön */
        wrapper = Wrapper.getInstance();

        /* Määritetään objektin tila (piirtolista ja tekoäly) */
        wrapper.addToDrawables(this);
        state = Wrapper.INACTIVE;
        
        if (_ai == AbstractAi.TURRET_AI) {
            ai = new TurretAllyAi(this, Wrapper.CLASS_TYPE_ALLY, _weaponManager);
        }
    }

    /**
     * Määrittää objektin aktiiviseksi.
     */
    @Override
    public final void setActive()
    {
        state = Wrapper.FULL_ACTIVITY;
        
    	currentHealth = health;
    }

    /**
     * Määrittää objektin epäaktiiviseksi. Sammuttaa myös tekoälyn jos se on tarpeen.
     */
    @Override
    public final void setUnactive()
    {
        state = Wrapper.INACTIVE;
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
            GLRenderer.allyAnimations[type-1][usedAnimation].draw(_gl, x, y, direction, currentFrame);
        }
        else{
            GLRenderer.allyTextures[type-1][usedTexture].draw(_gl, x, y, direction, currentFrame);
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
        Utility.checkDamage(this, _damage, 0);
        
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
