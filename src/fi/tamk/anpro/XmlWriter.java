package fi.tamk.anpro;

import android.os.Environment;
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
			// Asetetaan FileOutputStream ulostuloksi serializerille, k‰ytt‰en UTF-8-koodausta.
			serializer.setOutput(fileos, "UTF-8");
			
			// Kirjoitetaan <?xml -selite enkoodauksella (jos enkoodaus ei ole "null") 
			//ja "standalone flag" (jos "standalone" ei ole "null").
			serializer.startDocument(null, Boolean.valueOf(false));
			
			// Asetetaan sisennykset.
			serializer.setFeature("http://xmlpull.org/v1/doc/features.html#indent-output", true);
			
			// Asetetaan tagi nimelt‰ "res"  <--- LUULTAVASTI MUOKATAAN!
			serializer.startTag(null, "res");
			
			/* 
			 * T‰st‰ alkaa xml-tiedoston sisempi osuus.
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
		
		return true; // TODO: Pit‰‰ palauttaa FALSE ja k‰sitell‰ virhe, mik‰li tallennus jostain syyst‰ ep‰onnistui.
	}
	
	/**
	 * Tallentaa globaalit asetukset.
	 * 
	 * @param boolean Partikkeliasetus
	 * @param boolean Musiikkiasetus
	 * @param boolean ƒ‰niasetus
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
			// Asetetaan FileOutputStream ulostuloksi serializerille, k‰ytt‰en UTF-8-koodausta.
			serializer.setOutput(fileos, "UTF-8");
			// Kirjoitetaan <?xml -selite enkoodauksella (jos enkoodaus ei ole "null") 
			//ja "standalone flag" (jos "standalone" ei ole "null").
			serializer.startDocument(null, Boolean.valueOf(false));
			// Asetetaan sisennykset.
			serializer.setFeature("http://xmlpull.org/v1/doc/features.html#indent-output", true);
			// Asetetaan tagi nimelt‰ "res"  <--- LUULTAVASTI MUOKATAAN!
			serializer.startTag(null, "res");
			
			/* 
			 * T‰st‰ alkaa xml-tiedoston sisempi osuus.
			 * 
			 * Kirjoitetaan settings.xml-tiedostoon elementit 'particles',
			 * 'music' ja 'sounds', jotka pit‰v‰t tiedon siit‰, onko partikkelit 
			 * k‰ytˆss‰ vai ei.
			 */
			
			
			 /* 'Particles'-elementin sis‰ll‰ on attribuutti, jonka nimen‰ on 'value'.
			 * 'Value' tallentaa string-tiedon siit‰, onko partikkelit k‰ytˆss‰ vai ei.
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
		// Luodaan uusi XML-tiedosto highscoreille.
		File xmlSaveScores = new File(Environment.getExternalStorageDirectory()+"/Android/high_scores.xml");
		
		try {
			xmlSaveScores.createNewFile();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		// "Sidotaan" uusi tiedosto FileOutputStreamilla.
		FileOutputStream fileos = null;
		
		try {
			fileos = new FileOutputStream(xmlSaveScores);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		//Luodaan XmlSerializer, jotta voidaan kirjoittaa xml-dataa.
		XmlSerializer serializer = Xml.newSerializer();
		
		try {
			// Asetetaan FileOutputStream ulostuloksi serializerille, k‰ytt‰en UTF-8-koodausta.
			serializer.setOutput(fileos, "UTF-8");
			
			/*
			 * Kirjoitetaan <?xml -selite enkoodauksella (jos enkoodaus ei ole "null") 
			 *ja "standalone flag" (jos "standalone" ei ole "null").
			 */
			serializer.startDocument(null, Boolean.valueOf(false));
			
			// Asetetaan sisennykset.
			serializer.setFeature("http://xmlpull.org/v1/doc/features.html#indent-output", true);
			
			// Asetetaan tagi nimelt‰ "scores"
			serializer.startTag(null, "scores");
			
			/* 
			 * T‰st‰ alkaa xml-tiedoston sisempi osuus.
			 */
			
			// Tarkistetaan, onko pelaajan pisteet suuremmat, kuin aiemmat pisteet
			/*for(int i = 5; i >= 1; --i) {
				serializer.startTag(null, "player");
				serializer.attribute(null, "name", "empty");
				serializer.attribute(null, "score", String.valueOf(_score));
				serializer.endTag(null, "player");
			}*/
						
			/*
			 * Sisempi osuus p‰‰ttyy t‰h‰n.
			 */
			
			serializer.endTag(null, "scores");
			serializer.endDocument();
			
			//Kirjoitetaan xml-data FileOutputStreamiin.
			serializer.flush();
			//Suljetaan tiedostovirta.
			fileos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
