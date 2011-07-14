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
	public static final byte MESSAGE_OUTOFBOUNDS    = 62;
	public static final byte MESSAGE_AUTOPILOT_ON   = 63;
	public static final byte MESSAGE_AUTOPILOT_OFF  = 64;
	public static final byte MESSAGE_NEWSKILLSREADY = 65;
	public static final byte MESSAGE_REPAIR         = 66;
	public static final byte MESSAGE_ARMORSOFF      = 67;
	
	/* Ilmoitusobjektit */
	public Message outOfBounds;
	private Message autoPilotOn;
	private Message autoPilotOff;
	private Message newSkillsReady;
	private Message repair;
	private Message armorsOff;
	
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
	public final void showOutOfBoundsMessage()
	{
		outOfBounds.activate();
	}
}
