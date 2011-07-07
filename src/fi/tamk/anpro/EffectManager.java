package fi.tamk.anpro;

import android.util.Log;

/**
 *  Hallitsee kaikkia efektej‰.
 */
public class EffectManager 
{
	/* Osoitin t‰h‰n luokkaan (singleton-toimintoa varten) */
	private static EffectManager instance;
	
	/* Efektien vakiot */
	public static final byte EFFECT_BALLOON_EXCLAMATION = 0;
	public static final byte EFFECT_BALLOON_QUESTION    = 1;
	public static final byte EFFECT_ARMOR               = 2;
	public static final byte EFFECT_EXPLOSION           = 3;

	/* Efektiobjektit */
	private static EffectObject armorEffect;
	private static EffectObject balloonExclamation;
	private static EffectObject balloonQuestion;

	
	/**
	 * Alustaa luokan muuttujat.
	 */
	private EffectManager()
	{
		balloonExclamation = new EffectObject(0, EFFECT_BALLOON_EXCLAMATION);
		balloonQuestion = new EffectObject(0, EFFECT_BALLOON_QUESTION);
		armorEffect        = new EffectObject(0, EFFECT_ARMOR);
	}
	
	/**
	 * Palauttaa osoittimen t‰h‰n luokkaan.
	 * 
	 * @return EffectManager Osoitin t‰h‰n luokkaan
	 */
	static synchronized public EffectManager getInstance()
	{
		if (instance == null) {
			instance = new EffectManager();
		}
		return instance;
	}
	
	/**
	 * N‰ytt‰‰ pelaajan ymp‰rill‰ suojakent‰n vihollisen tai ammuksen osuessa siihen.
	 * 
	 * @param _x Efektin X-koordinaatti
	 * @param _y Efektin Y-koordinaatti
	 */
	public static void showArmorEffect(float _x, float _y) 
	{
		if (!armorEffect.activated) {
			armorEffect.activate(_x, _y);
		}
	}

	/**
	 * N‰ytt‰‰ objektin vieress‰ huutomerkin. 
	 * 
	 * @param _x Kohteen X-koordinaatti
	 * @param _y Kohteen Y-koordinaatti
	 */
	public static void showExclamationMarkBalloon(float _x, float _y)
	{
		
		
		if (!balloonExclamation.activated) {	
			balloonExclamation.activate(_x, _y);
		}
	}

	/**
	 * N‰ytt‰‰ objektin vieress‰ kysymysmerkin.
	 * 
	 * @param _x Kohteen X-koordinaatti
	 * @param _y Kohteen Y-koordinaatti
	 */
	public static void showQuestionMarkBalloon(float _x, float _y)
	{
		int a = Utility.getRandom(1, 100);
		Log.e("test", String.valueOf(a));
		
		if (!balloonQuestion.activated && a <= 50) {
			balloonQuestion.activate(_x, _y);			
		}
	}
}
