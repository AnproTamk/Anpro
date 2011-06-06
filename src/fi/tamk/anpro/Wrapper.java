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
	public ArrayList<Player> players = null;
	public ArrayList<Enemy> enemies = null;

	// Listat objektien tiloista
	public ArrayList<Integer> playerStates = null;
	public ArrayList<Integer> enemyStates = null;
	
	public int addToList(Object _object){
		if (_object instanceof Player) {
			players.add((Player)_object);
			playerIds.add(1);
		}
		else if (_object instanceof Enemy) {
			enemies.add((Enemy)_object);
			enemyIds.add(1);
		}

		return enemyIds.size()-1;
	}
}
