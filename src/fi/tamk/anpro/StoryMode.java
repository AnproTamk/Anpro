package fi.tamk.anpro;

import java.util.ArrayList;

import android.content.Context;
import android.util.DisplayMetrics;

/**
 * Story-pelitila. Sis‰lt‰‰ kaikki tarvittavat peliobjektit ja hallitsee
 * teht‰v‰t, kartan yms.
 */
public class StoryMode extends AbstractMode
{
	// Kartan objektit
	public ArrayList<Obstacle> planets;
	public ArrayList<Obstacle> asteroids;
	public ArrayList<Obstacle> stars;
	
	// Kykypuu ja -pisteet
	//private SkillTree skillTree;
	
	// Vihollisten spawnpointit
	//private int[][] spawnPoints;
	
	// Teht‰v‰t
	public ArrayList<Task> tasks;
	
    /**
     * Alustaa luokan muuttujat, lukee pelitilan tarvitsemat tiedot ja k‰ynnist‰‰ pelin.
     * 
     * @param GameActivity   Pelitilan aloittava aktiviteetti
     * @param DisplayMetrics N‰ytˆn tiedot
     * @param Context		 Ohjelman konteksti
     * @param WeaponManager  Osoitin WeaponManageriin
     */
	public StoryMode(GameActivity _gameActivity, DisplayMetrics _dm, Context _context, WeaponManager _weaponManager)
	{
		super(_gameActivity, _dm, _context);

        gameActivity  = _gameActivity;
        weaponManager = _weaponManager;
        
        // Alustetaan taulukot
        planets   = new ArrayList<Obstacle>();
        asteroids = new ArrayList<Obstacle>();
        stars     = new ArrayList<Obstacle>();
		
		// Luetaan StoryModesta haluttu kentt‰
        XmlReader reader = new XmlReader(_context);
        reader.readLevel((byte) 1, this, _weaponManager); // TODO: Lue kent‰n numero jostain
		
		// Kutsutaan WeaponManagerin initialize-funktiota
        //...
	}
	
	/**
	 * P‰ivitt‰‰ Story-pelitilan luomalla uusia vihollisia, antamalla uusia teht‰vi‰ jne.
	 */
	public void updateMode()
	{
		// P‰ivitet‰‰n viholliset
		
		// P‰ivitet‰‰n teht‰v‰t
	}
    
    /**
     * Lis‰‰ uuden vihollisen kent‰lle.
     */
	/*private void addEnemyToMap()
	{
		// ...
	}*/
	
	/**
	 * Siirt‰‰ pelitilan kykypuuvalikkoon.
	 */
	/*private void moveToSkillTree()
	{
		// ...
	}*/
}
