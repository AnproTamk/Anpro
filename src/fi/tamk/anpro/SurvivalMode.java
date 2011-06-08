package fi.tamk.anpro;
import java.util.ArrayList;

public class SurvivalMode {
    private static SurvivalMode instance = null;
    
    public int waves[][];
    ArrayList<Enemy> enemies;
    
    //StoryModen rakentaja
    protected SurvivalMode() {
    	// ...
    }
    
    public static SurvivalMode getInstance() {
        if(instance == null) {
            instance = new SurvivalMode();
        }
        return instance;
    }
    
    
}
