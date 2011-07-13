package fi.tamk.anpro;

import java.util.ArrayList;

import android.content.Context;
import android.util.DisplayMetrics;
import android.util.Log;

/**
 * Survival-pelitila. Luo pelaajan ja viholliset ja hallitsee vihollisaaltojen
 * kutsumisen ja pistelaskurin päivittämisen.
 */
public class GameMode
{
	/* Vakioita XML-tiedostojen lukemista ja muuttujien alustamista varten */
    public static final int AMOUNT_OF_WAVES            = 4;
    public static final int AMOUNT_OF_ENEMIES_PER_WAVE = 11;

    /* Pelaaja ja emoalus */
    public Player     player;
    public Mothership mothership;
    
    /* Tähtitausta */
    private BackgroundStar[] backgroundStars;

    /* Asteroidit */
    public Obstacle[] asteroids; // Asteroidit
    public Obstacle[] planets; // Planeetat
    
    /* Kerättävät esineet */
    public Collectable[] collectables; // Kerättävät esineet
    
    /* Viholliset */
    public           ArrayList<Enemy> enemies;         // Viholliset
    protected        int[][]          enemyStats;      // Vihollistyyppien statsit ([rank][attribuutti] = [arvo])
    public           int              waves[][];       // [aalto][vihollisen järjestysnumero] = [vihollisen indeksi enemies-taulukossa]
    private   static int              currentWave = 0;
    public    static int              enemiesLeft = 0; // Vihollisiä jäljellä kentällä
    
    /* Ruudun koko ja kentän rajat */
    protected int halfOfScreenWidth;
    protected int halfOfScreenHeight;
    protected int overBoundWidth;
    protected int overBoundHeight; 
    
    /* Muiden olioiden pointterit */
    protected WeaponManager weaponManager;
    protected CameraManager camera;
    protected GameActivity  gameActivity;
    
    /* Pisteet ja combot */
    private static long score;
    private static int  comboMultiplier = 2; // Combokerroin pisteiden laskemista varten
    private static long lastTime        = 0; // Edellisen pisteen lisäyksen aika
    private static long newTime;             // Uuden pisteen lisäyksen aika
    
    /* Vihollisten aloituspaikat */
    private int spawnPoints[][][]; // [rykelmä][paikka][x/y] = [koordinaatti]
    
    /* "Satunnaismuuttujat" objektien luomista varten, tarvitaan jos luominen randomisoidaan */
    private int randX;         // = Utility.getRandom(0, 400) + 1;
    private int randY;         // = Utility.getRandom(0, 400) + 1;
    private int randSpeed = Utility.getRandom(0, 10) + 1;
    private int randDirection = Utility.getRandom(1, 2) + 1;
    
    /**
     * Alustaa luokan muuttujat, lukee pelitilan tarvitsemat tiedot ja käynnistää pelin.
     * 
     * @param GameActivity   Pelitilan aloittava aktiviteetti
     * @param DisplayMetrics Näytön tiedot
     * @param Context		 Ohjelman konteksti
     * @param WeaponManager  Osoitin WeaponManageriin
     */
    public GameMode(GameActivity _gameActivity, DisplayMetrics _dm, Context _context, WeaponManager _weaponManager)
    {
    	// Tallennetaan osoitin peliaktiviteettiin
        gameActivity = _gameActivity;
        
        // Tallennetaan näytön tiedot
        halfOfScreenWidth  = _dm.widthPixels;
        halfOfScreenHeight = _dm.heightPixels;
        
        overBoundWidth  = halfOfScreenWidth + 50 + 0; // TODO: Nollien tilalle se matka, jonka pelaaja voi kulkea 2-3 sekunnissa
        overBoundHeight = halfOfScreenHeight + 50 + 0;
                
        // Otetaan CameraManager käyttöön
        camera = CameraManager.getInstance();
        
        // Alustetaan taulukot
        enemies         = new ArrayList<Enemy>();
        enemyStats      = new int[5][5];
        asteroids       = new Obstacle[3];
        planets         = new Obstacle[2];
        collectables    = new Collectable[3];
        backgroundStars = new BackgroundStar[15];
        
    	// Luodaan pelaaja
    	player = new Player(100, 100, this);
    	player.x = 0;
    	player.y = 0;
    	
    	// Luodaan emoalus
    	mothership = new Mothership(0);
        
        // Luetaan vihollistyyppien tiedot
        XmlReader reader = new XmlReader(_context);
        ArrayList<Integer> enemyStatsTemp = reader.readEnemyRanks();
        int rank = 0;
        for (int i = 0; i < enemyStatsTemp.size(); ++i) {
        	rank = (int)(i / 5);
        	
        	enemyStats[rank][i-rank*5] = enemyStatsTemp.get(i);
        }
        
        gameActivity  = _gameActivity;
        weaponManager = _weaponManager;
    	
    	// Alustetaan taulukot
        waves = new int[AMOUNT_OF_WAVES][AMOUNT_OF_ENEMIES_PER_WAVE];
        for (int j = 0; j < AMOUNT_OF_WAVES; ++j) {
        	for (int i = 0; i < AMOUNT_OF_ENEMIES_PER_WAVE; ++i) {
        		waves[j][i] = -1;
        	}
        }
        spawnPoints  = new int[9][3][2];
        
        // Luetaan pelitilan tiedot
        reader.readGameMode(this, _weaponManager);
        
        // Luodaan tähtitausta
        generateStarBackground();

        // Luodaan "pelikenttä" (objektit ja vihollisten spawnpointit)
        generateMap();
        
        // Käynnistetään ensimmäinen vihollisaalto
        startWave();
    }
    
    /**
     * Päivittää pisteet.
     * 
     * @param _rank Tuhotun vihollisen taso, jonka perusteella pisteitä lisätään
     * @param _x    X-koordinaatti
     * @param _y    Y-koordinaatti
     */
    public static void updateScore(int _rank, float _x, float _y)
    {
        // Päivitetään lastTime nykyisellä ajalla millisekunteina
        if (lastTime == 0) {
            lastTime = android.os.SystemClock.uptimeMillis();
            score += (10 * _rank + 5 * _rank * currentWave);
        }
        else {
            newTime = android.os.SystemClock.uptimeMillis();
            
            // Verrataan aikoja keskenään ja annetaan pisteitä sen mukaisesti
            if (newTime-lastTime <= 700) {
                ++comboMultiplier;
            	score += (10 * _rank + 5 * _rank * currentWave) * comboMultiplier;
            	
            	EffectManager.showComboMultiplier(comboMultiplier, _x ,_y);
            }
            // Jos pelaaja ei saa comboa resetoidaan comboMultiplier
            else {
                score += (10 * _rank + 5 * _rank * currentWave);
                comboMultiplier = 1;
                lastTime = android.os.SystemClock.uptimeMillis();
            }
        }
        
        Hud.updateScoreCounter(score);
    }
    
    /**
     * Käynnistää uuden vihollisaallon asettamalla siihen kuuluvat viholliset aktiivisiksi.
     */
    public void startWave()
    {
        /* Tarkastetaan onko kaikki vihollisaallot käyty läpi */
        if (currentWave == AMOUNT_OF_WAVES) { // TODO: TARKISTA MITEN MULTIDIMENSIONAL ARRAYN LENGTH TOIMII! (halutaan tietää wavejen määrä)
        	Log.e("testi", "currentWave: " + currentWave);
            currentWave = 0;
            
            // Tarkistetaan vihollisen luokka, kasvatetaan sitä yhdellä ja lähetetään sille uudet statsit
            int rankTemp;
            
            for (int index = enemies.size()-1; index >= 0; --index) {
            	Log.v("testi", "index: " + index);
                // Lasketaan uusi rank, käytetään väliaikaismuuttujana rankTemppiä
            	if (enemies.get(index).rank <= 4) {
	                rankTemp = enemies.get(index).rank;
	                Log.e("testi", "rank: " + enemies.get(index).rank);
                    enemies.get(index).setStats(enemyStats[rankTemp][0], enemyStats[rankTemp][1], enemyStats[rankTemp][2],
                                                enemyStats[rankTemp][3], enemyStats[rankTemp][4], rankTemp + 1);
            	}
            }
        }
        
        /* Aktivoidaan viholliset */
        int temp;
        int tempRandA, tempRandB;
        
        for (int index = 0; index < AMOUNT_OF_ENEMIES_PER_WAVE; ++index) {
        	if (waves[currentWave][index] != -1) {
	        	temp = waves[currentWave][index];
	        	
	        	tempRandA = Utility.getRandom(1, 8);
	        	tempRandB = Utility.getRandom(0, 2);
	        	
	        	enemies.get(temp).setActive();
	        	enemies.get(temp).x = spawnPoints[tempRandA][tempRandB][0];
	            enemies.get(temp).y = spawnPoints[tempRandA][tempRandB][1];
	            
	            // Eliminoidaan samasta spawnpontista spawnaaminen
	            for(int i = 0; i <= enemies.size()-1; ++i) {
	            	if(enemies.get(temp).x == enemies.get(i).x && enemies.get(temp).y == enemies.get(i).y) {
	            		
	            		tempRandA = Utility.getRandom(1, 8);
	    	        	tempRandB = Utility.getRandom(0, 2);
	    	        	
	            		enemies.get(temp).x = spawnPoints[tempRandA][tempRandB][0];
	    	            enemies.get(temp).y = spawnPoints[tempRandA][tempRandB][1];
	            	}
	            }
	            
	        	++enemiesLeft;
        	}
        }
        
        ++currentWave;
    }
    

    /**
     * Luodaan pelikartta ja sen kerättävät esineet, muut peliobjektit sekä vihollisten spawnpointit.
     */
    protected void generateMap()
    {
    	// Päivitetään ns obstacle-objektit
        generateObstacles();
    	
        // Päivitetään aloituspisteet
        generateSpawnPoints();
        
        // Päivitetään kerättävät esineet
        generateCollectables();
    }
    
    /**
     * Päivittää obstacle-objektit
     */
    protected void generateObstacles()
    {
    	// Luodaan kentän asteroidit
    	asteroids[0] = new Obstacle(1, -400, -400, randSpeed, randDirection);
    	asteroids[1] = new Obstacle(1, 800, 800, randSpeed, randDirection);
    	asteroids[2] = new Obstacle(1, -1200, 400, randSpeed, randDirection);
    		
    	// Luodaan kentän planeetat
		planets[0] = new Obstacle(0, 0, -600, 0, 0);
		planets[1] = new Obstacle(0, 800, 0, 0, 0);
    }
    
    /**
     * Päivittää vihollisten aloituspisteet kameran koordinaattien perusteella.
     */
    protected void generateSpawnPoints()
    {
	    /* 
	     * Tallennetaan reunojen koordinaatit taulukkoon kameran sijainnin muutoksen määrän mukaan (CameraManager.camX ja CameraManager.camY)
	     * { {vasen reuna X,Y}, {vasen yläreuna X,Y}, {yläreuna X,Y}, {oikea yläreuna X,Y},
	     *	 {oikea reuna X,Y}, {oikea alareuna X,Y}, {alareuna X,Y}, {vasen alareuna X,Y} }
	     * index: 	  1   0/1           2       0/1        3    0/1             4	  0/1
	     * index: 	  5   0/1           6       0/1        7    0/1             8	  0/1
	     * 
	     */
	    
	    // Vihollisten syntypisteiden koordinaatit
	    // [rykelmän järjestysnumero][spawnpointin järjestysnumero][pisteen x- ja y-koordinaatit]
	    //					 X								   				      Y
	    // Vasen reuna
	    spawnPoints[1][0][0] = (int) (-halfOfScreenWidth + camera.xTranslate); 	  spawnPoints[1][0][1] = (int) camera.yTranslate;
	    spawnPoints[1][1][0] = (int) (-halfOfScreenWidth + camera.xTranslate); 	  spawnPoints[1][1][1] = (int) (camera.yTranslate + 128 * Options.scale);
	    spawnPoints[1][2][0] = (int) (-halfOfScreenWidth + camera.xTranslate); 	  spawnPoints[1][2][1] = (int) (camera.yTranslate - 128 * Options.scale);
	    // Vasen yläkulma
	    spawnPoints[2][0][0] = (int) (-halfOfScreenWidth + camera.xTranslate); 	  spawnPoints[2][0][1] = (int) (halfOfScreenHeight + camera.yTranslate);
	    spawnPoints[2][1][0] = (int) (-halfOfScreenWidth + camera.xTranslate + 64 * Options.scale); spawnPoints[2][1][1] = (int) (halfOfScreenHeight + camera.yTranslate + 64 * Options.scale);
	    spawnPoints[2][2][0] = (int) (-halfOfScreenWidth + camera.xTranslate - 64 * Options.scale); spawnPoints[2][2][1] = (int) (halfOfScreenHeight + camera.yTranslate - 64 * Options.scale);
	    // Yläreuna
	    spawnPoints[3][0][0] = (int) camera.xTranslate; 					 	  spawnPoints[3][0][1] = (int) (halfOfScreenHeight + camera.yTranslate);
	    spawnPoints[3][1][0] = (int) (camera.xTranslate + 128 * Options.scale); 				 	  spawnPoints[3][1][1] = (int) (halfOfScreenHeight + camera.yTranslate);
	    spawnPoints[3][2][0] = (int) (camera.xTranslate - 128 * Options.scale); 				 	  spawnPoints[3][2][1] = (int) (halfOfScreenHeight + camera.yTranslate);
	    // Oikea yläkulma
	    spawnPoints[4][0][0] = (int) (halfOfScreenWidth + camera.xTranslate);  	  spawnPoints[3][0][1] = (int) (halfOfScreenHeight + camera.yTranslate);
	    spawnPoints[4][1][0] = (int) (halfOfScreenWidth + camera.xTranslate + 64 * Options.scale);  spawnPoints[3][1][1] = (int) (halfOfScreenHeight + camera.yTranslate - 64 * Options.scale);
	    spawnPoints[4][2][0] = (int) (halfOfScreenWidth + camera.xTranslate - 64 * Options.scale);  spawnPoints[3][2][1] = (int) (halfOfScreenHeight + camera.yTranslate + 64 * Options.scale);
	    // Oikea reuna
	    spawnPoints[5][0][0] = (int) (halfOfScreenWidth + camera.xTranslate);  	  spawnPoints[4][0][1] = (int) (0 + camera.yTranslate);
	    spawnPoints[5][1][0] = (int) (halfOfScreenWidth + camera.xTranslate);  	  spawnPoints[4][1][1] = (int) (0 + camera.yTranslate + 128 * Options.scale);
	    spawnPoints[5][2][0] = (int) (halfOfScreenWidth + camera.xTranslate);  	  spawnPoints[4][2][1] = (int) (0 + camera.yTranslate - 128 * Options.scale);
	    // Oikea alakulma
	    spawnPoints[6][0][0] = (int) (halfOfScreenWidth + camera.xTranslate);  	  spawnPoints[5][0][1] = (int) (-halfOfScreenHeight + camera.yTranslate);
	    spawnPoints[6][1][0] = (int) (halfOfScreenWidth + camera.xTranslate + 64 * Options.scale);  spawnPoints[5][1][1] = (int) (-halfOfScreenHeight + camera.yTranslate + 64 * Options.scale);
	    spawnPoints[6][2][0] = (int) (halfOfScreenWidth + camera.xTranslate - 64 * Options.scale);  spawnPoints[5][2][1] = (int) (-halfOfScreenHeight + camera.yTranslate - 64 * Options.scale);
	    // Alareuna
	    spawnPoints[7][0][0] = (int) camera.xTranslate; 				 		  spawnPoints[7][0][1] = (int) (-halfOfScreenHeight + camera.yTranslate);
	    spawnPoints[7][1][0] = (int) (camera.xTranslate + 128 * Options.scale);			 	 	  spawnPoints[7][1][1] = (int) (-halfOfScreenHeight + camera.yTranslate);
	    spawnPoints[7][2][0] = (int) (camera.xTranslate - 128 * Options.scale);					  spawnPoints[7][2][1] = (int) (-halfOfScreenHeight + camera.yTranslate);
	    // Vasen alareuna
	    spawnPoints[8][0][0] = (int) (-halfOfScreenWidth + camera.xTranslate);      spawnPoints[8][0][1] = (int) (-halfOfScreenHeight + camera.yTranslate);
	    spawnPoints[8][1][0] = (int) (-halfOfScreenWidth + camera.xTranslate + 64 * Options.scale); spawnPoints[8][1][1] = (int) (-halfOfScreenHeight + camera.yTranslate - 64 * Options.scale);
	    spawnPoints[8][2][0] = (int) (-halfOfScreenWidth + camera.xTranslate - 64 * Options.scale); spawnPoints[8][2][1] = (int) (-halfOfScreenHeight + camera.yTranslate + 64 * Options.scale);
	    // Random reuna
	    // spawnPoints[0][0][0] = ...
	    
	    /*
	     * Hakee, satunnoi ja asettaa vihollisten aloituspisteet
	     * 
	     * @param alignPoint - Määrittää reunan, jolle viholliset tulevat
	     * @param specificPoint - Määrittää tarkempia koordinaatteja 3:lle eri vihollisen spawnpointille
	     */
	    /*public void setSpawnPoints(int spawnPointCoords[][]) {
	        for (int alignPoint = 1; alignPoint <= 8; ++alignPoint) {
	            for (int specificPoint = 0; specificPoint < 3; ++specificPoint) {
	                spawnPoints[alignPoint][specificPoint][0] = spawnPointCoords[alignPoint][0]; // Spawnpointin tarkan sijainnin x-koordinaatti
	                spawnPoints[alignPoint][specificPoint][1] = spawnPointCoords[alignPoint][1]; // Spawnpointin tarkan sijainnin y-koordinaatti
	            }
	        }
	    }*/
	    
	    /*
	       1. [mille reunalle viholliset tulevat] <- hae tämä xmlReaderilla
	       2. [spawnpointin järjestysnumero] <- näitä aluksi 3 jokaiselle spawnpointille
	       3. [vihollisen x- ja y-sijainnit] <- esim. vasempaan reunaan esimääritettyjen x- ja y-koordinaattien lisäksi nämä
	           (leftSpawnPointX + joko -1* tai 1* spawnPoints[][][x,y] (x- ja y-arvot taulukosta)
	    */

	    // [1][0][0] = vasempaan reunaan ilmestyvän vihollisen x-koord. (ensimmäinen spawnpoint)
	    // [1][0][1] = vasempaan reunaan ilmestyvän vihollisen y-koord. (ensimmäinen spawnpoint)
	    
	    // [1][1][0] = vasempaan reunaan ilmestyvän vihollisen x-koord. (toinen spawnpoint)
	    // [1][1][1] = vasempaan reunaan ilmestyvän vihollisen y-koord. (toinen spawnpoint)
	    
	    // [4][2][0] = alareunaan ilmestyvän vihollisen x-koord. (kolmas spawnpoint)
	    // [4][2][1] = alareunaan ilmestyvän vihollisen y-koord. (kolmas spawnpoint)
	    
	    // [2][1][0] = x-koord.
	    
	    
    }

    /**
     * Päivittää kerättävät esineet
     */
    public void generateCollectables()
    {
        randX = Utility.getRandom(-400, 400);
        randY = Utility.getRandom(-240, 240);

   		collectables[0] = new Collectable(randX, randY);
    }

    /**
     * Lähettää pisteet GameActivitylle, joka siirtää pelin Highscores-valikkoon.
     */
	public void endGameMode()
	{
		gameActivity.continueToHighscores((int)score);
	}

    /**
     * Tarkastaa onko pelaaja pelialueen sisässä ja ottaa tarvittaessa autopilotin käyttöön.
     */
	public void checkBounds ()
	{
		if (player.x >= halfOfScreenWidth || player.x <= -halfOfScreenWidth ||
			player.y >= halfOfScreenHeight || player.y <= -halfOfScreenHeight	) {

			// TODO: Tähän toteutus rajojen ylittämisen ilmoittamiselle ! !

			if (player.x >= overBoundWidth || player.x <= -overBoundWidth ||
				player.y >= overBoundHeight || player.y <= -overBoundHeight	) {
					PlayerAi.activateAutoPilot();
			}
		}
		else {
			PlayerAi.deactivateAutoPilot();
		}
	}
	
	public void mirrorAsteroidPosition ()
	{
		/*for (int i = asteroids.length - 1; i >= 0; --i) {
			if (asteroids[i].x >= halfOfScreenWidth || asteroids[i].x <= -halfOfScreenWidth ||
				asteroids[i].y >= halfOfScreenHeight || asteroids[i].y <= -halfOfScreenHeight) {
				if (asteroids[i].x > overBoundWidth || asteroids[i].x < -overBoundWidth || 
					asteroids[i].y > overBoundHeight || asteroids[i].y < -overBoundHeight) {
					asteroids[i].x *= -1;
					asteroids[i].y *= -1;
				}
			}
		}*/
	}
	
	private void generateStarBackground()
    {
    	for (int i = 0; i < 15; ++i) {
    		backgroundStars[i] = new BackgroundStar(Utility.getRandom(-400, 400), Utility.getRandom(-240, 240));
    	}
	}
}