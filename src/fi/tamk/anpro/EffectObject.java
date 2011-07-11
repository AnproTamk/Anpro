package fi.tamk.anpro;

import javax.microedition.khronos.opengles.GL10;

/**
 *  Sis‰lt‰‰ yhden efektin toiminnot.
 */
public class EffectObject extends GameObject
{
	// Efektin sijainti
	private float x;
	private float y;

	// Efektin tyyppi
	private byte effectType;
	
	// Effektin tila
	public boolean activated = false;

	// Wrapper
	private Wrapper wrapper;
	
	// Peliobjekti, jota seurataan
	private GameObject parentObject;
	
	public EffectObject(int _speed, byte _effectType)
	{
		super(_speed);
		
		effectType = _effectType;
    
        // Haetaan animaatioiden pituudet
        animationLength = new int[GLRenderer.AMOUNT_OF_EFFECT_ANIMATIONS];
        
        if (GLRenderer.effectAnimations[_effectType] != null) {
            animationLength[_effectType] = GLRenderer.effectAnimations[_effectType].length;
        }
		
		wrapper = Wrapper.getInstance();
		
		listId = wrapper.addToList(this, Wrapper.CLASS_TYPE_EFFECT, 0);
	}
	
	/**
	 * Aktivoi peliobjektin efektin
	 * 
	 * @param _object Peliobjekti
	 */
	public void activate(GameObject _object)
	{
		parentObject = _object;
		x = _object.x;
		y = _object.y;
		
		setActive();
		
		setAction(effectType, 1, 8, GfxObject.ACTION_DESTROYED);
		updatePosition();
	}

	/**
	 * Aktivoi peliobjektin efektin
	 * 
	 * @param _x Objektin X-koordinaatti
	 * @param _y Objektin Y-koordinaatti
	 */
	public void activate(float _x, float _y)
	{
		x = _x;
		y = _y;
		
		setActive();
		
		setAction(effectType, 1, 8, GfxObject.ACTION_DESTROYED);
	}
	
	/**
	 * P‰ivitt‰‰ efektien sijainnit
	 */
	public void updatePosition()
	{
		if (parentObject != null) {
			x = parentObject.x;
			y = parentObject.y;
		}
	}
	
	/**
     * M‰‰ritt‰‰ objektin aktiiviseksi.
     */
    @Override
    public final void setActive()
    {
		activated = true;
        wrapper.effectStates.set(listId, Wrapper.FULL_ACTIVITY);
    }

    /**
     * M‰‰ritt‰‰ objektin ep‰aktiiviseksi. Sammuttaa myˆs teko‰lyn jos se on tarpeen.
     */
    @Override
    public final void setUnactive()
    {
    	activated = false;
        wrapper.effectStates.set(listId, Wrapper.INACTIVE);
    }
    
    /**
     * Piirt‰‰ objektin k‰ytˆss‰ olevan tekstuurin tai animaation ruudulle.
     * 
     * @param _gl OpenGL-konteksti
     */
	@Override
	public void draw(GL10 _gl)
	{
		// Tarkistaa onko animaatio p‰‰ll‰ ja kutsuu oikeaa animaatiota tai tekstuuria
        if (usedAnimation >= 0){
            GLRenderer.effectAnimations[usedAnimation].draw(_gl, x, y, 90, currentFrame);
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
		if (actionId == GfxObject.ACTION_DESTROYED) {
			setUnactive();
		}
	}
    
	/*
	Kun ammus tai alus osuu pelaajaan, tai pelaajan ammus osuu viholliseen, pit‰isi aluksen vieress‰ v‰l‰ht‰‰ suoja.
	Suojan n‰ytt‰v‰ funktio tehd‰‰n EffectManageriin (funktioksi esim. showArmorEffect) ja efektin toiminnot EffectObjectiin.

	EffectObject asettaisi objektin koordinaatit, tekstuurin ja k‰ynnist‰isi objektin animaation. 
	J‰lkimm‰isin voidaan toteuttaa setAction- ja triggerEndOfAction-funktioilla (ks. GfxObject).
	*/

}
