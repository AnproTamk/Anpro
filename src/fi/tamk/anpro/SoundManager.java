package fi.tamk.anpro;

import java.util.HashMap;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;

/*
 * SoundManager-class
 * 
 * K‰yttˆ:
 * 
 *	//Luo, alusta ja lataa SoundManager <- tee t‰m‰ MainActivityssa
 *  //SoundManager.getInstance();
 *  //SoundManager.initSounds(this);
 *  //SoundManager.loadSounds();
 *	//SoundManager.playSound(2, 1);
 * 
 * playSound(‰‰nen indeksinumero(int), toistonopeus(float));
 * 
 * Lopussa muista kutsua cleanup()-funktiota
 */
public class SoundManager
{
	private static SoundManager              instance;
	private static SoundPool                 soundPool;    // Objekti, jolla luodaan ja toistetaan ‰‰net
	private static HashMap<Integer, Integer> soundPoolMap; // Hashmappi, johon tallennetaan ‰‰net kun ne on ensin ladattu
	private static AudioManager              audioManager; // Osoitin palveluun, joka toistaa ‰‰nen, joka halutaan toistaa
	private static Context                   context;      // Osoitin ohjelman kontekstiin
	
	// SoundManagerin rakentaja
	private SoundManager()
	{
		// ...
	}
	
	/*
	 * Pyyd‰ Sound Managerista instanssi.
	 * Luo se, jos sit‰ ei ole jo valmiiksi olemassa.
	 * 
	 * @return Palauttaa yhden SoundManagerin
	 */
	static synchronized public SoundManager getInstance()
	{
		if (instance == null) {
			instance = new SoundManager();
		}
		return instance;
	}
	
	/*
	 * Valmistellaan s‰ilytyspaikka ‰‰nille
	 * 
	 * @param theContext - Ohjelman konteksti
	 */
	public static final void initSounds(Context _context)
	{
		context = _context;
		
		// Ensimm‰inen argumentti(4) m‰‰ritt‰‰, kuinka monta ‰‰nt‰ voidaan toistaa samaan aikaan
		soundPool = new SoundPool(4,AudioManager.STREAM_MUSIC,0);
		soundPoolMap = new HashMap<Integer, Integer>();
		audioManager = (AudioManager)context.getSystemService(Context.AUDIO_SERVICE);
		
		// Ladataan ‰‰net.
		loadSounds();
	}
	
	/*
	 * Lis‰t‰‰n uusi ‰‰ni soundPooliin
	 * 
	 * @param index - Indeksi ‰‰nen hakemiselle
	 * @param soundId - Androidin ID ‰‰nelle
	 */
	public static final void addSound(int _index, int _soundId)
	{
		soundPoolMap.put(_index, soundPool.load(context, _soundId, 1));
	}
	
	/*
	 * Lataa eri ‰‰net varastosta
	 * T‰ll‰ hetkell‰ 'kovakoodattu', mutta t‰m‰n voi muuttaa joustavaksi
	 */
	public static final void loadSounds()
	{
		// 1-9 efektej‰
		// 10-19 musiikkeja
		// (‰‰nen indeksinumero, ‰‰nen nimi, prioriteetti)
		soundPoolMap.put(1, soundPool.load(context, R.raw.sound1, 1));
		soundPoolMap.put(2, soundPool.load(context, R.raw.sound2, 1));
		soundPoolMap.put(3, soundPool.load(context, R.raw.default_weapon, 1));
	}
	
	/*
	 * Toistaa ‰‰nen
	 * 
	 * @param index - Indeksi ‰‰nelle, joka toistetaan
	 * @param speed - ƒ‰nen toistonopeus (ei k‰ytˆss‰ viel‰)
	 */
	public static final void playSound(int _index, float _speed)
	{
		if (Options.sounds) {
			float streamVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
			streamVolume = streamVolume / audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
			soundPool.play((Integer) soundPoolMap.get(_index), streamVolume, streamVolume, 1, 0, _speed);
		}
	}
	
	/*
	 * Toistaa musiikin
	 * 
	 * @param index - Indeksi musiikille, joka toistetaan
	 * @param speed - Musiikin toistonopeus (ei k‰ytˆss‰ viel‰)
	 */
	public static final void playMusic(int _index, float _speed)
	{
		if (Options.music) {
			float streamVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
			streamVolume = streamVolume / audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
			soundPool.play((Integer) soundPoolMap.get(_index), streamVolume, streamVolume, 1, 0, _speed);
		}
	}
	
	/*
	 * Pys‰ytt‰‰ ‰‰nen
	 * 
	 * @param index - Indeksi ‰‰nelle, joka pys‰ytet‰‰n
	 */
	public static final void stopSound(int _index)
	{
		soundPool.stop((Integer) soundPoolMap.get(_index));
	}
	
	/*
	 * Tyhjent‰‰ resurssit ja SoundManagerin instanssin
	 * KƒYTƒ TƒTƒ, KUN OHJELMA TUHOTAAN!
	 */
	public static final void cleanup()
	{
		soundPool.release();
		soundPool = null;
		soundPoolMap.clear();
		audioManager.unloadSoundEffects();
		instance = null;
	}
}