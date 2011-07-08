package fi.tamk.anpro;

import java.util.ArrayList;

/**
 * Sis‰lt‰‰ Story-pelitilan yhden kent‰n yhden teht‰v‰n tiedot ja toiminnallisuuden.
 */
public class Task
{
	// Teht‰v‰n tyyppi
	private byte type;
	
	// Teht‰v‰st‰ saatavat kykypisteet
	private byte skillPoints;
	
	private ArrayList<Script> onStart;
	private ArrayList<Script> onEnd;
	
	public Task(byte _type, byte _skillPoints)
	{
		// Tallennetaan tiedot
		type = _type;
	}
	
	/**
	 * K‰ynnist‰‰ teht‰v‰n aloitustapahtumat, kuten ilmoitukset ja tarinan kertomiseen
	 * liittyv‰t tapahtumat.
	 */
	public final void triggerOnStartSequence()
	{
		
	}

	/**
	 * K‰ynnist‰‰ teht‰v‰n lopetustapahtumat, kuten ilmoitukset ja tarinan kertomiseen
	 * liittyv‰t tapahtumat.
	 */
	public final void triggerOnEndSequence()
	{
		
	}
}
