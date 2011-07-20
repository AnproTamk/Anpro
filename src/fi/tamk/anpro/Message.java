package fi.tamk.anpro;

import javax.microedition.khronos.opengles.GL10;

/**
 *  Sis‰lt‰‰ yhden ilmoituksen toiminnot.
 */
public class Message extends GuiObject
{
	// Viestin n‰yttˆaika ja ajanoton alku
	private int  showTime;
	private long startTime;
	
	// Kierron suunta
	private byte messageState;				 // M‰‰ritt‰‰ objektin tilan
	private byte TURN_VISIBLE   = 1; // K‰‰nt‰‰ objektin n‰kyv‰ksi
	private byte STAY_STILL   = 2; // Pit‰‰ objektin n‰kyviss‰
	private byte TURN_INVISIBLE = 3; // K‰‰nt‰‰ objektin n‰kym‰ttˆm‰ksi
	
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
		super(0, 100);
		
		// M‰‰rit‰ tekstuuri
		usedTexture = _message;
		
		// Tallennetaan viestin tyyppi ja aika
		messageType = (byte) (_message - GLRenderer.TEXTURE_MESSAGE);
		showTime    = _showTime;
		
		// Asetetaan pois n‰kyvist‰
		wrapper = Wrapper.getInstance();
		
		state = Wrapper.INACTIVE;
	}
	
	public final void activate()
	{
		state = Wrapper.FULL_ACTIVITY;
		
		yAxisRotation = 90.0f;
		
		state = TURN_VISIBLE;
	}
	
	public final void updateAngle()
	{
		if (messageState == TURN_VISIBLE) {
			yAxisRotation -= (0.1f + ((90 - yAxisRotation) * 0.45f));
			
			if (yAxisRotation <= 3.0f) {
				
				yAxisRotation = 0.0f;
				messageState = STAY_STILL;
				startTime = android.os.SystemClock.uptimeMillis();
				
			}
		}
		else if (messageState == STAY_STILL) {
			if (android.os.SystemClock.uptimeMillis() - startTime >= showTime) {
				
				messageState = TURN_INVISIBLE;
			}
		}
		else if (messageState == TURN_INVISIBLE) {
			yAxisRotation += (0.1f + (yAxisRotation * 0.45f));

			if (yAxisRotation >= 87.0f) {
				yAxisRotation = 90.0f;
				messageState = STAY_STILL;
				
				state = Wrapper.INACTIVE;
			}
		}
	}

    /**
     * Piirt‰‰ k‰ytˆss‰ olevan tekstuurin ruudulle.
     * 
     * @param GL10 OpenGL-konteksti
     */
	@Override
    public final void draw(GL10 _gl)
    {
		GLRenderer.hudTextures[usedTexture].drawIn3D(_gl, x + CameraManager.xTranslate,  y + CameraManager.yTranslate, direction,
													 0, xAxisRotation, yAxisRotation);
    }
}
