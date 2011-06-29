package fi.tamk.anpro;

import java.util.ArrayList;
import java.util.Random;

import android.content.Context;
import android.util.DisplayMetrics;

/**
 * Sisältää pelitilojen yhteiset ominaisuudet.
 */
abstract public class AbstractMode
{
	/* Vakioita XML-tiedostojen lukemista ja muuttujien alustamista varten */
    public static final int AMOUNT_OF_WAVES            = 100;
    public static final int AMOUNT_OF_ENEMIES_PER_WAVE = 11;
    
    /* Viholliset */
    public    ArrayList<Enemy> enemies;         // Viholliset
    protected int[][]          enemyStats;      // Vihollistyyppien statsit ([rank][attribuutti] = [arvo])
    protected static int       enemiesLeft = 0; // Vihollisiä jäljellä kentällä
    protected int 			   rankTemp;
    
    /* Kentän koko */
    protected int halfOfScreenWidth;
    protected int halfOfScreenHeight;
    
    /* Muiden olioiden pointterit */
    protected WeaponManager weaponManager;
    protected CameraManager camera;
    protected GameActivity  gameActivity;
    
    /* Satunnaisgeneraattori */
    public static Random randomGen = new Random();
    
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
        
        // Luodaan WeaponManager ja ladataan aseet
        weaponManager = new WeaponManager();
        weaponManager.initialize(gameActivity.activeMode);
        
        // Otetaan CameraManager käyttöön
        camera = CameraManager.getInstance();
    }
    
    /**
     * Käynnistää uuden vihollisaallon asettamalla siihen kuuluvat viholliset aktiivisiksi.
     */
    public void startWave() { }
}
