package fi.tamk.anpro;

import java.io.IOException;

import javax.microedition.khronos.opengles.GL10;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import android.content.Context;
import android.content.res.Resources.NotFoundException;
import android.content.res.XmlResourceParser;

public class XmlReader 
{
	private Context context;
	private GLRenderer renderer;
	private GL10 gl;
	
	
	
	public XmlReader(Context _context, GLRenderer _renderer, GL10 _gl)
	{
		context = _context;
		renderer = _renderer;
		gl = _gl;
	}
	

	
	public void readLevel(int _id)
	{
		XmlResourceParser level = null;
		try {
			level = context.getResources().getXml(R.xml.class.getField("level_"+_id).getInt(getClass()));
		} catch (Exception e) {
			e.printStackTrace();
		}

		
        // Luetaan XML-tiedosto ja ladataan tarvittavat arvot muistiin
        try {
            while (level.getEventType() != XmlPullParser.END_DOCUMENT) {
                if (level.getEventType() == XmlPullParser.START_TAG) {
                    if (level.getName().equals("player")) {
                        renderer.players.add(new Player(gl, context, level.getAttributeResourceValue(null, "id", 0),
                        								level.getAttributeIntValue(null, "health", 10),
                        								level.getAttributeIntValue(null, "defence", 0)));

                    }
 
                    else if (level.getName().equals("enemy")) {
                    	//renderer.enemies.add(new Enemy(gl, context, level.getAttributeResourceValue(null, "id", 0),
						//								level.getAttributeIntValue(null, "rank", 1));

						//renderer.enemies.get(renderer.enemies.size()-1).spawnPoint = level.getAttributeIntValue(null, "spawnPoint", 0);
                    }
                }
                else if (level.getEventType() == XmlPullParser.END_TAG) {
                    // ...
                }
                else if (level.getEventType() == XmlPullParser.TEXT) {
                    // ...
                }

                level.next();
            }
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
	}
 	
	public void readHUD()
	{
		/*XmlResourceParser level = context.getResources().getXml(R.xml.HUD);

        // Luetaan XML-tiedosto ja ladataan tarvittavat arvot muistiin
        try {
            while (level.getEventType() != XmlPullParser.END_DOCUMENT) {
                if (level.getEventType() == XmlPullParser.START_TAG) {
                    // ...
                }
                else if (level.getEventType() == XmlPullParser.END_TAG) {
                    // ...
                }
                else if (level.getEventType() == XmlPullParser.TEXT) {
                    // ...
                }

                level.next();
            }
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }*/
	}
	
}