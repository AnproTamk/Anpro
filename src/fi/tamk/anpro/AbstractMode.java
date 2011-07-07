package fi.tamk.anpro;

import java.util.ArrayList;
import android.util.DisplayMetrics;

/**
 * Sisältää pelitilojen yhteiset ominaisuudet.
 */
abstract public class AbstractMode
{
    /* Pelaaja */
    public Player player;
    
    /* Viholliset */
    public    ArrayList<Enemy> enemies;         // Viholliset
    protected int[][]          enemyStats;      // Vihollistyyppien statsit ([rank][attribuutti] = [arvo])
    
    /* Kentän koko */
    protected int halfOfScreenWidth;
    protected int halfOfScreenHeight;
    
    /* Muiden olioiden pointterit */
    protected WeaponManager weaponManager;
    protected CameraManager camera;
    protected GameActivity  gameActivity;
    
    /**
     * Alustaa luokan muuttujat, lukee pelitilan tarvitsemat tiedot ja käynnistää pelin.
     * 
     * @param GameActivity   Pelitilan aloittava aktiviteetti
     * @param DisplayMetrics Näytön tiedot
     */
    public AbstractMode(GameActivity _gameActivity, DisplayMetrics _dm)
    {
    	// Tallennetaan osoitin peliaktiviteettiin
        gameActivity = _gameActivity;
        
        // Tallennetaan näytön tiedot
        halfOfScreenWidth  = _dm.widthPixels;
        halfOfScreenHeight = _dm.heightPixels;
        
        // Otetaan CameraManager käyttöön
        camera = CameraManager.getInstance();
    }
    
    /**
     * Käynnistää uuden vihollisaallon asettamalla siihen kuuluvat viholliset aktiivisiksi.
     */
    public void startWave() { };
}
