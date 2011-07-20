package fi.tamk.anpro;

/**
 *  Hallitsee kaikkia efektejä.
 */
public class EffectManager 
{
	/* Osoitin tähän luokkaan (singleton-toimintoa varten) */
	private static EffectManager instance;
	
	/* Efektiryhmät */
	public static final byte TYPE_FRONT_EFFECT = 0;
	public static final byte TYPE_BACK_EFFECT  = 1;
	
	/* Efektityypit */
	public static final byte EFFECT_BALLOON_EXCLAMATION = 0;
	public static final byte EFFECT_BALLOON_QUESTION    = 1;
	public static final byte EFFECT_PLAYER_ARMOR        = 2;
	public static final byte EFFECT_ENEMY_ARMOR         = 2;
	public static final byte EFFECT_COMBOMULTIPLIER_2   = 3;
	public static final byte EFFECT_COMBOMULTIPLIER_3   = 4;
	public static final byte EFFECT_COMBOMULTIPLIER_4   = 5;
	public static final byte EFFECT_COMBOMULTIPLIER_5   = 6;
	public static final byte EFFECT_EXPLOSION           = 7;
	public static final byte EFFECT_PLAYER_TRAIL        = 8;
	public static final byte EFFECT_ENEMY_TRAIL         = 11;
	public static final byte EFFECT_HUD_ARMOR			= 9;
	public static final byte EFFECT_HUD_HEALTH			= 10;
	
	/* Efektiobjektit */
	private static EffectObject   balloonExclamationEffect;
	private static EffectObject   balloonQuestionEffect;
	private static EffectObject   playerArmorEffect;
	private static EffectObject[] enemyArmorEffect;
	private static EffectObject   combo2MultiplierEffect;
	private static EffectObject   combo3MultiplierEffect;
	private static EffectObject   combo4MultiplierEffect;
	private static EffectObject   combo5MultiplierEffect;
	private static EffectObject   explosionEffect;
	private static EffectObject[] playerTrailEffect;
	private static EffectObject[] enemyTrailEffect;
	private static EffectObject	  hudArmorEffect;
	private static EffectObject   hudHealthEffect;

	/**
	 * Alustaa luokan muuttujat.
	 */
	private EffectManager()
	{
		playerTrailEffect        = new EffectObject[8];
		enemyTrailEffect         = new EffectObject[30];
		balloonExclamationEffect = new EffectObject(0, EFFECT_BALLOON_EXCLAMATION, TYPE_FRONT_EFFECT);
		balloonQuestionEffect    = new EffectObject(0, EFFECT_BALLOON_QUESTION, TYPE_FRONT_EFFECT);
		playerArmorEffect        = new EffectObject(0, EFFECT_PLAYER_ARMOR, TYPE_BACK_EFFECT);
		enemyArmorEffect         = new EffectObject[5];
		combo2MultiplierEffect   = new EffectObject(0, EFFECT_COMBOMULTIPLIER_2, TYPE_FRONT_EFFECT);
		combo3MultiplierEffect   = new EffectObject(0, EFFECT_COMBOMULTIPLIER_3, TYPE_FRONT_EFFECT);
		combo4MultiplierEffect   = new EffectObject(0, EFFECT_COMBOMULTIPLIER_4, TYPE_FRONT_EFFECT);
		combo5MultiplierEffect   = new EffectObject(0, EFFECT_COMBOMULTIPLIER_5, TYPE_FRONT_EFFECT);
		hudArmorEffect			 = new EffectObject(0, EFFECT_HUD_ARMOR, TYPE_FRONT_EFFECT);
		hudHealthEffect			 = new EffectObject(0, EFFECT_HUD_HEALTH, TYPE_FRONT_EFFECT);
		explosionEffect 		 = new EffectObject(0, EFFECT_EXPLOSION, TYPE_FRONT_EFFECT);
		
		for (int i = 0; i < 5; ++i) {
			enemyArmorEffect[i] = new EffectObject(0, EFFECT_ENEMY_ARMOR, TYPE_BACK_EFFECT);
		}
		for (int i = 0; i < 8; ++i) {
			playerTrailEffect[i] = new EffectObject(0, EFFECT_PLAYER_TRAIL, TYPE_BACK_EFFECT);
		}
		for (int i = 0; i < 30; ++i) {
			enemyTrailEffect[i] = new EffectObject(0, EFFECT_ENEMY_TRAIL, TYPE_BACK_EFFECT);
		}
	}
	
	/**
	 * Palauttaa osoittimen tähän luokkaan.
	 * 
	 * @return EffectManager Osoitin tähän luokkaan
	 */
	synchronized public static void getInstance()
	{
		if (instance == null) {
			instance = new EffectManager();
		}
	}
	
	synchronized public static void destroy()
	{
		instance = null;
	}

	/**
	 * Näyttää pelaajan ympärillä suojakentän toisen objektin osuessa siihen.
	 * 
	 * @param _object Pelaaja-objekti
	 */
	public static void showPlayerArmorEffect(GameObject _object) 
	{
		playerArmorEffect.activate(_object);
	}

	/**
	 * Näyttää vihollisen ympärillä suojakentän toisen objektin osuessa siihen.
	 * 
	 * @param _object Vihollis-objekti
	 */
	public static void showEnemyArmorEffect(GameObject _object) 
	{
		for (int i = 0; i < 5; ++i) {
			if (!enemyArmorEffect[i].activated) {
				enemyArmorEffect[i].activate(_object);
				break;
			}
		}
	}

	/**
	 * Näyttää objektin vieressä huutomerkin. 
	 * 
	 * @param _object Kohde-objekti
	 */
	public static void showExclamationMarkBalloon(GameObject _object)
	{
		if (!balloonExclamationEffect.activated && Utility.getRandom(1, 20) == 1) {	
			balloonExclamationEffect.activate(_object);
		}
	}

	/**
	 * Näyttää objektin vieressä kysymysmerkin.
	 * 
	 * @param _object Kohde-objekti
	 */
	public static void showQuestionMarkBalloon(GameObject _object)
	{
		if (!balloonQuestionEffect.activated && Utility.getRandom(1, 20) == 1) {
			balloonQuestionEffect.activate(_object);			
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
			combo2MultiplierEffect.activate(_x, _y);
		}
		else if (_multiplier == 3) {
			combo3MultiplierEffect.activate(_x, _y);
		}
		else if (_multiplier == 4) {
			combo4MultiplierEffect.activate(_x, _y);
		}
		else if (_multiplier == 5) {
			combo5MultiplierEffect.activate(_x, _y);
		}
	}
	
	/**
	 * Näyttää aluksen perässä "jälkipolton".
	 * 
	 * @param _object Kohde-objekti
	 */
	public static void showTrailEffect(GameObject _object)
	{
		if (_object instanceof Player) {
			for (int i = 0; i < 8; ++i) {
				if (!playerTrailEffect[i].activated) {
					playerTrailEffect[i].activate(_object.x, _object.y);
					playerTrailEffect[i].direction = _object.direction;
					break;
				}
			}
		}
		else if (_object instanceof Enemy) {
			for (int i = 0; i < 30; ++i) {
				if (!enemyTrailEffect[i].activated) {
					enemyTrailEffect[i].activate(_object.x, _object.y);
					enemyTrailEffect[i].direction = _object.direction;
					break;
				}
			}
		}
	}
	
	/**
	 * Näyttää pelaajaan osuessa Armor-kilven välähdyksen
	 * 
	 * @param _object Kohde-objekti
	 */
	public static void showArmorHitEffect(GuiObject _object)
	{
		if (!hudArmorEffect.activated) {
			hudArmorEffect.activate(_object);
		}
	}
	
	/**
	 * Näyttää pelaajaan osuessa Health-sydämen välähdyksen
	 * 
	 * @param _object Kohde-objekti
	 */
	public static void showHealthHitEffect(GuiObject _object)
	{
		if (!hudHealthEffect.activated) {
			hudHealthEffect.activate(_object);
		}
	}
	
	public static void showExplosionEffect(float _x, float _y) 
	{
		if(!explosionEffect.activated) {
			explosionEffect.activate(_x, _y);
		}
	}
}
