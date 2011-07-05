package fi.tamk.anpro;

import javax.microedition.khronos.opengles.GL10;

/**
 * Sisältää pelaajan omat ominaisuudet ja tiedot, kuten asettamisen aktiiviseksi ja
 * epäaktiiviseksi, piirtämisen ja törmäyksenhallinnan (ei tunnistusta).
 * 
 * @extends GameObject
 */
public class Player extends GameObject
{
    /* Osoittimet muihin luokkiin */
    private Wrapper      wrapper;
    private SurvivalMode survivalMode;
    
    /**
     * Alustaa luokan muuttujat.
     * 
     * @param int Pelaajan elämät/kestävyys
     * @param int Pelaajan puolustus
     * @param int Osoitin SurivalModeen
     */
    public Player(int _health, int _armor, SurvivalMode _survivalMode)
    {
        super(0); // TODO: Pelaaja tarvitsee nopeuden StoryModea varten
        
        // TODO: Ei voida tietää, kumpiko pelitila Playerin on otettava vastaan.
        // Eli ei voida ottaa vastaan suoraan SurvivalModea.
        
        // Otetaan Wrapper käyttöön ja tallennetaan pelitilan osoitin
        wrapper      = Wrapper.getInstance();
        survivalMode = _survivalMode;
        
        // Tallennetaan pelaajan tiedot
        health  	  = _health;
        currentHealth = _health;
        armor         = _armor;
        currentArmor  = _armor;
        
        // Määritetään Hudin healthBarin ja armorBarin tiedot
        Hud.healthBar.initBar(health);
        Hud.armorBar.initBar(armor);
        
        // Asetetaan törmäystunnistuksen säde
        collisionRadius = (int) (25 * Options.scale);
        
        // Haetaan käytettävien animaatioiden pituudet
        animationLength = new int[GLRenderer.AMOUNT_OF_PLAYER_ANIMATIONS];
        
        for (int i = 0; i < GLRenderer.AMOUNT_OF_PLAYER_ANIMATIONS; ++i) {
            if (GLRenderer.playerAnimations[i] != null) {
                animationLength[i] = GLRenderer.playerAnimations[i].length;
            }
        }
        
        // Lisätään pelaaja piirtolistalle ja määritetään tila
        wrapper.addToList(this, Wrapper.CLASS_TYPE_PLAYER, Wrapper.FULL_ACTIVITY);
    }

    /**
     * Asettaa pelaajan aktiiviseksi.
     */
    @Override
    public final void setActive()
    {
        wrapper.playerState = Wrapper.FULL_ACTIVITY;
    }

    /**
     * Määrittää objektin epäaktiiviseksi. Sammuttaa myös tekoälyn jos se on tarpeen.
     */
    @Override
    public final void setUnactive()
    {
        wrapper.playerState = Wrapper.INACTIVE;
    }

    /**
     * Piirtää käytössä olevan animaation tai tekstuurin ruudulle.
     * 
     * @param GL10 OpenGL-konteksti
     */
    public final void draw(GL10 _gl)
    {
        if (usedAnimation >= 0){
            GLRenderer.playerAnimations[usedAnimation].draw(_gl, x, y, direction, currentFrame);
        }
        else{
            GLRenderer.playerTextures[usedTexture].draw(_gl, x, y, direction);
        }
    }

    /**
     * Käsittelee törmäyksien vaikutukset pelaajaan.
     * 
     * @param int Osuman aiheuttama vahinko
     * @param int Osuman kyky läpäistä suojat (käytetään, kun törmättiin ammukseen)
     */
    @Override
    public final void triggerCollision(int _damage, int _armorPiercing)
    {
        VibrateManager.vibrateOnHit();
    	
        Utility.checkDamage(this, _damage, _armorPiercing);
        
        Hud.armorBar.updateValue(currentArmor);
        Hud.healthBar.updateValue(currentHealth);
        
        if (currentHealth <= 0) {
        	wrapper.playerState = 2;
        	setAction(GLRenderer.ANIMATION_DESTROY, 1, 1, 1);
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
     */
    @Override
    protected void triggerEndOfAction()
    {    	
        // Tuhotaan pelaaja ja siirrytään pois pelitilasta
        if (actionId == 1) {
            setUnactive();
            
            survivalMode.endGameMode();
        }
    }
}

