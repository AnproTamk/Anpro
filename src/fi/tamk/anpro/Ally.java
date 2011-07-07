package fi.tamk.anpro;

import javax.microedition.khronos.opengles.GL10;

/**
 * Sisältää liittolaisen omat ominaisuudet ja tiedot, kuten asettamisen aktiiviseksi ja
 * epäaktiiviseksi, piirtämisen ja törmäyksenhallinnan (ei tunnistusta).
 */
public class Ally extends GameObject
{
    /* Liittolaisen tiedot */
    public int attack;
    public int type;
    
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
    public Ally(int _health, int _armor, int _speed, int _attack, int _ai, int _type, WeaponManager _weaponManager)
    {
        super(_speed);
        
        // Tallennetaan tiedot
        health        = _health;
        currentHealth = _health;
        attack        = _attack;
        armor         = _armor;
        currentArmor  = _armor;
        type          = _type;
        
        // Asetetaan törmäysetäisyys
        if (type == 1) {
            collisionRadius = (int) (20 * Options.scale);
        }
        else if (type == 2) {
        	collisionRadius = (int) (25 * Options.scale);
        }
    
        // Haetaan animaatioiden pituudet
        animationLength = new int[GLRenderer.AMOUNT_OF_ENEMY_ANIMATIONS];
        
        for (int i = 0; i < GLRenderer.AMOUNT_OF_ENEMY_ANIMATIONS; ++i) {
            if (GLRenderer.allyAnimations[type-1][i] != null) {
                animationLength[i] = GLRenderer.allyAnimations[type-1][i].length;
            }
        }
        
        // Otetaan Wrapper käyttöön ja tallennetaan WeaponManagerin osoitin
        wrapper       = Wrapper.getInstance();
        weaponManager = _weaponManager;
        
        // Lisätään objekti piirtolistalle ja otetaan tekoäly käyttöön
        if (_ai == 0) {
            listId = wrapper.addToList(this, Wrapper.CLASS_TYPE_ALLY, 2);
            //ai = new RotaryAllyAi(listId, Wrapper.CLASS_TYPE_ALLY, _weaponManager);
        }
    }

    /**
     * Määrittää objektin aktiiviseksi.
     */
    @Override
    public final void setActive()
    {
        wrapper.allyStates.set(listId, Wrapper.FULL_ACTIVITY);
        
    	currentHealth = health;
    }

    /**
     * Määrittää objektin epäaktiiviseksi. Sammuttaa myös tekoälyn jos se on tarpeen.
     */
    @Override
    public final void setUnactive()
    {
        wrapper.allyStates.set(listId, Wrapper.INACTIVE);
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
            GLRenderer.allyTextures[type-1][usedTexture].draw(_gl, x, y, direction, 0);
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
        // Aktivoidaan vihollinen
        else if (actionId == 2) {
        	wrapper.enemyStates.set(listId, 1);
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
    	wrapper.enemyStates.set(listId, 3);

    	movementAcceleration = -15;
    	
        setAction(GLRenderer.ANIMATION_DESTROY, 1, 1, 1);
	}
}
