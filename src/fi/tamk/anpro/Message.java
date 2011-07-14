package fi.tamk.anpro;

import javax.microedition.khronos.opengles.GL10;

import android.util.Log;

/**
 *  Sisältää yhden ilmoituksen toiminnot.
 */
public class Message extends GuiObject
{
	// Viestin näyttöaika ja ajanoton alku
	private int  showTime;
	private long startTime;
	
	// Kierron suunta
	private byte state;
	
	// Osoitin Wrapperiin
	private Wrapper wrapper;
	
	// Viestin tyyppi
	private byte messageType;
	
	/**
	 * Alustaa luokan muuttujat.
	 * 
	 * @param _message  Viestin tyyppi (tekstuuri)
	 * @param _showTime Aika, jonka viesti on ruudulla
	 */
	public Message(byte _message, int _showTime)
	{
		super(0, 200);
		
		// Määritä tekstuuri
		usedTexture = _message;
		
		// Tallennetaan viestin tyyppi ja aika
		messageType = (byte) (_message - 62);
		showTime    = _showTime;
		
		// Asetetaan pois näkyvistä
		wrapper = Wrapper.getInstance();
		wrapper.guiObjectStates.set(listId, Wrapper.INACTIVE);
		wrapper.messageStates.set(messageType, Wrapper.FULL_ACTIVITY);
	}
	
	public final void activate()
	{
		wrapper.guiObjectStates.set(listId, Wrapper.FULL_ACTIVITY);
		wrapper.messageStates.set(messageType, Wrapper.FULL_ACTIVITY);
		
		yAxisRotation = 90.0f;
		
		state = 1;
	}
	
	public final void updateAngle()
	{
		if (state == 1) {
			yAxisRotation -= (0.1f + (yAxisRotation * 0.45f));
			
			if (yAxisRotation <= 3.0f) {
				yAxisRotation = 0.0f;
				state = 2;
				startTime = android.os.SystemClock.uptimeMillis();
			}
		}
		else if (state == 2) {
			if (android.os.SystemClock.uptimeMillis() - startTime >= showTime) {
				state = 3;
			}
		}
		else if (state == 3) {
			yAxisRotation += (0.1f + (yAxisRotation * 0.45f));

			if (yAxisRotation >= 87.0f) {
				state = 1;
				wrapper.guiObjectStates.set(listId, Wrapper.INACTIVE);
				wrapper.messageStates.set(messageType, Wrapper.INACTIVE);
			}
		}
	}

    /**
     * Piirtää käytössä olevan tekstuurin ruudulle.
     * 
     * @param GL10 OpenGL-konteksti
     */
	@Override
    public final void draw(GL10 _gl)
    {
		GLRenderer.hudTextures[usedTexture].drawIn3D(_gl, x + cameraManager.xTranslate,  y + cameraManager.yTranslate, direction,
													 0, xAxisRotation, yAxisRotation);
    }
}
