package fi.tamk.anpro;

/**
 * Sis�lt�� kaikkien graafisten objektien yhteiset ominaisuudet ja tiedot.
 * Hallitsee esimerkiksi animaatioiden p�ivitt�misen, k�yt�ss� olevat tekstuurit
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
    
    /* K�yt�ss� oleva animaatio ja sen tiedot */
    public  int   usedAnimation    = -1;
    public  int[] animationLength;
    public  int   currentFrame     = 0;
    private int   currentLoop      = 0;
    private int   animationLoops   = 0;
    
    /* Staattinen k�yt�ss� oleva tekstuuri */
    public int usedTexture = 0;
    
    /**
     * Alustaa luokan muuttujat.
     */
    public GfxObject()
    {
    	animationLength = new int[3];
    }
    
    /**
     * K�ynnist�� animaation ja m��ritt�� toistokerrat.
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
    	
		// M��ritet��n ensimm�inen toistokerta
    	if (_loops > 0) {
    		currentLoop = 1;
    	}
    	else {
    		currentLoop = 0;
    	}
    }
    
    /**
     * Lopettaa animaation ja ottaa halutun tekstuurin k�ytt��n.
     * 
     * @param int Tekstuurin tunnus
     */
    public void stopAnimation(int _texture)
    {
    	usedAnimation = -1;
    	
    	usedTexture   = _texture;
    }
    
    /**
     * P�ivitt�� animaation seuraavaan kuvaruutuun. Hallitsee my�s toistokerrat
     * ja mahdollisen palautuksen vakiotekstuuriin (tunnus 0).
     */
    public void update()
    {
    	// Animaatio on k�yt�ss�
    	if (usedAnimation > -1) {
    		
    		// Animaatiolle on m��ritetty toistokerrat
	    	if (animationLoops > 0) {
	    		
	    		// Tarkistetaan, p��ttyyk� animaation toistokerta ja toimitaan sen mukaisesti.
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
	    	// Animaatio on p��ttym�t�n
	    	else {
	    		
	    		// Tarkistetaan, p��ttyyk� animaation toistokerta. Kelataan takaisin alkuun
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
