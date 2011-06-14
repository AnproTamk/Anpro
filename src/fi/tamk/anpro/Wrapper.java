package fi.tamk.anpro;

import java.util.ArrayList;

public class Wrapper {
    private static Wrapper instance = null;
    
    public GLRenderer renderer;
	
	// Listat piirrettävistä objekteista
	public Player                     player           = null;
	public ArrayList<Enemy>           enemies          = null;
	public ArrayList<ProjectileLaser> projectileLasers = null;

	// Listat objektien tiloista
	public int                playerState           = 0;
	public ArrayList<Integer> enemyStates           = null;
	public ArrayList<Integer> projectileLaserStates = null;
	
	// HUD-objektit
	//...
    
    //Wrapperin rakentaja
    protected Wrapper() {
    	enemies               = new ArrayList<Enemy>();
    	projectileLasers      = new ArrayList<ProjectileLaser>();
    	enemyStates           = new ArrayList<Integer>();
    	projectileLaserStates = new ArrayList<Integer>();
    }
    
    public static Wrapper getInstance() {
        if(instance == null) {
            instance = new Wrapper();
        }
        return instance;
    }
	
	public int addToList(Object _object){
		if (_object instanceof Player) {
			player      = (Player)_object;
			playerState = 1;
		}
		else if (_object instanceof Enemy) {
			enemies.add((Enemy)_object);
			enemyStates.add(1);

			return enemyStates.size()-1;
		}
		else if (_object instanceof ProjectileLaser) {
			projectileLasers.add((ProjectileLaser)_object);
			projectileLaserStates.add(0);

			return projectileLaserStates.size()-1;
		}
		else if (_object instanceof GuiObject) {
			// ...
		}
		
		return 0;
	}
	
	public void setRenderer(GLRenderer _renderer) {
		renderer = _renderer;
	}
}
