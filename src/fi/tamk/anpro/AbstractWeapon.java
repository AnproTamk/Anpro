package fi.tamk.anpro;

/**
 * Sis�lt�� kaikkien aseiden yhteiset ominaisuudet.
 */
abstract public class AbstractWeapon
{
	// Osoitin Wrapperiin
	protected Wrapper wrapper;

	/**
     * Alustaa luokan muuttujat ja luo tarvittavan m��r�n ammuksia.
     * 
     * @param Wrapper Osoitin Wrapperiin
     * @param int     Aseen k�ytt�j�n tyyppi
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
     * Aktivoi ammukset. T�st� eteenp�in ammusten oma teko�ly hoitaa niiden
     * p�ivitt�misen.
     * 
     * @param int Kohteen X-koordinaatti
     * @param int Kohteen Y-koordinaatti
     * @param int L�ht�pisteen X-koordinaatti
     * @param int L�ht�pisteen Y-koordinaatti
     */
    public void activate(float _targetX, float _targetY, float _startX, float _startY) { }
    
    /**
     * Aktivoi ammukset. T�st� eteenp�in ammusten oma teko�ly hoitaa niiden
     * p�ivitt�misen.
     * 
     * @param int[][] Ammuksen reitti
     * @param int 	  L�ht�pisteen X-koordinaatti
     * @param int 	  L�ht�pisteen Y-koordinaatti
     */
    public void activate(int[][] _path, float _startX, float _startY) { }

    
    /* =======================================================
     * Uudet funktiot
     * ======================================================= */
    /**
     * Aiheuttaa r�j�hdyksen, jossa ammus jakaantuu useammaksi eri ammukseksi.
     * 
     * @param int   Aktivoitavien ammusten m��r� 
     * @param float Aloituskohdan X-koordinaatti
     * @param float Aloituskohdan Y-koordinaatti
     */
    public void triggerClusterExplosion(int _amount, float _startX, float _startY) { }
}
