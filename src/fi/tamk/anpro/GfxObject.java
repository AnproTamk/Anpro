package fi.tamk.anpro;

import javax.microedition.khronos.opengles.GL10;

/**
 * Sis‰lt‰‰ kaikkien graafisten objektien yhteiset ominaisuudet ja tiedot.
 * Hallitsee esimerkiksi animaatioiden p‰ivitt‰misen, k‰ytˆss‰ olevat tekstuurit
 * (tunnukset) ja objektin sijainnin.
 */
abstract public class GfxObject
{
	/* Toimintojen tilat */
	protected static final byte ACTION_DESTROYED = 1;
	protected static final byte ACTION_ENABLED   = 2;
    
	/* Objektin tunnus Wrapperissa */
    protected int listId;
	
    /* Objektin sijainti */
    public float x = 0;
    public float y = 0;
    
    /* K‰ytˆss‰ oleva animaatio ja sen tiedot */
    public  int   usedAnimation    = -1;
    public  int[] animationLength;
    public  int   currentFrame     = 0;
    private int   currentLoop      = 0;
    private int   animationLoops   = 0;
    public  int   animationSpeed   = 0;
    
    /* Staattinen k‰ytˆss‰ oleva tekstuuri */
    public int usedTexture = 0;
    
    /* Erityistoiminto */
    protected boolean actionActivated = false; // Kertoo, onko toiminto k‰ynniss‰ (asetetaan arvoksi true,
                                               // kun halutaan kutsua objektin triggerEndOfAction-funktiota
                                               // k‰ynniss‰ olevan animaation loputtua)
    
    protected int     actionId;                // Toiminnon tunnus
    
    /**
     * Alustaa luokan muuttujat.
     */
    public GfxObject()
    {
    	// ...
    }
    
    /**
     * M‰‰ritt‰‰ objektin aktiiviseksi.
     */
    public void setActive() { }
    
    /**
     * M‰‰ritt‰‰ objektin ep‰aktiiviseksi. Sammuttaa myˆs teko‰lyn jos se on tarpeen.
     */
    public void setUnactive() { }
    
    /**
     * Piirt‰‰ objektin k‰ytˆss‰ olevan tekstuurin tai animaation ruudulle.
     * 
     * @param GL10 OpenGL-konteksti
     */
    abstract public void draw(GL10 _gl);
    
    /**
     * K‰ynnist‰‰ animaation ja m‰‰ritt‰‰ toistokerrat.
     * 
     * @param int Animaation tunnus
     * @param int Toistokerrat
     * @param int P‰ivitysnopeus (ks. onDrawFrame GLRenderer-luokassa)
     */
    public final void startAnimation(int _animation, int _loops, int _speed)
    {
        // Tallennetaan muuttujat
        usedAnimation  = _animation;
        animationLoops = _loops;
        animationSpeed = _speed;
        currentFrame   = 0;
        
        // M‰‰ritet‰‰n ensimm‰inen toistokerta
        if (_loops > 0) {
            currentLoop = 1;
        }
        else {
            currentLoop = 0;
        }
    }
    
    /**
     * Lopettaa animaation ja ottaa halutun tekstuurin k‰yttˆˆn.
     * 
     * @param int Tekstuurin tunnus
     */
    public final void stopAnimation(int _texture)
    {
        usedAnimation = -1;
        
        usedTexture   = _texture;
    }
    
    /**
     * P‰ivitt‰‰ animaation seuraavaan kuvaruutuun. Hallitsee myˆs toistokerrat
     * ja mahdollisen palautuksen vakiotekstuuriin (tunnus 0).
     */
    public final void update()
    {
    	if (usedAnimation != -1) {
	        // Animaatiolle on m‰‰ritetty toistokerrat
	        if (animationLoops > 0) {
	            
	            // Tarkistetaan, p‰‰ttyykˆ animaation toistokerta ja toimitaan sen mukaisesti.
	            if (currentFrame + 1 > animationLength[usedAnimation]) {
	                currentFrame = 0;
	                ++currentLoop;
	                if (currentLoop > animationLoops) {
	                    usedAnimation = -1;
	                    usedTexture   = 0;
	                    
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
	        // Animaatio on p‰‰ttym‰tˆn
	        else {
	            
	            // Tarkistetaan, p‰‰ttyykˆ animaation toistokerta. Kelataan takaisin alkuun
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
    
    /**
     * Asettaa erityistoiminnon ja animaation. Kun animaatio loppuu, kutsuu update-funktio
     * objektin triggerEndOfAction-funktiota.
     * 
     * Toimintojen vakiot lˆytyv‰t GfxObject-luokan alusta.
     * 
     * @param int Animaation tunnus
     * @param int Toistokerrat
     * @param int Animaation p‰ivitysnopeus (ks. onDrawFrame GLRenderer-luokassa)
     * @param int Toiminnon tunnus
     */
    protected void setAction(int _animation, int _loops, int _animationSpeed, int _actionId)
    {
    	// TODO: Toimintojen tunnuksia varten voisi olla vakiot
    	
        startAnimation(_animation, _loops, _animationSpeed);
        
        actionActivated = true;
        actionId        = _actionId;
	}

    /**
     * K‰sittelee jonkin toiminnon p‰‰ttymisen. Kutsutaan animaation loputtua, mik‰li
     * actionActivated on TRUE.
     * 
     * K‰ytet‰‰n esimerkiksi objektin tuhoutuessa. Objektille m‰‰ritet‰‰n animaatioksi
     * sen tuhoutumisanimaatio, tilaksi Wrapperissa m‰‰ritet‰‰n 2 (piirret‰‰n, mutta
     * p‰ivitet‰‰n ainoastaan animaatio) ja asetetaan actionActivatedin arvoksi TRUE.
     * T‰llˆin GameThread p‰ivitt‰‰ objektin animaation, Renderer piirt‰‰ sen, ja kun
     * animaatio p‰‰ttyy, kutsutaan objektin triggerEndOfAction-funktiota. T‰ss‰
     * funktiossa objekti k‰sittelee tilansa. Tuhoutumisanimaation tapauksessa objekti
     * m‰‰ritt‰‰ itsens‰ ep‰aktiiviseksi.
     * 
     * Jokainen objekti luo funktiosta oman toteutuksensa, sill‰ toimintoja voi olla
     * useita. Objekteilla on myˆs k‰ytˆss‰‰n actionId-muuttuja, jolle voidaan asettaa
     * haluttu arvo. T‰m‰ arvo kertoo objektille, mink‰ toiminnon se juuri suoritti.
     * 
     * Toimintojen vakiot lˆytyv‰t GfxObject-luokan alusta.
     */
    abstract protected void triggerEndOfAction();
}
