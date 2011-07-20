package fi.tamk.anpro;

import javax.microedition.khronos.opengles.GL10;

/**
 * Sis�lt�� kaikkien graafisten objektien yhteiset ominaisuudet ja tiedot.
 * Hallitsee esimerkiksi animaatioiden p�ivitt�misen, k�yt�ss� olevat tekstuurit
 * (tunnukset) ja objektin sijainnin.
 */
abstract public class GfxObject
{
	/* Toimintojen tilat */
	protected static final byte ACTION_DESTROYED = 1;
	protected static final byte ACTION_ENABLED   = 2;
	protected static final byte ACTION_RESPAWN   = 3;
    
	/* Objektin tunnus Wrapperissa */
    protected int listId;
	
    /* Objektin sijainti */
    public float x = 0;
    public float y = 0;
    
    /* Objektin suunta */
    public    int direction       = 0; // Liikkumissuunta (0 oikealle, 90 yl�s)
    protected int facingDirection = 0; // Katsomissuunta (sama koordinaatisto, ei vaikuta liikkumiseen)
    
    /* Objektin k��nt� X- ja Y-akseleilla (3D) */
    protected float xAxisRotation = 0;
    protected float yAxisRotation = 0;
    
    /* Tekstuurisetti */
    protected GLSpriteSet usedSpriteSet;
    
    /* K�yt�ss� oleva animaatio ja sen tiedot */
    public    int   usedAnimation    = -1;
    protected int[] animationLength;
    protected int   currentFrame     = 0;
    private   int   currentLoop      = 0;
    private   int   animationLoops   = 0;
    public    int   animationSpeed   = 0;
    
    /* Palautustekstuuri ja -ruutu animaation loputtua */
    private int returnTexture;
    private int returnFrame;
    
    /* Animaation tauot (pys�ytt�� animaation johonkin ruutuun tietyksi aikaa) */
    private byte pauseFrame = -1;
    private int  pauseTime;
    private long startTime;
    
    /* Staattinen k�yt�ss� oleva tekstuuri */
    protected int usedTexture = 0;
    
    /* Erityistoiminto */
    protected boolean actionActivated = false; // Kertoo, onko toiminto k�ynniss� (asetetaan arvoksi true,
                                               // kun halutaan kutsua objektin triggerEndOfAction-funktiota
                                               // k�ynniss� olevan animaation loputtua)
    
    protected int     actionId;                // Toiminnon tunnus
    
    /**
     * Alustaa luokan muuttujat.
     */
    public GfxObject()
    {
    	// ...
    }
    
    /**
     * M��ritt�� objektin aktiiviseksi.
     */
    public void setActive() { }
    
    /**
     * M��ritt�� objektin ep�aktiiviseksi. Sammuttaa my�s teko�lyn jos se on tarpeen.
     */
    public void setUnactive() { }
    
    /**
     * Piirt�� objektin k�yt�ss� olevan tekstuurin tai animaation ruudulle.
     * 
     * @param GL10 OpenGL-konteksti
     */
    abstract public void draw(GL10 _gl);
    
    /**
     * K�ynnist�� animaation ja m��ritt�� toistokerrat.
     * 
     * @param int Animaation tunnus
     * @param int Toistokerrat
     * @param int P�ivitysnopeus (ks. onDrawFrame GLRenderer-luokassa)
     */
    public final void startAnimation(int _animation, int _loops, int _speed, int _returnTexture, int _returnFrame)
    {
        // Tallennetaan muuttujat
        usedAnimation  = _animation;
        animationLoops = _loops;
        animationSpeed = _speed;
        currentFrame   = 0;
        returnTexture  = _returnTexture;
        returnFrame    = _returnFrame;
        
        // M��ritet��n ensimm�inen toistokerta
        if (_loops > 0) {
            currentLoop = 1;
        }
        else {
            currentLoop = 0;
        }
        
        // Tallennetaan aloitusaika
        startTime = android.os.SystemClock.uptimeMillis();
    }
    
    /**
     * Lopettaa animaation ja ottaa halutun tekstuurin k�ytt��n.
     * 
     * @param int Tekstuurin tunnus
     */
    public final void stopAnimation(int _texture)
    {
        usedAnimation = -1;
        usedTexture   = _texture;
        usedTexture   = returnTexture;
        currentFrame  = returnFrame;
    }
    
    /**
     * P�ivitt�� animaation seuraavaan kuvaruutuun. Hallitsee my�s toistokerrat
     * ja mahdollisen palautuksen vakiotekstuuriin (tunnus 0).
     */
    public final void update()
    {
    	if (usedAnimation != -1) {
    		// Tarkistetaan animaation tauotus
    		if (currentFrame == pauseFrame) {
    			if (android.os.SystemClock.uptimeMillis() - startTime >= pauseTime) {
    				pauseFrame = -1;
    			}
    		}
    		else {
		        // Animaatiolle on m��ritetty toistokerrat
		        if (animationLoops > 0) {
		            
		            // Tarkistetaan, p��ttyyk� animaation toistokerta ja toimitaan sen mukaisesti.
		            if (currentFrame + 1 > animationLength[usedAnimation]) {
		                currentFrame = 0;
		                ++currentLoop;
		                if (currentLoop > animationLoops) {
		                    usedAnimation = -1;
		                    usedTexture   = returnTexture;
		                    currentFrame  = returnFrame;
		                    
		                    if (actionActivated) {
		                        actionActivated = false;
		                        triggerEndOfAction();
		                    }
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
    
    /**
     * Asettaa erityistoiminnon ja animaation. Kun animaatio loppuu, kutsuu update-funktio
     * objektin triggerEndOfAction-funktiota.
     * 
     * Toimintojen vakiot l�ytyv�t GfxObject-luokan alusta.
     * 
     * @param int Animaation tunnus
     * @param int Toistokerrat
     * @param int Animaation p�ivitysnopeus (ks. onDrawFrame GLRenderer-luokassa)
     * @param int Toiminnon tunnus
     */
    protected void setAction(int _animation, int _loops, int _animationSpeed, int _actionId, int _returnTexture, int _returnFrame)
    {
    	// TODO: Toimintojen tunnuksia varten voisi olla vakiot
    	
        startAnimation(_animation, _loops, _animationSpeed, _returnTexture, _returnFrame);
        
        actionActivated = true;
        actionId        = _actionId;
        pauseFrame      = -1;
	}
    
    /**
     * Asettaa erityistoiminnon ja animaation. Kun animaatio loppuu, kutsuu update-funktio
     * objektin triggerEndOfAction-funktiota.
     * 
     * Toimintojen vakiot l�ytyv�t GfxObject-luokan alusta.
     * 
     * Toisin kuin ylemm�ss� toteutuksessa setActionista, t�h�n funktioon voi antaa parametreina
     * ruudun, johon animaation haluaa pys�htyv�n tietyksi aikaa. T�ll� v�ltet��n useiden samojen
     * tekstuurien luominen.
     * 
     * @param int  Animaation tunnus
     * @param int  Toistokerrat
     * @param int  Animaation p�ivitysnopeus (ks. onDrawFrame GLRenderer-luokassa)
     * @param int  Toiminnon tunnus
     * @param byte Animaation ruutu, jossa haluttu aika odotetaan
     * @param int  Aika, joksi animaatio pys�ytet��n
     */
    protected void setAction(int _animation, int _loops, int _animationSpeed, int _actionId,
    		 				 int _returnTexture, int _returnFrame, byte _pauseFrame, int _pauseTime)
    {
    	// TODO: Toimintojen tunnuksia varten voisi olla vakiot
    	
        startAnimation(_animation, _loops, _animationSpeed, _returnTexture, _returnFrame);
        
        actionActivated = true;
        actionId        = _actionId;
        pauseFrame      = _pauseFrame;
        pauseTime       = _pauseTime;
	}

    /**
     * K�sittelee jonkin toiminnon p��ttymisen. Kutsutaan animaation loputtua, mik�li
     * <i>actionActivated</i> on TRUE.<br /><br />
     * 
     * K�ytet��n seuraavasti:<br />
     * <ul>
     *   <li>1. Objekti kutsuu funktiota <b>setAction</b>, jolle annetaan parametreina haluttu animaatio,
     *     animaation toistokerrat, animaation nopeus, toiminnon tunnus (vakiot <b>GfxObject</b>issa).
     *     Toiminnon tunnus tallennetaan <i>actionId</i>-muuttujaan.
     *     		<ul><li>-> Lis�ksi voi antaa my�s jonkin animaation ruudun j�rjestysnumeron (alkaen 0:sta)
     *     		   ja ajan, joka siin� ruudussa on tarkoitus odottaa.</li></ul></li>
     *  <li>2. <b>GfxObject</b>in <b>setAction</b>-funktio kutsuu startAnimation-funktiota (sis�lt�� my�s
     *     <b>GfxObject</b>issa), joka k�ynnist�� animaation asettamalla <i>usedAnimation</i>-muuttujan arvoksi
     *     kohdassa 1 annetun animaation tunnuksen.</li>
     *  <li>3. <b>GLRenderer</b> p�ivitt�� animaatiota kutsumalla <b>GfxObject</b>in <b>update</b>-funktiota.</li>
     *  <li>4. Kun animaatio on loppunut, kutsuu <b>update</b>-funktio koko ketjun aloittaneen objektin
     *     <b>triggerEndOfAction</b>-funktiota (funktio on abstrakti, joten alaluokat luovat siit� aina
     *     oman toteutuksensa).</li>
     *  <li>5. <b>triggerEndOfAction</b>-funktio tulkitsee <i>actionId</i>-muuttujan arvoa, johon toiminnon tunnus
     *     tallennettiin, ja toimii sen mukaisesti.</li>
     * </ul>
     * 
     * Funktiota k�ytet��n esimerkiksi objektin tuhoutuessa, jolloin se voi asettaa itsens�
     * "puoliaktiiviseen" tilaan (esimerkiksi 2, eli ONLY_ANIMATION) ja k�ynnist�� yll� esitetyn
     * tapahtumaketjun. Objekti tuhoutuu asettumalla tilaan 0 (INACTIVE) vasta ketjun p��tytty�.
     * Tuhoutuminen toteutettaisiin triggerEndOfAction-funktion sis�ll�.
     * 
     * Toimintojen vakiot l�ytyv�t GfxObject-luokan alusta.
     */
    abstract protected void triggerEndOfAction();
}
