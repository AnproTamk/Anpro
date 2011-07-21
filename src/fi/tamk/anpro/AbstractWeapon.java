package fi.tamk.anpro;

/**
 * Sisältää kaikkien aseiden yhteiset ominaisuudet.
 */
abstract public class AbstractWeapon
{
	// Osoitin Wrapperiin
	protected Wrapper wrapper;

	/**
     * Alustaa luokan muuttujat ja luo tarvittavan määrän ammuksia.
     * 
     * @param Wrapper Osoitin Wrapperiin
     * @param int     Aseen käyttäjän tyyppi
     */
    public AbstractWeapon(Wrapper _wrapper, int _userType)
    {
    	/* Tallennetaan muuttujat */
    	wrapper = _wrapper;
    }
    
    /* =======================================================
     * Perityt funktiot
     * ======================================================= */    
    /**
     * Aktivoi ammukset. Tästä eteenpäin ammusten oma tekoäly hoitaa niiden
     * päivittämisen.
     * 
     * @param int Kohteen X-koordinaatti
     * @param int Kohteen Y-koordinaatti
     * @param int Lähtöpisteen X-koordinaatti
     * @param int Lähtöpisteen Y-koordinaatti
     */
    public void activate(float _targetX, float _targetY, float _startX, float _startY) { }
    
    /**
     * Aktivoi ammukset. Tästä eteenpäin ammusten oma tekoäly hoitaa niiden
     * päivittämisen.
     * 
     * @param int[][] Ammuksen reitti
     * @param int 	  Lähtöpisteen X-koordinaatti
     * @param int 	  Lähtöpisteen Y-koordinaatti
     */
    public void activate(int[][] _path, float _startX, float _startY) { }

    
    /* =======================================================
     * Uudet funktiot
     * ======================================================= */
    /**
     * Aiheuttaa räjähdyksen, jossa ammus jakaantuu useammaksi eri ammukseksi.
     * 
     * @param int   Aktivoitavien ammusten määrä 
     * @param float Aloituskohdan X-koordinaatti
     * @param float Aloituskohdan Y-koordinaatti
     */
    public void triggerClusterExplosion(int _amount, float _startX, float _startY) { }
}
