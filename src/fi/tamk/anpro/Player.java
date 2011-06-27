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
    /* Pelaajan tiedot */
    public int health;
    public int defence;
    
    /* Osoittimet muihin luokkiin */
    private Wrapper wrapper;
    private Bar     healthBar;
    
    /**
     * Alustaa luokan muuttujat.
     * 
     * @param int Pelaajan elämät/kestävyys
     * @param int Pelaajan puolustus
     */
    public Player(int _health, int _defence)
    {
        super();
        
        // Tallennetaan tiedot
        health  = _health;
        defence = _defence;
        
        // Määritetään healthBarin tiedot
        healthBar = Hud.getHealthBar();
        healthBar.initHealthBar(health);
        
        // Asetetaan törmäystunnistuksen säde
        collisionRadius = (int) (25 * Options.scale);
        
        // Haetaan käytettävien animaatioiden pituudet
        for (int i = 0; i < 4; ++i) {
            if (GLRenderer.playerAnimations[i] != null) {
                animationLength[i] = GLRenderer.playerAnimations[i].length;
            }
        }
        
        // Haetaan osoitin Wrapper-luokkaan
        wrapper = Wrapper.getInstance();
        
        // Lisätään pelaaja piirtolistalle ja määritetään tila
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
     * Asettaa pelaajan epäaktiiviseksi.
     */
    @Override
    public final void setUnactive()
    {
        wrapper.playerState = 0;
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
     * Käsittelee räjähdyksien vaikutukset pelaajaan.
     * 
     * @param int Räjähdyksen aiheuttama vahinko
     */
    @Override
    public final void triggerImpact(int _damage)
    {
        // Räjähdykset eivät toistaiseksi vaikuta pelaajaan
    }

    /**
     * Käsittelee törmäyksien vaikutukset pelaajaan.
     * 
     * @param int Osuman tyyppi, eli mihin törmättiin (tyypit löytyvät GameObjectista)
     * @param int Osuman aiheuttama vahinko
     * @param int Osuman kyky läpäistä suojat (käytetään, kun törmättiin ammukseen)
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
     * Käsittelee jonkin toiminnon päättymisen. Kutsutaan animaation loputtua, mikäli
     * actionActivated on TRUE.
     * 
     * (lue lisää GfxObject-luokasta!)
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

