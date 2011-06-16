package fi.tamk.anpro;

import java.util.ArrayList;
import java.util.Random;

import android.content.Context;
import android.util.DisplayMetrics;

/**
 * Survival-pelitila. Sis‰lt‰‰ peliobjektit, WeaponManagerin ja AchievementManagerin.
 * Hallitsee vihollisaaltojen kutsumisen ja pistelaskurin p‰ivitt‰misen.
 * 
 * @extends AbstractMode
 */
public class SurvivalMode extends AbstractMode
{
    /* Vihollisaallot */
    public  int waves[][];       // [aalto][vihollisen j‰rjestysnumero] = [vihollisen indeksi enemies-taulukossa]
    private int currentWave = 0;
    
    /* Pistelaskuri ja pisteet */
    public  GuiObject scoreCounter;
    private long      score;
    
    /* Combot */
    private int  comboMultiplier = 2; // Combokerroin pisteiden laskemista varten
    private long lastTime        = 0; // Edellisen pisteen lis‰yksen aika
    private long newTime;             // Uuden pisteen lis‰yksen aika
    
    /* Vihollisten aloituspaikat */
    private int spawnPoints[][][]; // [rykelm‰][paikka][x/y] = [koordinaatti]
    
    /* Satunnaisgeneraattori */
    public static Random randomGen = new Random();
    
    /**
     * Alustaa luokan muuttujat, lukee pelitilan tarvitsemat tiedot ja k‰ynnist‰‰ pelin.
     * 
     * @param DisplayMetrics N‰ytˆn tiedot
     * @param Context		 Ohjelman konteksti
     */
    public SurvivalMode(DisplayMetrics _dm, Context _context)
    {
    	// Alustetaan muuttujat
        waves        = new int[AMOUNT_OF_WAVES][AMOUNT_OF_ENEMIES_PER_WAVE];
        enemies      = new ArrayList<Enemy>();
        enemyStats   = new int[5][5];
        scoreCounter = new GuiObject();
        spawnPoints  = new int[9][3][2];
        
        // Tallennetaan n‰ytˆn tiedot
        halfOfScreenWidth  = _dm.widthPixels;
        halfOfScreenHeight = _dm.heightPixels;
        
        // Luetaan vihollistyyppien tiedot
        XmlReader reader = new XmlReader(_context);
        ArrayList<Integer> enemyStatsTemp = reader.readRanks();
        int rank = 0;
        for (int i = 0; i < enemyStatsTemp.size()-1; ++i) {
        	enemyStats[rank][i-rank*5] = enemyStatsTemp.get(i);
        	
        	rank = (int)(i / 5);
        }
        
        // Luetaan pelitilan tiedot
        reader.readSurvivalMode(this);
        
        // Luodaan WeaponManager ja ladataan aseet
        weaponManager = new WeaponManager();
        weaponManager.initialize(WeaponManager.SURVIVAL_MODE);
        
        // P‰ivitet‰‰n aloituspisteet ja k‰ynnistet‰‰n ensimm‰inen vihollisaalto
        updateSpawnPoints();
        startWave();
    }
    
    /**
     * P‰ivitt‰‰ pisteet.
     * 
     * @param int Tuhotun vihollisen taso, jonka perusteella pisteit‰ lis‰t‰‰n
     */
    public void updateScore(int _rank) {
        // P‰ivitet‰‰n lastTime nykyisell‰ ajalla millisekunteina
        if (lastTime == 0) {
            lastTime = android.os.SystemClock.uptimeMillis();
        }
        else {
            newTime = android.os.SystemClock.uptimeMillis();
            
            // Verrataan aikoja kesken‰‰n ja annetaan pisteit‰ sen mukaisesti
            if (newTime-lastTime <= 500) {
                score += (10 * _rank + 5 * _rank * currentWave) * comboMultiplier;
                ++comboMultiplier;
            }
            // Jos pelaaja ei saa comboa resetoidaan comboMultiplier
            else {
                score += (10 * _rank + 5 * _rank * currentWave);
                comboMultiplier = 2;
            }
        }
        // scoreCounter.updateText(score);
    }
    
    /*
     * K‰ynnist‰‰ seuraavan waven, nostaa vihollisten tasoa ja aktivoi viholliset
     */
    @Override
    public void startWave() {
        ++currentWave;
        
        // Tarkastaa onko kaikki wavet k‰yty l‰pi
        if (currentWave == AMOUNT_OF_WAVES) { // TARKISTA MITEN MULTIDIMENSIONAL ARRAYN LENGTH TOIMII! (halutaan tiet‰‰ wavejen m‰‰r‰)
            currentWave = 0;
            
            // Tarkistetaan vihollisen luokka, kasvatetaan sit‰ yhdell‰ ja l‰hetet‰‰n sille uudet statsit
            for (int index = enemies.size()-1; index >= 0; --index) {
                // Lasketaan uusi rank, k‰ytet‰‰n v‰liaikaismuuttujana rankTemppi‰
                rankTemp = enemies.get(index).rank + 1;
                if (rankTemp <= 5) {
                    enemies.get(index).setStats(enemyStats[rankTemp][0], enemyStats[rankTemp][1], enemyStats[rankTemp][2],
                                                enemyStats[rankTemp][3], enemyStats[rankTemp][4], rankTemp);
                }
            }
        }
        
        // Aktivoidaan viholliset
        int temp;
        int tempRandA, tempRandB;
        for (int index = AMOUNT_OF_ENEMIES_PER_WAVE-1; index > 0; --index) {
        	temp = waves[currentWave][index];
        	
        	tempRandA = randomGen.nextInt(7)+1;
        	tempRandB = randomGen.nextInt(2);
        	
            enemies.get(temp).setActive();
            enemies.get(temp).x = spawnPoints[tempRandA][tempRandB][0];
            enemies.get(temp).y = spawnPoints[tempRandA][tempRandB][1];
            
        }
    }

    @Override
    protected void updateSpawnPoints() {
	    /* 
	     * Tallennetaan reunojen koordinaatit taulukkoon kameran sijainnin muutoksen m‰‰r‰n mukaan (CameraManager.camX ja CameraManager.camY)
	     * { {vasen reuna X,Y}, {vasen yl‰reuna X,Y}, {yl‰reuna X,Y}, {oikea yl‰reuna X,Y},
	     *	 {oikea reuna X,Y}, {oikea alareuna X,Y}, {alareuna X,Y}, {vasen alareuna X,Y} }
	     * index: 	  1   0/1           2       0/1        3    0/1             4	  0/1
	     * index: 	  5   0/1           6       0/1        7    0/1             8	  0/1
	     * 
	     */
	    
	    // Vihollisten syntypisteiden koordinaatit
	    // [rykelm‰n j‰rjestysnumero][spawnpointin j‰rjestysnumero][pisteen x- ja y-koordinaatit]
	    //					 X								   				      Y
	    // Vasen reuna
	    spawnPoints[1][0][0] = -halfOfScreenWidth + cameraX; 	  spawnPoints[1][0][1] = cameraY;
	    spawnPoints[1][1][0] = -halfOfScreenWidth + cameraX; 	  spawnPoints[1][1][1] = cameraY + 128;
	    spawnPoints[1][2][0] = -halfOfScreenWidth + cameraX; 	  spawnPoints[1][2][1] = cameraY - 128;
	    // Vasen yl‰kulma
	    spawnPoints[2][0][0] = -halfOfScreenWidth + cameraX; 	  spawnPoints[2][0][1] = halfOfScreenHeight + cameraY;
	    spawnPoints[2][1][0] = -halfOfScreenWidth + cameraX + 64; spawnPoints[2][1][1] = halfOfScreenHeight + cameraY + 64;
	    spawnPoints[2][2][0] = -halfOfScreenWidth + cameraX - 64; spawnPoints[2][2][1] = halfOfScreenHeight + cameraY - 64;
	    // Yl‰reuna
	    spawnPoints[3][0][0] = cameraX; 					 	  spawnPoints[3][0][1] = halfOfScreenHeight + cameraY;
	    spawnPoints[3][1][0] = cameraX + 128; 				 	  spawnPoints[3][1][1] = halfOfScreenHeight + cameraY;
	    spawnPoints[3][2][0] = cameraX - 128; 				 	  spawnPoints[3][2][1] = halfOfScreenHeight + cameraY;
	    // Oikea yl‰kulma
	    spawnPoints[4][0][0] = halfOfScreenWidth + cameraX;  	  spawnPoints[3][0][1] = halfOfScreenHeight + cameraY;
	    spawnPoints[4][1][0] = halfOfScreenWidth + cameraX + 64;  spawnPoints[3][1][1] = halfOfScreenHeight + cameraY - 64;
	    spawnPoints[4][2][0] = halfOfScreenWidth + cameraX - 64;  spawnPoints[3][2][1] = halfOfScreenHeight + cameraY + 64;
	    // Oikea reuna
	    spawnPoints[5][0][0] = halfOfScreenWidth + cameraX;  	  spawnPoints[4][0][1] = 0 + cameraY;
	    spawnPoints[5][1][0] = halfOfScreenWidth + cameraX;  	  spawnPoints[4][1][1] = 0 + cameraY + 128;
	    spawnPoints[5][2][0] = halfOfScreenWidth + cameraX;  	  spawnPoints[4][2][1] = 0 + cameraY - 128;
	    // Oikea alakulma
	    spawnPoints[6][0][0] = halfOfScreenWidth + cameraX;  	  spawnPoints[5][0][1] = -halfOfScreenHeight + cameraY;
	    spawnPoints[6][1][0] = halfOfScreenWidth + cameraX + 64;  spawnPoints[5][1][1] = -halfOfScreenHeight + cameraY + 64;
	    spawnPoints[6][2][0] = halfOfScreenWidth + cameraX - 64;  spawnPoints[5][2][1] = -halfOfScreenHeight + cameraY - 64;
	    // Alareuna
	    spawnPoints[7][0][0] = cameraX; 				 		  spawnPoints[7][0][1] = -halfOfScreenHeight + cameraY;
	    spawnPoints[7][1][0] = cameraX + 128;			 	 	  spawnPoints[7][1][1] = -halfOfScreenHeight + cameraY;
	    spawnPoints[7][2][0] = cameraX - 128;					  spawnPoints[7][2][1] = -halfOfScreenHeight + cameraY;
	    // Vasen alareuna
	    spawnPoints[8][0][0] = -halfOfScreenWidth + cameraX;      spawnPoints[8][0][1] = -halfOfScreenHeight + cameraY;
	    spawnPoints[8][1][0] = -halfOfScreenWidth + cameraX + 64; spawnPoints[8][1][1] = -halfOfScreenHeight + cameraY - 64;
	    spawnPoints[8][2][0] = -halfOfScreenWidth + cameraX - 64; spawnPoints[8][2][1] = -halfOfScreenHeight + cameraY + 64;
	    // Random reuna
	    // spawnPoints[0][0][0] = ...
    }
    
    /*
     * Hakee, satunnoi ja asettaa vihollisten aloituspisteet
     * 
     * @param alignPoint - M‰‰ritt‰‰ reunan, jolle viholliset tulevat
     * @param specificPoint - M‰‰ritt‰‰ tarkempia koordinaatteja 3:lle eri vihollisen spawnpointille
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
       1. [mille reunalle viholliset tulevat] <- hae t‰m‰ xmlReaderilla
       2. [spawnpointin j‰rjestysnumero] <- n‰it‰ aluksi 3 jokaiselle spawnpointille
       3. [vihollisen x- ja y-sijainnit] <- esim. vasempaan reunaan esim‰‰ritettyjen x- ja y-koordinaattien lis‰ksi n‰m‰
           (leftSpawnPointX + joko -1* tai 1* spawnPoints[][][x,y] (x- ja y-arvot taulukosta)
    */

    // [1][0][0] = vasempaan reunaan ilmestyv‰n vihollisen x-koord. (ensimm‰inen spawnpoint)
    // [1][0][1] = vasempaan reunaan ilmestyv‰n vihollisen y-koord. (ensimm‰inen spawnpoint)
    
    // [1][1][0] = vasempaan reunaan ilmestyv‰n vihollisen x-koord. (toinen spawnpoint)
    // [1][1][1] = vasempaan reunaan ilmestyv‰n vihollisen y-koord. (toinen spawnpoint)
    
    // [4][2][0] = alareunaan ilmestyv‰n vihollisen x-koord. (kolmas spawnpoint)
    // [4][2][1] = alareunaan ilmestyv‰n vihollisen y-koord. (kolmas spawnpoint)
    
    // [2][1][0] = x-koord.
    
}

