package fi.tamk.anpro;

import java.util.HashMap;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;

/**
 * Hallitsee pelin ‰‰nien lataamisen, niiden poistamisen ja ‰‰nentoiston.
 */
public class SoundManager
{
	/**
     * Musiikin toistamiseen k‰ytett‰v‰ soitin.
     */
    private static MediaPlayer mp;
	
	/* Osoitin t‰h‰n luokkaan (singleton-toimintoa varten) */
	private static SoundManager instance;
	
	/* ƒ‰nien vakiot */
	public static final int SOUND_EXPLOSION_1    		= 0;
	public static final int SOUND_EXPLOSION_2    		= 1;
	public static final int SOUND_BUTTONCLICK    		= 2;
	public static final int SOUND_WEAPON_LASER	        = 3;
	public static final int SOUND_HIT_HEALTH	 		= 4;
	public static final int SOUND_HIT_ARMOR     		= 5;
	public static final int SOUND_PICKUP_WEAPON  		= 6;
	public static final int SOUND_PICKUP_SCORE   		= 7;
	public static final int SOUND_WEAPON_SPINNING_LASER = 8;
	public static final int SOUND_DEATH 				= 9;
	public static final int SOUND_WEAPON_EMP			= 10;
	public static final int SOUND_WEAPON_SPITFIRE		= 11;
	
	/* ƒ‰nimuuttujat */
	private static SoundPool                 soundPool;    // Objekti, jolla luodaan ja toistetaan ‰‰net
	private static HashMap<Integer, Integer> soundPoolMap; // Hashmappi, johon tallennetaan ‰‰net kun ne on ensin ladattu
	private static AudioManager              audioManager; // Osoitin palveluun, joka toistaa ‰‰nen, joka halutaan toistaa

	/* Ohjelman konteksti */
	private static Context context;
	
	/**
     * Kertoo, onko jokin kappale jo soimassa.
     */
    public static boolean isPlaying;
	
	/**
	 * Alustaa luokan muuttujat.
	 */
	private SoundManager()
	{
		isPlaying = false;
	}
	
	/**
	 * Palauttaa osoittimen t‰h‰n luokkaan.
	 */
	synchronized public static void getInstance()
	{
		if (instance == null) {
			instance = new SoundManager();
		}
	}
	
	synchronized public static void destroy()
	{
		instance = null;
	}

	/* =======================================================
	 * Perityt funktiot
	 * ======================================================= */
	/**
	 * Valmistelee s‰ilytyspaikan ‰‰nille.
	 * 
	 * @param Context Ohjelman konteksti
	 */
	public static final void initSounds(Context _context)
	{
		if (soundPool == null) {
			context = _context;
		
			// Ensimm‰inen argumentti(4) m‰‰ritt‰‰, kuinka monta ‰‰nt‰ voidaan toistaa samaan aikaan
			soundPool    = new SoundPool(4, AudioManager.STREAM_MUSIC,0);
			soundPoolMap = new HashMap<Integer, Integer>();
			audioManager = (AudioManager)context.getSystemService(Context.AUDIO_SERVICE);
			
			// Ladataan ‰‰net
			loadSounds();
		}

	}
	
	/**
	 * Lataa eri ‰‰net resursseista.
	 */
	public static final void loadSounds()
	{
		// (‰‰nen indeksinumero, ‰‰nen nimi, prioriteetti)
		soundPoolMap.put(SOUND_EXPLOSION_1, soundPool.load(context, R.raw.explosion1, 1));
		soundPoolMap.put(SOUND_EXPLOSION_2, soundPool.load(context, R.raw.explosion2, 1));
		soundPoolMap.put(SOUND_BUTTONCLICK, soundPool.load(context, R.raw.buttonclick, 1));
		soundPoolMap.put(SOUND_WEAPON_LASER, soundPool.load(context, R.raw.weapon_laser, 1));
		soundPoolMap.put(SOUND_HIT_HEALTH, soundPool.load(context, R.raw.hit_health, 1));
		soundPoolMap.put(SOUND_HIT_ARMOR, soundPool.load(context, R.raw.hit_armor, 1));
		soundPoolMap.put(SOUND_PICKUP_WEAPON, soundPool.load(context, R.raw.pickup_weapon, 1));
		soundPoolMap.put(SOUND_PICKUP_SCORE, soundPool.load(context, R.raw.pickup_score, 1));
		soundPoolMap.put(SOUND_WEAPON_SPINNING_LASER, soundPool.load(context, R.raw.weapon_spinning_laser, 1));
		soundPoolMap.put(SOUND_DEATH, soundPool.load(context, R.raw.death, 1));
		soundPoolMap.put(SOUND_WEAPON_EMP, soundPool.load(context, R.raw.weapon_emp, 1));
		soundPoolMap.put(SOUND_WEAPON_SPITFIRE, soundPool.load(context, R.raw.weapon_spitfire, 1));
	}
	
	/**
	 * Toistaa ‰‰nen.
	 * 
	 * @param int Soitettavan ‰‰nen indeksi
	 * @param int ƒ‰nen toistonopeus (ei k‰ytˆss‰ viel‰)
	 */
	public static final void playSound(int _index, float _speed)
	{
		if (Options.sounds == true) {
			float streamVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
			streamVolume = streamVolume / audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
			soundPool.play((Integer) soundPoolMap.get(_index), streamVolume, streamVolume, 1, 0, _speed);
		}
	}
		
	/**
	 * Pys‰ytt‰‰ ‰‰nen.
	 * 
	 * @param int Pys‰ytett‰v‰n ‰‰nen indeksi
	 */
	public static final void stopSound(int _index)
	{
		soundPool.stop((Integer) soundPoolMap.get(_index));
	}
    
	/* ================================================================
     *     Uudet metodit
     * ================================================================*/
    /**
     * Lataa halutun kappaleen muistiin. Kappaleita on muistissa vain yksi kerrallaan,
     * jotta varatun muistin m‰‰r‰ pysyisi mahdollisimman alhaalla.
     * 
     * @param _id Kappaleen resurssitunnus
     */
    public static final void loadMusic(int _id)
    {
        mp = MediaPlayer.create(context, _id);
    }
    
    /**
     * K‰ynnist‰‰ ladatun musiikin soittamisen. Toistokertojen m‰‰r‰ m‰‰ritet‰‰n parametrilla.
     * 
     * @param _looping Toistetaanko kappaletta loputtomiin?
     */
    public static final void playMusic(boolean _looping)
    {
        isPlaying = true;
        
        mp.setLooping(_looping);
        mp.start();
    }
    
    /**
     * Pys‰ytt‰‰ musiikin soittamisen.
     */
    public static final void stopMusic()
    {
        isPlaying = false;
        
        mp.setLooping(false);
        mp.stop();
    }
	
    /**
     * Pys‰ytt‰‰ musiikin ja ‰‰nien soittamisen ja tyhjent‰‰ ladatun kappaleen muistista.
     */
	public static final void cleanUp()
	{
		isPlaying = false;
		stopMusic();
		mp.release();
		soundPool.release();
		soundPool = null;
		soundPoolMap.clear();
		audioManager.unloadSoundEffects();
		instance = null;
	}
}