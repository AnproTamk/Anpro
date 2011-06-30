package fi.tamk.anpro;

/**
 * Sisältää kaikkien aseiden yhteiset ominaisuudet.
 * 
 * Tämä luokka on abstrakti, joten siitä ei voi luoda olioita.
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
     * Aktivoi ammukset. Tästä eteenpäin ammusten oma tekoäly hoitaa niiden
     * päivittämisen.
     * 
     * @param int Kohteen X-koordinaatti
     * @param int Kohteen Y-koordinaatti
     * @param int Lähtöpisteen X-koordinaatti
     * @param int Lähtöpisteen Y-koordinaatti
     * 
     * TODO: ...
     */
    public void activate(int _targetX, int _targetY, float _startX, float _startY) { }
    public void activate(int[][] _path, float _startX, float _startY) { }

    /**
     * Aktivoi räjähdyksessä tarvittavat ammukset. 
     * 
     * @param int   Aktivoitavien ammusten määrä 
     * @param float Aloituskohdan X-koordinaatti
     * @param float Aloituskohdan Y-koordinaatti
     */
    public void triggerCluster(int _amount, float _startX, float _startY) { }
}
