package fi.tamk.anpro;

import javax.microedition.khronos.opengles.GL10;

import android.util.Log;

/**
 * Sis�lt�� pelaajan omat ominaisuudet ja tiedot, kuten asettamisen aktiiviseksi ja
 * ep�aktiiviseksi, piirt�misen ja t�rm�yksenhallinnan (ei tunnistusta).
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
     * @param _health Pelaajan el�m�t/kest�vyys
     * @param _armor Pelaajan puolustus
     * @param _gameMode Osoitin SurvivalModeen
     */
    public Player(int _health, int _armor, GameMode _gameMode, Hud _hud)
    {
        super(8); // TODO: Pelaajalle voisi mieluummin antaa nopeuden suoraan rakentajassa
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
    	hud.healthBar.initBar(health);
    	hud.armorBar.initBar(armor);
        
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
        wrapper.addToDrawables(this);
        
        // Asetetaan pelaajan "teko�ly"
        ai = new PlayerAi(this, Wrapper.CLASS_TYPE_PLAYER);
        
        // Asetetaan pelaajan asetukset
        setMovementSpeed(0.0f);
    }

    /**
     * Asettaa pelaajan aktiiviseksi.
     */
    @Override
    public final void setActive()
    {
        state = Wrapper.FULL_ACTIVITY;
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
    		
    		if (wrapper.collectables.get(i).state == Wrapper.FULL_ACTIVITY) {
    		
				if (Math.abs(x - wrapper.collectables.get(i).x) <= Wrapper.gridSize) {
		        	if (Math.abs(y - wrapper.collectables.get(i).y) <= Wrapper.gridSize) {
		        		
		        		if (Utility.isColliding(wrapper.collectables.get(i), this)) {
		        			wrapper.collectables.get(i).triggerCollision(COLLISION_WITH_PLAYER, 0, 0);
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
            
            direction -= 180;
            
            if (direction < 0) {
            	direction *= -1;
            }
            
            setAction(GLRenderer.ANIMATION_RESPAWN, 1, 2, ACTION_RESPAWN, 0, 0);
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

