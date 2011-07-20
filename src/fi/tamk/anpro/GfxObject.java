package fi.tamk.anpro;

import javax.microedition.khronos.opengles.GL10;

/**
 * Sisältää kaikkien graafisten objektien yhteiset ominaisuudet ja tiedot.
 * Hallitsee esimerkiksi animaatioiden päivittämisen, käytössä olevat tekstuurit
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
    public    int direction       = 0; // Liikkumissuunta (0 oikealle, 90 ylös)
    protected int facingDirection = 0; // Katsomissuunta (sama koordinaatisto, ei vaikuta liikkumiseen)
    
    /* Objektin kääntö X- ja Y-akseleilla (3D) */
    protected float xAxisRotation = 0;
    protected float yAxisRotation = 0;
    
    /* Tekstuurisetti */
    protected GLSpriteSet usedSpriteSet;
    
    /* Käytössä oleva animaatio ja sen tiedot */
    public    int   usedAnimation    = -1;
    protected int[] animationLength;
    protected int   currentFrame     = 0;
    private   int   currentLoop      = 0;
    private   int   animationLoops   = 0;
    public    int   animationSpeed   = 0;
    
    /* Palautustekstuuri ja -ruutu animaation loputtua */
    private int returnTexture;
    private int returnFrame;
    
    /* Animaation tauot (pysäyttää animaation johonkin ruutuun tietyksi aikaa) */
    private byte pauseFrame = -1;
    private int  pauseTime;
    private long startTime;
    
    /* Staattinen käytössä oleva tekstuuri */
    protected int usedTexture = 0;
    
    /* Erityistoiminto */
    protected boolean actionActivated = false; // Kertoo, onko toiminto käynnissä (asetetaan arvoksi true,
                                               // kun halutaan kutsua objektin triggerEndOfAction-funktiota
                                               // käynnissä olevan animaation loputtua)
    
    protected int     actionId;                // Toiminnon tunnus
    
    /**
     * Alustaa luokan muuttujat.
     */
    public GfxObject()
    {
    	// ...
    }
    
    /**
     * Määrittää objektin aktiiviseksi.
     */
    public void setActive() { }
    
    /**
     * Määrittää objektin epäaktiiviseksi. Sammuttaa myös tekoälyn jos se on tarpeen.
     */
    public void setUnactive() { }
    
    /**
     * Piirtää objektin käytössä olevan tekstuurin tai animaation ruudulle.
     * 
     * @param GL10 OpenGL-konteksti
     */
    abstract public void draw(GL10 _gl);
    
    /**
     * Käynnistää animaation ja määrittää toistokerrat.
     * 
     * @param int Animaation tunnus
     * @param int Toistokerrat
     * @param int Päivitysnopeus (ks. onDrawFrame GLRenderer-luokassa)
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
        
        // Määritetään ensimmäinen toistokerta
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
     * Lopettaa animaation ja ottaa halutun tekstuurin käyttöön.
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
     * Päivittää animaation seuraavaan kuvaruutuun. Hallitsee myös toistokerrat
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
		        // Animaatiolle on määritetty toistokerrat
		        if (animationLoops > 0) {
		            
		            // Tarkistetaan, päättyykö animaation toistokerta ja toimitaan sen mukaisesti.
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
    
    /**
     * Asettaa erityistoiminnon ja animaation. Kun animaatio loppuu, kutsuu update-funktio
     * objektin triggerEndOfAction-funktiota.
     * 
     * Toimintojen vakiot löytyvät GfxObject-luokan alusta.
     * 
     * @param int Animaation tunnus
     * @param int Toistokerrat
     * @param int Animaation päivitysnopeus (ks. onDrawFrame GLRenderer-luokassa)
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
     * Toimintojen vakiot löytyvät GfxObject-luokan alusta.
     * 
     * Toisin kuin ylemmässä toteutuksessa setActionista, tähän funktioon voi antaa parametreina
     * ruudun, johon animaation haluaa pysähtyvän tietyksi aikaa. Tällä vältetään useiden samojen
     * tekstuurien luominen.
     * 
     * @param int  Animaation tunnus
     * @param int  Toistokerrat
     * @param int  Animaation päivitysnopeus (ks. onDrawFrame GLRenderer-luokassa)
     * @param int  Toiminnon tunnus
     * @param byte Animaation ruutu, jossa haluttu aika odotetaan
     * @param int  Aika, joksi animaatio pysäytetään
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
     * Käsittelee jonkin toiminnon päättymisen. Kutsutaan animaation loputtua, mikäli
     * <i>actionActivated</i> on TRUE.<br /><br />
     * 
     * Käytetään seuraavasti:<br />
     * <ul>
     *   <li>1. Objekti kutsuu funktiota <b>setAction</b>, jolle annetaan parametreina haluttu animaatio,
     *     animaation toistokerrat, animaation nopeus, toiminnon tunnus (vakiot <b>GfxObject</b>issa).
     *     Toiminnon tunnus tallennetaan <i>actionId</i>-muuttujaan.
     *     		<ul><li>-> Lisäksi voi antaa myös jonkin animaation ruudun järjestysnumeron (alkaen 0:sta)
     *     		   ja ajan, joka siinä ruudussa on tarkoitus odottaa.</li></ul></li>
     *  <li>2. <b>GfxObject</b>in <b>setAction</b>-funktio kutsuu startAnimation-funktiota (sisältää myös
     *     <b>GfxObject</b>issa), joka käynnistää animaation asettamalla <i>usedAnimation</i>-muuttujan arvoksi
     *     kohdassa 1 annetun animaation tunnuksen.</li>
     *  <li>3. <b>GLRenderer</b> päivittää animaatiota kutsumalla <b>GfxObject</b>in <b>update</b>-funktiota.</li>
     *  <li>4. Kun animaatio on loppunut, kutsuu <b>update</b>-funktio koko ketjun aloittaneen objektin
     *     <b>triggerEndOfAction</b>-funktiota (funktio on abstrakti, joten alaluokat luovat siitä aina
     *     oman toteutuksensa).</li>
     *  <li>5. <b>triggerEndOfAction</b>-funktio tulkitsee <i>actionId</i>-muuttujan arvoa, johon toiminnon tunnus
     *     tallennettiin, ja toimii sen mukaisesti.</li>
     * </ul>
     * 
     * Funktiota käytetään esimerkiksi objektin tuhoutuessa, jolloin se voi asettaa itsensä
     * "puoliaktiiviseen" tilaan (esimerkiksi 2, eli ONLY_ANIMATION) ja käynnistää yllä esitetyn
     * tapahtumaketjun. Objekti tuhoutuu asettumalla tilaan 0 (INACTIVE) vasta ketjun päätyttyä.
     * Tuhoutuminen toteutettaisiin triggerEndOfAction-funktion sisällä.
     * 
     * Toimintojen vakiot löytyvät GfxObject-luokan alusta.
     */
    abstract protected void triggerEndOfAction();
}
