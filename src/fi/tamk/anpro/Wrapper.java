package fi.tamk.anpro;

import java.util.ArrayList;

/**
 * Yhdistää peliobjektit ja renderöijän toisiinsa. Ylläpitää piirtolistoja.
 */
public class Wrapper
{
    public static final int CLASS_TYPE_PLAYER     = 1;
    public static final int CLASS_TYPE_ENEMY      = 2;
    public static final int CLASS_TYPE_PROJECTILE = 3;
    public static final int CLASS_TYPE_GUI        = 4;
    
    private static Wrapper instance = null;
    
    // Listat piirrettävistä objekteista
    public Player                        player      = null;
    public ArrayList<Enemy>              enemies     = null;
    public ArrayList<AbstractProjectile> projectiles = null;

    // Listat objektien tiloista
    public int                playerState      = 0;
    public ArrayList<Integer> enemyStates      = null;
    public ArrayList<Integer> projectileStates = null;
    
    // HUD-objektit
    //...
    
    //Wrapperin rakentaja
    private Wrapper() {
        enemies          = new ArrayList<Enemy>();
        projectiles      = new ArrayList<AbstractProjectile>();
        enemyStates      = new ArrayList<Integer>();
        projectileStates = new ArrayList<Integer>();
    }
    
    public static Wrapper getInstance() {
        if(instance == null) {
            instance = new Wrapper();
        }
        return instance;
    }
    
    public final int addToList(Object _object, int _classType){
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
        
        return 0;
    }
}
