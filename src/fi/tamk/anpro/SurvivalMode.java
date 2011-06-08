package fi.tamk.anpro;
import java.util.ArrayList;

public class SurvivalMode {
    private static SurvivalMode instance = null;
    
    public int waves[][];
    public int enemyStats[][];
    public ArrayList<Enemy> enemies;
    
    //StoryModen rakentaja
    protected SurvivalMode() {
    	waves = new int[100][10];
    }
    
    public static SurvivalMode getInstance() {
        if(instance == null) {
            instance = new SurvivalMode();
        }
        return instance;
    }
    
    
}
