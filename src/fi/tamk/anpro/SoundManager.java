package fi.tamk.anpro;

import java.util.HashMap;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;

/*
 * SoundManager-class
 * 
 * K‰yttˆ:
 * playSound(‰‰nen indeksinumero(int), toistonopeus(float));
 */

public class SoundManager {

	static private SoundManager instance;
	private static SoundPool soundPool; // Objekti, jolla luodaan ja toistetaan ‰‰net
	private static HashMap soundPoolMap; // Hashmappi, johon tallennetaan ‰‰net kun ne on ensin ladattu
	private static AudioManager audioManager; // Kahva palveluun, joka toistaa ‰‰nen, joka halutaan toistaa
	private static Context context; // Kahva ohjelman kontekstiin
	
	// SoundManagerin rakentaja
	private SoundManager() {
		// ...
	}
	
	/*
	 * Pyyd‰ Sound Managerista instanssi.
	 * Luo se, jos sit‰ ei ole jo valmiiksi olemassa.
	 * 
	 * @return Palauttaa yhden SoundManagerin
	 */
	static synchronized public SoundManager getInstance() {
		if (instance == null) {
			instance = new SoundManager();
		}
		return instance;
	}
	
	/*
	 * Valmistellaan s‰ilytyspaikka ‰‰nille
	 * 
	 * @param theContext - Ohjelman contexti
	 */
	public static void initSounds(Context theContext) {
		context = theContext;
		// Ensimm‰inen argumentti(4) m‰‰ritt‰‰, kuinka monta ‰‰nt‰ voidaan toistaa samaan aikaan
		soundPool = new SoundPool(4,AudioManager.STREAM_MUSIC,0);
		soundPoolMap = new HashMap();
		audioManager = (AudioManager)context.getSystemService(context.AUDIO_SERVICE);
	}
	
	/*
	 * Lis‰t‰‰n uusi ‰‰ni soundPooliin
	 * 
	 * @param index - Indeksi ‰‰nen hakemiselle
	 * @param soundId - Androidin ID ‰‰nelle
	 */
	public static void addSound(int index, int soundId) {
		soundPoolMap.put(index, soundPool.load(context, soundId, 1));
	}
	
	/*
	 * Lataa eri ‰‰net varastosta
	 * T‰ll‰ hetkell‰ 'kovakoodattu', mutta t‰m‰n voi muuttaa joustavaksi
	 */
	public static void loadSounds() {
		// (‰‰nen indeksinumero, ‰‰nen nimi, prioriteetti)
		soundPoolMap.put(1, soundPool.load(context, R.raw.sound1, 1));
		soundPoolMap.put(2, soundPool.load(context, R.raw.sound2, 1));
	}
	
	/*
	 * Toistaa ‰‰nen
	 * 
	 * @param index - Indeksi ‰‰nelle, joka toistetaan
	 * @param speed - ƒ‰nen toistonopeus (ei k‰ytˆss‰ viel‰)
	 */
	public static void playSound(int index, float speed) {
		float streamVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
		streamVolume = streamVolume / audioManager.getStreamMaxVolume(audioManager.STREAM_MUSIC);
		soundPool.play((Integer) soundPoolMap.get(index), streamVolume, streamVolume, 1, 0, speed);
	}
	
	/*
	 * Pys‰ytt‰‰ ‰‰nen
	 * 
	 * @param index - Indeksi ‰‰nelle, joka pys‰ytet‰‰n
	 */
	public static void stopSound(int index) {
		soundPool.stop((Integer) soundPoolMap.get(index));
	}
	
	/*
	 * Tyhjent‰‰ resurssit ja SoundManagerin instanssin
	 * KƒYTƒ TƒTƒ, KUN OHJELMA TUHOTAAN!
	 */
	public static void cleanup() {
		soundPool.release();
		soundPool = null;
		soundPoolMap.clear();
		audioManager.unloadSoundEffects();
		instance = null;
	}
}