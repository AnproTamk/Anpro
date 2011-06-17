package fi.tamk.anpro;

/**
 * Sis‰lt‰‰ kaikkien aseiden yhteiset ominaisuudet.
 * 
 * T‰m‰ luokka on abstrakti, joten siit‰ ei voi luoda olioita.
 */
abstract public class AbstractWeapon
{
	
	
    /**
     * Alustaa luokan muuttujat.
     */
	public AbstractWeapon() { }
    
    /**
     * Aktivoi ammukset. T‰st‰ eteenp‰in ammusten oma teko‰ly hoitaa niiden
     * p‰ivitt‰misen.
     * 
     * @param int Kohteen X-koordinaatti
     * @param int Kohteen Y-koordinaatti
     */
	abstract public void activate(int _x, int _y);
}
