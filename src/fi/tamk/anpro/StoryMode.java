package fi.tamk.anpro;

import android.content.Context;
import android.util.DisplayMetrics;

/**
 * Story-pelitila. Sis‰lt‰‰ kaikki tarvittavat peliobjektit ja hallitsee
 * teht‰v‰t, kartan yms.
 */
public class StoryMode extends AbstractMode
{
	// Kartan objektit
	private Obstacle[] planets;
	private Obstacle[] asteroids;
	private Obstacle[] stars;
	
	// Kykypuu ja -pisteet
	//private SkillTree skillTree;
	
	// Vihollisten spawnpointit
	private int[][] spawnPoints;
	
	// Teht‰v‰t
	//private ArrayList<task> tasks;
	
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
		super(_gameActivity, _dm);
		
		// Luetaan StoryModesta haluttu kentt‰
		
		// Kutsutaan WeaponManagerin initialize-funktiota
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
	private void addEnemy()
	{
		// ...
	}
	
	/**
	 * Siirt‰‰ pelitilan kykypuuvalikkoon.
	 */
	private void moveToSkillTree()
	{
		// ...
	}
}
