package fi.tamk.anpro;

/**
 *  Sis‰lt‰‰ yhden ilmoituksen toiminnot.
 */
public class Message extends GuiObject
{
	private int showTime;
	
	/**
	 * Alustaa luokan muuttujat.
	 * 
	 * @param _message  Viestin tyyppi (tekstuuri)
	 * @param _showTime Aika, jonka viesti on ruudulla
	 */
	public Message(byte _message, int _showTime)
	{
		super(0, 200);
		
		// M‰‰rit‰ tekstuuri
		usedTexture = _message;
		
		// Tallenna aika
		showTime = _showTime;
	}
	
	public final void activate()
	{
		
	}
}
