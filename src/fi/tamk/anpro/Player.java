package fi.tamk.anpro;

import javax.microedition.khronos.opengles.GL10;

import android.util.Log;

/**
 * Sisältää pelaajan omat ominaisuudet ja tiedot, kuten asettamisen aktiiviseksi ja
 * epäaktiiviseksi, piirtämisen ja törmäyksenhallinnan (ei tunnistusta).
 * 
 * @extends GameObject
 */
public class Player extends GameObject
{
    /* Osoittimet muihin luokkiin */
    private Wrapper  wrapper;
    private GameMode gameMode;
    private Hud      hud;
    
    /* Tekoäly */
    public AbstractAi ai;
    
    /* Haluttu liikkumissuunta */
    public int movementTargetDirection;
    
    /**
     * Alustaa luokan muuttujat.
     * 
     * @param _health Pelaajan elämät/kestävyys
     * @param _armor Pelaajan puolustus
     * @param _gameMode Osoitin SurvivalModeen
     */
    public Player(int _health, int _armor, GameMode _gameMode, Hud _hud)
    {
        super(6); // TODO: Pelaajalle voisi mieluummin antaa nopeuden suoraan rakentajassa
        		  // Muiden GameObjectien tapaan.
        
        // Otetaan Wrapper käyttöön ja tallennetaan pelitilan osoitin
        wrapper  = Wrapper.getInstance();
        gameMode = _gameMode;
        hud      = _hud;
        
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
        wrapper.addToList(this, Wrapper.CLASS_TYPE_PLAYER, 4);
        
        // Asetetaan pelaajan "tekoäly"
        ai = new PlayerAi(0, Wrapper.CLASS_TYPE_PLAYER);
        
        // Asetetaan pelaajan asetukset
        setMovementSpeed(0.0f);
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
     * @param _gl OpenGL-konteksti
     */
    @Override
    public final void draw(GL10 _gl)
    {
        if (usedAnimation >= 0){
            GLRenderer.playerAnimations[usedAnimation].draw(_gl, x, y, direction, currentFrame);
        }
        else{
            GLRenderer.playerTextures[usedTexture].draw(_gl, x, y, direction, currentFrame);
        }
    }
	
	/**
     * Käsittelee objektin törmäystarkistukset.
     */
    public final void checkCollision()
    {
    	/* Tarkistaa törmäykset kerättäviin esineiseen */
    	for (int i = wrapper.collectables.size()-1; i >= 0; --i) {
    		
    		if (wrapper.collectableStates.get(i) == Wrapper.FULL_ACTIVITY) {
    		
				if (Math.abs(x - wrapper.collectables.get(i).x) <= Wrapper.gridSize) {
		        	if (Math.abs(y - wrapper.collectables.get(i).y) <= Wrapper.gridSize) {
		        		
		        		if (Utility.isColliding(wrapper.collectables.get(i), this)) {
		        			wrapper.collectables.get(i).triggerCollision(0, 0);
		        		}
		        	}
				}
    		}
    	}
    	
    	/* Tarkistetaan törmäykset emoalukseen */
    	// Tarkistetaan, onko emoaluksen ja pelaajan välinen etäisyys riittävän pieni
    	// tarkkoja osumatarkistuksia varten
    	if (Math.abs(wrapper.mothership.x - x) <= Wrapper.gridSize) {
        	if (Math.abs(wrapper.mothership.y - y) <= Wrapper.gridSize) {
        		
        		// Tarkistetaan osuma
        		if (Utility.isColliding(wrapper.mothership, this)) {
        			// Siirrytään emoaluksen valikkoon
        			gameMode.moveToMothershipMenu();
        		}
        	}
    	}
    }
    
    /**
     * Käsittelee törmäykset.
     * 
     * @param _damage Osuman aiheuttama vahinko
     * @param _armorPiercing Osuman kyky läpäistä suojat (käytetään, kun törmättiin ammukseen)
     */
    @Override
    public final void triggerCollision(int _damage, int _armorPiercing)
    {
        VibrateManager.vibrateOnHit();
    	
        if (currentArmor > 0) {
        	EffectManager.showPlayerArmorEffect(this);
        	EffectManager.showArmorHitEffect(Hud.armorBar.x, Hud.armorBar.y);
        }
        else {
            EffectManager.showHealthHitEffect(Hud.healthBar.x, Hud.healthBar.y);
        }
        
        Utility.checkDamage(this, _damage, _armorPiercing);
        
        hud.armorBar.updateValue(currentArmor);
        hud.healthBar.updateValue(currentHealth);

        if (currentHealth <= 0 && wrapper.playerState == Wrapper.FULL_ACTIVITY) {
        	wrapper.playerState = Wrapper.ONLY_ANIMATION;
        	setAction(GLRenderer.ANIMATION_DESTROY, 1, 1, GfxObject.ACTION_DESTROYED, 0, 0);
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
        // Tuhotaan pelaaja ja siirrytään pois pelitilasta
        if (actionId == GfxObject.ACTION_DESTROYED) {
            setUnactive();
            
            gameMode.endGameMode();
        }
    }
}

