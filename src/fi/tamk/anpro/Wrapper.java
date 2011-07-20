package fi.tamk.anpro;

import java.util.ArrayList;

/**
 * Yhdistää luokat toisiinsa ylläpitämällä piirtolistoja, objektien tiloja
 * ja tekoälyjen päivitysnopeuksia.
 */
public class Wrapper
{
    /* Osoitin tähän luokkaan (singleton-toimintoa varten) */
    private static Wrapper instance = null;

    /* Vakioita */
    // Luokan tyyppi (käytetään myös aseissa ja ammuksissa)
    public static final int CLASS_TYPE_PLAYER         = 1;
    public static final int CLASS_TYPE_ALLY           = 5;
    public static final int CLASS_TYPE_ENEMY          = 2;
    public static final int CLASS_TYPE_PROJECTILE     = 3;
    public static final int CLASS_TYPE_GUI            = 4;
    public static final int CLASS_TYPE_OBSTACLE       = 6;
    public static final int CLASS_TYPE_BACKEFFECT     = 7;
    public static final int CLASS_TYPE_FRONTEFFECT    = 11;
    public static final int CLASS_TYPE_COLLECTABLE    = 8;
    public static final int CLASS_TYPE_BACKGROUNDSTAR = 9;
    public static final int CLASS_TYPE_MOTHERSHIP     = 10;

    // Objektien tilat (ks. projektin Wiki)
    public static final int INACTIVE               = 0;
    public static final int FULL_ACTIVITY          = 1;
    public static final int ONLY_ANIMATION         = 2;
    public static final int ANIMATION_AND_MOVEMENT = 3;

    /* Piirtolista ja linkityslista */
    public ArrayList<GfxObject> drawables   = null;
    public ArrayList<AiObject>  aiGroupOne  = null;
    public ArrayList<AiObject> aiGroupTwo   = null;
    public ArrayList<AiObject> aiGroupThree = null;

    public Player                        player          = null;
    public Mothership 					 mothership      = null;
    public ArrayList<Ally>               allies          = null;
    public ArrayList<Enemy>              enemies         = null;
    public ArrayList<AbstractProjectile> projectiles     = null;
    //public ArrayList<GuiObject> 		 guiObjects      = null;
    public ArrayList<Obstacle>  		 obstacles       = null;
    //public ArrayList<EffectObject> 	     backEffects     = null;
    //public ArrayList<EffectObject> 	     frontEffects    = null;
    //public ArrayList<Message>            messages        = null;
    public ArrayList<Collectable> 	     collectables    = null;
    //public ArrayList<BackgroundStar>     backgroundStars = null;

    /* Osumatarkistuksen ruudukon yhden ruudun leveys/korkeus */
    public static int gridSize;

    /**
     * Alustaa luokan muuttujat ja määrittelee osumatarkistuksissa käytettävän
     * ruudukon koon.
     */
    private Wrapper()
    {
        // Lasketaan osumatarkistuksessa käytettävien "ruutujen" koko
        gridSize = (int) (((Options.screenWidth * Options.scaleX) / 20) * 10);

        // Alustetaan taulukot
        drawables    = new ArrayList<GfxObject>();
        aiGroupOne   = new ArrayList<AiObject>();
        aiGroupTwo   = new ArrayList<AiObject>();
        aiGroupThree = new ArrayList<AiObject>();
        
        allies       = new ArrayList<Ally>();
        enemies      = new ArrayList<Enemy>();
        projectiles  = new ArrayList<AbstractProjectile>();
        obstacles    = new ArrayList<Obstacle>();
        collectables = new ArrayList<Collectable>();
    }

    /**
     * Palauttaa osoittimen tähän luokkaan.
     *
     * @return Osoitin tähän luokkaan
     */
    synchronized public static Wrapper getInstance()
    {
        if(instance == null) {
            instance = new Wrapper();
        }

        return instance;
    }

    synchronized public static void destroy()
    {
        instance = null;
    }

    /**
     * Lisää parametrina annetun luokan piirtolistalle ja tallentaa objektin tilan.
     *
     * Objektin tärkeys määritetään seuraavasti:
     * 		-------------------------------------
     * 		| TÄRKEYS | TEKOÄLYN PÄIVITYSTIHEYS |
     * 		-------------------------------------
     * 		|    0    |      Ei päivitetä       |
     * 		|    1    |         400 ms          |
     * 		|    2    |         200 ms          |
     * 		|    3    |         100 ms          |
     * 		|    4    |          50 ms          |
     * 		-------------------------------------
     *
     * Objektin tärkeys käytetään ainoastaan kertomaan pelisäikeelle (GameThread) aika,
     * joka sen on odotettava jokaisen päivityksen välillä. Objektit lisätään tärkeyslistoihin
     * niiden tärkeyden mukaan, joita pelisäie lukee. Tällä pyritään välttämään kaikkien
     * objektien läpi käyminen, vaikka haluttaisiin päivittää vain kriittisimmät.
     *
     * @param Object Lisättävä objekti
     * @param int    Lisättävän objektin tyyppi
     * @param int    Objektin tärkeys (määrittää tekoälyn päivitysnopeuden)
     *
     * @return int Lisätyn objektin tunnus piirtolistalla
     */
    public final void addToDrawables(GfxObject _object)
    {
        drawables.add(_object);

        if (_object instanceof Player) {
            player = (Player) _object;
        }
        else if (_object instanceof Enemy) {
            enemies.add((Enemy) _object);
        }
        else if (_object instanceof Ally) {
            allies.add((Ally) _object);
        }
        else if (_object instanceof Mothership) {
            mothership = (Mothership) _object;
        }
        else if (_object instanceof AbstractProjectile) {
            projectiles.add((AbstractProjectile) _object);
        }
        else if (_object instanceof Obstacle) {
            obstacles.add((Obstacle) _object);
        }
        else if (_object instanceof Collectable) {
            collectables.add((Collectable) _object);
        }
    }

    public final void sortDrawables()
    {
        GfxObject temp1;
        Integer   temp2;

        for (int i = drawables.size()-1; i >= 1; --i) {
            for (int j = drawables.size()-1; j >= 1; --j) {
                if (drawables.get(j).z > drawables.get(j-1).z) {

                    temp1 = drawables.get(j);
                    drawables.set(j, drawables.get(j-1));
                    drawables.set(j-1, temp1);
                }
            }
        }
    }

    public final void generateAiGroups()
    {
    	// TODO: Optimoi.
    	
        for (Object object : drawables) {
        	try {
	        	object.getClass().getField("aiPriority");
	        	if (((AiObject)object).aiPriority == 1) {
	        		aiGroupOne.add((AiObject)object);
	        	}
	        	else if (((AiObject)object).aiPriority == 2) {
	        		aiGroupTwo.add((AiObject)object);
	        	}
	        	else if (((AiObject)object).aiPriority == 3) {
	        		aiGroupThree.add((AiObject)object);
	        	}
        	}
        	catch (NoSuchFieldException e) {
        		// Luokasta ei löytynyt aiPriority-muuttujaa.
        		// Siirrytään seuraavaan luokkaan.
        	}
        }
    }
}
