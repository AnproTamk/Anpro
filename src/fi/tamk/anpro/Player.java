package fi.tamk.anpro;

import javax.microedition.khronos.opengles.GL10;

import android.util.Log;

/**
 * Sis‰lt‰‰ pelaajan omat ominaisuudet ja tiedot, kuten asettamisen aktiiviseksi ja
 * ep‰aktiiviseksi, piirt‰misen ja tˆrm‰yksenhallinnan (ei tunnistusta).
 * 
 * @extends GameObject
 */
public class Player extends AiObject
{
    /* Osoittimet muihin luokkiin */
    private Wrapper  wrapper;
    private GameMode gameMode;
    private Hud      hud;
    
    /* Suojien palautumisen ajastin */
    public long outOfBattleTime = 0;
    
    /**
     * Alustaa luokan muuttujat.
     * 
     * @param _health Pelaajan el‰m‰t/kest‰vyys
     * @param _armor Pelaajan puolustus
     * @param _gameMode Osoitin SurvivalModeen
     */
    public Player(int _health, int _armor, GameMode _gameMode, Hud _hud)
    {
        super(8); // TODO: Pelaajalle voisi mieluummin antaa nopeuden suoraan rakentajassa
        		  // Muiden GameObjectien tapaan.

        /* Tallennetaan muuttujat */
        gameMode      = _gameMode;
        hud           = _hud;
        health  	  = _health;
        currentHealth = _health;
        armor         = _armor;
        currentArmor  = _armor;
        
        /* Haetaan tarvittavat luokat k‰yttˆˆn */
        wrapper = Wrapper.getInstance();
        
        /* M‰‰ritet‰‰n Health- ja Armor-palkit */
    	hud.healthBar.initBar(health);
    	hud.armorBar.initBar(armor);
        
    	/* Alustetaan muuttujat */
    	z = 3;
    	
        // M‰‰ritet‰‰n asetukset
        collisionRadius = (int) (25 * Options.scale);
        setMovementSpeed(0.0f);
        
        // Haetaan k‰ytett‰vien animaatioiden pituudet
        animationLength = new int[GLRenderer.AMOUNT_OF_PLAYER_ANIMATIONS];
        
        for (int i = 0; i < GLRenderer.AMOUNT_OF_PLAYER_ANIMATIONS; ++i) {
            if (GLRenderer.playerAnimations[i] != null) {
                animationLength[i] = GLRenderer.playerAnimations[i].length;
            }
        }
        
        /* M‰‰ritet‰‰n objektin tila (piirtolista ja teko‰ly) */
        wrapper.addToDrawables(this);
        ai = new PlayerAi(this, Wrapper.CLASS_TYPE_PLAYER);
    }

	/* =======================================================
	 * Perityt funktiot
	 * ======================================================= */
    /**
     * Asettaa pelaajan aktiiviseksi.
     */
    @Override
    public final void setActive()
    {
        state = Wrapper.FULL_ACTIVITY;
    }

    /**
     * M‰‰ritt‰‰ objektin ep‰aktiiviseksi. Sammuttaa myˆs teko‰lyn jos se on tarpeen.
     */
    @Override
    public final void setUnactive()
    {
        state = Wrapper.INACTIVE;
    }

    /**
     * Piirt‰‰ k‰ytˆss‰ olevan animaation tai tekstuurin ruudulle.
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
     * K‰sittelee objektin tˆrm‰ystarkistukset.
     */
    public final void checkCollision()
    {
    	/* Tarkistaa tˆrm‰ykset ker‰tt‰viin piste-esineiseen */
    	for (int i = wrapper.scoreCollectables.size()-1; i >= 0; --i) {
    		
    		if (wrapper.scoreCollectables.get(i).state == Wrapper.FULL_ACTIVITY) {
    		
				if (Math.abs(x - wrapper.scoreCollectables.get(i).x) <= Wrapper.gridSize) {
		        	if (Math.abs(y - wrapper.scoreCollectables.get(i).y) <= Wrapper.gridSize) {
		        		
		        		if (Utility.isColliding(wrapper.scoreCollectables.get(i), this)) {
		        			wrapper.scoreCollectables.get(i).triggerCollision(COLLISION_WITH_PLAYER, 0, 0);
		        		}
		        	}
				}
    		}
    	}
    	
    	/* Tarkistaa tˆrm‰ykset ker‰tt‰viin aseisiin */
    	if (wrapper.weaponCollectable.state == Wrapper.FULL_ACTIVITY) {
			if (Math.abs(x - wrapper.weaponCollectable.x) <= Wrapper.gridSize) {
	        	if (Math.abs(y - wrapper.weaponCollectable.y) <= Wrapper.gridSize) {
	        		
	        		if (Utility.isColliding(wrapper.weaponCollectable, this)) {
	        			wrapper.weaponCollectable.triggerCollision(COLLISION_WITH_PLAYER, 0, 0);
	        		}
	        	}
			}
    	}
    }
    
    /**
     * K‰sittelee tˆrm‰ykset.
     * 
     * @param _damage Osuman aiheuttama vahinko
     * @param _armorPiercing Osuman kyky l‰p‰ist‰ suojat (k‰ytet‰‰n, kun tˆrm‰ttiin ammukseen)
     */
    @Override
    public final void triggerCollision(int _eventType, int _damage, int _armorPiercing)
    {
    	VibrateManager.vibrateOnHit();
	    
	    int armorTemp = currentArmor;
	    int healthTemp = currentHealth;
	    
	    Utility.checkDamage(this, _damage, _armorPiercing);
	    
	    if (currentArmor < armorTemp) {
	    	EffectManager.showPlayerArmorEffect(this);
	    	EffectManager.showArmorHitEffect(hud.armorBar);
	    }
	    if (currentHealth < healthTemp) {
	    	EffectManager.showHealthHitEffect(hud.healthBar);
	    }
	    
	    hud.armorBar.updateValue(currentArmor);
	    hud.healthBar.updateValue(currentHealth);
	    
	    if (currentHealth <= 0 && state == Wrapper.FULL_ACTIVITY) {
	    	state = Wrapper.ONLY_ANIMATION;
	    	setAction(GLRenderer.ANIMATION_DESTROY, 1, 1, ACTION_DESTROYED, 0, 0);
	    }
	    
	    if (_eventType == COLLISION_WITH_OBSTACLE && currentHealth > 0) {
    		state = Wrapper.ONLY_ANIMATION;
    		
    		turningDirection = 0;
            
            setMovementSpeed(0.0f);

            x -= Math.cos((direction * Math.PI)/180) * 100 * Options.scaleX;
            y -= Math.sin((direction * Math.PI)/180) * 100 * Options.scaleY;
            
            CameraManager.updateCameraPosition();
            
            direction -= 180;
            
            if (direction < 0) {
            	direction *= -1;
            }
            
            setAction(GLRenderer.ANIMATION_RESPAWN, 1, 2, ACTION_RESPAWN, 0, 0);
    	}
    }

    /**
     * K‰sittelee jonkin toiminnon p‰‰ttymisen. Kutsutaan animaation loputtua, mik‰li
     * actionActivated on TRUE.
     * 
     * K‰ytet‰‰n esimerkiksi objektin tuhoutuessa. Objektille m‰‰ritet‰‰n animaatioksi
     * sen tuhoutumisanimaatio, tilaksi Wrapperissa m‰‰ritet‰‰n 2 (piirret‰‰n, mutta
     * p‰ivitet‰‰n ainoastaan animaatio) ja asetetaan actionActivatedin arvoksi TRUE.
     * T‰llˆin GameThread p‰ivitt‰‰ objektin animaation, Renderer piirt‰‰ sen, ja kun
     * animaatio p‰‰ttyy, kutsutaan objektin triggerEndOfAction-funktiota. T‰ss‰
     * funktiossa objekti k‰sittelee tilansa. Tuhoutumisanimaation tapauksessa objekti
     * m‰‰ritt‰‰ itsens‰ ep‰aktiiviseksi.
     * 
     * Jokainen objekti luo funktiosta oman toteutuksensa, sill‰ toimintoja voi olla
     * useita. Objekteilla on myˆs k‰ytˆss‰‰n actionId-muuttuja, jolle voidaan asettaa
     * haluttu arvo. T‰m‰ arvo kertoo objektille, mink‰ toiminnon se juuri suoritti.
     * 
     * Toimintojen vakiot lˆytyv‰t GfxObject-luokan alusta.
     */
    @Override
    protected void triggerEndOfAction()
    {    	
        // Tuhotaan pelaaja ja siirryt‰‰n pois pelitilasta
        if (actionId == ACTION_DESTROYED) {
            setUnactive();
            
            gameMode.endGameMode();
        }
        else if (actionId == ACTION_RESPAWN) {
        	setMovementSpeed(1.0f);
        	
        	state = Wrapper.FULL_ACTIVITY;
        }
    }
}

