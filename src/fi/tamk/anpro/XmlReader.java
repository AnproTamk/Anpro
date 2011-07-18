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
	                        _hud.cooldownCounter.add(new CooldownCounter(Integer.parseInt(hud.getAttributeValue(null, "x")),
	                                				Integer.parseInt(hud.getAttributeValue(null, "y"))));
	                        _hud.buttons.add(new Button(Integer.parseInt(hud.getAttributeValue(null, "x")),
	                                                    Integer.parseInt(hud.getAttributeValue(null, "y"))));
	                    }
	                    else if (hud.getName().equals("icon")) {
	                    	Hud.icons.add(new Icon(Integer.parseInt(hud.getAttributeValue(null, "x")),
	                                				     Integer.parseInt(hud.getAttributeValue(null, "y"))));
	                    }
	                    else if (hud.getName().equals("counter")) {
	                        Hud.scoreCounter = new Counter(Integer.parseInt(hud.getAttributeValue(null, "x")),
	                                				       Integer.parseInt(hud.getAttributeValue(null, "y")));
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
	                
	                    else if (hud.getName().equals("joystick") && Options.joystick) {
	                    	Hud.joystick = new Joystick(Integer.parseInt(hud.getAttributeValue(null, "x")),
	                    								Integer.parseInt(hud.getAttributeValue(null, "y")));
	                    }
	                    
	                    else if (hud.getName().equals("radar_top")) {
	                    	Hud.radar_top = new Radar(Integer.parseInt(hud.getAttributeValue(null, "x")),
	                    						  	  Integer.parseInt(hud.getAttributeValue(null, "y")),
	                    						  	  Integer.parseInt(hud.getAttributeValue(null, "type")));
	                    }
	                    
	                    else if (hud.getName().equals("radar_left")) {
	 	                    Hud.radar_left = new Radar(Integer.parseInt(hud.getAttributeValue(null, "x")),
	 	                    						   Integer.parseInt(hud.getAttributeValue(null, "y")),
	 	                    						   Integer.parseInt(hud.getAttributeValue(null, "type")));
	                    }
	                    
	 	                else if (hud.getName().equals("radar_right")) {
	 		                Hud.radar_right = new Radar(Integer.parseInt(hud.getAttributeValue(null, "x")),
	 		                    						Integer.parseInt(hud.getAttributeValue(null, "y")),
	 		                    					    Integer.parseInt(hud.getAttributeValue(null, "type")));
	 	                }
	                    
                    	else if (hud.getName().equals("radar_down")) {
	                    	Hud.radar_down = new Radar(Integer.parseInt(hud.getAttributeValue(null, "x")),
	                    						  	   Integer.parseInt(hud.getAttributeValue(null, "y")),
	                    						  	   Integer.parseInt(hud.getAttributeValue(null, "type")));
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
    public final int[] readEnemyRanks()
    {
        XmlResourceParser ranks = null;
        int[] enemyStats = new int[25];
        
        try {
        	ranks = context.getResources().getXml(R.xml.enemy_ranks);
        }
        catch (Exception e) {
        	e.printStackTrace();
        }
        
        if (ranks != null) {
	        try {
	        	int index = 0;
	            while (ranks.getEventType() != XmlPullParser.END_DOCUMENT) {
	                if (ranks.getEventType() == XmlPullParser.START_TAG) {
	                    if (ranks.getName().equals("rank")) {
	                        // Muunnetaan saatujen attribuuttien tiedot integer-arvoiksi, jotka sijoitetaan taulukkoon.
	                        enemyStats[index] = ranks.getAttributeIntValue(null, "health", 0);
	                        index++;
	                        enemyStats[index] = ranks.getAttributeIntValue(null, "armor", 0);
	                        index++;
	                        enemyStats[index] = ranks.getAttributeIntValue(null, "speed", 0);
	                        index++;
	                        enemyStats[index] = ranks.getAttributeIntValue(null, "attack", 0);
	                        index++;
	                        enemyStats[index] = ranks.getAttributeIntValue(null, "ai", 0);
	                        index++;
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
     * Lukee pelitilan tiedot.
     * 
     * @param GameMode  Osoitin pelitilaan
     * @param WeaponManager Osoitin WeaponManageriin
     */
    public final void readGameMode(GameMode _survivalMode, WeaponManager _weaponManager)
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
    	
    		XmlPullParser parser = Xml.newPullParser();
    		
			parser.setInput(fis, "UTF-8");
			
			int index = 0;
			
			if (parser != null) {
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
			
			fis.close();
    	}
    	catch (Exception e){
    		e.printStackTrace();
    	}
    	
    	return scores;
    }
}