package fi.tamk.anpro;

/**
 * Hallitsee kameraa tallentamalla sen tiedot.
 */
public class CameraManager
{
	/* Kameran sijainti */
	public int x = 0; // Kameran x-koordinaatti
	public int y = 0; // Kameran y-koordinaatti
	public int zoom;  // Kameran zoomi
	
	/* Kameran liikkuminen */
	public int speed; 		 // Kameran nopeus
	public int acceleration; // Kameran kiihtyvyys
	public int targetX;		 // Kameran kohteen x-koordinaatti
	public int targetY;		 // Kameran kohteen y-koordinaatti
	
	/* Osoitin tähän luokkaan */
    private static CameraManager instance = null;
    
    //Wrapperin rakentaja
    private CameraManager()
    {
    	// ...
    }
    
    public static CameraManager getInstance()
    {
        if(instance == null) {
            instance = new CameraManager();
        }
        return instance;
    }
}
