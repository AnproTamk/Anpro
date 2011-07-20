package fi.tamk.anpro;

import java.util.ArrayList;

import android.content.Context;
import android.util.DisplayMetrics;

/**
 * Survival-pelitila. Luo pelaajan ja viholliset ja hallitsee vihollisaaltojen
 * kutsumisen ja pistelaskurin p�ivitt�misen.
 */
public class GameMode
{
	/* Vakioita XML-tiedostojen lukemista ja muuttujien alustamista varten */
    public static final int AMOUNT_OF_WAVES            = 4;
    public static final int AMOUNT_OF_ENEMIES_PER_WAVE = 11;

    /* Pelaaja, emoalus ja liittolaiset */
    private Player     player;
    @SuppressWarnings("unused")
    private Mothership mothership;
    private Ally	   turret1;
    private Ally	   turret2;
    private Ally	   turret3;
    
    /* T�htitausta */
    private BackgroundStar[] backgroundStars;

    /* Asteroidit */
    private Obstacle[] asteroids; // Asteroidit
    private Obstacle[] planets; // Planeetat
    
    /* Ker�tt�v�t esineet */
    private Collectable[] collectables; // Ker�tt�v�t esineet
    
    /* Viholliset */
    public           ArrayList<Enemy> enemies;         // Viholliset
    protected        int[][]          enemyStats;      // Vihollistyyppien statsit ([rank][attribuutti] = [arvo])
    public           int              waves[][];       // [aalto][vihollisen j�rjestysnumero] = [vihollisen indeksi enemies-taulukossa]
    private   static int              currentWave = 0;
    private   static int			  totalWaves  = 0; // T�m�nhetkisen aallon j�rjestysnumero
    												   // (k�ytet��n pisteiden laskemiseen)
    public    static int              enemiesLeft = 0; // Vihollisia j�ljell� kent�ll�
    
    /* Ruudun koko ja kent�n rajat */
    protected int halfOfScreenWidth;
    protected int halfOfScreenHeight;
    
    protected static int mapWidth;
    protected static int mapHeight;
    
    protected int overBoundWidth;
    protected int overBoundHeight; 
    
    /* Muiden olioiden pointterit */
    protected        WeaponManager weaponManager;
    protected        CameraManager camera;
    protected        GameActivity  gameActivity;
    private   static Hud		   hud;
    
    /* Pisteet ja combot */
    private static long score;
    private static int  comboMultiplier = 2; // Combokerroin pisteiden laskemista varten
    private static long lastTime        = 0; // Edellisen pisteen lis�yksen aika
    private static long newTime;             // Uuden pisteen lis�yksen aika
    
    /* Vihollisten aloituspaikat */
    private int spawnPoints[][][]; // [rykelm�][paikka][x/y] = [koordinaatti]
    
    /**
     * Alustaa luokan muuttujat, lukee pelitilan tarvitsemat tiedot ja k�ynnist�� pelin.
     * 
     * @param GameActivity   Pelitilan aloittava aktiviteetti
     * @param DisplayMetrics N�yt�n tiedot
     * @param Context		 Ohjelman konteksti
     * @param WeaponManager  Osoitin WeaponManageriin
     */
    public GameMode(GameActivity _gameActivity, DisplayMetrics _dm, Context _context, Hud _hud, WeaponManager _weaponManager)
    {
    	// Tallennetaan osoitin peliaktiviteettiin ja hudiin
        gameActivity = _gameActivity;
        hud          = _hud;
        
        // Tallennetaan n�yt�n tiedot
        halfOfScreenWidth  = _dm.widthPixels;
        halfOfScreenHeight = _dm.heightPixels;
        
        mapWidth  = 1750;
        mapHeight = 1250;
        
        overBoundWidth  = mapWidth + 20;
        overBoundHeight = mapHeight + 20;
        
        // Alustetaan taulukot
        enemies         = new ArrayList<Enemy>();
        enemyStats      = new int[5][5];
        asteroids       = new Obstacle[3];
        planets         = new Obstacle[3];
        collectables    = new Collectable[3];
        backgroundStars = new BackgroundStar[15];
        
    	// Luodaan pelaaja
    	player = new Player(40, 15, this, hud);
    	player.x = 0;
    	player.y = 0;
    	
    	// Luodaan emoalus ja sen tykit
    	mothership = new Mothership(0);
		mothership.direction = 160;
		mothership.x         = 100 * Options.scaleX;
		mothership.y         = 90 * Options.scaleY;
    	turret1    = new Ally(1000, 0, 0, 0, AbstractAi.TURRET_AI, Ally.ALLY_TURRET, _weaponManager);
    	turret1.x  = 135 * Options.scaleX;
    	turret1.y  = 8 * Options.scaleY;
    	turret2    = new Ally(1000, 0, 0, 0, AbstractAi.TURRET_AI, Ally.ALLY_TURRET, _weaponManager);
    	turret2.x  = 280 * Options.scaleX;
    	turret2.y  = -45 * Options.scaleY;
    	turret3    = new Ally(1000, 0, 0, 0, AbstractAi.TURRET_AI, Ally.ALLY_TURRET, _weaponManager);
    	turret3.x  = 332 * Options.scaleX;
    	turret3.y  = 84 * Options.scaleY;
        
        // Luetaan vihollistyyppien tiedot
        XmlReader reader = new XmlReader(_context);
        int[] enemyStatsTemp = reader.readEnemyRanks();
        int rank = 0;
        for (int i = 0; i < enemyStatsTemp.length; ++i) {
        	rank = (int)(i / 5);
        	
        	enemyStats[rank][i-rank*5] = enemyStatsTemp[i];
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

        // Luodaan "pelikentt�" (objektit ja vihollisten spawnpointit)
        generateMap();
        
        // K�ynnistet��n ensimm�inen vihollisaalto
        startWave();
    }
    
    /**
     * P�ivitt�� pisteet.
     * 
     * @param _rank Tuhotun vihollisen taso, jonka perusteella pisteit� lis�t��n
     * 				(my�s Collectable m��ritt�� siit� saatavan pistem��r�n rankin
     * 				mukaisesti)
     * @param _x    X-koordinaatti
     * @param _y    Y-koordinaatti
     */
    public static void updateScore(int _rank, float _x, float _y)
    {
        // P�ivitet��n lastTime nykyisell� ajalla millisekunteina
        if (lastTime == 0) {
            lastTime = android.os.SystemClock.uptimeMillis();
            score += (10 * _rank + 5 * _rank * totalWaves);
        }
        else {
            newTime = android.os.SystemClock.uptimeMillis();
            
            // Verrataan aikoja kesken��n ja annetaan pisteit� sen mukaisesti
            if (newTime-lastTime <= 700) {
                ++comboMultiplier;
            	score += (10 * _rank + 5 * _rank * totalWaves) * comboMultiplier;
            	
            	EffectManager.showComboMultiplier(comboMultiplier, _x ,_y);
            }
            // Jos pelaaja ei saa comboa resetoidaan comboMultiplier
            else {
                score += (10 * _rank + 5 * _rank * totalWaves);
                comboMultiplier = 1;
                lastTime = android.os.SystemClock.uptimeMillis();
            }
        }
        
        hud.updateScoreCounter(score);
    }
    
    /**
     * K�ynnist�� uuden vihollisaallon asettamalla siihen kuuluvat viholliset aktiivisiksi.
     */
    public void startWave()
    {
        /* Tarkastetaan onko kaikki vihollisaallot k�yty l�pi */
        if (currentWave == AMOUNT_OF_WAVES) { // TODO: TARKISTA MITEN MULTIDIMENSIONAL ARRAYN LENGTH TOIMII! (halutaan tiet�� wavejen m��r�)
            currentWave = 0;
            
            // Tarkistetaan vihollisen luokka, kasvatetaan sit� yhdell� ja l�hetet��n sille uudet statsit
            int rankTemp;
            
            for (int index = enemies.size()-1; index >= 0; --index) {
                // Lasketaan uusi rank, k�ytet��n v�liaikaismuuttujana rankTemppi�
            	if (enemies.get(index).rank <= 4) {
	                rankTemp = enemies.get(index).rank;
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
        ++totalWaves;
    }

    /**
     * Luodaan pelikartta ja sen ker�tt�v�t esineet, muut peliobjektit sek� vihollisten spawnpointit.
     */
    protected void generateMap()
    {
    	// P�ivitet��n ns obstacle-objektit
        generateObstacles();
    	
        // P�ivitet��n aloituspisteet
        generateSpawnPoints();
        
        // P�ivitet��n ker�tt�v�t esineet
        generateCollectables();
    }
    
    /**
     * P�ivitt�� obstacle-objektit
     */
    protected void generateObstacles()
    {
    	// Luodaan kent�n asteroidit
        int randDirection = Utility.getRandom(0, 359);
        
    	asteroids[0] = new Obstacle(Obstacle.OBSTACLE_ASTEROID, 0, -400, -400, 2, 120);
    	asteroids[1] = new Obstacle(Obstacle.OBSTACLE_ASTEROID, 0, 400, 400, 2, randDirection);
    	asteroids[2] = new Obstacle(Obstacle.OBSTACLE_ASTEROID, 0, 0, -400, 2, 240);
    		
    	// Luodaan kent�n planeetat
		planets[0] = new Obstacle(Obstacle.OBSTACLE_PLANET, Obstacle.PLANET_EARTH, 0, -600, 0, 0);
		planets[1] = new Obstacle(Obstacle.OBSTACLE_PLANET, Obstacle.PLANET_X, 800, 0, 0, 0);
		planets[2] = new Obstacle(Obstacle.OBSTACLE_STAR, 0, 1200, 1200, 0, 0);
    }
    
    /**
     * P�ivitt�� vihollisten aloituspisteet kameran koordinaattien perusteella.
     */
    protected void generateSpawnPoints()
    {
	    /* 
	     * Tallennetaan reunojen koordinaatit taulukkoon kameran sijainnin muutoksen m��r�n mukaan (CameraManager.camX ja CameraManager.camY)
	     * { {vasen reuna X,Y}, {vasen yl�reuna X,Y}, {yl�reuna X,Y}, {oikea yl�reuna X,Y},
	     *	 {oikea reuna X,Y}, {oikea alareuna X,Y}, {alareuna X,Y}, {vasen alareuna X,Y} }
	     * index: 	  1   0/1           2       0/1        3    0/1             4	  0/1
	     * index: 	  5   0/1           6       0/1        7    0/1             8	  0/1
	     * 
	     */
    	
    	// Satunnaisten spawnpointtien alustus
	    for(int i = 1; i < 8; ++i) {
	    	for(int j = 0; j < 2; ++j) {
	    			spawnPoints[i][j][0] = (int) (Utility.getRandom(-3000, 3000));
	    			spawnPoints[i][j][1] = (int) (Utility.getRandom(-3000, 3000));
	    	}
	    }
	    
	    //****HUOM!**** LUKITTUJEN SPAWNPOINTTIEN ALUSTUS
	    
	    // Vihollisten syntypisteiden koordinaatit
	    // [rykelm�n j�rjestysnumero][spawnpointin j�rjestysnumero][pisteen x- ja y-koordinaatit]
	    //					 X								   				      Y
	    // Vasen reuna
    	/*
    	spawnPoints[1][0][0] = (int) (-halfOfScreenWidth + camera.xTranslate); 	  						spawnPoints[1][0][1] = (int) camera.yTranslate;
	    spawnPoints[1][1][0] = (int) (-halfOfScreenWidth + camera.xTranslate); 	  						spawnPoints[1][1][1] = (int) (camera.yTranslate + 128 * Options.scale);
	    spawnPoints[1][2][0] = (int) (-halfOfScreenWidth + camera.xTranslate); 	  						spawnPoints[1][2][1] = (int) (camera.yTranslate - 128 * Options.scale);
	    // Vasen yl�kulma
	    spawnPoints[2][0][0] = (int) (-halfOfScreenWidth + camera.xTranslate); 	  						spawnPoints[2][0][1] = (int) (halfOfScreenHeight + camera.yTranslate);
	    spawnPoints[2][1][0] = (int) (-halfOfScreenWidth + camera.xTranslate + 64 * Options.scale); 	spawnPoints[2][1][1] = (int) (halfOfScreenHeight + camera.yTranslate + 64 * Options.scale);
	    spawnPoints[2][2][0] = (int) (-halfOfScreenWidth + camera.xTranslate - 64 * Options.scale); 	spawnPoints[2][2][1] = (int) (halfOfScreenHeight + camera.yTranslate - 64 * Options.scale);
	    // Yl�reuna
	    spawnPoints[3][0][0] = (int) camera.xTranslate; 					 	  						spawnPoints[3][0][1] = (int) (halfOfScreenHeight + camera.yTranslate);
	    spawnPoints[3][1][0] = (int) (camera.xTranslate + 128 * Options.scale); 						spawnPoints[3][1][1] = (int) (halfOfScreenHeight + camera.yTranslate);
	    spawnPoints[3][2][0] = (int) (camera.xTranslate - 128 * Options.scale); 						spawnPoints[3][2][1] = (int) (halfOfScreenHeight + camera.yTranslate);
	    // Oikea yl�kulma
	    spawnPoints[4][0][0] = (int) (halfOfScreenWidth + camera.xTranslate);  	  						spawnPoints[3][0][1] = (int) (halfOfScreenHeight + camera.yTranslate);
	    spawnPoints[4][1][0] = (int) (halfOfScreenWidth + camera.xTranslate + 64 * Options.scale);  	spawnPoints[3][1][1] = (int) (halfOfScreenHeight + camera.yTranslate - 64 * Options.scale);
	    spawnPoints[4][2][0] = (int) (halfOfScreenWidth + camera.xTranslate - 64 * Options.scale);  	spawnPoints[3][2][1] = (int) (halfOfScreenHeight + camera.yTranslate + 64 * Options.scale);
	    // Oikea reuna
	    spawnPoints[5][0][0] = (int) (halfOfScreenWidth + camera.xTranslate);  	  						spawnPoints[4][0][1] = (int) camera.yTranslate;
	    spawnPoints[5][1][0] = (int) (halfOfScreenWidth + camera.xTranslate);  	  						spawnPoints[4][1][1] = (int) (camera.yTranslate + 128 * Options.scale);
	    spawnPoints[5][2][0] = (int) (halfOfScreenWidth + camera.xTranslate);  	  						spawnPoints[4][2][1] = (int) (camera.yTranslate - 128 * Options.scale);
	    // Oikea alakulma
	    spawnPoints[6][0][0] = (int) (halfOfScreenWidth + camera.xTranslate);  	  						spawnPoints[5][0][1] = (int) (-halfOfScreenHeight + camera.yTranslate);
	    spawnPoints[6][1][0] = (int) (halfOfScreenWidth + camera.xTranslate + 64 * Options.scale);  	spawnPoints[5][1][1] = (int) (-halfOfScreenHeight + camera.yTranslate + 64 * Options.scale);
	    spawnPoints[6][2][0] = (int) (halfOfScreenWidth + camera.xTranslate - 64 * Options.scale);  	spawnPoints[5][2][1] = (int) (-halfOfScreenHeight + camera.yTranslate - 64 * Options.scale);
	    // Alareuna
	    spawnPoints[7][0][0] = (int) (camera.xTranslate); 				 		  						spawnPoints[7][0][1] = (int) (-halfOfScreenHeight + camera.yTranslate);
	    spawnPoints[7][1][0] = (int) (camera.xTranslate + 128 * Options.scale);			 	 	  		spawnPoints[7][1][1] = (int) (-halfOfScreenHeight + camera.yTranslate);
	    spawnPoints[7][2][0] = (int) (camera.xTranslate - 128 * Options.scale);					  		spawnPoints[7][2][1] = (int) (-halfOfScreenHeight + camera.yTranslate);
	    // Vasen alareuna
	    spawnPoints[8][0][0] = (int) (-halfOfScreenWidth + camera.xTranslate);      					spawnPoints[8][0][1] = (int) (-halfOfScreenHeight + camera.yTranslate);
	    spawnPoints[8][1][0] = (int) (-halfOfScreenWidth + camera.xTranslate + 64 * Options.scale); 	spawnPoints[8][1][1] = (int) (-halfOfScreenHeight + camera.yTranslate - 64 * Options.scale);
	    spawnPoints[8][2][0] = (int) (-halfOfScreenWidth + camera.xTranslate - 64 * Options.scale); 	spawnPoints[8][2][1] = (int) (-halfOfScreenHeight + camera.yTranslate + 64 * Options.scale);
	    */
	    // Random reuna
	    // spawnPoints[0][0][0] = ...
	    /*
	     * Hakee, satunnoi ja asettaa vihollisten aloituspisteet
	     * 
	     * @param alignPoint - M��ritt�� reunan, jolle viholliset tulevat
	     * @param specificPoint - M��ritt�� tarkempia koordinaatteja 3:lle eri vihollisen spawnpointille
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
	       1. [mille reunalle viholliset tulevat] <- hae t�m� xmlReaderilla
	       2. [spawnpointin j�rjestysnumero] <- n�it� aluksi 3 jokaiselle spawnpointille
	       3. [vihollisen x- ja y-sijainnit] <- esim. vasempaan reunaan esim��ritettyjen x- ja y-koordinaattien lis�ksi n�m�
	           (leftSpawnPointX + joko -1* tai 1* spawnPoints[][][x,y] (x- ja y-arvot taulukosta)
	    */

	    // [1][0][0] = vasempaan reunaan ilmestyv�n vihollisen x-koord. (ensimm�inen spawnpoint)
	    // [1][0][1] = vasempaan reunaan ilmestyv�n vihollisen y-koord. (ensimm�inen spawnpoint)
	    
	    // [1][1][0] = vasempaan reunaan ilmestyv�n vihollisen x-koord. (toinen spawnpoint)
	    // [1][1][1] = vasempaan reunaan ilmestyv�n vihollisen y-koord. (toinen spawnpoint)
	    
	    // [4][2][0] = alareunaan ilmestyv�n vihollisen x-koord. (kolmas spawnpoint)
	    // [4][2][1] = alareunaan ilmestyv�n vihollisen y-koord. (kolmas spawnpoint)
	    
	    // [2][1][0] = x-koord.
	    
	    
    }

    /**
     * P�ivitt�� ker�tt�v�t esineet
     */
    public void generateCollectables()
    {
   		collectables[0] = new Collectable(0, 0);
   		collectables[0].setActive();
   		
   		collectables[1] = new Collectable(0, 0);
   		collectables[1].setActive();
   		
   		collectables[2] = new Collectable(0, 0);
   		collectables[2].setActive();
    }

    /**
     * L�hett�� pisteet pelaajan kuoltua GameActivitylle, joka siirt�� pelin Highscores-valikkoon.
     */
	public void endGameMode()
	{
		gameActivity.continueToHighscores((int)score);
	}

    /**
     * Tarkastaa onko pelaaja pelialueen sis�ss� ja ottaa tarvittaessa autopilotin k�ytt��n.
     */
	public void checkBounds ()
	{
		if (player.x >= halfOfScreenWidth || player.x <= -halfOfScreenWidth ||
			player.y >= halfOfScreenHeight || player.y <= -halfOfScreenHeight	) {

			// TODO: T�h�n toteutus rajojen ylitt�misen ilmoittamiselle ! !

			if (player.x >= overBoundWidth || player.x <= -overBoundWidth ||
				player.y >= overBoundHeight || player.y <= -overBoundHeight	) {
					PlayerAi.activateAutoPilot();
			}
		}
		else {
			PlayerAi.deactivateAutoPilot();
		}
	}

	/**
	 * "Looppaa" asteroidit pelikent�n toiselle laidalle, kun ne ylitt�v�t alueen rajan.
	 */
	public void mirrorAsteroidPosition ()
	{
		for (int i = asteroids.length - 1; i >= 0; --i) {
			if ( asteroids[i].x < -overBoundWidth || asteroids[i].y < -overBoundHeight) {
				asteroids[i].x = asteroids[i].x * (-1) - 100 ;
				asteroids[i].y = asteroids[i].y * (-1) - 100 ;
			}
			else if (asteroids[i].x > overBoundWidth || asteroids[i].y > overBoundHeight) {
				asteroids[i].x = asteroids[i].x * (-1) + 100 ;
				asteroids[i].y = asteroids[i].y * (-1) + 100 ;
			}
		}
	}

	/**
	 * Avaa "emoalusmenun", jossa p�ivitet��n skillej�, tallennetaan pelitilanne
	 * sek� korjataan pelaajan alusta/emoalusta.
	 * 
	 * @param MAHDOLLISET PARAMETRIT TULEE MY�HEMMIN!
	 */
	public void moveToMothershipMenu()
	{
		gameActivity.continueToMothership();
	}
}