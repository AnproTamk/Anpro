package fi.tamk.anpro;

import javax.microedition.khronos.opengles.GL10;

import android.util.Log;

/**
 *  Sisältää yhden ilmoituksen toiminnot.
 */
public class Message extends GuiObject
{
	// Viestin näyttöaika ja animaatio
	private int showTime;
	private int returnAnimation;
	
	// Osoitin Wrapperiin
	private Wrapper wrapper;
	
	/**
	 * Alustaa luokan muuttujat.
	 * 
	 * @param _message  Viestin tyyppi (tekstuuri)
	 * @param _showTime Aika, jonka viesti on ruudulla
	 */
	public Message(int _message, int _showTime)
	{
		super(0, 100);
		
		/* Tallennetaan muuttujat */
		returnAnimation = _message;
		showTime        = _showTime;
		
		/* Alustetaan muuttujat */
		z = 0;

        /* Määritetään objektin tila (piirtolista) */
		wrapper = Wrapper.getInstance();
		wrapper.addToDrawables(this);
		state = Wrapper.INACTIVE;
	}

	/* =======================================================
	 * Perityt funktiot
	 * ======================================================= */
    /**
     * Piirtää käytössä olevan tekstuurin ruudulle.
     * 
     * @param GL10 OpenGL-konteksti
     */
	@Override
    public final void draw(GL10 _gl)
    {
		GLRenderer.hudAnimations[usedAnimation].draw(_gl, -128 + CameraManager.xTranslate, 180 + CameraManager.yTranslate, 90, currentFrame);
		GLRenderer.hudAnimations[usedAnimation+1].draw(_gl, 128 + CameraManager.xTranslate, 180 + CameraManager.yTranslate, 90, currentFrame);
    }

	/* =======================================================
	 * Uudet funktiot
	 * ======================================================= */
	public final void activate()
	{
		state = Wrapper.FULL_ACTIVITY;
		
		setAction(returnAnimation, 1, 1, ACTION_SHOWMESSAGE, 0, 0, 3, showTime);
	}
	
	@Override
	public void triggerEndOfAction()
	{
		usedAnimation = returnAnimation;
		
		state = Wrapper.INACTIVE;
		
		MessageManager.isShowing = false;
	}
}
