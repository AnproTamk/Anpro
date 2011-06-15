package fi.tamk.anpro;

/**
 * Sisältää kaikkien graafisten objektien yhteiset ominaisuudet ja tiedot.
 * Hallitsee esimerkiksi animaatioiden päivittämisen, käytössä olevat tekstuurit
 * (tunnukset) ja objektin sijainnin.
 */
public class GfxObject
{
	/* Animaatiot ja tekstuurit */
	//public ArrayList<Animation> animations;
	//public ArrayList<Texture>   textures;
	
    /* Objektin sijainti */
    public float x = 0;
    public float y = 0;
    
    /* Käytössä oleva animaatio ja sen tiedot */
    public  int   usedAnimation    = -1;
    public  int[] animationLength;
    public  int   currentFrame     = 0;
    private int   currentLoop      = 0;
    private int   animationLoops   = 0;
    
    /* Staattinen käytössä oleva tekstuuri */
    public int usedTexture = 0;
    
    /**
     * Alustaa luokan muuttujat.
     */
    public GfxObject()
    {
    	animationLength = new int[3];
    }
    
    /**
     * Käynnistää animaation ja määrittää toistokerrat.
     * 
     * @param int Animaation tunnus
     * @param int Toistokerrat
     */
    public void startAnimation(int _animation, int _loops)
    {
    	// Tallennetaan muuttujat
    	usedAnimation  = _animation;
    	animationLoops = _loops;
		currentFrame   = 0;
    	
		// Määritetään ensimmäinen toistokerta
    	if (_loops > 0) {
    		currentLoop = 1;
    	}
    	else {
    		currentLoop = 0;
    	}
    }
    
    /**
     * Lopettaa animaation ja ottaa halutun tekstuurin käyttöön.
     * 
     * @param int Tekstuurin tunnus
     */
    public void stopAnimation(int _texture)
    {
    	usedAnimation = -1;
    	
    	usedTexture   = _texture;
    }
    
    /**
     * Päivittää animaation seuraavaan kuvaruutuun. Hallitsee myös toistokerrat
     * ja mahdollisen palautuksen vakiotekstuuriin (tunnus 0).
     */
    public void update()
    {
    	// Animaatio on käytössä
    	if (usedAnimation > -1) {
    		
    		// Animaatiolle on määritetty toistokerrat
	    	if (animationLoops > 0) {
	    		
	    		// Tarkistetaan, päättyykö animaation toistokerta ja toimitaan sen mukaisesti.
	    		if (currentFrame + 1 > animationLength[usedAnimation]) {
	    			currentFrame = 0;
	    			++currentLoop;
	    			if (currentLoop > animationLoops) {
	    				usedAnimation = -1;
	    				usedTexture   = 0;
	    			}
	    		}
	    		else {
	    			++currentFrame;
	    		}
	    	}
	    	// Animaatio on päättymätön
	    	else {
	    		
	    		// Tarkistetaan, päättyykö animaation toistokerta. Kelataan takaisin alkuun
	    		// tarvittaessa.
	    		if (currentFrame + 1 > animationLength[usedAnimation]) {
	    			currentFrame = 0;
	    		}
	    		else {
	    			++currentFrame;
	    		}
	    	}
    	}
    }
}
