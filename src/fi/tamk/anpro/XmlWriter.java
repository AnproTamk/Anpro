package fi.tamk.anpro;

import android.content.Context;
import android.os.Environment;
import android.util.Xml;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.xmlpull.v1.XmlSerializer;

/*
 * Tämä luokka tallentaa pelin asetukset
 * sekä StoryModen pisteet, kentän, 
 * achievemintit, kyvyt yms.
 */
public class XmlWriter {
	
	private Context context;
	
	public XmlWriter(Context _context) {
		context = _context;
	}
	
	/*
	 * Tämä funktio tallentaa StoryModen tiedot XML-tiedostoon.
	 */
	public void saveGame() {
		// Luodaan uusi XML-tiedosto pelitallennukselle.
		File xmlSaveGame = new File(Environment.getExternalStorageDirectory()+"/saved_game.xml");
		
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
			serializer.startDocument(null, Boolean.FALSE);
			// Asetetaan sisennykset.
			serializer.setFeature("http://xmlpull.org/v1/doc/features.html#indent-output", true);
			// Asetetaan tagi nimeltä "root"  <--- LUULTAVASTI MUOKATAAN!
			serializer.startTag(null, "root");
			// Tästä alkaa xml-tiedoston sisempi osuus.
			serializer.startTag(null, "child1");
			serializer.endTag(null, "child1");
			
			serializer.startTag(null, "child2");
			serializer.attribute(null, "attribute", "value");
			serializer.endTag(null, "child2");
			
			serializer.startTag(null, "child3");
			serializer.text("This is a 'child3' element");
			serializer.endTag(null, "child3");
			
			serializer.endTag(null, "root");
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
