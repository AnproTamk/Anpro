package fi.tamk.anpro;

import java.util.ArrayList;

public class Wrapper {
    private static Wrapper instance = null;
    
    public GLRenderer renderer;
    
    //Wrapperin rakentaja
    protected Wrapper() {
    	players = new ArrayList<Player>();
    	enemies = new ArrayList<Enemy>();
    	playerStates = new ArrayList<Integer>();
    	enemyStates = new ArrayList<Integer>();
    }
    
    public static Wrapper getInstance() {
        if(instance == null) {
            instance = new Wrapper();
        }
        return instance;
    }
	
	// Listat piirrettävistä objekteista
	public ArrayList<Player> players = null;
	public ArrayList<Enemy> enemies = null;

	// Listat objektien tiloista
	public ArrayList<Integer> playerStates = null;
	public ArrayList<Integer> enemyStates = null;
	
	public int addToList(Object _object){
		if (_object instanceof Player) {
			players.add((Player)_object);
			playerStates.add(1);
		}
		else if (_object instanceof Enemy) {
			enemies.add((Enemy)_object);
			enemyStates.add(1);
		}

		return enemyStates.size()-1;
	}
	
	public void setRenderer(GLRenderer _renderer) {
		renderer = _renderer;
	}
}
