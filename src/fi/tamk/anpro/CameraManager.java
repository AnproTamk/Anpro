package fi.tamk.anpro;

/**
 * Hallitsee pelin kameran tilan ja sen liikuttamisen.
 */
public class CameraManager
{
	/* Kameran sijainti */
	public float xTranslate = 0; // Muutos X-akselilla (pelaajan et‰isyys keskipisteest‰)
	public float yTranslate = 0; // Muutos Y-akselilla (pelaajan et‰isyys keskipisteest‰)
	public int   zoom       = 0; // Kameran zoomi
	
	/* Kameran liikkuminen */
	public int speed; 		 // Kameran nopeus
	public int acceleration; // Kameran kiihtyvyys
	public int targetX;		 // Kameran kohteen x-koordinaatti
	public int targetY;		 // Kameran kohteen y-koordinaatti
	
	/* Osoitin t‰h‰n luokkaan */
    private static CameraManager instance = null;
    
    /* Osoitin Wrapperiin */
    private Wrapper wrapper;
    
    /**
     * Alustaa luokan muuttujat.
     */
    private CameraManager()
    {
    	wrapper = Wrapper.getInstance();
    }
    
    /**
     * Palauttaa osoittimen t‰st‰ luokasta.
     * 
     * @return CameraManager
     */
    public static CameraManager getInstance()
    {
        if(instance == null) {
            instance = new CameraManager();
        }
        return instance;
    }
    
    /**
     * P‰ivitt‰‰ kameran sijainnin.
     */
    public void updateCameraPosition()
    {
    	if (wrapper.player != null) {
    		xTranslate = wrapper.player.x;
    		yTranslate = wrapper.player.y;
    	}
    }
}
