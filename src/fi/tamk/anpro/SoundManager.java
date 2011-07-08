package fi.tamk.anpro;

import java.util.HashMap;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;

/**
 * Hallitsee pelin ‰‰nien lataamisen, niiden poistamisen ja ‰‰nentoiston.
 */
public class SoundManager
{
	/* Osoitin t‰h‰n luokkaan (singleton-toimintoa varten) */
	private static SoundManager instance;
	
	/* ƒ‰nimuuttujat */
	private static SoundPool                 soundPool;    // Objekti, jolla luodaan ja toistetaan ‰‰net
	private static HashMap<Integer, Integer> soundPoolMap; // Hashmappi, johon tallennetaan ‰‰net kun ne on ensin ladattu
	private static AudioManager              audioManager; // Osoitin palveluun, joka toistaa ‰‰nen, joka halutaan toistaa

	/* Ohjelman konteksti */
	private static Context context;
	
	/* ƒ‰nien vakiot */
	public static final int SOUND_EXPLOSION   = 1;
	public static final int SOUND_BUTTONCLICK = 2;
	public static final int SOUND_LASER       = 3;
	
	/**
	 * Alustaa luokan muuttujat.
	 */
	private SoundManager()
	{
		// ...
	}
	
	/**
	 * Palauttaa osoittimen t‰h‰n luokkaan.
	 * 
	 * @return SoundManager Osoitin t‰h‰n luokkaan
	 */
	synchronized public static SoundManager getInstance()
	{
		if (instance == null) {
			instance = new SoundManager();
		}
		return instance;
	}
	
	/**
	 * Valmistelee s‰ilytyspaikan ‰‰nille.
	 * 
	 * @param Context Ohjelman konteksti
	 */
	public static final void initSounds(Context _context)
	{
		context = _context;
		
		// Ensimm‰inen argumentti(4) m‰‰ritt‰‰, kuinka monta ‰‰nt‰ voidaan toistaa samaan aikaan
		soundPool    = new SoundPool(4, AudioManager.STREAM_MUSIC,0);
		soundPoolMap = new HashMap<Integer, Integer>();
		audioManager = (AudioManager)context.getSystemService(Context.AUDIO_SERVICE);
		
		// Ladataan ‰‰net
		loadSounds();
	}
	
	/**
	 * Lis‰‰ uuden ‰‰nen soundPooliin.
	 * 
	 * @param int Indeksi ‰‰nen hakemiselle
	 * @param int Androidin ID ‰‰nelle
	 */
	public static final void addSound(int _index, int _soundId)
	{
		soundPoolMap.put(_index, soundPool.load(context, _soundId, 1));
	}
	
	/**
	 * Lataa eri ‰‰net resursseista.
	 */
	public static final void loadSounds()
	{
		// 1-9 efektej‰
		// 10-19 musiikkeja
		// (‰‰nen indeksinumero, ‰‰nen nimi, prioriteetti)
		soundPoolMap.put(SOUND_EXPLOSION, soundPool.load(context, R.raw.sound1, 1));
		soundPoolMap.put(SOUND_BUTTONCLICK, soundPool.load(context, R.raw.sound2, 1));
		soundPoolMap.put(SOUND_LASER, soundPool.load(context, R.raw.default_weapon, 1));
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
	 * Toistaa musiikin.
	 * 
	 * @param int Soitettavan ‰‰nen indeksi
	 * @param int ƒ‰nen toistonopeus (ei k‰ytˆss‰ viel‰)
	 */
	/*public static final void playMusic(int _index, float _speed)
	{
		if (Options.music) {
			float streamVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
			streamVolume = streamVolume / audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
			soundPool.play((Integer) soundPoolMap.get(_index), streamVolume, streamVolume, 1, 0, _speed);
		}
	}*/
	
	/**
	 * Pys‰ytt‰‰ ‰‰nen.
	 * 
	 * @param int Pys‰ytett‰v‰n ‰‰nen indeksi
	 */
	public static final void stopSound(int _index)
	{
		soundPool.stop((Integer) soundPoolMap.get(_index));
	}
	
	/**
	 * Tyhjent‰‰ resurssit ja SoundManagerin olion.
	 */
	public static final void cleanUp()
	{
		soundPool.release();
		soundPool = null;
		soundPoolMap.clear();
		audioManager.unloadSoundEffects();
		instance = null;
	}
}