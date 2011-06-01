package fi.tamk.anpro;
import java.util.ArrayList;

public class SurvivalMode {
    private static SurvivalMode instance = null;
    
    //StoryModen rakentaja
    protected SurvivalMode() { 
    	/*
    	 * XmlReader reader = new XmlReader();
    	 * reader.readWeapons(weapons);
    	 */
    }
    
    public static SurvivalMode getInstance() {
        if(instance == null) {
            instance = new SurvivalMode();
        }
        return instance;
    }
    
    //cooldown-ajat aseille
    public ArrayList<Integer> cooldownTime;
    
    public ArrayList<Weapon> weapons;
}
