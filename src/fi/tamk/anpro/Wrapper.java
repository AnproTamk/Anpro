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
    
    /* Eri tilat */
    public static final int INACTIVE               = 0;
    public static final int FULL_ACTIVITY          = 1;
    public static final int ONLY_ANIMATION         = 2;
    public static final int ANIMATION_AND_MOVEMENT = 3;
    
    /* Osoitin t‰h‰n luokasta */
    private static Wrapper instance = null;
    
    /* Piirtolistat */
    public Player                        player      = null;
    public ArrayList<Enemy>              enemies     = null;
    public ArrayList<AbstractProjectile> projectiles = null;
    public ArrayList<GuiObject> 		 guiObjects  = null;

    /* Peliobjektien tilat
             0. Ei piirret‰, ei p‰ivitet‰
             1. Piirret‰‰n, p‰ivitet‰‰n kaikki
             2. Piirret‰‰n, p‰ivitet‰‰n vain animaatio
             3. Piirret‰‰n, p‰ivitet‰‰n vain liike ja animaatio */
    public int                playerState      = 0;
    public ArrayList<Integer> enemyStates      = null;
    public ArrayList<Integer> projectileStates = null;
    public ArrayList<Integer> guiObjectStates  = null;

    /* Peliobjektien teko‰lyjen tasot. N‰iden taulukoiden arvot viittaavat
       piirtolistojen soluihin. N‰it‰ kutsutaan ainoastaan GameThreadin
       p‰‰silmukassa ja niill‰ pyrit‰‰n nopeuttamaan teko‰lyjen p‰ivitt‰mist‰. */
    public ArrayList<Integer> priorityOneEnemies   = null;
    public ArrayList<Integer> priorityTwoEnemies   = null;
    public ArrayList<Integer> priorityThreeEnemies = null;
    public ArrayList<Integer> priorityFourEnemies  = null;
    
    public ArrayList<Integer> priorityOneProjectiles   = null;
    public ArrayList<Integer> priorityTwoProjectiles   = null;
    public ArrayList<Integer> priorityThreeProjectiles = null;
    public ArrayList<Integer> priorityFourProjectiles  = null;
    
    /**
     * Alustaa luokan muuttujat.
     */
    private Wrapper()
    {
        enemies     = new ArrayList<Enemy>();
        projectiles = new ArrayList<AbstractProjectile>();
        guiObjects  = new ArrayList<GuiObject>();
        
        enemyStates      = new ArrayList<Integer>();
        projectileStates = new ArrayList<Integer>();
        guiObjectStates  = new ArrayList<Integer>();
        
        priorityOneEnemies   = new ArrayList<Integer>();
        priorityTwoEnemies   = new ArrayList<Integer>();
        priorityThreeEnemies = new ArrayList<Integer>();
        priorityFourEnemies  = new ArrayList<Integer>();
        
        priorityOneProjectiles   = new ArrayList<Integer>();
        priorityTwoProjectiles   = new ArrayList<Integer>();
        priorityThreeProjectiles = new ArrayList<Integer>();
        priorityFourProjectiles  = new ArrayList<Integer>();
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
     * Lis‰‰ parametrina annetun luokan piirtolistalle ja tallentaa objektin tilan.
     * 
     * Objektin t‰rkeys m‰‰ritet‰‰n seuraavasti:
     * 		-------------------------------------
     * 		| TƒRKEYS | TEKOƒLYN PƒIVITYSTIHEYS |
     * 		-------------------------------------
     * 		|    0    |      Ei p‰ivitet‰       |
     * 		|    1    |         400 ms          |
     * 		|    2    |         200 ms          |
     * 		|    3    |         100 ms          |
     * 		|    4    |          50 ms          |
     * 		-------------------------------------
     * 
     * Objektin t‰rkeys k‰ytet‰‰n ainoastaan kertomaan pelis‰ikeelle (GameThread) aika,
     * joka sen on odotettava jokaisen p‰ivityksen v‰lill‰. Objektit lis‰t‰‰n t‰rkeyslistoihin
     * niiden t‰rkeyden mukaan, joita pelis‰ie lukee. T‰ll‰ pyrit‰‰n v‰ltt‰m‰‰n kaikkien
     * objektien l‰pi k‰yminen, vaikka haluttaisiin p‰ivitt‰‰ vain kriittisimm‰t.
     * 
     * @param Object Lis‰tt‰v‰ olio
     * @param int    Lis‰tt‰v‰n olion tyyppi
     * @param int    Objektin t‰rkeys
     * 
     * @return int Lis‰tyn olion tunnus piirtolistalla
     */
    public final int addToList(Object _object, int _classType, int _priority)
    {
        if (_classType == CLASS_TYPE_PLAYER) {
            player      = (Player)_object;
            playerState = 1;
        }
        else if (_classType == CLASS_TYPE_ENEMY) {
            enemies.add((Enemy)_object);
            enemyStates.add(0);
            
            if (_priority == 1) {
                priorityOneEnemies.add(enemyStates.size()-1);
            }
            else if (_priority == 2) {
                priorityTwoEnemies.add(enemyStates.size()-1);
            }
            else if (_priority == 3) {
                priorityThreeEnemies.add(enemyStates.size()-1);
            }
            else if (_priority == 4) {
                priorityFourEnemies.add(enemyStates.size()-1);
            }

            return enemyStates.size()-1;
        }
        else if (_classType == CLASS_TYPE_PROJECTILE) {
            projectiles.add((AbstractProjectile)_object);
            projectileStates.add(0);
            
            if (_priority == 1) {
                priorityOneProjectiles.add(projectileStates.size()-1);
            }
            else if (_priority == 2) {
                priorityTwoProjectiles.add(projectileStates.size()-1);
            }
            else if (_priority == 3) {
                priorityThreeProjectiles.add(projectileStates.size()-1);
            }
            else if (_priority == 4) {
                priorityFourProjectiles.add(projectileStates.size()-1);
            }

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
