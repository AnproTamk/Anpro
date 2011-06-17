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
        
        // Asetetaan t�rm�ystunnistuksen s�de
        collisionRadius = 25;
        
        // Haetaan k�ytett�vien animaatioiden pituudet
        /*animationLength[0] = GLRenderer.playerAnimations.get(0).length;
        animationLength[1] = GLRenderer.playerAnimations.get(1).length;
        animationLength[2] = GLRenderer.playerAnimations.get(2).length;*/
        
        // Haetaan osoitin Wrapper-luokkaan
        wrapper = Wrapper.getInstance();
        
        // Lis�t��n pelaaja piirtolistalle
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
            GLRenderer.playerAnimations.get(usedAnimation).draw(_gl, x, y, direction, currentFrame);
            //animations.get(usedAnimation).draw(_gl, x, y, direction, currentFrame);
        }
        else{
            GLRenderer.playerTextures.get(usedTexture).draw(_gl, x, y, direction);
            //textures.get(usedTexture).draw(_gl, x, y, direction);
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
    	}
    }
}

