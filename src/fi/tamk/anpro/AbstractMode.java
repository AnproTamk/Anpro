package fi.tamk.anpro;

import java.util.ArrayList;
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
    public AbstractMode(GameActivity _gameActivity, DisplayMetrics _dm)
    {
    	// Tallennetaan osoitin peliaktiviteettiin
        gameActivity = _gameActivity;
        
        // Tallennetaan n�yt�n tiedot
        halfOfScreenWidth  = _dm.widthPixels;
        halfOfScreenHeight = _dm.heightPixels;
        
        // Otetaan CameraManager k�ytt��n
        camera = CameraManager.getInstance();
    }
    
    /**
     * K�ynnist�� uuden vihollisaallon asettamalla siihen kuuluvat viholliset aktiivisiksi.
     */
    public void startWave() { };
}
