package fi.tamk.anpro;

import javax.microedition.khronos.opengles.GL10;

import android.util.Log;

/**
 * Sis�lt�� pelaajan omat ominaisuudet ja tiedot, kuten asettamisen aktiiviseksi ja
 * ep�aktiiviseksi, piirt�misen ja t�rm�yksenhallinnan (ei tunnistusta).
 * 
 * @extends GameObject
 */
public class Player extends GameObject
{
    /* Osoittimet muihin luokkiin */
    private Wrapper  wrapper;
    private GameMode gameMode;
    private Hud      hud;
    
    /* Teko�ly */
    public AbstractAi ai;
    
    /* Haluttu liikkumissuunta */
    public int movementTargetDirection;
    
    /**
     * Alustaa luokan muuttujat.
     * 
     * @param _health Pelaajan el�m�t/kest�vyys
     * @param _armor Pelaajan puolustus
     * @param _gameMode Osoitin SurvivalModeen
     */
    public Player(int _health, int _armor, GameMode _gameMode, Hud _hud)
    {
        super(6); // TODO: Pelaajalle voisi mieluummin antaa nopeuden suoraan rakentajassa
        		  // Muiden GameObjectien tapaan.
        
        // Otetaan Wrapper k�ytt��n ja tallennetaan pelitilan osoitin
        wrapper  = Wrapper.getInstance();
        gameMode = _gameMode;
        hud      = _hud;
        
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
        wrapper.addToList(this, Wrapper.CLASS_TYPE_PLAYER, 4);
        
        // Asetetaan pelaajan "teko�ly"
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
     * K�sittelee objektin t�rm�ystarkistukset.
     */
    public final void checkCollision()
    {
    	/* Tarkistaa t�rm�ykset ker�tt�viin esineiseen */
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
    	
    	/* Tarkistetaan t�rm�ykset emoalukseen */
    	// Tarkistetaan, onko emoaluksen ja pelaajan v�linen et�isyys riitt�v�n pieni
    	// tarkkoja osumatarkistuksia varten
    	if (Math.abs(wrapper.mothership.x - x) <= Wrapper.gridSize) {
        	if (Math.abs(wrapper.mothership.y - y) <= Wrapper.gridSize) {
        		
        		// Tarkistetaan osuma
        		if (Utility.isColliding(wrapper.mothership, this)) {
        			// Siirryt��n emoaluksen valikkoon
        			gameMode.moveToMothershipMenu();
        		}
        	}
    	}
    }
    
    /**
     * K�sittelee t�rm�ykset.
     * 
     * @param _damage Osuman aiheuttama vahinko
     * @param _armorPiercing Osuman kyky l�p�ist� suojat (k�ytet��n, kun t�rm�ttiin ammukseen)
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
     * 
     * Toimintojen vakiot l�ytyv�t GfxObject-luokan alusta.
     */
    @Override
    protected void triggerEndOfAction()
    {    	
        // Tuhotaan pelaaja ja siirryt��n pois pelitilasta
        if (actionId == GfxObject.ACTION_DESTROYED) {
            setUnactive();
            
            gameMode.endGameMode();
        }
    }
}

