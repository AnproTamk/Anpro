package fi.tamk.anpro;

import java.util.ArrayList;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.opengl.GLSurfaceView.Renderer;
import android.opengl.GLU;

public class GLRenderer implements Renderer {
    // Piirrettävät objektit
    public static ArrayList<Animation> playerAnimations; // 3
    public static ArrayList<Texture>   playerTextures;   // 2
    public static ArrayList<Animation> enemyAnimations;  // 3 per rank
    public static ArrayList<Texture>   enemyTextures;    // 2 per rank
    public static ArrayList<Animation> hudAnimations;
    public static ArrayList<Texture>   hudTextures;
    
    public StringTexture testText = null;

    private Context context;
    
    private Wrapper wrapper;
    
	public GameThread gameThread = null;
    
    // Näytön tiedot
    public static int width;
    public static int height;
    
    // Lataustiedot
    public boolean allLoaded = false;

    /** Rakentaja */
    public GLRenderer(Context _context)
    {
        context = _context;
        
        wrapper = Wrapper.getInstance();
        
        wrapper.setRenderer(this);
        
        // Määritetään taulukoiden koot
        playerAnimations = new ArrayList<Animation>(3);
        playerTextures   = new ArrayList<Texture>(2);
        enemyAnimations  = new ArrayList<Animation>(3);
        enemyTextures    = new ArrayList<Texture>(2);
        hudAnimations    = new ArrayList<Animation>();
        hudTextures      = new ArrayList<Texture>();
    }

    /** Kutsutaan, kun pinta luodaan. */
    public void onSurfaceCreated(GL10 _gl, EGLConfig _config)
    {
        // Otetaan käyttöön 2D-tekstuurit ja shademalli
        _gl.glEnable(GL10.GL_TEXTURE_2D);
        _gl.glShadeModel(GL10.GL_SMOOTH);

        // Piirretään tausta mustaksi
        _gl.glClearColor(0.0f, 0.0f, 0.0f, 0.5f);

        // Syvyyspuskurin oletusarvo
        //_gl.glClearDepthf(1.0f);

        // Perspektiivilaskennat
        //_gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_NICEST);

        // Määritetään läpinäkyvyysasetukset
        _gl.glEnable(GL10.GL_ALPHA_TEST);
        _gl.glAlphaFunc(GL10.GL_GREATER, 0);
        
        // Ladataan graffat (väliaikainen)
        if (gameThread != null) {
        	playerTextures.add(new Texture(_gl, context, R.drawable.icon));
        	enemyTextures.add(new Texture(_gl, context, R.drawable.icon));

        	allLoaded = true;
            
            gameThread.start();
        }
    }

    /** Kutsutaan, kun pinta muuttuu (kännykkää käännetään tai muuten vain koko muuttuu) */
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
        
        // Muutetaan ruudun koko
        //GLU.gluOrtho2D(_gl, (-1)*(_width/2), (_width/2), (-1)*(_height/2), (_height/2));
        GLU.gluOrtho2D(_gl, 0, _width, 0, _height);

        // Valitaan ja resetoidaan mallimatriisi
        _gl.glMatrixMode(GL10.GL_MODELVIEW);
        _gl.glLoadIdentity();
    }

    /** Kutsutaan, kun pinta tuhoutuu */
    public void onSurfaceDestroyed() {
    }

    /** Kutsutaan, kun pinta päivitetään. */
    public void onDrawFrame(GL10 _gl)
    {
    	_gl.glEnable(GL10.GL_TEXTURE_2D);
    	
        // Tyhjätään ruutu ja syvyyspuskuri
        _gl.glClearColor(0, 0, 0, 0);
        _gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);

        // Käydään läpi piirtolistat
        if (allLoaded) {
	        if (wrapper.player != null && wrapper.playerState == 1) {
	        	wrapper.player.draw(_gl);
	        }
	        
	        for (int i = wrapper.enemies.size()-1; i >= 0; --i) {
	        	if (wrapper.enemyStates.get(i) == 1) {
	        		wrapper.enemies.get(i).draw(_gl);
	        	}
	        }
	        
	        /*for (int i = wrapper.projectileLasers.size()-1; i >= 0; --i) {
	            wrapper.projectileLasers.get(i).draw(_gl);
	        }*/
        }
        else {
        	playerTextures.add(new Texture(_gl, context, R.drawable.icon));
        	enemyTextures.add(new Texture(_gl, context, R.drawable.icon));
        	
        	allLoaded = true;
        	
        	gameThread.start();
        }
    }
}
