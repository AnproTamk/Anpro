package fi.tamk.anpro;

import android.os.Environment;
import android.util.Log;
import android.util.Xml;

import java.io.File;
import java.io.FileOutputStream;

import org.xmlpull.v1.XmlSerializer;

/**
 * Tallentaa tietoja XML-tiedostoihin, kuten asetukset ja pelitilan.
 */
public class XmlWriter
{
	/**
	 * Tallentaa pelitilan.
	 */
	public final boolean saveGame()
	{
		// Luodaan uusi XML-tiedosto pelin tallennukselle
		File xmlSaveGame = new File(Environment.getExternalStorageDirectory()+"/storymode.xml");
		
		// Kokeillaan, onko tiedosto jo olemassa. Jos ei ole, se luodaan.
		try {
			xmlSaveGame.createNewFile();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		// "Sidotaan" uusi tiedosto FileOutputStreamilla.
		FileOutputStream fileos = null;
		
		try {
			fileos = new FileOutputStream(xmlSaveGame);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		//Luodaan XmlSerializer, jotta voidaan kirjoittaa xml-dataa.
		XmlSerializer serializer = Xml.newSerializer();
		
		try {
			// Asetetaan FileOutputStream ulostuloksi serializerille, käyttäen UTF-8-koodausta.
			serializer.setOutput(fileos, "UTF-8");
			
			// Kirjoitetaan <?xml -selite enkoodauksella (jos enkoodaus ei ole "null") 
			//ja "standalone flag" (jos "standalone" ei ole "null").
			serializer.startDocument(null, Boolean.valueOf(false));
			
			// Asetetaan sisennykset.
			serializer.setFeature("http://xmlpull.org/v1/doc/features.html#indent-output", true);
			
			// Asetetaan tagi nimeltä "res"  <--- LUULTAVASTI MUOKATAAN!
			serializer.startTag(null, "res");
			
			/* 
			 * Tästä alkaa xml-tiedoston sisempi osuus.
			 * Tallennetaan storymoden pelitiedot.
			 * 
			 
			serializer.startTag(null, "particles");
			serializer.attribute(null, "value", stateId[0]);
			serializer.endTag(null, "particles");
			
			serializer.startTag(null, "music");
			serializer.attribute(null, "value", stateId[1]);
			serializer.endTag(null, "music");
			
			serializer.startTag(null, "sounds");
			serializer.attribute(null, "value", stateId[2]);
			serializer.endTag(null, "sounds");
			
			serializer.endTag(null, "res");
			serializer.endDocument();
			
			//Kirjoitetaan xml-data FileOutputStreamiin.
			serializer.flush();
			//Suljetaan tiedostovirta.
			fileos.close();
			*/
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return true; // TODO: Pitää palauttaa FALSE ja käsitellä virhe, mikäli tallennus jostain syystä epäonnistui.
	}
	
	/**
	 * Tallentaa globaalit asetukset.
	 * 
	 * @param boolean Partikkeliasetus
	 * @param boolean Musiikkiasetus
	 * @param boolean Ääniasetus
	 * 
	 * @deprecated
	 **/
	public final void saveSettings(boolean _particleState, boolean _musicState, boolean _soundState)
	{
		// Luodaan uusi XML-tiedosto asetuksille.
		File xmlStoreSettings = new File("HWUserData/Android/settings.xml");
		
		String[] stateId = {"0", "0", "0"};
		
		if (_particleState == true)
			stateId[0] = "1";
		if (_musicState == true)
			stateId[1] = "1";
		if (_soundState == true)
			stateId[2] = "1";

		try {
			xmlStoreSettings.createNewFile();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		// "Sidotaan" uusi tiedosto FileOutputStreamilla.
		FileOutputStream fileos = null;
		
		try {
			fileos = new FileOutputStream(xmlStoreSettings);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		//Luodaan XmlSerializer, jotta voidaan kirjoittaa xml-dataa.
		XmlSerializer serializer = Xml.newSerializer();
		
		try {
			// Asetetaan FileOutputStream ulostuloksi serializerille, käyttäen UTF-8-koodausta.
			serializer.setOutput(fileos, "UTF-8");
			// Kirjoitetaan <?xml -selite enkoodauksella (jos enkoodaus ei ole "null") 
			//ja "standalone flag" (jos "standalone" ei ole "null").
			serializer.startDocument(null, Boolean.valueOf(false));
			// Asetetaan sisennykset.
			serializer.setFeature("http://xmlpull.org/v1/doc/features.html#indent-output", true);
			// Asetetaan tagi nimeltä "res"  <--- LUULTAVASTI MUOKATAAN!
			serializer.startTag(null, "res");
			
			/* 
			 * Tästä alkaa xml-tiedoston sisempi osuus.
			 * 
			 * Kirjoitetaan settings.xml-tiedostoon elementit 'particles',
			 * 'music' ja 'sounds', jotka pitävät tiedon siitä, onko partikkelit 
			 * käytössä vai ei.
			 */
			
			
			 /* 'Particles'-elementin sisällä on attribuutti, jonka nimenä on 'value'.
			 * 'Value' tallentaa string-tiedon siitä, onko partikkelit käytössä vai ei.
			 */
			serializer.startTag(null, "particles");
			serializer.attribute(null, "value", stateId[0]);
			serializer.endTag(null, "particles");
			
			serializer.startTag(null, "music");
			serializer.attribute(null, "value", stateId[1]);
			serializer.endTag(null, "music");
			
			serializer.startTag(null, "sounds");
			serializer.attribute(null, "value", stateId[2]);
			serializer.endTag(null, "sounds");
			
			serializer.endTag(null, "res");
			serializer.endDocument();
						
			
			//Kirjoitetaan xml-data FileOutputStreamiin.
			serializer.flush();
			//Suljetaan tiedostovirta.
			fileos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Tallentaa achievementit.
	 * 
	 * @return boolean Onnistuiko tallennus?
	 */
	public final boolean saveAchievements()
	{
		// Luodaan uusi XML-tiedosto achievementeille.
		File xmlSaveAchievements = new File(Environment.getExternalStorageDirectory()+"/achievements.xml");
		
		try {
			xmlSaveAchievements.createNewFile();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return true;
	}
	
	/**
	 * Tallentaa pelaajan highscoret.
	 * 
	 * @param _score Pelaajan pisteet
	 * @param scoreList 
	 * @return 
	 */
	public final int[] saveHighScores(int _score, int[] _scoreList)
	{
		FileOutputStream fos = null;
		
		int[] scores = new int[5];
		
		for (int i = 0; i < scores.length; ++i) {
			if (_score > _scoreList[i]) {
				int scoreTemp 	  = _scoreList[i];
				_scoreList[i] 	  = _score;
				_scoreList[i + 1] = scoreTemp;
				break;
			}
		}
		
		scores = _scoreList;
		
		//String[] string;
		/*String[] stringTemp;
		
		stringTemp = string.split("(?<=\\G.{1})");*/
		
		File xmlHighScores = new File(Environment.getExternalStorageDirectory()+"/highscores.xml");
		
		try {
			xmlHighScores.createNewFile();
		} catch (Exception e) {
			e.printStackTrace();
			Log.e("TESTI", "Writer Error 1: " + e.getMessage());
		}
		
		try {
			fos = new FileOutputStream(xmlHighScores);
		} catch (Exception e) {
			e.printStackTrace();
			Log.e("TESTI", "Writer Error 2: " + e.getMessage());
		}
		
		XmlSerializer serializer = Xml.newSerializer();
		
		try {
			// Asetetaan FileOutputStream ulostuloksi serializerille, käyttäen UTF-8-koodausta.
			serializer.setOutput(fos, "UTF-8");
			// Kirjoitetaan <?xml -selite enkoodauksella (jos enkoodaus ei ole "null") 
			//ja "standalone flag" (jos "standalone" ei ole "null").
			serializer.startDocument(null, Boolean.valueOf(false));
			// Asetetaan sisennykset.
			serializer.setFeature("http://xmlpull.org/v1/doc/features.html#indent-output", true);
			// Asetetaan tagi nimeltä "res"  <--- LUULTAVASTI MUOKATAAN!
			serializer.startTag(null, "scores");
			
			
			for (int i = 0; i < scores.length; ++i) {
				serializer.startTag(null, "score");
				
				serializer.attribute(null, "id", String.valueOf(i));
				serializer.attribute(null, "value", String.valueOf(_scoreList[i]));
				
				serializer.endTag(null, "score");
			}
			
			serializer.endTag(null, "scores");
			serializer.endDocument();
						
			
			//Kirjoitetaan xml-data FileOutputStreamiin.
			serializer.flush();
			//Suljetaan tiedostovirta.
			fos.close();
		} catch (Exception e) {
			e.printStackTrace();
			Log.e("TESTI", "Writer Error 3: " + e.getMessage());
		}
		
		return scores;
	}
}
