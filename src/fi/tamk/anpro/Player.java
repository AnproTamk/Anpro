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
    private Wrapper  wrapper;
    private GameMode gameMode;
    
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
    public Player(int _health, int _armor, GameMode _gameMode)
    {
        super(6); // TODO: Pelaaja tarvitsee nopeuden varten
        
        // Otetaan Wrapper k�ytt��n ja tallennetaan pelitilan osoitin
        wrapper  = Wrapper.getInstance();
        gameMode = _gameMode;
        
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
     * K�sittelee objektin t�rm�ystarkistukset.
     */
    public final void checkCollision()
    {
    	/* Tarkistaa t�rm�ykset ker�tt�viin esineiseen */
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
        // Tuhotaan pelaaja ja siirryt��n pois pelitilasta
        if (actionId == GfxObject.ACTION_DESTROYED) {
            setUnactive();
            
            gameMode.endGameMode();
        }
    }
}

