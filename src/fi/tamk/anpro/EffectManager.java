package fi.tamk.anpro;

/**
 *  Hallitsee kaikkia efektejä.
 */
public class EffectManager 
{
	/* Osoitin tähän luokkaan (singleton-toimintoa varten) */
	private static EffectManager instance;
	
	/* Efektien vakiot */
	public static final byte EFFECT_BALLOON_EXCLAMATION = 0;
	public static final byte EFFECT_BALLOON_QUESTION    = 1;
	public static final byte EFFECT_PLAYER_ARMOR        = 2;
	public static final byte EFFECT_ENEMY_ARMOR         = 3;
	public static final byte EFFECT_COMBOMULTIPLIER_2   = 4;
	public static final byte EFFECT_COMBOMULTIPLIER_3   = 5;
	public static final byte EFFECT_COMBOMULTIPLIER_4   = 6;
	public static final byte EFFECT_COMBOMULTIPLIER_5   = 7;
	public static final byte EFFECT_EXPLOSION           = 8;
	
	/* Efektiobjektit */
	private static EffectObject   playerArmorEffect;
	private static EffectObject[] enemyArmorEffect;
	private static EffectObject   balloonExclamation;
	private static EffectObject   balloonQuestion;
	private static EffectObject   combo2Multiplier;
	private static EffectObject   combo3Multiplier;
	private static EffectObject   combo4Multiplier;
	private static EffectObject   combo5Multiplier;

	/**
	 * Alustaa luokan muuttujat.
	 */
	private EffectManager()
	{
		balloonExclamation  = new EffectObject(0, EFFECT_BALLOON_EXCLAMATION);
		balloonQuestion     = new EffectObject(0, EFFECT_BALLOON_QUESTION);
		playerArmorEffect   = new EffectObject(0, EFFECT_PLAYER_ARMOR);
		enemyArmorEffect    = new EffectObject[5];
		combo2Multiplier    = new EffectObject(0, EFFECT_COMBOMULTIPLIER_2);
		combo3Multiplier    = new EffectObject(0, EFFECT_COMBOMULTIPLIER_3);
		combo4Multiplier    = new EffectObject(0, EFFECT_COMBOMULTIPLIER_4);
		combo5Multiplier    = new EffectObject(0, EFFECT_COMBOMULTIPLIER_5);
		
		for (int i = 0; i < 5; ++i) {
			enemyArmorEffect[i] = new EffectObject(0, EFFECT_ENEMY_ARMOR);
		}
	}
	
	/**
	 * Palauttaa osoittimen tähän luokkaan.
	 * 
	 * @return EffectManager Osoitin tähän luokkaan
	 */
	synchronized public static EffectManager getInstance()
	{
		if (instance == null) {
			instance = new EffectManager();
		}
		return instance;
	}

	/**
	 * Näyttää pelaajan ympärillä suojakentän toisen objektin osuessa siihen.
	 * 
	 * @param _x Efektin X-koordinaatti
	 * @param _y Efektin Y-koordinaatti
	 */
	public static void showPlayerArmorEffect(float _x, float _y) 
	{
		playerArmorEffect.activate(_x, _y);
	}

	/**
	 * Näyttää vihollisen ympärillä suojakentän toisen objektin osuessa siihen.
	 * 
	 * @param _x Efektin X-koordinaatti
	 * @param _y Efektin Y-koordinaatti
	 */
	public static void showEnemyArmorEffect(float _x, float _y) 
	{
		for (int i = 0; i < 5; ++i) {
			if (!enemyArmorEffect[i].activated) {
				enemyArmorEffect[i].activate(_x, _y);
				break;
			}
		}
	}

	/**
	 * Näyttää objektin vieressä huutomerkin. 
	 * 
	 * @param _x Kohteen X-koordinaatti
	 * @param _y Kohteen Y-koordinaatti
	 */
	public static void showExclamationMarkBalloon(float _x, float _y)
	{
		if (!balloonExclamation.activated && Utility.getRandom(1, 20) == 1) {	
			balloonExclamation.activate(_x, _y);
		}
	}

	/**
	 * Näyttää objektin vieressä kysymysmerkin.
	 * 
	 * @param _x Kohteen X-koordinaatti
	 * @param _y Kohteen Y-koordinaatti
	 */
	public static void showQuestionMarkBalloon(float _x, float _y)
	{
		if (!balloonQuestion.activated && Utility.getRandom(1, 20) == 1) {
			balloonQuestion.activate(_x, _y);			
		}
	}
	
	/**
	 * Näyttää tuhoutunen objektin vieressä combokertoimen.
	 * 
	 * @param _multiplier Kombinaatiokerroin
	 * @param _x Objektin X-koordinaatti
	 * @param _y Objektin Y-koordinaatti
	 */
	public static void showComboMultiplier(int _multiplier, float _x, float _y)
	{
		if(_multiplier == 2) {
			combo2Multiplier.activate(_x, _y);
		}
		else if (_multiplier == 3) {
			combo3Multiplier.activate(_x, _y);
		}
		else if (_multiplier == 4) {
			combo4Multiplier.activate(_x, _y);
		}
		else if (_multiplier == 5) {
			combo5Multiplier.activate(_x, _y);
		}
	}
}
