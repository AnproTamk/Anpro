package fi.tamk.anpro;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.content.res.Resources;
import android.opengl.GLSurfaceView;
import android.opengl.GLSurfaceView.Renderer;
import android.opengl.GLU;
import android.util.DisplayMetrics;

/**
 * Lataa ja varastoi tekstuurit ja hallitsee niiden piirtämisen ruudulle.
 * 
 * @implements Renderer
 */
public class GLRenderer implements Renderer
{
    /* Vakiot peliobjektien tekstuureille ja animaatioille */
    public static final int TEXTURE_STATIC   = 0;
    public static final int TEXTURE_INACTIVE = 1;
    
    public static final int ANIMATION_STATIC   = 0;
    public static final int ANIMATION_MOVE     = 1;
    public static final int ANIMATION_SHOOT    = 2;
    public static final int ANIMATION_DESTROY  = 3;
    public static final int ANIMATION_DISABLED = 4;

    /* Vakiot HUDin tekstuureille ja animaatioille */
    public static final int TEXTURE_BUTTON_NOTSELECTED = 0;
    public static final int TEXTURE_BUTTON_SELECTED    = 1;
    public static final int TEXTURE_JOYSTICK           = 2;
    public static final int TEXTURE_HEALTH             = 3;
    
    public static final int ANIMATION_CLICK = 0;
    public static final int ANIMATION_READY = 1;

    /* Piirrettävät animaatiot ja objektit */
    public static Texture[]     playerTextures;
    public static Animation[]   playerAnimations;
    public static Texture[][]   enemyTextures;
    public static Animation[][] enemyAnimations;
    public static Texture[][]   projectileTextures;
    public static Animation[][] projectileAnimations;
    
    public static Texture[]     hudTextures;
    public static Animation[]   hudAnimations;
    
    /* Ohjelman konteksti ja resurssit */
    private Context   context;
    private Resources resources;
    
    /* Tarvittavat oliot */
    private       Wrapper    wrapper;
    private       GameThread gameThread = null;
    public static Hud        hud;
    
    /* Lataustiedot (kertoo, onko tekstuureja vielä ladattu) */
    public boolean allLoaded = false;
    
    /* Animaatiopäivitysten muuttujat */
	private long lastAnimationUpdate;

    /**
     * Alustaa luokan muuttujat.
     * 
     * @param Context   Ohjelman konteksti
     * @param Resources Ohjelman resurssit
     */
    public GLRenderer(Context _context, GLSurfaceView _surface, Resources _resources, DisplayMetrics _dm)
    {
        // Määritetään taulukoiden koot
        playerAnimations     = new Animation[5];
        playerTextures       = new Texture[4];
        enemyAnimations      = new Animation[5][5];
        enemyTextures        = new Texture[5][4];
        projectileAnimations = new Animation[5][5];
        projectileTextures   = new Texture[5][4];
        hudAnimations        = new Animation[4];
        hudTextures          = new Texture[4];
        
        // Tallennetaan konteksti ja resurssit
        context   = _context;
        resources = _resources;
        
        // Luodaan HUD
        hud = new Hud(_context);
        
        // Otetaan Wrapper käyttöön
        wrapper = Wrapper.getInstance();
    }

    /**
     * Määrittää OpenGL-asetukset ja lataa tekstuurit.
     * Android kutsuu tätä automaattisesti.
     * 
     * @param GL10      OpenGL-konteksti
     * @param EGLConfig OpenGL-asetukset
     */
    public void onSurfaceCreated(GL10 _gl, EGLConfig _config)
    {
        // Otetaan käyttöön 2D-tekstuurit ja shademalli
        //_gl.glEnable(GL10.GL_TEXTURE_2D);
        _gl.glShadeModel(GL10.GL_SMOOTH);

        // Piirretään tausta mustaksi
        _gl.glClearColor(0.0f, 0.0f, 0.0f, 0.5f);

        // Määritetään syvyyspuskurin oletusarvo
        //_gl.glClearDepthf(0.0f);

        // Määritetään perspektiivilaskennat
        //_gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_NICEST);

        // Määritetään läpinäkyvyysasetukset
        _gl.glEnable(GL10.GL_ALPHA_TEST);
        _gl.glAlphaFunc(GL10.GL_GREATER, 0);
        
        // Ladataan graffat (väliaikainen)
        if (!allLoaded && gameThread != null) {
            if (loadTextures(_gl)) {
                startThread();
            }
            else {
                // TODO: Käsittele virhe
            }
        }
    }

    /**
     * Määrittää pinnan uudet asetukset.
     * Android kutsuu tätä automaattisesti.
     * 
     * @param GL10 OpenGL-konteksti
     * @param int  Näytön leveys
     * @param int  Näytön korkeus
     */
    public void onSurfaceChanged(GL10 _gl, int _width, int _height)
    {
        // Estetään nollalla jakaminen
        if (_height == 0) {
            _height = 1;
        }

        // Resetoidaan viewport
        _gl.glViewport(0, 0, _width, _height);

        // Valitaan ja resetoidaan projektiomatriisi
        _gl.glMatrixMode(GL10.GL_PROJECTION);
        _gl.glLoadIdentity();
        
        // Määritetään ruudun koko (OpenGL:ää varten)
        GLU.gluOrtho2D(_gl, -(_width/2), (_width/2), -(_height/2), (_height/2));
        //_gl.glOrthof(0, 800, 480, 0, -1, 1);

        // Valitaan ja resetoidaan mallimatriisi
        _gl.glMatrixMode(GL10.GL_MODELVIEW);
        _gl.glLoadIdentity();
    }

    /**
     * Tuhoaa pinnan.
     * Android kutsuu tätä automaattisesti.
     */
    public void onSurfaceDestroyed() {
    }

    /**
     * Käy läpi piirtolistat ja piirtää tarvittavat tekstuurit ruudulle.
     * Android kutsuu tätä automaattisesti (maks. 60 kertaa sekunnissa).
     * 
     * @param GL10 OpenGL-konteksti
     */
    public void onDrawFrame(GL10 _gl)
    {
        // Otetaan 2D-piirtäminen käyttöön
        _gl.glEnable(GL10.GL_TEXTURE_2D);
        
        // Tyhjätään ruutu ja syvyyspuskuri
        _gl.glClearColor(0, 0, 0, 0);
        _gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);

        // Haetaan tämänhetkinen aika
        long currentTime = android.os.SystemClock.uptimeMillis();
        
        // Tekstuurit on ladattu
        if (allLoaded) {
            
            /* Käydään läpi piirtolistat */
            for (int i = wrapper.enemies.size()-1; i >= 0; --i) {
                if (wrapper.enemyStates.get(i) > 0) {
                    wrapper.enemies.get(i).draw(_gl);
                }
            }
            for (int i = wrapper.projectiles.size()-1; i >= 0; --i) {
                if (wrapper.projectileStates.get(i) > 0) {
                    wrapper.projectiles.get(i).draw(_gl);
                }
            }
            for (int i = wrapper.guiObjects.size()-1; i >= 0; --i) {
                if (wrapper.guiObjectStates.get(i) > 0) {
                    wrapper.guiObjects.get(i).draw(_gl);
                }
            }
            if (wrapper.player != null && wrapper.playerState > 0) {
                wrapper.player.draw(_gl);
            }
            
            /* Päivitetään animaatiot */
            if (currentTime - lastAnimationUpdate >= 40) {
                
                lastAnimationUpdate = currentTime;
                
                if (wrapper.player.usedAnimation != -1) {
                    wrapper.player.update();
                }
                
                for (int i = wrapper.enemies.size()-1; i >= 0; --i) {
                    if (wrapper.enemyStates.get(i) > 0 && wrapper.enemies.get(i).usedAnimation != -1) {
                        wrapper.enemies.get(i).update();
                    }
                }
                
                for (int i = wrapper.projectiles.size()-1; i >= 0; --i) {
                    if (wrapper.projectileStates.get(i) > 0 && wrapper.projectiles.get(i).usedAnimation != -1) {
                        wrapper.projectiles.get(i).update();
                    }
                }
                
                /*for (int i = hud.buttons.size()-1; i >= 0; --i) {
                	hud.buttons.get(i).update();
                }*/
            }
        }
        // Tekstuureja ei ole vielä ladattu
        else {
            if (loadTextures(_gl)) {
                startThread();
            }
            else {
                // TODO: Käsittele virhe
            }
        }
    }

    /**
     * Yhdistää renderöijän pelisäikeeseen tallentamalla pelisäikeen pointterin muistiin.
     * 
     * @param GameThread Osoitin pelisäikeeseen
     */
    public final void connectToGameThread(GameThread _gameThread) {
        gameThread = _gameThread;
    }

    /**
     * Lataa kaikki tekstuurit.
     * 
     * @param GL10 OpenGL-konteksti
     * 
     * @return boolean Onnistuiko lataaminen?
     */
    private final boolean loadTextures(GL10 _gl)
    {
        playerTextures[0]   = new Texture(_gl, context, R.drawable.player_tex0); 
        playerAnimations[3] = new Animation(_gl, context, resources, "enemy1_destroy", 20);
        
        enemyTextures[0][0]   = new Texture(_gl, context, R.drawable.enemy1_tex0);
        enemyAnimations[0][3] = new Animation(_gl, context, resources, "enemy1_destroy", 20);
        
        projectileTextures[0][0]   = new Texture(_gl, context, R.drawable.projectilelaser_tex0);
        projectileAnimations[0][3] = new Animation(_gl, context, resources, "projectilelaser_destroy", 5);
        projectileTextures[1][0]   = new Texture(_gl, context, R.drawable.projectileemp_anim_9);
        projectileAnimations[1][3] = new Animation(_gl, context, resources, "projectileemp", 26);
        
        hudTextures[0] = new Texture(_gl, context, R.drawable.button_tex0);
        hudTextures[1] = new Texture(_gl, context, R.drawable.button_tex1);
        hudTextures[2] = new Texture(_gl, context, R.drawable.joystick);
        hudTextures[3] = new Texture(_gl, context, R.drawable.health_bar);
        
        hudAnimations[0] = new Animation(_gl, context, resources, "button_press", 9);
        
        allLoaded = true;
        
        return true;
        // TODO: Palauta FALSE virheiden tapahtuessa.
    }

    /**
     * Käynnistää pelisäikeen.
     */
    private final void startThread()
    {
        if (gameThread != null) {
            gameThread.setRunning(true);
            gameThread.start();
        }
    }
}
