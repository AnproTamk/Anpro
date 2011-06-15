package fi.tamk.anpro;

public class StoryMode extends AbstractMode {
    private static StoryMode instance = null;
    
    // Ensimmäinen ulottuvuus = rivit
    // Toinen ulottuvuus = sarakkeet
    // Kolmas ulottuvuus = GO DEEPER.
    // spawnPoints[][][] = { {{0,1},{2,3}}, {2,3}, {4,5} }
    /*private int spawnPoints[][][] = {
            {{0,0},{0,10}},
            {{10,10},{14,20}},
            
        };
        */
    
    // Ruudun leveys ja korkeus tallennetaan muuttujiin
    int screenWidth = GLRenderer.width;
    int screenHeight = GLRenderer.height;
    
    // Tallennetaan reunojen koordinaatit taulukkoon kameran sijainnin muutoksen määrän mukaan
    // 		{ {vasen reuna X,Y}, {vasen yläreuna X,Y}, {yläreuna X,Y}, {oikea yläreuna X,Y},
    // 		  {oikea reuna X,Y}, {oikea alareuna X,Y}, {alareuna X,Y}, {vasen alareuna X,Y} }
    // index:  		  0    0 1           1       0 1        2    0 1             3	  0 1
    int spawnPointCoords[][] = {{(-(screenWidth / 2) /*+ KAMERANMUUTOKSEN_X_MÄÄRÄ*/), ( 0 /*+ KAMERANMUUTOKSEN_Y_MÄÄRÄ*/)}, // vasen reuna
                                {(-(screenWidth / 2) /*+ KAMERANMUUTOKSEN_X_MÄÄRÄ*/), ( (screenHeight / 2) /*+ KAMERANMUUTOKSEN_Y_MÄÄRÄ*/)}, // vasen yläreuna
                                {( 0 /*+ KAMERANMUUTOKSEN_X_MÄÄRÄ*/), ( (screenHeight / 2) /*+ KAMERANMUUTOKSEN_Y_MÄÄRÄ*/)}, // yläreuna
                                {( (screenWidth / 2) /*+ KAMERANMUUTOKSEN_X_MÄÄRÄ*/), ( (screenHeight / 2) /*+ KAMERANMUUTOKSEN_Y_MÄÄRÄ*/)}, // oikea yläreuna
                                {( (screenWidth / 2) /*+ KAMERANMUUTOKSEN_X_MÄÄRÄ*/), ( 0 /*+ KAMERANMUUTOKSEN_Y_MÄÄRÄ*/)}, // oikea reuna
                                {( (screenWidth / 2) /*+ KAMERANMUUTOKSEN_X_MÄÄRÄ*/), (-(screenHeight / 2) /*+ KAMERANMUUTOKSEN_Y_MÄÄRÄ*/)}, // oikea alareuna
                                {( 0 /*+ KAMERANMUUTOKSEN_X_MÄÄRÄ*/), (-(screenHeight / 2) /*+ KAMERANMUUTOKSEN_Y_MÄÄRÄ*/)}, // alareuna
                                {(-(screenWidth / 2) /*+ KAMERANMUUTOKSEN_X_MÄÄRÄ*/), (-(screenHeight / 2) /*+ KAMERANMUUTOKSEN_Y_MÄÄRÄ*/)}  // vasen alareuna
    };
    
    /*
     * Hakee, satunnoi ja asettaa vihollisten aloituspisteet
     */
    /*public void setSpawnPoints() {
        for (int index = 0; index < 8; ++index) {
            if (spawnPoints[][][] == {0) {
                // Asettele viholliset randomilla ympäri kenttää
            }
            else if(spawnPoints[][][] == 1) {
                // Asettele viholliset kentän vasempaan reunaan
            }
            else if(spawnPoints[][][] == 2) {
                // Asettele viholliset kentän yläreunaan
            }
            else if(spawnPoints[][][] == 3) {
                // Asettele viholliset kentän oikeaan reunaan
            }
            else if(spawnPoints[][][] == 4) {
                // Asettele viholliset kentän alareunaan
            }
        }
    }*/
    
    /*
       1. [mille reunalle viholliset tulevat] <- hae tämä xmlReaderilla
       2. [spawnpointin järjestysnumero] <- näitä aluksi 3 jokaiselle spawnpointille
       3. [vihollisen x- ja y-sijainnit] <- esim. vasempaan reunaan esimääritettyjen x- ja y-koordinaattien lisäksi nämä
           (leftSpawnPointX + joko -1* tai 1* spawnPoints[][][x,y] (x- ja y-arvot taulukosta)
    */

    // [1][0][0] = vasempaan reunaan ilmestyvän vihollisen x-koord. (ensimmäinen spawnpoint)
    // [1][0][1] = vasempaan reunaan ilmestyvän vihollisen y-koord. (ensimmäinen spawnpoint)
    
    // [1][1][0] = vasempaan reunaan ilmestyvän vihollisen x-koord. (toinen spawnpoint)
    // [1][1][1] = vasempaan reunaan ilmestyvän vihollisen y-koord. (toinen spawnpoint)
    
    // [4][2][0] = alareunaan ilmestyvän vihollisen x-koord. (kolmas spawnpoint)
    // [4][2][1] = alareunaan ilmestyvän vihollisen y-koord. (kolmas spawnpoint)
    
    // [2][1][0] = x-koord.
    
    /*
     * Rakentaja
     */
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

    @Override
    public void startWave() {
        // TODO Auto-generated method stub
        
    }

    @Override
    protected void updateSpawnPoints() {
        // TODO Auto-generated method stub
        
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
