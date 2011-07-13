package fi.tamk.anpro;

import javax.microedition.khronos.opengles.GL10;

public class Collectable extends GameObject
{
	// Wrapper
	private Wrapper wrapper;
	
	/**
	 * Alustaa luokan muuttujat.
	 * 
	 * @param _x         X-koordinaatti
	 * @param _y         Y-koordinaatti
	 * @param _speed     Liikkumisnopeus
	 * @param _direction Liikkumissuunta
	 */
	public Collectable(int _x, int _y)
	{
		super(0);
		
		// Tallennetaan koordinaatit
		x = _x;
		y = _y;
		
		// Otetaan Wrapper k‰yttˆˆn
		wrapper = Wrapper.getInstance();
		
		// M‰‰ritell‰‰n tˆrm‰ystunnistus
		collisionRadius = (int) (128 * Options.scale);

		// Lis‰t‰‰n objekti piirtolistalle
		listId = wrapper.addToList(this, Wrapper.CLASS_TYPE_COLLECTABLE, 0);
	}
    
    /**
     * Piirt‰‰ objektin k‰ytˆss‰ olevan tekstuurin tai animaation ruudulle.
     * 
     * @param GL10 OpenGL-konteksti
     */
	@Override
	public void draw(GL10 _gl)
	{
        // Tarkistaa onko animaatio p‰‰ll‰ ja kutsuu oikeaa animaatiota tai tekstuuria
        if (usedAnimation >= 0) {
            GLRenderer.collectableAnimations[usedAnimation].draw(_gl, x, y, 0, currentFrame);
        }
        else {
            GLRenderer.collectableTextures[usedTexture].draw(_gl, x, y, direction, 0);
        }
	}
    
    /**
     * M‰‰ritt‰‰ objektin aktiiviseksi.
     */
	@Override
	public void setActive()
	{
		wrapper.collectableStates.set(listId, Wrapper.FULL_ACTIVITY);
	}

    /**
     * M‰‰ritt‰‰ objektin ep‰aktiiviseksi. Sammuttaa myˆs teko‰lyn jos se on tarpeen.
     */
	@Override
	public void setUnactive()
	{
		wrapper.collectableStates.set(listId, Wrapper.INACTIVE);
	}

    /**
     * K‰sittelee tˆrm‰ykset.
     * 
     * @param int Osuman aiheuttama vahinko
     * @param int Osuman kyky l‰p‰ist‰ suojat (k‰ytet‰‰n, kun tˆrm‰ttiin ammukseen)
     */
    @Override
    public final void triggerCollision(int _damage, int _armorPiercing)
    {
    	wrapper.collectableStates.set(listId, Wrapper.ONLY_ANIMATION);
    	setAction(GLRenderer.ANIMATION_COLLECTED, 1, 1, GfxObject.ACTION_DESTROYED);
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
		if (actionId == GfxObject.ACTION_DESTROYED) {
			setUnactive();
		}
	}
}
