package fi.tamk.anpro;

import android.content.Context;
import android.os.Environment;
import android.util.Log;
import android.util.Xml;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

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
	 */
	public final void saveHighScores(int _score)
	{
		FileOutputStream fos = null;
		FileInputStream  fis = null;
		
		int[] scores = new int[5];
		Context context = MainActivity.context;
		String file = "high_scores.txt";
		String string = String.valueOf(_score);
		String[] stringTemp;
		
		stringTemp = string.split("(?<=\\G.{1})");
		
		byte[] buffer = new byte[stringTemp.length];
		
		for (int i = 0; i < stringTemp.length; ++i) {
			buffer[i] = (byte) Integer.parseInt(stringTemp[i]);
		}
		
		try {
			fis = context.openFileInput(file);

			for (int i = scores.length - 1; i >= 0; --i) {
				scores[i] = fis.read();
			}
			
			fis.close();
		} 
		catch (FileNotFoundException e) {
			e.printStackTrace();
			Log.e("TESTI", "ERROR");
		}
		catch (IOException e) {
			e.printStackTrace();
			Log.e("TESTI", "ERROR");
		}
		
		
		try {
			fos = context.openFileOutput(file, 0);
		} 
		catch (FileNotFoundException e) {
			e.printStackTrace();
			Log.e("TESTI", "ERROR");
		}
		
		try {
			if (_score > 0) {
				for(int i = 0; i < scores.length - 1; ++i) {
					if (_score > scores[i]) {
						int scoreTemp = scores[i];
						scores[i] = _score;
						scores[i + 1] = scoreTemp;
						fos.write(buffer);
						break;
					}
				}
			}
			else {
				for(int i = 0; i < scores.length - 1; ++i) {
					fos.write(0);
				}
			}
			
			//fos.write(string.getBytes());
			fos.close();
		}
		catch (IOException e) {
			e.printStackTrace();
			Log.e("TESTI", "ERROR");
		}
	}
}
