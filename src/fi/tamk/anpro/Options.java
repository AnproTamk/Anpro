package fi.tamk.anpro;

/*
import java.io.IOException;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.content.Context;
import android.content.res.XmlResourceParser;
*/

public class Options {
	
	private boolean particles, music, sounds;
	
	/*
	 * Options-luokan rakentaja
	 */
	public Options(boolean _particles, boolean _music, boolean _sounds) {
		particles = _particles;
		music = _music;
		sounds = _sounds;
	}
}
