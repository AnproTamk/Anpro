package fi.tamk.anpro;

import java.util.ArrayList;

import android.content.Context;
import android.util.DisplayMetrics;
import android.util.Log;

/**
 * Survival-pelitila. Luo pelaajan ja viholliset ja hallitsee vihollisaaltojen
 * kutsumisen ja pistelaskurin p‰ivitt‰misen.
 */
public class GameMode
{
	/* Vakioita XML-tiedostojen lukemista ja muuttujien alustamista varten */
    public static final int AMOUNT_OF_WAVES            = 4;
    public static final int AMOUNT_OF_ENEMIES_PER_WAVE = 11;

    /* Pelaaja, emoalus ja liittolaiset */
    private Player     player;
    private Mothership mothership;
    private Ally	   turret1;
    private Ally	   turret2;
    private Ally	   turret3;

    /* Asteroidit */
    private Obstacle[] asteroids; // Asteroidit
    private Obstacle[] planets; // Planeetat
    
    /* Ker‰tt‰v‰t esineet */
    private Collectable[] collectables; // Ker‰tt‰v‰t esineet
    
    /* Viholliset */
    public           ArrayList<Enemy> enemies;         // Viholliset
    protected        int[][]          enemyStats;      // Vihollistyyppien statsit ([rank][attribuutti] = [arvo])
    public           int              waves[][];       // [aalto][vihollisen j‰rjestysnumero] = [vihollisen indeksi enemies-taulukossa]
    private   static int              currentWave = 0;
    private   static int			  totalWaves  = 0; // T‰m‰nhetkisen aallon j‰rjestysnumero
    												   // (k‰ytet‰‰n pisteiden laskemiseen)
    public    static int              enemiesLeft = 0; // Vihollisia j‰ljell‰ kent‰ll‰
    
    /* Ruudun koko ja kent‰n rajat */
    protected int halfOfScreenWidth;
    protected int halfOfScreenHeight;
    
    protected static int mapWidth;
    protected static int mapHeight;
    
    protected static int overBoundWidth;
    protected static int overBoundHeight; 
    
    /* Muiden olioiden pointterit */
    protected        WeaponManager weaponManager;
    protected        CameraManager camera;
    protected        GameActivity  gameActivity;
    private   static Hud		   hud;
    private          Wrapper       wrapper;
    
    /* Pisteet ja combot */
    private static long score;
    private static int  comboMultiplier = 2; // Combokerroin pisteiden laskemista varten
    private static long lastTime        = 0; // Edellisen pisteen lis‰yksen aika
    private static long newTime;             // Uuden pisteen lis‰yksen aika
    
    /* Vihollisten aloituspaikat */
    private int spawnPoints[][][]; // [rykelm‰][paikka][x/y] = [koordinaatti]
    
    /**
     * Alustaa luokan muuttujat, lukee pelitilan tarvitsemat tiedot ja k‰ynnist‰‰ pelin.
     * 
     * @param GameActivity   Pelitilan aloittava aktiviteetti
     * @param DisplayMetrics N‰ytˆn tiedot
     * @param Context		 Ohjelman konteksti
     * @param WeaponManager  Osoitin WeaponManageriin
     */
    public GameMode(GameActivity _gameActivity, DisplayMetrics _dm, Context _context, Hud _hud, WeaponManager _weaponManager)
    {
    	/* Tallennetaan muuttujat */
        gameActivity       = _gameActivity;
        hud                = _hud;
        weaponManager      = _weaponManager;
        halfOfScreenWidth  = _dm.widthPixels / 2;
        halfOfScreenHeight = _dm.heightPixels / 2;
        
        /* Otetaan tarvittavat luokat k‰yttˆˆn */
        wrapper = Wrapper.getInstance();
        
        /* Alustetaan muuttujat */
        // M‰‰ritet‰‰n kartan leveys
        mapWidth  = 1200;
        mapHeight = 800;
        
        // M‰‰ritet‰‰n reuna-alue (alue, jossa autopilot aktivoituu)
        overBoundWidth  = mapWidth + 700;
        overBoundHeight = mapHeight + 700;
        
        // Alustetaan taulukot
        enemies         = new ArrayList<Enemy>();
        enemyStats      = new int[5][5];
        asteroids       = new Obstacle[3];
        planets         = new Obstacle[2];
        collectables    = new Collectable[3];
        
        waves       = new int[AMOUNT_OF_WAVES][AMOUNT_OF_ENEMIES_PER_WAVE];
        spawnPoints = new int[9][3][2];
        
        // Luetaan vihollistyyppien tiedot
        XmlReader reader = new XmlReader(_context);
        int[] enemyStatsTemp = reader.readEnemyRanks();
        int rank = 0;
        for (int i = 0; i < enemyStatsTemp.length; ++i) {
        	rank = (int)(i / 5);
        	
        	enemyStats[rank][i-rank*5] = enemyStatsTemp[i];
        }
    	
    	// M‰‰ritet‰‰n vihollisaallot
        for (int j = 0; j < AMOUNT_OF_WAVES; ++j) {
        	for (int i = 0; i < AMOUNT_OF_ENEMIES_PER_WAVE; ++i) {
        		waves[j][i] = -1;
        	}
        }
        
    	/* Luodaan tarvittavat objektit */
        // Luodaan pelaaja
    	player = new Player(40, 15, this, hud);
    	player.x = 0;
    	player.y = 0;
    	
    	// Luodaan emoalus
    	mothership           = new Mothership(0);
		mothership.direction = 160;
		mothership.x         = 100 * Options.scaleX;
		mothership.y         = 90 * Options.scaleY;
		
		// Luodaan emoaluksen tykit
    	turret1    = new Ally(1000, 0, 0, 0, AbstractAi.TURRET_AI, Ally.ALLY_TURRET, _weaponManager);
    	turret1.x  = 135 * Options.scaleX; turret1.y  = 8 * Options.scaleY;
    	turret1.state = Wrapper.FULL_ACTIVITY;
    	
    	turret2    = new Ally(1000, 0, 0, 0, AbstractAi.TURRET_AI, Ally.ALLY_TURRET, _weaponManager);
    	turret2.x  = 280 * Options.scaleX; turret2.y  = -45 * Options.scaleY;
    	turret2.state = Wrapper.FULL_ACTIVITY;
    	
    	turret3    = new Ally(1000, 0, 0, 0, AbstractAi.TURRET_AI, Ally.ALLY_TURRET, _weaponManager);
    	turret3.x  = 332 * Options.scaleX; turret3.y  = 84 * Options.scaleY;
    	turret3.state = Wrapper.FULL_ACTIVITY;
        
        // Luetaan pelitilan tiedot
        reader.readGameMode(this, _weaponManager);

        // Luodaan "pelikentt‰" (objektit ja vihollisten spawnpointit)
        generateMap();
        
        // K‰ynnistet‰‰n ensimm‰inen vihollisaalto
        startWave();
    }
    
    /* =======================================================
     * Uudet funktiot
     * ======================================================= */
    /**
     * P‰ivitt‰‰ pisteet.
     * 
     * @param _rank Tuhotun vihollisen taso, jonka perusteella pisteit‰ lis‰t‰‰n
     * 				(myˆs Collectable m‰‰ritt‰‰ siit‰ saatavan pistem‰‰r‰n rankin
     * 				mukaisesti)
     * @param _x    X-koordinaatti
     * @param _y    Y-koordinaatti
     */
    public static void updateScore(int _rank, float _x, float _y)
    {
        // P‰ivitet‰‰n lastTime nykyisell‰ ajalla millisekunteina
        if (lastTime == 0) {
            lastTime = android.os.SystemClock.uptimeMillis();
            score += (10 * _rank + 5 * _rank * totalWaves);
        }
        else {
            newTime = android.os.SystemClock.uptimeMillis();
            
            // Verrataan aikoja kesken‰‰n ja annetaan pisteit‰ sen mukaisesti
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
     * K‰ynnist‰‰ uuden vihollisaallon asettamalla siihen kuuluvat viholliset aktiivisiksi.
     */
    public void startWave()
    {
    	if ((wrapper.player.x < -(Options.scaledScreenWidth / 2) || wrapper.player.x > (Options.scaledScreenWidth / 2)) &&
    		(wrapper.player.y < -(Options.scaledScreenHeight / 2) || wrapper.player.y > (Options.scaledScreenHeight / 2))) {
    		
	        /* Tarkastetaan onko kaikki vihollisaallot k‰yty l‰pi */
	        if (currentWave == AMOUNT_OF_WAVES) { // TODO: TARKISTA MITEN MULTIDIMENSIONAL ARRAYN LENGTH TOIMII! (halutaan tiet‰‰ wavejen m‰‰r‰)
	            currentWave = 0;
	            
	            // Tarkistetaan vihollisen luokka, kasvatetaan sit‰ yhdell‰ ja l‰hetet‰‰n sille uudet statsit
	            int rankTemp;
	            
	            for (int index = enemies.size()-1; index >= 0; --index) {
	                // Lasketaan uusi rank, k‰ytet‰‰n v‰liaikaismuuttujana rankTemppi‰
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
    }

    /**
     * Luodaan pelikartta ja sen ker‰tt‰v‰t esineet, muut peliobjektit sek‰ vihollisten spawnpointit.
     */
    protected void generateMap()
    {
    	// P‰ivitet‰‰n ns obstacle-objektit
        generateObstacles();
    	
        // P‰ivitet‰‰n aloituspisteet
        generateSpawnPoints();
        
        // P‰ivitet‰‰n ker‰tt‰v‰t esineet
        generateCollectables();
    }
    
    /**
     * P‰ivitt‰‰ obstacle-objektit
     */
    protected void generateObstacles()
    {
        int tempDirection;
        int tempX;
        int tempY;
        
        // Luodaan asteroidit
        for (int i = 0; i < 3; ++i) {
        	tempDirection = Utility.getRandom(0, 359);
            tempX         = Utility.getRandom(-mapWidth, mapWidth);
            tempY         = Utility.getRandom(-mapHeight, mapHeight);
			
			if (tempX > -Options.scaledScreenWidth * 2 && tempX < Options.scaledScreenWidth * 2 &&
				tempY > -Options.scaledScreenHeight * 2 && tempY < Options.scaledScreenHeight * 2) {
	    			--i;
			}
			else {
				asteroids[i] = new Obstacle(Obstacle.OBSTACLE_ASTEROID, 0, tempX, tempY, 2, tempDirection);
			}
        }
        
        // Luodaan planeetat
        for (int i = 0; i < 2; ++i) {
        	tempDirection = Utility.getRandom(0, 359);
            tempX         = Utility.getRandom(-mapWidth, mapWidth);
            tempY         = Utility.getRandom(-mapHeight, mapHeight);
			
			if (tempX > -Options.scaledScreenWidth * 2 && tempX < Options.scaledScreenWidth * 2 &&
				tempY > -Options.scaledScreenHeight * 2 && tempY < Options.scaledScreenHeight * 2) {
	    			--i;
			}
			else {
				// Planeetan erityistyypiksi m‰‰ritet‰‰n i, jolloin se saa arvoksi 0 tai 1.
				// Vastaavasti planeettojen erityistyypit m‰‰ritt‰v‰t vakiot sis‰lt‰v‰t arvot 0 tai 1.
				planets[i] = new Obstacle(Obstacle.OBSTACLE_PLANET, i, tempX, tempY, 0, tempDirection);
			}
        }
    }
    
    /**
     * P‰ivitt‰‰ vihollisten aloituspisteet kameran koordinaattien perusteella.
     */
    protected void generateSpawnPoints()
    {
    	// Satunnaisten spawnpointtien alustus
	    for(int i = 1; i < 8; ++i) {
	    	for(int j = 0; j < 2; ++j) {
	    			spawnPoints[i][j][0] = (int) (Utility.getRandom(-mapWidth, mapWidth));
	    			spawnPoints[i][j][1] = (int) (Utility.getRandom(-mapHeight, mapHeight));
	    			
	    			if (spawnPoints[i][j][0] > -Options.scaledScreenWidth * 2 &&
	    				spawnPoints[i][j][0] < Options.scaledScreenWidth * 2) {
	    				if (spawnPoints[i][j][1] > -Options.scaledScreenHeight * 2 &&
	    	    			spawnPoints[i][j][1] < Options.scaledScreenHeight * 2) {
	    	    			--j;
	    	    		}
	    			}
	    	}
	    }
    }

    /**
     * P‰ivitt‰‰ ker‰tt‰v‰t esineet
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
     * L‰hett‰‰ pisteet pelaajan kuoltua GameActivitylle, joka siirt‰‰ pelin Highscores-valikkoon.
     */
	public void endGameMode()
	{
		gameActivity.continueToHighscores((int)score);
	}

    /**
     * Tarkastaa onko pelaaja pelialueen sis‰ss‰ ja ottaa tarvittaessa autopilotin k‰yttˆˆn.
     */
	public void checkBounds ()
	{
		if (player.x >= mapWidth  || player.x <= -mapWidth || player.y >= mapHeight || player.y <= -mapHeight) {
			
			if (player.x >= overBoundWidth || player.x <= -overBoundWidth ||
				player.y >= overBoundHeight || player.y <= -overBoundHeight	) {
				if (!((PlayerAi)player.ai).autoPilotActivated) {
					((PlayerAi)player.ai).activateAutoPilot();
				}
			}
			else {
				if (((PlayerAi)player.ai).autoPilotActivated) {
					((PlayerAi)player.ai).deactivateAutoPilot();
				}
				else {
					MessageManager.showOutOfBoundsMessage();
				}
			}
		}
	}

	/**
	 * "Looppaa" asteroidit pelikent‰n toiselle laidalle, kun ne ylitt‰v‰t alueen rajan.
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
	 * Avaa "emoalusmenun", jossa p‰ivitet‰‰n skillej‰, tallennetaan pelitilanne
	 * sek‰ korjataan pelaajan alusta/emoalusta.
	 * 
	 * @param MAHDOLLISET PARAMETRIT TULEE MY÷HEMMIN!
	 */
	public void moveToMothershipMenu()
	{
		gameActivity.continueToMothership();
	}
}