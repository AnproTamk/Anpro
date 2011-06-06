package fi.tamk.anpro;

import java.util.ArrayList;

public class Wrapper {
    private static Wrapper instance = null;
    
    //Wrapperin rakentaja
    protected Wrapper() { }
    
    public static Wrapper getInstance() {
        if(instance == null) {
            instance = new Wrapper();
        }
        return instance;
    }
	
	// Listat piirrettävistä objekteista
	public ArrayList<Player> playersToDraw = null;
	//public ArrayList<Enemy> enemiesToDraw = null;
	public ArrayList<ProjectileLaser> projectileLasersToDraw = null;
	
	// Listat päivitettävistä objekteista
	public ArrayList<Player> playersToUpdate = null;
	//public ArrayList<Enemy> enemiesToUpdate = null;
	public ArrayList<ProjectileLaser> projectileLasersToUpdate = null;
}
