package fi.tamk.anpro;

/**
 *  Hallitsee kaikkia ilmoituksia, esimerkiksi rajojen ylitt‰misest‰ annettavaa
 *  "Out of bounds" teksti‰.
 */
public class MessageManager
{
	/* Osoitin t‰h‰n luokkaan (singleton-toimintoa varten) */
	private static MessageManager instance;
	
	/* Ilmoitusten vakiot */
	public static final byte MESSAGE_OUTOFBOUNDS    = GLRenderer.TEXTURE_MESSAGE;
	public static final byte MESSAGE_AUTOPILOT_ON   = GLRenderer.TEXTURE_MESSAGE + 1;
	public static final byte MESSAGE_AUTOPILOT_OFF  = GLRenderer.TEXTURE_MESSAGE + 2;
	public static final byte MESSAGE_NEWSKILLSREADY = GLRenderer.TEXTURE_MESSAGE + 3;
	public static final byte MESSAGE_REPAIR         = GLRenderer.TEXTURE_MESSAGE + 4;
	public static final byte MESSAGE_ARMORSOFF      = GLRenderer.TEXTURE_MESSAGE + 5;
	
	/* Ilmoitusobjektit */
	public static Message outOfBounds;
	public static Message autoPilotOn;
	public static Message autoPilotOff;
	public static Message newSkillsReady;
	public static Message repair;
	public static Message armorsOff;
	
	/**
	 * Alustaa luokan muuttujat.
	 */
	public MessageManager()
	{
		outOfBounds = new Message(MESSAGE_OUTOFBOUNDS, 1500);
	}
	
	/**
	 * Palauttaa osoittimen t‰h‰n luokkaan.
	 * 
	 * @return MessageManager Osoitin t‰h‰n luokkaan
	 */
	synchronized public static MessageManager getInstance()
	{
		if (instance == null) {
			instance = new MessageManager();
		}
		return instance;
	}
	
	/**
	 * N‰ytt‰‰ "Out of bounds"-viestin.
	 */
	public static final void showOutOfBoundsMessage()
	{
		outOfBounds.activate();
	}
}
