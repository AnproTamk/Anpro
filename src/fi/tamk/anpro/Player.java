package fi.tamk.anpro;

import javax.microedition.khronos.opengles.GL10;

/**
 * Sis�lt�� pelaajan omat ominaisuudet ja tiedot, kuten asettamisen aktiiviseksi ja
 * ep�aktiiviseksi, piirt�misen ja t�rm�yksenhallinnan (ei tunnistusta).
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
     * @param int Pelaajan el�m�t/kest�vyys
     * @param int Pelaajan puolustus
     * @param int Osoitin SurivalModeen
     */
    public Player(int _health, int _armor, SurvivalMode _survivalMode)
    {
        super(0); // TODO: Pelaaja tarvitsee nopeuden StoryModea varten
        
        // TODO: Ei voida tiet��, kumpiko pelitila Playerin on otettava vastaan.
        // Eli ei voida ottaa vastaan suoraan SurvivalModea.
        
        // Otetaan Wrapper k�ytt��n ja tallennetaan pelitilan osoitin
        wrapper      = Wrapper.getInstance();
        survivalMode = _survivalMode;
        
        // Tallennetaan pelaajan tiedot
        health  	  = _health;
        currentHealth = _health;
        armor         = _armor;
        currentArmor  = _armor;
        
        // M��ritet��n Hudin healthBarin ja armorBarin tiedot
        Hud.healthBar.initBar(health);
        Hud.armorBar.initBar(armor);
        
        // Asetetaan t�rm�ystunnistuksen s�de
        collisionRadius = (int) (25 * Options.scale);
        
        // Haetaan k�ytett�vien animaatioiden pituudet
        animationLength = new int[GLRenderer.AMOUNT_OF_PLAYER_ANIMATIONS];
        
        for (int i = 0; i < GLRenderer.AMOUNT_OF_PLAYER_ANIMATIONS; ++i) {
            if (GLRenderer.playerAnimations[i] != null) {
                animationLength[i] = GLRenderer.playerAnimations[i].length;
            }
        }
        
        // Lis�t��n pelaaja piirtolistalle ja m��ritet��n tila
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
     * M��ritt�� objektin ep�aktiiviseksi. Sammuttaa my�s teko�lyn jos se on tarpeen.
     */
    @Override
    public final void setUnactive()
    {
        wrapper.playerState = Wrapper.INACTIVE;
    }

    /**
     * Piirt�� k�yt�ss� olevan animaation tai tekstuurin ruudulle.
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
     * K�sittelee t�rm�yksien vaikutukset pelaajaan.
     * 
     * @param int Osuman aiheuttama vahinko
     * @param int Osuman kyky l�p�ist� suojat (k�ytet��n, kun t�rm�ttiin ammukseen)
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
        // Tuhotaan pelaaja ja siirryt��n pois pelitilasta
        if (actionId == 1) {
            setUnactive();
            
            survivalMode.endGameMode();
        }
    }
}

