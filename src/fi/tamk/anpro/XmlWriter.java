package fi.tamk.anpro;

import android.content.Context;
import android.os.Environment;
import android.util.Xml;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.xmlpull.v1.XmlSerializer;

/*
 * Tämä luokka tallentaa pelin asetukset
 * sekä StoryModen pisteet, kentän, 
 * achievemintit, kyvyt yms.
 */
public class XmlWriter {
	
	/*
	 * private Context context;
	 * 
	 * public XmlWriter(Context _context) {
	 *     context = _context;
	 * }
	 */
	
	
	/*
	 * Tämä funktio tallentaa StoryModen tiedot XML-tiedostoon.
	 */
	public boolean saveGame() {
		
		
		
		return false;
	}
	
	/*
	 * Tämä funktio tallentaa pelin asetukset XML-tiedostoon.
	 * @param boolean[] settingStates
	 **/
	public void saveSettings(boolean[] settingStates) {
		// Luodaan uusi XML-tiedosto pelitallennukselle.
		File xmlSaveGame = new File(Environment.getExternalStorageDirectory()+"/settings.xml");
		
		String[] stateId = {"0", "0", "0"};
		
		for (int i = 0; i < 3; i++) {
			if (settingStates[i] == false) {
				stateId[i] = "0";
			}
			else
				stateId[i] = "1";
		}
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
			 * 
			 * Kirjoitetaan settings.xml-tiedostoon elementit 'particles',
			 * 'music' ja 'sounds', jotka pitävät tiedon siitä, onko partikkelit 
			 * käytössä vai ei.
			 */
			
			/*
			 * 'Particles'-elementin sisällä on attribuutti, jonka nimenä on 'value'.
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
	
	/*
	 * Tämä funktio tallentaa pelaajan saavutukset XML-tiedostoon.
	 */
	public boolean saveAchievements() {
		// Luodaan uusi XML-tiedosto achievementeille.
		File xmlSaveAchievements = new File(Environment.getExternalStorageDirectory()+"/achievements.xml");
		
		try {
			xmlSaveAchievements.createNewFile();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		
		return false;
	}
	
}
