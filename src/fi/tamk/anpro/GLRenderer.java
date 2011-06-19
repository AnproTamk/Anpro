package fi.tamk.anpro;

import java.util.ArrayList;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.opengl.GLSurfaceView.Renderer;
import android.opengl.GLU;

/**
 * Lataa ja varastoi tekstuurit ja hallitsee niiden piirt�misen ruudulle.
 * 
 * @implements Renderer
 */
public class GLRenderer implements Renderer
{
    /* Piirrett�v�t animaatiot ja objektit */
    public static ArrayList<Animation> playerAnimations;     // 3
    public static ArrayList<Texture>   playerTextures;       // 2
    public static ArrayList<Animation> enemyAnimations;      // 3 per vihollistyyppi
    public static ArrayList<Texture>   enemyTextures;        // 2 per vihollistyyppi
    public static ArrayList<Animation> projectileAnimations; // 2 per ammus
    public static ArrayList<Texture>   projectileTextures;   // 1 per ammus
    public static ArrayList<Animation> hudAnimations;
    public static ArrayList<Texture>   hudTextures;

    /* Ohjelman konteksti */
    private Context context;
    
    /* Tarvittavat oliot */
    private Wrapper    wrapper;
    private GameThread gameThread = null;
    
    /* Lataustiedot (kertoo, onko tekstuureja viel� ladattu) */
    public boolean allLoaded = false;

    /**
     * Alustaa luokan muuttujat.
     * 
     * @param Context Ohjelman konteksti
     */
    public GLRenderer(Context _context)
    {
        // Tallennetaan konteksti
        context = _context;
        
        // Otetaan Wrapper k�ytt��n
        wrapper = Wrapper.getInstance();
        
        // M��ritet��n taulukoiden koot
        playerAnimations     = new ArrayList<Animation>(3);
        playerTextures       = new ArrayList<Texture>(2);
        enemyAnimations      = new ArrayList<Animation>(15);
        enemyTextures        = new ArrayList<Texture>(10);
        projectileAnimations = new ArrayList<Animation>(2);
        projectileTextures   = new ArrayList<Texture>(1);
        hudAnimations        = new ArrayList<Animation>();
        hudTextures          = new ArrayList<Texture>();
    }

    /**
     * M��ritt�� OpenGL-asetukset ja lataa tekstuurit.
     * Android kutsuu t�t� automaattisesti.
     * 
     * @param GL10      OpenGL-konteksti
     * @param EGLConfig OpenGL-asetukset
     */
    public void onSurfaceCreated(GL10 _gl, EGLConfig _config)
    {
        // Otetaan k�ytt��n 2D-tekstuurit ja shademalli
        _gl.glEnable(GL10.GL_TEXTURE_2D);
        _gl.glShadeModel(GL10.GL_SMOOTH);

        // Piirret��n tausta mustaksi
        _gl.glClearColor(0.0f, 0.0f, 0.0f, 0.5f);

        /*// M��ritet��n syvyyspuskurin oletusarvo
        _gl.glClearDepthf(1.0f);

        // M��ritet��n perspektiivilaskennat
        _gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_NICEST);*/

        // M��ritet��n l�pin�kyvyysasetukset
        _gl.glEnable(GL10.GL_ALPHA_TEST);
        _gl.glAlphaFunc(GL10.GL_GREATER, 0);
        
        // Ladataan graffat (v�liaikainen)
        if (!allLoaded && gameThread != null) {
            if (loadTextures(_gl)) {
                startThread();
            }
            else {
                // TODO: K�sittele virhe
            }
        }
    }

    /**
     * M��ritt�� pinnan uudet asetukset.
     * Android kutsuu t�t� automaattisesti.
     * 
     * @param GL10 OpenGL-konteksti
     * @param int  N�yt�n leveys
     * @param int  N�yt�n korkeus
     */
    public void onSurfaceChanged(GL10 _gl, int _width, int _height)
    {
        // Estet��n nollalla jakaminen
        if (_height == 0) {
            _height = 1;
        }

        // Resetoidaan viewport
        _gl.glViewport(0, 0, _width, _height);

        // Valitaan ja resetoidaan projektiomatriisi
        _gl.glMatrixMode(GL10.GL_PROJECTION);
        _gl.glLoadIdentity();
        
        // M��ritet��n ruudun koko (OpenGL:�� varten)
        GLU.gluOrtho2D(_gl, -(_width / 2), (_width / 2), -(_height / 2), (_height/ 2));

        // Valitaan ja resetoidaan mallimatriisi
        _gl.glMatrixMode(GL10.GL_MODELVIEW);
        _gl.glLoadIdentity();
    }

    /**
     * Tuhoaa pinnan.
     * Android kutsuu t�t� automaattisesti.
     */
    public void onSurfaceDestroyed() {
    }

    /**
     * K�y l�pi piirtolistat ja piirt�� tarvittavat tekstuurit ruudulle.
     * Android kutsuu t�t� automaattisesti (maks. 60 kertaa sekunnissa).
     * 
     * @param GL10 OpenGL-konteksti
     */
    public void onDrawFrame(GL10 _gl)
    {
        // Otetaan 2D-piirt�minen k�ytt��n
        _gl.glEnable(GL10.GL_TEXTURE_2D);
        
        // Tyhj�t��n ruutu ja syvyyspuskuri
        _gl.glClearColor(0, 0, 0, 0);
        _gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);

        // Tekstuurit on ladattu
        if (allLoaded) {
            
            /* K�yd��n l�pi piirtolistat */
            for (int i = wrapper.enemies.size()-1; i >= 0; --i) {
                if (wrapper.enemyStates.get(i) == 1) {
                    wrapper.enemies.get(i).draw(_gl);
                }
            }
            for (int i = wrapper.projectiles.size()-1; i >= 0; --i) {
                if (wrapper.projectileStates.get(i) == 1) {
                    wrapper.projectiles.get(i).draw(_gl);
                }
            }
            if (wrapper.player != null && wrapper.playerState == 1) {
                wrapper.player.draw(_gl);
            }
        }
        // Tekstuureja ei ole viel� ladattu
        else {
            if (loadTextures(_gl)) {
                startThread();
            }
            else {
                // TODO: K�sittele virhe
            }
        }
    }

    /**
     * Yhdist�� render�ij�n pelis�ikeeseen tallentamalla pelis�ikeen pointterin muistiin.
     * 
     * @param GameThread Osoitin pelis�ikeeseen
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
        playerTextures.add(new Texture(_gl, context, R.drawable.player_tex0)); 
        enemyTextures.add(new Texture(_gl, context, R.drawable.enemy1_tex0));
        projectileTextures.add(new Texture(_gl, context, R.drawable.projectilelaser_tex0));

        allLoaded = true;
        
        return true;
        // TODO: Palauta FALSE virheiden tapahtuessa.
    }

    /**
     * K�ynnist�� pelis�ikeen.
     */
    private final void startThread()
    {
        if (gameThread != null) {
            gameThread.setRunning(true);
            gameThread.start();
        }
    }
}
