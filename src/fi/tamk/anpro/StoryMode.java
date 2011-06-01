package fi.tamk.anpro;
import java.util.ArrayList;

import fi.tamk.anpro.R;

public class StoryMode {
    private static StoryMode instance = null;
    
    //StoryModen rakentaja
    protected StoryMode() { 
    	/*
    	 * XmlReader reader = new XmlReader();
    	 * reader.readWeapons(weapons);
    	 */
    }
    
    public static StoryMode getInstance() {
        if(instance == null) {
            instance = new StoryMode();
        }
        return instance;
    }
    
    //cooldown-ajat aseille
    public ArrayList<Integer> cooldownTime;
    
    public ArrayList<Weapon> weapons;
}
