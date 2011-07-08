package fi.tamk.anpro;

import java.util.ArrayList;
import android.content.Context;
import android.util.DisplayMetrics;

/**
 * Survival-pelitila. Luo pelaajan ja viholliset ja hallitsee vihollisaaltojen
 * kutsumisen ja pistelaskurin päivittämisen.
 */
public class SurvivalMode extends AbstractMode
{
	/* Vakioita XML-tiedostojen lukemista ja muuttujien alustamista varten */
    public static final int AMOUNT_OF_WAVES            = 4;
    public static final int AMOUNT_OF_ENEMIES_PER_WAVE = 11;
    
    /* Vihollisaallot */
    public         int waves[][];       // [aalto][vihollisen järjestysnumero] = [vihollisen indeksi enemies-taulukossa]
    private static int currentWave = 0;
    public  static int enemiesLeft = 0; // Vihollisiä jäljellä kentällä
    
    /* Pisteet ja combot */
    private static long score;
    private static int  comboMultiplier = 2; // Combokerroin pisteiden laskemista varten
    private static long lastTime        = 0; // Edellisen pisteen lisäyksen aika
    private static long newTime;             // Uuden pisteen lisäyksen aika
    
    /* Vihollisten aloituspaikat */
    private int spawnPoints[][][]; // [rykelmä][paikka][x/y] = [koordinaatti]
    
    /**
     * Alustaa luokan muuttujat, lukee pelitilan tarvitsemat tiedot ja käynnistää pelin.
     * 
     * @param GameActivity   Pelitilan aloittava aktiviteetti
     * @param DisplayMetrics Näytön tiedot
     * @param Context		 Ohjelman konteksti
     * @param WeaponManager  Osoitin WeaponManageriin
     */
    public SurvivalMode(GameActivity _gameActivity, DisplayMetrics _dm, Context _context, WeaponManager _weaponManager)
    {
    	super(_gameActivity, _dm);

        gameActivity  = _gameActivity;
        weaponManager = _weaponManager;
        
    	// Alustetaan pelaaja
    	player = new Player(100, 100, this);
    	player.x = 0;
    	player.y = 0;
    	
    	// Alustetaan muuttujat
        waves = new int[AMOUNT_OF_WAVES][AMOUNT_OF_ENEMIES_PER_WAVE];
        for (int j = 0; j < AMOUNT_OF_WAVES; ++j) {
        	for (int i = 0; i < AMOUNT_OF_ENEMIES_PER_WAVE; ++i) {
        		waves[j][i] = -1;
        	}
        }
        enemies      = new ArrayList<Enemy>();
        enemyStats   = new int[5][5];
        spawnPoints  = new int[9][3][2];
        
        // Luetaan vihollistyyppien tiedot
        XmlReader reader = new XmlReader(_context);
        ArrayList<Integer> enemyStatsTemp = reader.readEnemyRanks();
        int rank = 0;
        for (int i = 0; i < enemyStatsTemp.size(); ++i) {
        	rank = (int)(i / 5);
        	
        	enemyStats[rank][i-rank*5] = enemyStatsTemp.get(i);
        }
        
        // Luetaan pelitilan tiedot
        reader.readSurvivalMode(this, _weaponManager);
        
        // Päivitetään aloituspisteet ja käynnistetään ensimmäinen vihollisaalto
        updateSpawnPoints();
        startWave();
    }
    
    /**
     * Päivittää pisteet.
     * 
     * @param int Tuhotun vihollisen taso, jonka perusteella pisteitä lisätään
     */
    public static void updateScore(int _rank)
    {
        // Päivitetään lastTime nykyisellä ajalla millisekunteina
        if (lastTime == 0) {
            lastTime = android.os.SystemClock.uptimeMillis();
            score += (10 * _rank + 5 * _rank * currentWave);
        }
        else {
            newTime = android.os.SystemClock.uptimeMillis();
            
            // Verrataan aikoja keskenään ja annetaan pisteitä sen mukaisesti
            if (newTime-lastTime <= 500) {
                score += (10 * _rank + 5 * _rank * currentWave) * comboMultiplier;
                ++comboMultiplier;
            }
            // Jos pelaaja ei saa comboa resetoidaan comboMultiplier
            else {
                score += (10 * _rank + 5 * _rank * currentWave);
                comboMultiplier = 2;
                lastTime = android.os.SystemClock.uptimeMillis();
            }
        }
        
        Hud.updateScoreCounter(score);
    }
    
    /**
     * Käynnistää uuden vihollisaallon asettamalla siihen kuuluvat viholliset aktiivisiksi.
     */
    @Override
    public void startWave()
    {
        /* Tarkastetaan onko kaikki vihollisaallot käyty läpi */
        if (currentWave == AMOUNT_OF_WAVES) { // TODO: TARKISTA MITEN MULTIDIMENSIONAL ARRAYN LENGTH TOIMII! (halutaan tietää wavejen määrä)
        	
            currentWave = 0;
            
            // Tarkistetaan vihollisen luokka, kasvatetaan sitä yhdellä ja lähetetään sille uudet statsit
            int rankTemp;
            
            for (int index = enemies.size()-1; index >= 0; --index) {
                // Lasketaan uusi rank, käytetään väliaikaismuuttujana rankTemppiä
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
	            for(int i = 0; i < enemies.size()-1; ++i) {
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
     * Päivittää vihollisten aloituspisteet kameran koordinaattien perusteella.
     */
    protected void updateSpawnPoints()
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
	    spawnPoints[1][0][0] = -halfOfScreenWidth + camera.x; 	  spawnPoints[1][0][1] = camera.y;
	    spawnPoints[1][1][0] = -halfOfScreenWidth + camera.x; 	  spawnPoints[1][1][1] = (int) (camera.y + 128 * Options.scale);
	    spawnPoints[1][2][0] = -halfOfScreenWidth + camera.x; 	  spawnPoints[1][2][1] = (int) (camera.y - 128 * Options.scale);
	    // Vasen yläkulma
	    spawnPoints[2][0][0] = -halfOfScreenWidth + camera.x; 	  spawnPoints[2][0][1] = halfOfScreenHeight + camera.y;
	    spawnPoints[2][1][0] = (int) (-halfOfScreenWidth + camera.x + 64 * Options.scale); spawnPoints[2][1][1] = (int) (halfOfScreenHeight + camera.y + 64 * Options.scale);
	    spawnPoints[2][2][0] = (int) (-halfOfScreenWidth + camera.x - 64 * Options.scale); spawnPoints[2][2][1] = (int) (halfOfScreenHeight + camera.y - 64 * Options.scale);
	    // Yläreuna
	    spawnPoints[3][0][0] = camera.x; 					 	  spawnPoints[3][0][1] = halfOfScreenHeight + camera.y;
	    spawnPoints[3][1][0] = (int) (camera.x + 128 * Options.scale); 				 	  spawnPoints[3][1][1] = halfOfScreenHeight + camera.y;
	    spawnPoints[3][2][0] = (int) (camera.x - 128 * Options.scale); 				 	  spawnPoints[3][2][1] = halfOfScreenHeight + camera.y;
	    // Oikea yläkulma
	    spawnPoints[4][0][0] = halfOfScreenWidth + camera.x;  	  spawnPoints[3][0][1] = halfOfScreenHeight + camera.y;
	    spawnPoints[4][1][0] = (int) (halfOfScreenWidth + camera.x + 64 * Options.scale);  spawnPoints[3][1][1] = (int) (halfOfScreenHeight + camera.y - 64 * Options.scale);
	    spawnPoints[4][2][0] = (int) (halfOfScreenWidth + camera.x - 64 * Options.scale);  spawnPoints[3][2][1] = (int) (halfOfScreenHeight + camera.y + 64 * Options.scale);
	    // Oikea reuna
	    spawnPoints[5][0][0] = halfOfScreenWidth + camera.x;  	  spawnPoints[4][0][1] = 0 + camera.y;
	    spawnPoints[5][1][0] = halfOfScreenWidth + camera.x;  	  spawnPoints[4][1][1] = (int) (0 + camera.y + 128 * Options.scale);
	    spawnPoints[5][2][0] = halfOfScreenWidth + camera.x;  	  spawnPoints[4][2][1] = (int) (0 + camera.y - 128 * Options.scale);
	    // Oikea alakulma
	    spawnPoints[6][0][0] = halfOfScreenWidth + camera.x;  	  spawnPoints[5][0][1] = -halfOfScreenHeight + camera.y;
	    spawnPoints[6][1][0] = (int) (halfOfScreenWidth + camera.x + 64 * Options.scale);  spawnPoints[5][1][1] = (int) (-halfOfScreenHeight + camera.y + 64 * Options.scale);
	    spawnPoints[6][2][0] = (int) (halfOfScreenWidth + camera.x - 64 * Options.scale);  spawnPoints[5][2][1] = (int) (-halfOfScreenHeight + camera.y - 64 * Options.scale);
	    // Alareuna
	    spawnPoints[7][0][0] = camera.x; 				 		  spawnPoints[7][0][1] = -halfOfScreenHeight + camera.y;
	    spawnPoints[7][1][0] = (int) (camera.x + 128 * Options.scale);			 	 	  spawnPoints[7][1][1] = -halfOfScreenHeight + camera.y;
	    spawnPoints[7][2][0] = (int) (camera.x - 128 * Options.scale);					  spawnPoints[7][2][1] = -halfOfScreenHeight + camera.y;
	    // Vasen alareuna
	    spawnPoints[8][0][0] = -halfOfScreenWidth + camera.x;      spawnPoints[8][0][1] = -halfOfScreenHeight + camera.y;
	    spawnPoints[8][1][0] = (int) (-halfOfScreenWidth + camera.x + 64 * Options.scale); spawnPoints[8][1][1] = (int) (-halfOfScreenHeight + camera.y - 64 * Options.scale);
	    spawnPoints[8][2][0] = (int) (-halfOfScreenWidth + camera.x - 64 * Options.scale); spawnPoints[8][2][1] = (int) (-halfOfScreenHeight + camera.y + 64 * Options.scale);
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
     * Lähettää pisteet GameActivitylle, joka siirtää pelin Highscores-valikkoon.
     */
	public void endGameMode()
	{
		gameActivity.continueToHighscores((int)score);
	}
}