package fi.tamk.anpro;

/**
 * Hallitsee pelin kameran tilan ja sen liikuttamisen.
 */
public class CameraManager
{
	/* Kameran sijainti */
	public int x    = 0; // Kameran x-koordinaatti
	public int y    = 0; // Kameran y-koordinaatti
	public int zoom = 0; // Kameran zoomi
	
	/* Kameran liikkuminen */
	public int speed; 		 // Kameran nopeus
	public int acceleration; // Kameran kiihtyvyys
	public int targetX;		 // Kameran kohteen x-koordinaatti
	public int targetY;		 // Kameran kohteen y-koordinaatti
	
	/* Osoitin tähän luokkaan */
    private static CameraManager instance = null;
    
    /**
     * Alustaa luokan muuttujat.
     */
    private CameraManager()
    {
    	// ...
    }
    
    /**
     * Palauttaa osoittimen tästä luokasta.
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
}
