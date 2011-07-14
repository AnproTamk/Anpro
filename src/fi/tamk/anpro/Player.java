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
    private Wrapper  wrapper;
    private GameMode gameMode;
    
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
    public Player(int _health, int _armor, GameMode _gameMode)
    {
        super(6); // TODO: Pelaaja tarvitsee nopeuden varten
        
        // Otetaan Wrapper käyttöön ja tallennetaan pelitilan osoitin
        wrapper  = Wrapper.getInstance();
        gameMode = _gameMode;
        
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
    public final void draw(GL10 _gl)
    {
        if (usedAnimation >= 0){
            GLRenderer.playerAnimations[usedAnimation].draw(_gl, x, y, direction, currentFrame);
        }
        else{
            GLRenderer.playerTextures[usedTexture].draw(_gl, x, y, direction, 0);
        }
    }
	
	/**
     * Käsittelee objektin törmäystarkistukset.
     */
    public final void checkCollision()
    {
    	/* Tarkistaa törmäykset kerättäviin esineiseen */
    	for (int i = wrapper.collectables.size()-1; i >= 0; --i) {
    		
			if (Math.abs(x - wrapper.collectables.get(i).x) <= Wrapper.gridSize) {
	        	if (Math.abs(y - wrapper.collectables.get(i).y) <= Wrapper.gridSize) {
	        		
	        		if (Utility.isColliding(wrapper.collectables.get(i), this)) {
	        			wrapper.collectables.get(i).triggerCollision(0, 0);
	        		}
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
        }
        
        Utility.checkDamage(this, _damage, _armorPiercing);
        
        Hud.armorBar.updateValue(currentArmor);
        Hud.healthBar.updateValue(currentHealth);
        
        if (currentHealth <= 0) {
        	wrapper.playerState = Wrapper.ONLY_ANIMATION;
        	setAction(GLRenderer.ANIMATION_DESTROY, 1, 1, GfxObject.ACTION_DESTROYED);
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
        // Tuhotaan pelaaja ja siirrytään pois pelitilasta
        if (actionId == GfxObject.ACTION_DESTROYED) {
            setUnactive();
            
            gameMode.endGameMode();
        }
    }
}

