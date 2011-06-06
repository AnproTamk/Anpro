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
 * T�m� luokka tallentaa pelin asetukset
 * sek� StoryModen pisteet, kent�n, 
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
	 * T�m� funktio tallentaa StoryModen tiedot XML-tiedostoon.
	 */
	public boolean saveGame() {
		
		
		
		return false;
	}
	
	/*
	 * T�m� funktio tallentaa pelin asetukset XML-tiedostoon.
	 **/
	public boolean saveOptions() {
		// Luodaan uusi XML-tiedosto pelitallennukselle.
		File xmlSaveGame = new File(Environment.getExternalStorageDirectory()+"/options.xml");
		
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
			// Asetetaan FileOutputStream ulostuloksi serializerille, k�ytt�en UTF-8-koodausta.
			serializer.setOutput(fileos, "UTF-8");
			// Kirjoitetaan <?xml -selite enkoodauksella (jos enkoodaus ei ole "null") 
			//ja "standalone flag" (jos "standalone" ei ole "null").
			serializer.startDocument(null, Boolean.valueOf(false));
			// Asetetaan sisennykset.
			serializer.setFeature("http://xmlpull.org/v1/doc/features.html#indent-output", true);
			// Asetetaan tagi nimelt� "res"  <--- LUULTAVASTI MUOKATAAN!
			serializer.startTag(null, "res");
			// T�st� alkaa xml-tiedoston sisempi osuus.
			
			/*
			 * Kirjoitetaan settings.xml-tiedostoon elementti 'particles',
			 * joka pit�� tiedon siit�, onko partikkelit k�yt�ss� vai ei.
			 */
			serializer.startTag(null, "particles");
			/*
			 * 'Particles'-elementin sis�ll� on attribuutti, jonka nimen� on 'value'.
			 * 'Value' tallentaa numeerisen tiedon siit�, onko partikkelit k�yt�ss� vai ei.
			 */
			serializer.attribute(null, "value", "0");
			serializer.endTag(null, "particles");
			
			serializer.startTag(null, "music");
			serializer.attribute(null, "value", "0");
			serializer.endTag(null, "music");
			
			serializer.startTag(null, "sounds");
			serializer.attribute(null, "value", "0");
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
		
		return false;
	}
	
	/*
	 * T�m� funktio tallentaa pelaajan saavutukset XML-tiedostoon.
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
