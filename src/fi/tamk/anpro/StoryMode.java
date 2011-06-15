package fi.tamk.anpro;

public class StoryMode extends AbstractMode {
    private static StoryMode instance = null;
    
    // Ensimm�inen ulottuvuus = rivit
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
    
    // Tallennetaan reunojen koordinaatit taulukkoon kameran sijainnin muutoksen m��r�n mukaan
    // 		{ {vasen reuna X,Y}, {vasen yl�reuna X,Y}, {yl�reuna X,Y}, {oikea yl�reuna X,Y},
    // 		  {oikea reuna X,Y}, {oikea alareuna X,Y}, {alareuna X,Y}, {vasen alareuna X,Y} }
    // index:  		  0    0 1           1       0 1        2    0 1             3	  0 1
    int spawnPointCoords[][] = {{(-(screenWidth / 2) /*+ KAMERANMUUTOKSEN_X_M��R�*/), ( 0 /*+ KAMERANMUUTOKSEN_Y_M��R�*/)}, // vasen reuna
                                {(-(screenWidth / 2) /*+ KAMERANMUUTOKSEN_X_M��R�*/), ( (screenHeight / 2) /*+ KAMERANMUUTOKSEN_Y_M��R�*/)}, // vasen yl�reuna
                                {( 0 /*+ KAMERANMUUTOKSEN_X_M��R�*/), ( (screenHeight / 2) /*+ KAMERANMUUTOKSEN_Y_M��R�*/)}, // yl�reuna
                                {( (screenWidth / 2) /*+ KAMERANMUUTOKSEN_X_M��R�*/), ( (screenHeight / 2) /*+ KAMERANMUUTOKSEN_Y_M��R�*/)}, // oikea yl�reuna
                                {( (screenWidth / 2) /*+ KAMERANMUUTOKSEN_X_M��R�*/), ( 0 /*+ KAMERANMUUTOKSEN_Y_M��R�*/)}, // oikea reuna
                                {( (screenWidth / 2) /*+ KAMERANMUUTOKSEN_X_M��R�*/), (-(screenHeight / 2) /*+ KAMERANMUUTOKSEN_Y_M��R�*/)}, // oikea alareuna
                                {( 0 /*+ KAMERANMUUTOKSEN_X_M��R�*/), (-(screenHeight / 2) /*+ KAMERANMUUTOKSEN_Y_M��R�*/)}, // alareuna
                                {(-(screenWidth / 2) /*+ KAMERANMUUTOKSEN_X_M��R�*/), (-(screenHeight / 2) /*+ KAMERANMUUTOKSEN_Y_M��R�*/)}  // vasen alareuna
    };
    
    /*
     * Hakee, satunnoi ja asettaa vihollisten aloituspisteet
     */
    /*public void setSpawnPoints() {
        for (int index = 0; index < 8; ++index) {
            if (spawnPoints[][][] == {0) {
                // Asettele viholliset randomilla ymp�ri kentt��
            }
            else if(spawnPoints[][][] == 1) {
                // Asettele viholliset kent�n vasempaan reunaan
            }
            else if(spawnPoints[][][] == 2) {
                // Asettele viholliset kent�n yl�reunaan
            }
            else if(spawnPoints[][][] == 3) {
                // Asettele viholliset kent�n oikeaan reunaan
            }
            else if(spawnPoints[][][] == 4) {
                // Asettele viholliset kent�n alareunaan
            }
        }
    }*/
    
    /*
       1. [mille reunalle viholliset tulevat] <- hae t�m� xmlReaderilla
       2. [spawnpointin j�rjestysnumero] <- n�it� aluksi 3 jokaiselle spawnpointille
       3. [vihollisen x- ja y-sijainnit] <- esim. vasempaan reunaan esim��ritettyjen x- ja y-koordinaattien lis�ksi n�m�
           (leftSpawnPointX + joko -1* tai 1* spawnPoints[][][x,y] (x- ja y-arvot taulukosta)
    */

    // [1][0][0] = vasempaan reunaan ilmestyv�n vihollisen x-koord. (ensimm�inen spawnpoint)
    // [1][0][1] = vasempaan reunaan ilmestyv�n vihollisen y-koord. (ensimm�inen spawnpoint)
    
    // [1][1][0] = vasempaan reunaan ilmestyv�n vihollisen x-koord. (toinen spawnpoint)
    // [1][1][1] = vasempaan reunaan ilmestyv�n vihollisen y-koord. (toinen spawnpoint)
    
    // [4][2][0] = alareunaan ilmestyv�n vihollisen x-koord. (kolmas spawnpoint)
    // [4][2][1] = alareunaan ilmestyv�n vihollisen y-koord. (kolmas spawnpoint)
    
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
    
    //Avataan l�p�istyt kent�t pelaajalle
    //Reset-napista muokataan LevelNumber = 1 ja muut muuttujat kohdilleen, jolloin pelaaja voi aloittaa alusta..
    public int LevelNumber = XmlReader.readStoryMode("LevelNumber");
    
    //skillTree aloittaa default-aseen p�ivitystasolla 1
    public int skillTree[][] = XmlReader.readStoryMode(, "skillTree");
        if (skillTree[0][0] == {0}) {
            skillTree[0][0] = {1}
        }
    
    */
}
