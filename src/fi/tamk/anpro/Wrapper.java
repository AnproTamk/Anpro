package fi.tamk.anpro;

import java.util.ArrayList;

/**
 * Yhdist‰‰ peliobjektit ja renderˆij‰n toisiinsa. Yll‰pit‰‰ piirtolistoja.
 */
public class Wrapper
{
	/* Luokan tyyppi */
    public static final int CLASS_TYPE_PLAYER     = 1;
    public static final int CLASS_TYPE_ENEMY      = 2;
    public static final int CLASS_TYPE_PROJECTILE = 3;
    public static final int CLASS_TYPE_GUI        = 4;
    
    /* Osoitin t‰h‰n luokasta */
    private static Wrapper instance = null;
    
    /* Piirtolistat */
    public Player                        player      = null;
    public ArrayList<Enemy>              enemies     = null;
    public ArrayList<AbstractProjectile> projectiles = null;
    public ArrayList<GuiObject> 		 guiObjects  = null;

    /* Peliobjektien tilat */
    public int                playerState      = 0;
    public ArrayList<Integer> enemyStates      = null;
    public ArrayList<Integer> projectileStates = null;
    public ArrayList<Integer> guiObjectStates  = null;
    
    /**
     * Alustaa luokan muuttujat.
     */
    private Wrapper()
    {
        enemies          = new ArrayList<Enemy>();
        projectiles      = new ArrayList<AbstractProjectile>();
        guiObjects       = new ArrayList<GuiObject>();
        
        enemyStates      = new ArrayList<Integer>();
        projectileStates = new ArrayList<Integer>();
        guiObjectStates  = new ArrayList<Integer>();
    }
    
    /**
     * Palauttaa osoittimen t‰h‰n luokkaan.
     * 
     * @return Wrapper Osoitin t‰h‰n luokkaan
     */
    public static Wrapper getInstance()
    {
        if(instance == null) {
            instance = new Wrapper();
        }
        return instance;
    }
    
    /**
     * Lis‰‰ parametrina annetun luokan piirtolistalle.
     * 
     * @param Object Lis‰tt‰v‰ olio
     * @param int    Lis‰tt‰v‰n olion tyyppi
     * 
     * @return int Lis‰tyn olion tunnus piirtolistalla
     */
    public final int addToList(Object _object, int _classType)
    {
        if (_classType == CLASS_TYPE_PLAYER) {
            player      = (Player)_object;
            playerState = 1;
        }
        else if (_classType == CLASS_TYPE_ENEMY) {
            enemies.add((Enemy)_object);
            enemyStates.add(1);

            return enemyStates.size()-1;
        }
        else if (_classType == CLASS_TYPE_PROJECTILE) {
            projectiles.add((AbstractProjectile)_object);
            projectileStates.add(0);

            return projectileStates.size()-1;
        }
        else if (_classType == CLASS_TYPE_GUI) {
        	guiObjects.add((GuiObject)_object);
        	guiObjectStates.add(1);

            return guiObjectStates.size()-1;
        }
        
        return 0;
    }
}
