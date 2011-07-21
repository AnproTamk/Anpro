package fi.tamk.anpro;

import android.util.Log;

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
	public static final byte MESSAGE_REPAIR_NEEDED  = GLRenderer.TEXTURE_MESSAGE + 4;
	public static final byte MESSAGE_ARMORS_OFF     = GLRenderer.TEXTURE_MESSAGE + 5;
	
	/* Ilmoitusobjektit */
	public static Message outOfBounds;
	public static Message autoPilotOn;
	public static Message autoPilotOff;
	public static Message newSkillsReady;
	public static Message repairNeeded;
	public static Message armorsOff;

	/* Onko viesti n‰kyviss‰ */
	public static boolean isShowing;
	
	/**
	 * Alustaa luokan muuttujat.
	 */
	public MessageManager()
	{
		outOfBounds    = new Message(MESSAGE_OUTOFBOUNDS, 1500);
		autoPilotOn    = new Message(MESSAGE_AUTOPILOT_ON, 1500);
		autoPilotOff   = new Message(MESSAGE_AUTOPILOT_OFF, 1500);
		newSkillsReady = new Message(MESSAGE_NEWSKILLSREADY, 3000);
		repairNeeded   = new Message(MESSAGE_REPAIR_NEEDED, 3000);
		armorsOff      = new Message(MESSAGE_ARMORS_OFF, 2000);
		
		isShowing = false;
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

	/* =======================================================
	 * Uudet funktiot
	 * ======================================================= */
	public static final void updateMessages()
	{
		outOfBounds.updateAngle();
		autoPilotOn.updateAngle();
		autoPilotOff.updateAngle();
		newSkillsReady.updateAngle();
		repairNeeded.updateAngle();
		armorsOff.updateAngle();
	}

	/**
	 * N‰ytt‰‰ "Out of bounds"-viestin.
	 */
	public static final void showOutOfBoundsMessage()
	{
		if (!isShowing) {
			isShowing = true;
			outOfBounds.activate();
		}
	}
	
	/**
	 * N‰ytt‰‰ "Autopilot On"-viestin.
	 */
	public static final void showAutoPilotOnMessage()
	{
		if (!isShowing) {
			isShowing = true;
			autoPilotOn.activate();			
		}
	}
	
	/**
	 * N‰ytt‰‰ "AutoPilot Off"-viestin.
	 */
	public static final void showAutoPilotOffMessage()
	{
		if (!isShowing) {
			isShowing = true;
			autoPilotOff.activate();
		}
	}
	
	/**
	 * N‰ytt‰‰ "New Skills Ready"-viestin.
	 */
	public static final void showNewSkillsReadyMessage()
	{
		if (!isShowing) {
			isShowing = true;
			newSkillsReady.activate();
		}
	}
	
	/**
	 * N‰ytt‰‰ "Repair Needed"-viestin.
	 */
	public static final void showRepairNeededMessage()
	{
		if (!isShowing) {
			isShowing = true;
			repairNeeded.activate();
		}
	}
	
	/**
	 * N‰ytt‰‰ "Armors Off"-viestin.
	 */
	public static final void showArmorsOffMessage()
	{
		if (!isShowing) {
			isShowing = true;
			armorsOff.activate();
		}
	}

}
