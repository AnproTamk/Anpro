package fi.tamk.anpro;

/**
 * Sis‰lt‰‰ kaikkien aseiden yhteiset ominaisuudet.
 * 
 * T‰m‰ luokka on abstrakti, joten siit‰ ei voi luoda olioita.
 */
abstract public class AbstractWeapon
{
	protected Wrapper wrapper;
	
    /**
     * Alustaa luokan muuttujat.
     */
    public AbstractWeapon(Wrapper _wrapper, int _userType) {
    	wrapper = _wrapper;
    }
    
    /**
     * Aktivoi ammukset. T‰st‰ eteenp‰in ammusten oma teko‰ly hoitaa niiden
     * p‰ivitt‰misen.
     * 
     * @param int Kohteen X-koordinaatti
     * @param int Kohteen Y-koordinaatti
     */
    abstract public void activate(int _targetX, int _targetY, float _startX, float _startY);

    /**
     * Aktivoi r‰j‰hdyksess‰ tarvittavat ammukset. 
     * 
     * @param int   Aktivoitavien ammusten m‰‰r‰ 
     * @param float Aloituskohdan X-koordinaatti
     * @param float Aloituskohdan Y-koordinaatti
     */
    public void triggerCluster(int _amount, float _startX, float _startY) { }
}
