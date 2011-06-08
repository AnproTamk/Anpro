package fi.tamk.anpro;

import java.io.IOException;
import java.util.ArrayList;

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
                        /*renderer.players.add(new Player(gl, context, level.getAttributeResourceValue(null, "id", 0),
                        								level.getAttributeIntValue(null, "health", 10),
                        								level.getAttributeIntValue(null, "defence", 0)));*/

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
	
	// Luetaan XML-tiedosto pelin asetuksia varten.
	public boolean[] readSettings() {
		XmlResourceParser settings = null;
		boolean particles = false, music = false, sounds = false;
		
		settings = context.getResources().getXml(R.xml.settings);
        
        try {
        	while (settings.getEventType() != XmlPullParser.END_DOCUMENT) {
        		if (settings.getEventType() == XmlPullParser.START_TAG) {
                    if (settings.getName().equals("particles")) {
                    	if (settings.getAttributeValue(null, "value") == "true") {
                    		particles = true;
                    	}
                    	else
                    		particles = false;
                    }
                    else if (settings.getName().equals("music")) {
                    	if (settings.getAttributeValue(null, "value") == "true") {
                    		music = true;
                    	}
                    	else
                    		music = false;
                    }
                    else if (settings.getName().equals("sounds")) {
                    	if (settings.getAttributeValue(null, "value") == "true") {
                    		sounds = true;
                    	}
                    	else
                    		sounds = false;
                    }
                }
                else if (settings.getEventType() == XmlPullParser.END_TAG) {
                    // ...
                }
        	}
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }
		boolean settingValues[] = {particles, music, sounds};
		return settingValues;
	}
	
	/*
	 * Funktio lukee ranks.xml-tiedostosta vihollisten tason
	 * ja sijoittaa ne kaksiulotteiseen taulukkoon.
	 */
	public ArrayList<Integer> readRanks() {
		XmlResourceParser ranks = null;
		ArrayList<Integer> enemyStats = null;
		
		ranks = context.getResources().getXml(R.xml.ranks);
		
		try {
        	while (ranks.getEventType() != XmlPullParser.END_DOCUMENT) {
        		if (ranks.getEventType() == XmlPullParser.START_TAG) {
                    if (ranks.getName().equals("ranks")) {
                    	// Muunnetaan saatujen attribuuttien tiedot integer-arvoiksi, jotka sijoitetaan taulukkoon.
                    	enemyStats.add(Integer.parseInt(ranks.getAttributeValue(null, "health")));
                    	enemyStats.add(Integer.parseInt(ranks.getAttributeValue(null, "speed")));
                    	enemyStats.add(Integer.parseInt(ranks.getAttributeValue(null, "attack")));
                    	enemyStats.add(Integer.parseInt(ranks.getAttributeValue(null, "defence")));
                    	enemyStats.add(Integer.parseInt(ranks.getAttributeValue(null, "ai")));
                    }
                }
                else if (ranks.getEventType() == XmlPullParser.END_TAG) {
                    // ...
                }
        		
        		ranks.next();
        	}
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return enemyStats;
	}
	
	public void readSurvivalMode(SurvivalMode _survivalMode) {
		XmlResourceParser rsm = null;
		rsm = context.getResources().getXml(R.xml.survivalmode);
		int currentWave = 0;
		
		try {
        	while (rsm.getEventType() != XmlPullParser.END_DOCUMENT) {
        		if (rsm.getEventType() == XmlPullParser.START_TAG) {
        			if (rsm.getName().equals("enemy")) {
        				int rankTemp = Integer.parseInt(rsm.getAttributeValue(null, "rank"));
        				
        				_survivalMode.enemies.add(new Enemy(_survivalMode.enemyStats[rankTemp][0],
        													_survivalMode.enemyStats[rankTemp][1],
        													_survivalMode.enemyStats[rankTemp][2],
        													_survivalMode.enemyStats[rankTemp][3],
        													_survivalMode.enemyStats[rankTemp][4]));
        			}
        			if (rsm.getName().equals("wave")) {
        				// int enemisTemp = Integer.parseInt(rsm.getAttributeValue(null, "enemies"));
        				
        				String waveTemp = rsm.getAttributeValue(null, "enemies");
        				String wave[] = waveTemp.split("\\,");
        				
        				// Muunnetaan tietotyypit ja lisätään tiedot waves-taulukkoon.
        				int index = 0;
        				for (int i = wave.length - 1; i >= 0 ; --i) {
        					_survivalMode.waves[currentWave][index] = Integer.parseInt(wave[i]);
        					++index;
        				}
        				
        				++currentWave;
        			}
        			
                }
                else if (rsm.getEventType() == XmlPullParser.END_TAG) {
                    // ...
                }
        		
        		rsm.next();
        	}
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
	}
}