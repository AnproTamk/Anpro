package fi.tamk.anpro;

import java.util.ArrayList;

import android.content.Context;
import android.util.DisplayMetrics;

/**
 * Sis‰lt‰‰ pelitilojen yhteiset ominaisuudet.
 */
abstract public class AbstractMode
{
    
    /**
     * Alustaa luokan muuttujat, lukee pelitilan tarvitsemat tiedot ja k‰ynnist‰‰ pelin.
     * 
     * @param GameActivity   Pelitilan aloittava aktiviteetti
     * @param DisplayMetrics N‰ytˆn tiedot
     */
    public AbstractMode(GameActivity _gameActivity, DisplayMetrics _dm, Context _context)
    {
    	
    }
    
    /**
     * K‰ynnist‰‰ uuden vihollisaallon asettamalla siihen kuuluvat viholliset aktiivisiksi.
     */
    public void startWave() { }

    /**
     * L‰hett‰‰ pisteet GameActivitylle, joka siirt‰‰ pelin Highscores-valikkoon.
     */
	public void endGameMode() { }
}
