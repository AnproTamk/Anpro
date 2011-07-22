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
	public static final byte MESSAGE_OUTOFBOUNDS = GLRenderer.ANIMATION_MESSAGE;
	public static final byte MESSAGE_ARMORS_OFF  = GLRenderer.ANIMATION_MESSAGE + 2;
	public static final byte MESSAGE_BOSS_NEARBY = GLRenderer.ANIMATION_MESSAGE + 4;
	
	/* Ilmoitusobjektit */
	public static Message outOfBounds;
	public static Message armorsOff;
	public static Message bossNearby;

	/* Onko viesti n‰kyviss‰ */
	public static boolean isShowing;
	
	/**
	 * Alustaa luokan muuttujat.
	 */
	public MessageManager()
	{
		outOfBounds = new Message(MESSAGE_OUTOFBOUNDS, 1500);
		armorsOff   = new Message(MESSAGE_ARMORS_OFF, 2000);
		bossNearby  = new Message(MESSAGE_BOSS_NEARBY, 3000);
		
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
	 * N‰ytt‰‰ "Armors Off"-viestin.
	 */
	public static final void showArmorsOffMessage()
	{
		if (!isShowing) {
			isShowing = true;
			armorsOff.activate();
		}
	}
	
	/**
	 * N‰ytt‰‰ "Boss Nearby"-viestin.
	 */
	public static final void showBossNearbyMessage()
	{
		if (!isShowing) {
			isShowing = true;
			bossNearby.activate();
		}
	}

}
