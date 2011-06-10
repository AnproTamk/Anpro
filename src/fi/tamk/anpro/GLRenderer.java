package fi.tamk.anpro;

import java.util.ArrayList;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.opengl.GLSurfaceView.Renderer;
import android.opengl.GLU;

public class GLRenderer implements Renderer {
    // Piirrett‰v‰t objektit
    public ArrayList<Animation> playerAnimations; // 3
    public ArrayList<Texture>   playerTextures;   // 2
    public ArrayList<Animation> enemyAnimations;  // 3 per rank
    public ArrayList<Texture>   enemyTextures;    // 2 per rank
    
    public StringTexture testText = null;

    private Context context;
    
    private Wrapper wrapper;
    
    // Ruudunp‰ivityksen ajastus
    private long lastDraw;
    private long time;
    
    public static int width;
    public static int height;

    /** Rakentaja */
    public GLRenderer(Context _context)
    {
        context = _context;
        
        wrapper = Wrapper.getInstance();
        
        wrapper.setRenderer(this);
        
        // M‰‰ritet‰‰n taulukoiden koot
        playerAnimations = new ArrayList<Animation>(3);
        playerTextures   = new ArrayList<Texture>(2);
        enemyAnimations = new ArrayList<Animation>(15);
        enemyTextures   = new ArrayList<Texture>(10);
    }

    /** Kutsutaan, kun pinta luodaan. */
    public void onSurfaceCreated(GL10 _gl, EGLConfig _config)
    {
        // Otetaan k‰yttˆˆn 2D-tekstuurit ja shademalli
        _gl.glEnable(GL10.GL_TEXTURE_2D);
        _gl.glShadeModel(GL10.GL_SMOOTH);

        // Piirret‰‰n tausta mustaksi
        _gl.glClearColor(0.0f, 0.0f, 0.0f, 0.5f);

        // Syvyyspuskurin oletusarvo
        //_gl.glClearDepthf(1.0f);

        // Perspektiivilaskennat
        //_gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_NICEST);

        // M‰‰ritet‰‰n l‰pin‰kyvyysasetukset
        _gl.glEnable(GL10.GL_ALPHA_TEST);
        _gl.glAlphaFunc(GL10.GL_GREATER, 0);
        
        // Ladataan graffat (v‰liaikainen)
        enemyTextures.add(new Texture(_gl, context, R.drawable.icon));
        testText = new StringTexture(_gl, context, "testi");
    }

    /** Kutsutaan, kun pinta muuttuu (k‰nnykk‰‰ k‰‰nnet‰‰n tai muuten vain koko muuttuu) */
    public void onSurfaceChanged(GL10 _gl, int _width, int _height)
    {
        // Estet‰‰n nollalla jakaminen
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

    /** Kutsutaan, kun pinta p‰ivitet‰‰n. */
    public void onDrawFrame(GL10 _gl)
    {
    	_gl.glEnable(GL10.GL_TEXTURE_2D);
    	
    	//time = android.os.SystemClock.uptimeMillis();
    	
    	//if (time - lastDraw >= 20) {
	        // Tyhj‰t‰‰n ruutu ja syvyyspuskuri
	        _gl.glClearColor(0, 0, 0, 0);
	        _gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
	
	        // K‰yd‰‰n l‰pi piirtolistat
	        for (int i = wrapper.players.size()-1; i >= 0; --i) {
	            wrapper.players.get(0).draw(_gl);
	        }
	        for (int i = wrapper.enemies.size()-1; i >= 0; --i) {
	            wrapper.enemies.get(i).draw(_gl);
	        }
	        
	        wrapper.testText.draw(_gl);
	        
	        //lastDraw = time;
    	//}
    }
}
