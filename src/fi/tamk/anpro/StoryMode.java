package fi.tamk.anpro;

import android.content.Context;
import android.util.DisplayMetrics;

/**
 * Story-pelitila. Sis‰lt‰‰ kaikki tarvittavat peliobjektit ja hallitsee
 * teht‰v‰t, kartan yms.
 */
public class StoryMode extends AbstractMode
{
	/* Kartta (obstacles) */
	private Obstacle[] planets;
	private Obstacle[] asteroids;
	private Obstacle[] stars;
	
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
	}
    
    /**
     * K‰ynnist‰‰ uuden vihollisaallon asettamalla siihen kuuluvat viholliset aktiivisiksi.
     */
	@Override
	public void startWave()
	{
		
	}
	
}
