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
        
        // Asetetaan törmäystunnistuksen säde
        collisionRadius = 25;
        
        // Haetaan käytettävien animaatioiden pituudet
        /*animationLength[0] = GLRenderer.playerAnimations.get(0).length;
        animationLength[1] = GLRenderer.playerAnimations.get(1).length;
        animationLength[2] = GLRenderer.playerAnimations.get(2).length;*/
        
        // Haetaan osoitin Wrapper-luokkaan
        wrapper = Wrapper.getInstance();
        
        // Lisätään pelaaja piirtolistalle
        wrapper.addToList(this, Wrapper.CLASS_TYPE_PLAYER);
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
            GLRenderer.playerAnimations.get(usedAnimation).draw(_gl, x, y, direction, currentFrame);
            //animations.get(usedAnimation).draw(_gl, x, y, direction, currentFrame);
        }
        else{
            GLRenderer.playerTextures.get(usedTexture).draw(_gl, x, y, direction);
            //textures.get(usedTexture).draw(_gl, x, y, direction);
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
    	}
    }
}

