package fi.tamk.anpro;

import java.util.ArrayList;

abstract public class AbstractMode
{
	/* Vakioita XML-tiedostojen lukemista ja muuttujien alustamista varten */
    public static final int AMOUNT_OF_WAVES            = 100;
    public static final int AMOUNT_OF_ENEMIES_PER_WAVE = 11;
    
    /* Viholliset */
    public    ArrayList<Enemy> enemies;         // Viholliset
    protected int[][]          enemyStats;      // Vihollistyyppien statsit ([rank][attribuutti] = [arvo])
    protected int              enemiesLeft = 0; // Vihollisiä jäljellä kentällä
    protected int 			   rankTemp;
    
    /* Kentän koko */
    protected int halfOfScreenWidth;
    protected int halfOfScreenHeight;
    
    /* WeaponManager */
    protected WeaponManager weaponManager;
    
    /* Kameran koordinaatit */
    protected int cameraX = CameraManager.camX;
    protected int cameraY = CameraManager.camY;
    
    abstract public void startWave();
    
    abstract protected void updateSpawnPoints();
}
