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
    

    /*Hae Xml-Readerilla XML-tiedostosta tallennetut tiedot
    
    //Avataan läpäistyt kentät pelaajalle
    //Reset-napista muokataan LevelNumber = 1 ja muut muuttujat kohdilleen, jolloin pelaaja voi aloittaa alusta..
    public int LevelNumber = XmlReader.readStoryMode("LevelNumber");
    
    //skillTree aloittaa default-aseen päivitystasolla 1
    public int skillTree[][] = XmlReader.readStoryMode(, "skillTree");
    	if (skillTree[0][0] == {0}) {
    		skillTree[0][0] = {1}
    	}
    
    */
    
    
    
}
