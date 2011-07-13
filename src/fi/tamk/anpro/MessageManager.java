package fi.tamk.anpro;

/**
 *  Hallitsee kaikkia ilmoituksia, esimerkiksi rajojen ylittämisestä annettavaa
 *  "Out of bounds" tekstiä.
 */
public class MessageManager
{
	/* Osoitin tähän luokkaan (singleton-toimintoa varten) */
	private static MessageManager instance;
	
	/* Ilmoitusten vakiot */
	public static final int MESSAGE_OUTOFBOUNDS    = 0;
	public static final int MESSAGE_AUTOPILOT_ON   = 1;
	public static final int MESSAGE_AUTOPILOT_OFF  = 2;
	public static final int MESSAGE_NEWSKILLSREADY = 3;
	public static final int MESSAGE_REPAIR         = 4;
	public static final int MESSAGE_ARMORSOFF      = 5;
	
	/* Ilmoitusobjektit */
	private Message outOfBounds;
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
		//outOfBounds = new Message(0, 1500);
	}
	
	/**
	 * Palauttaa osoittimen tähän luokkaan.
	 * 
	 * @return MessageManager Osoitin tähän luokkaan
	 */
	synchronized public static MessageManager getInstance()
	{
		if (instance == null) {
			instance = new MessageManager();
		}
		return instance;
	}
}
