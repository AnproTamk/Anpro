package fi.tamk.anpro;

/**
 *  Hallitsee kaikkia efektej�.
 */
public class EffectManager 
{
	/* Osoitin t�h�n luokkaan (singleton-toimintoa varten) */
	private static EffectManager instance;
	
	/* Efektien vakiot */
	public static final byte EFFECT_BALLOON_EXCLAMATION = 0;
	public static final byte EFFECT_BALLOON_QUESTION    = 1;
	public static final byte EFFECT_PLAYER_ARMOR        = 2;
	public static final byte EFFECT_ENEMY_ARMOR         = 3;
	public static final byte EFFECT_EXPLOSION           = 4;
	
	/* Efektiobjektit */
	private static EffectObject   playerArmorEffect;
	private static EffectObject[] enemyArmorEffect;
	private static EffectObject   balloonExclamation;
	private static EffectObject   balloonQuestion;

	/**
	 * Alustaa luokan muuttujat.
	 */
	private EffectManager()
	{
		balloonExclamation = new EffectObject(0, EFFECT_BALLOON_EXCLAMATION);
		balloonQuestion    = new EffectObject(0, EFFECT_BALLOON_QUESTION);
		playerArmorEffect  = new EffectObject(0, EFFECT_PLAYER_ARMOR);
		enemyArmorEffect   = new EffectObject[5];
		
		for (int i = 0; i < 5; ++i) {
			enemyArmorEffect[i] = new EffectObject(0, EFFECT_ENEMY_ARMOR);
		}
	}
	
	/**
	 * Palauttaa osoittimen t�h�n luokkaan.
	 * 
	 * @return EffectManager Osoitin t�h�n luokkaan
	 */
	static synchronized public EffectManager getInstance()
	{
		if (instance == null) {
			instance = new EffectManager();
		}
		return instance;
	}

	/**
	 * N�ytt�� pelaajan ymp�rill� suojakent�n toisen objektin osuessa siihen.
	 * 
	 * @param _x Efektin X-koordinaatti
	 * @param _y Efektin Y-koordinaatti
	 */
	public static void showPlayerArmorEffect(float _x, float _y) 
	{
		playerArmorEffect.activate(_x, _y);
	}

	/**
	 * N�ytt�� vihollisen ymp�rill� suojakent�n toisen objektin osuessa siihen.
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
	 * N�ytt�� objektin vieress� huutomerkin. 
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
	 * N�ytt�� objektin vieress� kysymysmerkin.
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
}
