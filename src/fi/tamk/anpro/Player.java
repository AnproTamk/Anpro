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
    /* Pelaajan tiedot */
    public int health;
    public int defence;
    
    /* Osoittimet muihin luokkiin */
    private Wrapper wrapper;
    private Bar     healthBar;
    
    /**
     * Alustaa luokan muuttujat.
     * 
     * @param int Pelaajan el�m�t/kest�vyys
     * @param int Pelaajan puolustus
     */
    public Player(int _health, int _defence)
    {
        super();
        
        // Tallennetaan tiedot
        health  = _health;
        defence = _defence;
        
        // M��ritet��n healthBarin tiedot
        healthBar = Hud.getHealthBar();
        healthBar.initHealthBar(health);
        
        // Asetetaan t�rm�ystunnistuksen s�de
        collisionRadius = (int) (25 * Options.scale);
        
        // Haetaan k�ytett�vien animaatioiden pituudet
        for (int i = 0; i < 4; ++i) {
            if (GLRenderer.playerAnimations[i] != null) {
                animationLength[i] = GLRenderer.playerAnimations[i].length;
            }
        }
        
        // Haetaan osoitin Wrapper-luokkaan
        wrapper = Wrapper.getInstance();
        
        // Lis�t��n pelaaja piirtolistalle ja m��ritet��n tila
        wrapper.addToList(this, Wrapper.CLASS_TYPE_PLAYER, 1);
    }

    /**
     * Asettaa pelaajan aktiiviseksi.
     */
    @Override
    public final void setActive()
    {
        wrapper.playerState = 1;
    }

    /**
     * Asettaa pelaajan ep�aktiiviseksi.
     */
    @Override
    public final void setUnactive()
    {
        wrapper.playerState = 0;
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
     * K�sittelee r�j�hdyksien vaikutukset pelaajaan.
     * 
     * @param int R�j�hdyksen aiheuttama vahinko
     */
    @Override
    public final void triggerImpact(int _damage)
    {
        // R�j�hdykset eiv�t toistaiseksi vaikuta pelaajaan
    }

    /**
     * K�sittelee t�rm�yksien vaikutukset pelaajaan.
     * 
     * @param int Osuman tyyppi, eli mihin t�rm�ttiin (tyypit l�ytyv�t GameObjectista)
     * @param int Osuman aiheuttama vahinko
     * @param int Osuman kyky l�p�ist� suojat (k�ytet��n, kun t�rm�ttiin ammukseen)
     */
    @Override
    public final void triggerCollision(int _eventType, int _damage, int _armorPiercing)
    {
        if (_eventType == GameObject.COLLISION_WITH_ENEMY) {
            health -= _damage;
            
            healthBar.updateValue(health);
            
            if (health <= 0) {
            	wrapper.playerState = 2;
            	setAction(GLRenderer.ANIMATION_DESTROY, 1, 1);
            }
        }
    }

    /**
     * K�sittelee jonkin toiminnon p��ttymisen. Kutsutaan animaation loputtua, mik�li
     * actionActivated on TRUE.
     * 
     * (lue lis�� GfxObject-luokasta!)
     */
    @Override
    protected void triggerEndOfAction()
    {
        /* Tuhoutuminen */
        if (actionId == 1) {
            setUnactive();
            
            System.exit(0); // TODO
        }
    }
}

