package fi.tamk.anpro;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import android.content.Context;
import android.content.res.XmlResourceParser;
import android.os.Environment;
import android.util.Log;
import android.util.Xml;

/**
 * Lukee XML-tiedostot.
 */
public class XmlReader 
{
    // Ohjelman konteksti.
	private Context context;
    
    /**
     * Alustaa luokan muuttujat.
     * 
     * @param Context Ohjelman konteksti
     */
    public XmlReader(Context _context)
    {
        context = _context;
    }
    
    /**
     * Lukee HUDin tiedot. Riippuen pelimodesta, lukee tarvittavat tiedot 
     * XML-tiedostosta joko story- tai survival-modea varten.
     * 
     * @param Hud Osoitin käyttöliittymään
     */
    public final void readHud(Hud _hud)
    {
        XmlResourceParser hud = null;
        
        // Ehto, joka tarkistaa, mikä pelitila on valittuna.
        if (GameActivity.activeMode == GameActivity.SURVIVAL_MODE) {
        	try {
                hud = context.getResources().getXml(R.xml.class.getField("hud_survival_" + Options.screenWidth + "_" + Options.screenHeight).getInt(getClass()));
            }
            catch (Exception e) {
            	// TODO: Käsittele virhe
                e.printStackTrace();
            }
        }
        else {
            hud = context.getResources().getXml(R.xml.hud_story);
        }

        // TODO: Funktio ei saa edetä tiedoston lukemiseen, mikäli tiedoston avaaminen ylempänä epäonnistui.
        
        // Luetaan XML-tiedosto ja ladataan tarvittavat arvot muistiin
        if (hud != null) {
	        try {
	            while (hud.getEventType() != XmlPullParser.END_DOCUMENT) {
	                if (hud.getEventType() == XmlPullParser.START_TAG) {
	                    if (hud.getName().equals("button")) {
	                        // NÄILLE RIVEILLE TEHDÄÄN GuiObject LUOKKAAN VASTAAVAT KOHTANSA MYÖHEMMIN!
	                        _hud.cooldowncounter.add(new CooldownCounter(Integer.parseInt(hud.getAttributeValue(null, "x")),
	                                				Integer.parseInt(hud.getAttributeValue(null, "y"))));
	                        _hud.buttons.add(new Button(Integer.parseInt(hud.getAttributeValue(null, "x")),
	                                                    Integer.parseInt(hud.getAttributeValue(null, "y"))));
	                    }
	                    else if (hud.getName().equals("counter")) {
	                        Hud.counters.add(new Counter(Integer.parseInt(hud.getAttributeValue(null, "x")),
	                                				     Integer.parseInt(hud.getAttributeValue(null, "y")),
	                                				     Integer.parseInt(hud.getAttributeValue(null, "value"))));
	                    }
	                    else if (hud.getName().equals("health_bar")) {
	                    	Hud.healthBar = new Bar(Integer.parseInt(hud.getAttributeValue(null, "x")),
	                    						    Integer.parseInt(hud.getAttributeValue(null, "y")),
	                    						    Integer.parseInt(hud.getAttributeValue(null, "type")));
	                    }
	                    else if (hud.getName().equals("armor_bar")) {
	                    	Hud.armorBar = new Bar(Integer.parseInt(hud.getAttributeValue(null, "x")),
	    						    			   Integer.parseInt(hud.getAttributeValue(null, "y")),
	    						    			   Integer.parseInt(hud.getAttributeValue(null, "type")));
	                    }
	                
	                    else if (hud.getName().equals("joystick") && Options.joystick &&
	                    		 GameActivity.activeMode == GameActivity.STORY_MODE) {
	                    	Hud.joystick = new Joystick(Integer.parseInt(hud.getAttributeValue(null, "x")),
	                    								Integer.parseInt(hud.getAttributeValue(null, "y")));
	                    }
	                }
	                else if (hud.getEventType() == XmlPullParser.END_TAG) {
	                    // ...
	                }
	                else if (hud.getEventType() == XmlPullParser.TEXT) {
	                    // ...
	                }
	
	                hud.next();
	            }
	        } catch (XmlPullParserException e) {
	            e.printStackTrace();
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
        }
        
    }
    
    /**
     * Lukee vihollistyyppien tiedot.
     * 
     * @return ArrayList<Integer> Vihollistyyppien tiedot
     */
    public final ArrayList<Integer> readEnemyRanks()
    {
        XmlResourceParser ranks = null;
        ArrayList<Integer> enemyStats = new ArrayList<Integer>();
        
        try {
        	ranks = context.getResources().getXml(R.xml.enemy_ranks);
        }
        catch (Exception e) {
        	e.printStackTrace();
        }
        
        if (ranks != null) {
	        try {
	            while (ranks.getEventType() != XmlPullParser.END_DOCUMENT) {
	                if (ranks.getEventType() == XmlPullParser.START_TAG) {
	                    if (ranks.getName().equals("rank")) {
	                        // Muunnetaan saatujen attribuuttien tiedot integer-arvoiksi, jotka sijoitetaan taulukkoon.
	                        enemyStats.add(ranks.getAttributeIntValue(null, "health", 0));
	                        enemyStats.add(ranks.getAttributeIntValue(null, "armor", 0));
	                        enemyStats.add(ranks.getAttributeIntValue(null, "speed", 0));
	                        enemyStats.add(ranks.getAttributeIntValue(null, "attack", 0));
	                        enemyStats.add(ranks.getAttributeIntValue(null, "ai", 0));
	                    }
	                }
	                else if (ranks.getEventType() == XmlPullParser.END_TAG) {
	                    // ...
	                }
	                
	                ranks.next();
	            }
	        }
	        catch (Exception e) {
	        	// TODO: Käsittele virhe
	            e.printStackTrace();
	        }
        }
        
        return enemyStats;
    }
    
    /**
     * Lukee Survival-pelitilan tiedot.
     * 
     * @param SurvivalMode  Osoitin pelitilaan
     * @param WeaponManager Osoitin WeaponManageriin
     */
    public final void readSurvivalMode(SurvivalMode _survivalMode, WeaponManager _weaponManager)
    {
        XmlResourceParser rsm = null;
        
        try {
        	rsm = context.getResources().getXml(R.xml.survivalmode);
        }
        catch (Exception e) {
        	e.printStackTrace();
        }
        
        int currentWave = 0;
        
        if (rsm != null) {
	        try {
	            while (rsm.getEventType() != XmlPullParser.END_DOCUMENT) {
	                if (rsm.getEventType() == XmlPullParser.START_TAG) {
	                    if (rsm.getName().equals("enemy")) {
	                        /*
	                         * Tilapäinen "rankTemp" muuttuja, joka saa survivalmode-xml-tiedostosta
	                         * string-arvon muutettuna int-arvoksi.
	                         */ 
	                        int rankTemp = Integer.parseInt(rsm.getAttributeValue(null, "rank")) - 1;
	                        
	                        /*
	                         * _survivalmode-olio/osoittaja asettaa enemies-taulukon arvoksi enemyStats-taulukosta
	                         * saadut arvot.
	                         */
	                        _survivalMode.enemies.add(new Enemy(_survivalMode.enemyStats[rankTemp][0],
	                                                            _survivalMode.enemyStats[rankTemp][1],
	                                                            _survivalMode.enemyStats[rankTemp][2],
	                                                            _survivalMode.enemyStats[rankTemp][3],
	                                                            _survivalMode.enemyStats[rankTemp][4],
	                                                            rankTemp + 1, _weaponManager));
	                    }
	                    if (rsm.getName().equals("wave")) {
	                        
	                        /*
	                         *  Tallennetaan waveTemp-muuttujaan survivalmode-xml-tiedostosta enemies-
	                         *  attribuutin tiedot string-tyyppinä.
	                         */
	                        String waveTemp = rsm.getAttributeValue(null, "enemies");
	                        // Jaetaan waveTemp-muuttujan tiedot yksittäisiksi merkeiksi ja tallennetaan string-taulukkoon "wave".
	                        String[] wave = null;
	                        wave = waveTemp.split("\\,");
	                        
	                        // Muunnetaan tietotyypit ja lisätään tiedot waves-taulukkoon.
	                        int index = 0;
	                        for (int i = wave.length - 1; i >= 0 ; --i) {
	                        	if (wave[i] != null && wave[i] != "") {
	                        		_survivalMode.waves[currentWave][index] = Integer.parseInt(wave[i]);
	                        		++index;
	                        	}
	                        }
	                        
	                        ++currentWave;
	                    }
	                    
	                }
	                else if (rsm.getEventType() == XmlPullParser.END_TAG) {
	                    // ...
	                }
	                
	                rsm.next();
	            }
	        }
	        catch (Exception e) {
	        	// TODO: Käsittele virhe
	            e.printStackTrace();
	        }
        }
    }
    
    /**
     * Lukee yhden kentän tiedot Story-pelitilasta.
     * 
     * @param _level         Kentän järjestysnumero
     * @param _storyMode     Osoitin pelitilaan
     * @param _weaponManager Osoitin WeaponManageriin
     */
    public final void readLevel(byte _level, StoryMode _storyMode, WeaponManager _weaponManager)
    {
    	byte readingState = 0; // 0 ei mitään
    						   // 1 enemies
		                       // 2 map
		                       // 3 mission
		                       // 4 task
    	
    	boolean enemiesGiven = false;
    	boolean mapGiven     = false;
    	boolean missionGiven = false;
    	
        XmlResourceParser xrp = null;
        
        try {
        	xrp = context.getResources().getXml(context.getResources().getIdentifier("level_"+_level, "xml", "fi.tamk.anpro"));
        }
        catch (Exception e) {
        	Log.e("XMLREADER", "Virhe readLevel:ssä");
        }
        
        if (xrp != null) {
	        try {
	            while (xrp.getEventType() != XmlPullParser.END_DOCUMENT) {
	                if (xrp.getEventType() == XmlPullParser.START_TAG) {
	                    if (xrp.getName().equals("enemies") && !enemiesGiven) {
	                    	readingState = 1;
	                    	enemiesGiven = true;
	                    }
	                    else if (xrp.getName().equals("map") && !mapGiven && enemiesGiven) {
	                    	readingState = 2;
	                    	mapGiven = true;
	                    }
	                    else if (xrp.getName().equals("mission") && !missionGiven && mapGiven) {
	                    	readingState = 3;
	                    	missionGiven = true;
	                    }
	                    else if (xrp.getName().equals("task") && readingState == 3) {
	                    	readingState = 4;
	                    	_storyMode.tasks.add(new Task((byte)xrp.getAttributeIntValue(null, "type", 0),
	                    								  (byte)xrp.getAttributeIntValue(null, "skill_points", 0)));
	                    }
	                    
	                    if (xrp.getName().equals("enemy") && readingState == 1) {
	                    	int rank = xrp.getAttributeIntValue(null, "rank", 0);
	                        _storyMode.enemies.add(new Enemy(_storyMode.enemyStats[rank][0],
	                        								 _storyMode.enemyStats[rank][1],
	                        								 _storyMode.enemyStats[rank][2],
	                        								 _storyMode.enemyStats[rank][3],
	                        								 _storyMode.enemyStats[rank][4],
	                        								 rank,
	                        								 _weaponManager));
	                    }
	                    else if (xrp.getName().equals("planet") && readingState == 2) {
	                    	_storyMode.planets.add(new Obstacle((byte)xrp.getAttributeIntValue(null, "type", 0),
	                    										xrp.getAttributeIntValue(null, "x", 0),
	                    										xrp.getAttributeIntValue(null, "y", 0),
	                    										xrp.getAttributeIntValue(null, "speed", 0),
	                    										xrp.getAttributeIntValue(null, "direction", 0)));
	                    }
	                    else if (xrp.getName().equals("asteroid") && readingState == 2) {
	                    	_storyMode.asteroids.add(new Obstacle((byte)xrp.getAttributeIntValue(null, "type", 1),
	                    									      xrp.getAttributeIntValue(null, "x", 0),
																  xrp.getAttributeIntValue(null, "y", 0),
																  xrp.getAttributeIntValue(null, "speed", 0),
																  xrp.getAttributeIntValue(null, "direction", 0)));
	                    }
	                    else if (xrp.getName().equals("star") && readingState == 2) {
	                    	_storyMode.stars.add(new Obstacle((byte)xrp.getAttributeIntValue(null, "type", 2),
	                    									  xrp.getAttributeIntValue(null, "x", 0),
															  xrp.getAttributeIntValue(null, "y", 0),
															  xrp.getAttributeIntValue(null, "speed", 0),
															  xrp.getAttributeIntValue(null, "direction", 0)));
	                    }
	                    else if (xrp.getName().equals("item") && readingState == 4) {
	                    }
	                    else if (xrp.getName().equals("target") && readingState == 4) {
	                    	// ...
	                    }
	                }
	                else if (xrp.getEventType() == XmlPullParser.END_TAG) {
	                    if (xrp.getName().equals("enemies")) {
	                    	readingState = 0;
	                    }
	                    else if (xrp.getName().equals("map")) {
	                    	readingState = 0;
	                    }
	                    else if (xrp.getName().equals("mission")) {
	                    	readingState = 0;
	                    }
	                    else if (xrp.getName().equals("task")) {
	                    	readingState = 3;
	                    }
	                }
	                
	                xrp.next();
	            }
	        }
	        catch (Exception e) {
	        	Log.e("XMLREADER", "Virhe readLevel:ssä");
	        }
        }
    }
    
    /**
     * Lukee vanhan pelitilanteen.
     */
    public final void readSavedGame() {
        /*XmlResourceParser rsg = null;
        rsg = context.getResources().getXml(R.xml.savedgame);
        
        if (rsg != null) {
	        try {
	            while (rsg.getEventType() != XmlPullParser.END_DOCUMENT) {
	                if (rsg.getEventType() == XmlPullParser.START_TAG) {
	                    if (rsg().equals("")) {
	                        
	                    }
	                    if (rsg().equals("")) {
	                        
	                    }
	                }
	                else if (rsg.getEventType() == XmlPullParser.END_TAG) {
	                    // ...
	                }
	                
	                rsg.next();
	            }
	        }
	        catch (Exception e) {
	        	// TODO: Käsittele virhe
	            e.printStackTrace();
	        }
    	}*/
    }
    
    /**
     * 
     * @return scores-taulukko
     */
    public final int[] readHighScores()
    {
    	File 			  file   = new File(Environment.getExternalStorageDirectory()+"/highscores.xml");
    	FileInputStream   fis = null;
    	int[] 		      scores = new int[5];
    	
    	try {
    		fis = new FileInputStream(file);
    	}
    	catch (Exception e){
    		e.printStackTrace();
    		Log.e("TESTI", "Reader Error 1: " + e.getMessage());
    	}
    	
    	XmlPullParser parser = Xml.newPullParser();
    	try {
			parser.setInput(fis, "UTF-8");
		} catch (XmlPullParserException e) {
			e.printStackTrace();
			Log.e("TESTI", "Reader Error 2: " + e.getMessage());
		}
		
		int index = 0;
		if (parser != null) {
	        try {
	            while (parser.getEventType() != XmlPullParser.END_DOCUMENT) {
	                if (parser.getEventType() == XmlPullParser.START_TAG) {
	                    if (parser.getName().equals("score")) {
	                    	scores[index - 1] = Integer.parseInt(parser.getAttributeValue(null, "value"));
	                    }
	                    
	                    ++index;
	                }
	                else if (parser.getEventType() == XmlPullParser.END_TAG) {
	                    // ...
	                }
	                
	                parser.next();
	            }
	        }
	        catch (Exception e) {
	            e.printStackTrace();
	            Log.e("TESTI", "Reader Error 3: " + e.getMessage());
	        }
        }
    	
    	try {
			fis.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
    	
    	return scores;
    }
}