package fi.tamk.anpro;

import javax.microedition.khronos.opengles.GL10;

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
		
		// Tallenna aika
		showTime = _showTime;
		
		// Lisätään piirtolistaan
		wrapper = Wrapper.getInstance();
		listId  = wrapper.addToList(this, Wrapper.CLASS_TYPE_MESSAGE, 0);
	}
	
	public final void activate()
	{
		wrapper.messageStates.set(listId, Wrapper.FULL_ACTIVITY);
		
		yAxisRotation = 0.0f;
		
		state = 1;
	}
	
	public final void updateAngle()
	{
		if (state == 1) {
			yAxisRotation += 0.1f;
			
			if (yAxisRotation == 1) {
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
			yAxisRotation -= 0.1f;

			if (yAxisRotation == 0) {
				state = 1;
				wrapper.messageStates.set(listId, Wrapper.INACTIVE);
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
