package fi.tamk.anpro;

import java.io.IOException;
import java.util.ArrayList;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import android.content.Context;
import android.content.res.XmlResourceParser;

/**
 * Lukee XML-tiedostot.
 */
public class XmlReader 
{
    /* Ohjelman konteksti */
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
     * Lukee yhden kentän tiedot. (VANHENTUNUT!)
     * 
     * @param int Kentän järjestysnumero
     */
    public final void readLevel(int _id)
    {
        XmlResourceParser level = null;
        try {
            level = context.getResources().getXml(R.xml.class.getField("level_"+_id).getInt(getClass()));
        } catch (Exception e) {
            e.printStackTrace();
        }

        //( Luetaan XML-tiedosto ja ladataan tarvittavat arvot muistiin
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
            } catch (Exception e) {
                e.printStackTrace();
            }
        		//								   .class.getField("level_"+_id).getInt(getClass()));
            	//hud = context.getResources().getXml(R.xml."hud_survival_");
        }
        else {
            hud = context.getResources().getXml(R.xml.hud_story);
        }

        // Luetaan XML-tiedosto ja ladataan tarvittavat arvot muistiin
        try {
            while (hud.getEventType() != XmlPullParser.END_DOCUMENT) {
                if (hud.getEventType() == XmlPullParser.START_TAG) {
                    if (hud.getName().equals("button")) {
                        // NÄILLE RIVEILLE TEHDÄÄN GuiObject LUOKKAAN VASTAAVAT KOHTANSA MYÖHEMMIN!
                        _hud.icons.add(new Icon(Integer.parseInt(hud.getAttributeValue(null, "x")),
                                				Integer.parseInt(hud.getAttributeValue(null, "y"))));
                        _hud.buttons.add(new Button(Integer.parseInt(hud.getAttributeValue(null, "x")),
                                                    Integer.parseInt(hud.getAttributeValue(null, "y"))));
                    }
                    else if (hud.getName().equals("counter")) {
                        // ...
                    }
                    else if (hud.getName().equals("health_bar")) {
                    	Hud.healthBar = new Bar(Integer.parseInt(hud.getAttributeValue(null, "x")),
                    						     Integer.parseInt(hud.getAttributeValue(null, "y")));
                    }
                    else if (hud.getName().equals("joystick")) {
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
    
    /**
     * Lukee globaalit asetukset.
     * 
     * @return boolean[] Asetukset
     */
    public final boolean[] readSettings()
    {
        XmlResourceParser settings = null;
        boolean particles = false, music = false, sounds = false;
        
        settings = context.getResources().getXml(R.xml.settings);
        
        try {
            while (settings.getEventType() != XmlPullParser.END_DOCUMENT) {
                if (settings.getEventType() == XmlPullParser.START_TAG) {
                    if (settings.getName().equals("particles")) {
                        if (settings.getAttributeValue(null, "value") == "1") {
                            particles = true;
                        }
                        else
                            particles = false;
                    }
                    else if (settings.getName().equals("music")) {
                        if (settings.getAttributeValue(null, "value") == "1") {
                            music = true;
                        }
                        else
                            music = false;
                    }
                    else if (settings.getName().equals("sounds")) {
                        if (settings.getAttributeValue(null, "value") == "1") {
                            sounds = true;
                        }
                        else
                            sounds = false;
                    }
                }
                else if (settings.getEventType() == XmlPullParser.END_TAG) {
                    // ...
                }
                
                settings.next();
            }
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        boolean settingValues[] = {particles, music, sounds};
        return settingValues;
    }
    
    /**
     * Lukee vihollistyyppien tiedot.
     * 
     * @return ArrayList<Integer> Vihollistyyppien tiedot
     */
    public final ArrayList<Integer> readRanks()
    {
        XmlResourceParser ranks = null;
        ArrayList<Integer> enemyStats = new ArrayList<Integer>();
        
        ranks = context.getResources().getXml(R.xml.ranks);
        
        try {
            while (ranks.getEventType() != XmlPullParser.END_DOCUMENT) {
                if (ranks.getEventType() == XmlPullParser.START_TAG) {
                    if (ranks.getName().equals("rank")) {
                        // Muunnetaan saatujen attribuuttien tiedot integer-arvoiksi, jotka sijoitetaan taulukkoon.
                        enemyStats.add(ranks.getAttributeIntValue(null, "health", 0));
                        enemyStats.add(ranks.getAttributeIntValue(null, "speed", 0));
                        enemyStats.add(ranks.getAttributeIntValue(null, "attack", 0));
                        enemyStats.add(ranks.getAttributeIntValue(null, "defence", 0));
                        enemyStats.add(ranks.getAttributeIntValue(null, "ai", 0));
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
    
    /**
     * Lukee Survival-pelitilan tiedot.
     * 
     * @param SurvivalMode Osoitin pelitilaan
     */
    public final void readSurvivalMode(SurvivalMode _survivalMode, WeaponManager _weaponManager)
    {
        XmlResourceParser rsm = null;
        // Haetaan oikea xml-tiedosto resoluution perusteella
        rsm = context.getResources().getXml(R.xml.survivalmode);

        int currentWave = 0;
        
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
    
    /**
     * Lukee Story-pelitilan tiedot.
     * 
     * @param StoryMode Osoitin pelitilaan
     */
    /*public final void readStoryMode(StoryMode _storyMode){
        XmlResourceParser rStoryMode = null;
        rStoryMode = context.getResources().getXml(R.xml.storymode);
        int currentWave = 0;
        
        try {
            while (rStoryMode.getEventType() != XmlPullParser.END_DOCUMENT) {
                if (rStoryMode.getEventType() == XmlPullParser.START_TAG) {
                    if (rStoryMode.getName().equals("enemy")) {
                        int rankTemp = Integer.parseInt(rStoryMode.getAttributeValue(null, "rank"));
                        
                        _storyMode.enemies.add(new Enemy(_storyMode.enemyStats[rankTemp][0],
                                _storyMode.enemyStats[rankTemp][1],
                                _storyMode.enemyStats[rankTemp][2],
                                _storyMode.enemyStats[rankTemp][3],
                                _storyMode.enemyStats[rankTemp][4]));
                        
                    }
                    if (rStoryMode.getName().equals("story")) {
                        // Tähän funktio "chapterin" tallentamiseksi.
                        // int currentChapter = blaa...
                        
                        if (rStoryMode.getName().equals("level")) {
                            // Tähän funktio "levelin" tallentamiseksi.
                            // int levelType = ...
                        }
                    }
                }
                else if (rStoryMode.getEventType() == XmlPullParser.END_TAG) {
                    // ...
                }
                
                rStoryMode.next();
            }
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }*/
    
    /**
     * Lukee vanhan pelitilanteen.
     */
    public final void readSavedGame() {
        /*XmlResourceParser rsg = null;
        rsg = context.getResources().getXml(R.xml.savedgame);
        
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
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }*/
    }
}