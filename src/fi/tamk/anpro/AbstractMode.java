package fi.tamk.anpro;

import java.util.ArrayList;

import android.content.Context;
import android.util.DisplayMetrics;

/**
 * Sis�lt�� pelitilojen yhteiset ominaisuudet.
 */
abstract public class AbstractMode
{
    /* Pelaaja */
    public Player player;
    
    /* Viholliset */
    public    ArrayList<Enemy> enemies;         // Viholliset
    protected int[][]          enemyStats;      // Vihollistyyppien statsit ([rank][attribuutti] = [arvo])
    
    /* Kent�n koko */
    protected int halfOfScreenWidth;
    protected int halfOfScreenHeight;
    
    /* Muiden olioiden pointterit */
    protected WeaponManager weaponManager;
    protected CameraManager camera;
    protected GameActivity  gameActivity;
    
    /**
     * Alustaa luokan muuttujat, lukee pelitilan tarvitsemat tiedot ja k�ynnist�� pelin.
     * 
     * @param GameActivity   Pelitilan aloittava aktiviteetti
     * @param DisplayMetrics N�yt�n tiedot
     */
    public AbstractMode(GameActivity _gameActivity, DisplayMetrics _dm, Context _context)
    {
    	// Tallennetaan osoitin peliaktiviteettiin
        gameActivity = _gameActivity;
        
        // Tallennetaan n�yt�n tiedot
        halfOfScreenWidth  = _dm.widthPixels;
        halfOfScreenHeight = _dm.heightPixels;
        
        // Otetaan CameraManager k�ytt��n
        camera = CameraManager.getInstance();
        
        // Alustetaan taulukot
        enemies    = new ArrayList<Enemy>();
        enemyStats = new int[5][5];
        
    	// Alustetaan pelaaja
    	player = new Player(100, 100, this);
    	player.x = 0;
    	player.y = 0;
        
        // Luetaan vihollistyyppien tiedot
        XmlReader reader = new XmlReader(_context);
        ArrayList<Integer> enemyStatsTemp = reader.readEnemyRanks();
        int rank = 0;
        for (int i = 0; i < enemyStatsTemp.size(); ++i) {
        	rank = (int)(i / 5);
        	
        	enemyStats[rank][i-rank*5] = enemyStatsTemp.get(i);
        }
    }
    
    /**
     * K�ynnist�� uuden vihollisaallon asettamalla siihen kuuluvat viholliset aktiivisiksi.
     */
    public void startWave() { }

    /**
     * L�hett�� pisteet GameActivitylle, joka siirt�� pelin Highscores-valikkoon.
     */
	public void endGameMode() { }
}
