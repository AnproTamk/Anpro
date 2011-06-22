package fi.tamk.anpro;

import java.util.ArrayList;

/**
 * Sis‰lt‰‰ pelitilojen yhteiset ominaisuudet.
 */
abstract public class AbstractMode
{
	/* Vakioita XML-tiedostojen lukemista ja muuttujien alustamista varten */
    public static final int AMOUNT_OF_WAVES            = 100;
    public static final int AMOUNT_OF_ENEMIES_PER_WAVE = 11;
    
    /* Viholliset */
    public    ArrayList<Enemy> enemies;         // Viholliset
    protected int[][]          enemyStats;      // Vihollistyyppien statsit ([rank][attribuutti] = [arvo])
    protected static int       enemiesLeft = 0; // Vihollisi‰ j‰ljell‰ kent‰ll‰
    protected int 			   rankTemp;
    
    /* Kent‰n koko */
    protected int halfOfScreenWidth;
    protected int halfOfScreenHeight;
    
    /* Muiden olioiden pointterit */
    protected WeaponManager weaponManager;
    protected CameraManager camera;
    
    /**
     * K‰ynnist‰‰ uuden vihollisaallon asettamalla siihen kuuluvat viholliset aktiivisiksi.
     */
    abstract public void startWave();
    
    /**
     * P‰ivitt‰‰ vihollisten aloituspisteet kameran koordinaattien perusteella.
     */
    abstract protected void updateSpawnPoints();
}
