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
    
    /*Hae Xml-Readerilla XML-tiedostosta tallennetut tiedot
    
    //Avataan läpäistyt kentät pelaajalle
    //Reset-napista muokataan LevelNumber = 1 ja muut muuttujat kohdilleen, jolloin pelaaja voi aloittaa alusta..
    public int PlayerHealth = XmlReader.readSurvivalMode("Player", "Health");
    public int PlayerDefence = XmlReader.readSurvivalMode("Player", "Defence");
    public int PlayerCurrentWeapon = 0;
    

    */
    public void initWeapons(){
    	weapons.add(new Weapon(0, 0, 0));
        weapons.get(0).weaponDefault = new WeaponDefault();
    }
    
    
    
    public int waveNumber = 0;
}
