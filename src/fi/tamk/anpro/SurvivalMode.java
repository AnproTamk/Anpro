package fi.tamk.anpro;
import java.util.ArrayList;

public class SurvivalMode {
	public static final int AMOUNT_OF_WAVES            = 100;
	public static final int AMOUNT_OF_ENEMIES_PER_WAVE = 11;
	
    private static SurvivalMode instance = null;
    
    public  int waves[][];       // Vihollisaallot [aalto][lista vihollisista]
    private int currentWave = 0; // Nykyinen vihollisaalto
    
    public  ArrayList<Enemy> enemies;         // Viholliset
    public  int              enemiesLeft = 0; // Vihollisi‰ j‰ljell‰ kent‰ll‰
    public  int              enemyStats[][];  // Vihollisten (rankkien) statsit [rank][statsit]
    private int              rankTemp;
    
    public  GuiObject scoreCounter;        // Pistelaskuri
    private long      score;               // Pisteet
    private int       comboMultiplier = 2; // Combokerroin
    
    private long lastTime; // Edellisen pisteen lis‰yksen aika
    private long newTime;  // Nykyisen pisteen lis‰yksen aika
    
    
    //StoryModen rakentaja
    protected SurvivalMode() {
    	waves = new int[AMOUNT_OF_WAVES][AMOUNT_OF_ENEMIES_PER_WAVE];
    	scoreCounter = new GuiObject();
    }
    
    public static SurvivalMode getInstance() {
        if(instance == null) {
            instance = new SurvivalMode();
        }
        return instance;
    }
    
    /*
     * Pisteiden p‰ivitys
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
    	for (int index = AMOUNT_OF_ENEMIES_PER_WAVE-1; index > 0; --index) {
    		enemies.get(waves[currentWave][index]).setActive();
    	}
    }
}
