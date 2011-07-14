package fi.tamk.anpro;

import java.util.ArrayList;

/**
 * Yhdist‰‰ luokat toisiinsa yll‰pit‰m‰ll‰ piirtolistoja, objektien tiloja
 * ja teko‰lyjen p‰ivitysnopeuksia.
 */
public class Wrapper
{
    /* Osoitin t‰h‰n luokkaan (singleton-toimintoa varten) */
    private static Wrapper instance = null;
    
    /* Vakioita */
    // Luokan tyyppi (k‰ytet‰‰n myˆs aseissa ja ammuksissa)
    public static final int CLASS_TYPE_PLAYER         = 1;
    public static final int CLASS_TYPE_ALLY           = 5;
    public static final int CLASS_TYPE_ENEMY          = 2;
    public static final int CLASS_TYPE_PROJECTILE     = 3;
    public static final int CLASS_TYPE_GUI            = 4;
    public static final int CLASS_TYPE_OBSTACLE       = 6;
    public static final int CLASS_TYPE_EFFECT         = 7;
    public static final int CLASS_TYPE_COLLECTABLE    = 8;
    public static final int CLASS_TYPE_BACKGROUNDSTAR = 9;
    public static final int CLASS_TYPE_MOTHERSHIP     = 10;
    public static final int CLASS_TYPE_MESSAGE        = 11;
    
    // Objektien tilat (ks. projektin Wiki)
    public static final int INACTIVE               = 0;
    public static final int FULL_ACTIVITY          = 1;
    public static final int ONLY_ANIMATION         = 2;
    public static final int ANIMATION_AND_MOVEMENT = 3;
    
    /* Piirto- ja p‰ivityslistat */
    public Player                        player       = null;
    public ArrayList<Ally>               allies       = null;
    public ArrayList<Enemy>              enemies      = null;
    public ArrayList<AbstractProjectile> projectiles  = null;
    public ArrayList<GuiObject> 		 guiObjects   = null;
    public ArrayList<Obstacle>  		 obstacles    = null;
    public ArrayList<EffectObject> 	     effects      = null;
    public ArrayList<Message> 	         messages     = null;
    public ArrayList<Collectable> 	     collectables = null;
    
    public ArrayList<BackgroundStar> backgroundStars = null;
    
    public Mothership mothership = null;

    /* Peliobjektien tilat */
    public int                playerState       = 0;
    public ArrayList<Integer> allyStates        = null;
    public ArrayList<Integer> enemyStates       = null;
    public ArrayList<Integer> projectileStates  = null;
    public ArrayList<Integer> guiObjectStates   = null;
    public ArrayList<Integer> obstacleStates    = null;
    public ArrayList<Integer> effectStates      = null;
    public ArrayList<Integer> messageStates     = null;
    public ArrayList<Integer> collectableStates = null;

    /* Peliobjektien teko‰lyjen tasot. N‰iden taulukoiden arvot viittaavat
       piirtolistojen soluihin. N‰it‰ kutsutaan ainoastaan GameThreadin
       p‰‰silmukassa ja niill‰ pyrit‰‰n nopeuttamaan teko‰lyjen p‰ivitt‰mist‰. */
    public ArrayList<Integer> priorityOneAllies   = null;
    public ArrayList<Integer> priorityTwoAllies   = null;
    public ArrayList<Integer> priorityThreeAllies = null;
    public ArrayList<Integer> priorityFourAllies  = null;
    
    public ArrayList<Integer> priorityOneEnemies   = null;
    public ArrayList<Integer> priorityTwoEnemies   = null;
    public ArrayList<Integer> priorityThreeEnemies = null;
    public ArrayList<Integer> priorityFourEnemies  = null;
    
    public ArrayList<Integer> priorityOneProjectiles   = null;
    public ArrayList<Integer> priorityTwoProjectiles   = null;
    public ArrayList<Integer> priorityThreeProjectiles = null;
    public ArrayList<Integer> priorityFourProjectiles  = null;
    
    /* Osumatarkistuksen ruudukon yhden ruudun leveys/korkeus */
    public static int gridSize;
    
    /**
     * Alustaa luokan muuttujat ja m‰‰rittelee osumatarkistuksissa k‰ytett‰v‰n
     * ruudukon koon.
     */
    private Wrapper()
    {
    	// Lasketaan osumatarkistuksessa k‰ytett‰vien "ruutujen" koko
    	gridSize = (int) (((Options.screenWidth * Options.scaleX) / 20) * 3);
    	
    	// Alustetaan taulukot
        allies                   = new ArrayList<Ally>();
        enemies                  = new ArrayList<Enemy>();
        projectiles              = new ArrayList<AbstractProjectile>();
        guiObjects               = new ArrayList<GuiObject>();
        obstacles                = new ArrayList<Obstacle>();
        effects                  = new ArrayList<EffectObject>();
        messages                 = new ArrayList<Message>();
        collectables             = new ArrayList<Collectable>();
        backgroundStars          = new ArrayList<BackgroundStar>();
        allyStates               = new ArrayList<Integer>();
        enemyStates              = new ArrayList<Integer>();
        projectileStates         = new ArrayList<Integer>();
        guiObjectStates          = new ArrayList<Integer>();
        obstacleStates           = new ArrayList<Integer>();
        effectStates             = new ArrayList<Integer>();
        messageStates            = new ArrayList<Integer>();
        collectableStates        = new ArrayList<Integer>();
        priorityOneAllies        = new ArrayList<Integer>();
        priorityTwoAllies        = new ArrayList<Integer>();
        priorityThreeAllies      = new ArrayList<Integer>();
        priorityFourAllies       = new ArrayList<Integer>();
        priorityOneEnemies       = new ArrayList<Integer>();
        priorityTwoEnemies       = new ArrayList<Integer>();
        priorityThreeEnemies     = new ArrayList<Integer>();
        priorityFourEnemies      = new ArrayList<Integer>();
        priorityOneProjectiles   = new ArrayList<Integer>();
        priorityTwoProjectiles   = new ArrayList<Integer>();
        priorityThreeProjectiles = new ArrayList<Integer>();
        priorityFourProjectiles  = new ArrayList<Integer>();
    }
    
    /**
     * Palauttaa osoittimen t‰h‰n luokkaan.
     * 
     * @return Osoitin t‰h‰n luokkaan
     */
    synchronized public static Wrapper getInstance()
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
     * @param Object Lis‰tt‰v‰ objekti
     * @param int    Lis‰tt‰v‰n objektin tyyppi
     * @param int    Objektin t‰rkeys (m‰‰ritt‰‰ teko‰lyn p‰ivitysnopeuden)
     * 
     * @return int Lis‰tyn objektin tunnus piirtolistalla
     */
    public final int addToList(Object _object, int _classType, int _priority)
    {
        if (_classType == CLASS_TYPE_PLAYER) {
            player      = (Player)_object;
            playerState = 1;
        }
        else if (_classType == CLASS_TYPE_ALLY) {
            allies.add((Ally)_object);
            allyStates.add(0);
            
            if (_priority == 1) {
                priorityOneAllies.add(allyStates.size()-1);
            }
            else if (_priority == 2) {
                priorityTwoAllies.add(allyStates.size()-1);
            }
            else if (_priority == 3) {
                priorityThreeAllies.add(allyStates.size()-1);
            }
            else if (_priority == 4) {
                priorityFourAllies.add(allyStates.size()-1);
            }

            return allyStates.size()-1;
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
        else if (_classType == CLASS_TYPE_OBSTACLE) {
        	obstacles.add((Obstacle)_object);
        	obstacleStates.add(1);

            return obstacleStates.size()-1;
        }
        else if (_classType == CLASS_TYPE_EFFECT) {
        	effects.add((EffectObject)_object);
        	effectStates.add(1);

            return effectStates.size()-1;
        }
        else if (_classType == CLASS_TYPE_MESSAGE) {
        	messages.add((Message)_object);
        	messageStates.add(0);
        	
        	return messageStates.size()-1;
        }
        else if (_classType == CLASS_TYPE_COLLECTABLE) {
        	collectables.add((Collectable)_object);
        	collectableStates.add(1);

            return collectableStates.size()-1;
        }
        else if (_classType == CLASS_TYPE_BACKGROUNDSTAR) {
        	backgroundStars.add((BackgroundStar)_object);
        	// Tila aina 1
        }
        else if (_classType == CLASS_TYPE_MOTHERSHIP) {
        	mothership      = (Mothership)_object;
        	// Tila aina 1
        }
        
        return 0;
    }
}
