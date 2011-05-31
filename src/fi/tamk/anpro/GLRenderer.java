package fi.tamk.anpro;

import java.util.ArrayList;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.opengl.GLSurfaceView.Renderer;
import android.opengl.GLU;

public class GLRenderer implements Renderer {

    // Piirrett‰v‰t objektit
    public ArrayList <GfxObject>players;
    public ArrayList <GfxObject>enemies;
    public ArrayList <GfxObject>obstacles;
    public ArrayList <GfxObject>background;
    public ArrayList <GfxObject>foreground;
    public ArrayList <GfxObject>gui;

    private Context context;

    /** Rakentaja */
    public GLRenderer(Context _context)
    {
        context = _context;

        // Alustetaan dynaamiset taulukot
        players    = new ArrayList<GfxObject>();
        enemies    = new ArrayList<GfxObject>();
        obstacles  = new ArrayList<GfxObject>();
        background = new ArrayList<GfxObject>();
        foreground = new ArrayList<GfxObject>();
        gui        = new ArrayList<GfxObject>();
    }

    /** Kutsutaan, kun pinta luodaan. */
    public void onSurfaceCreated(GL10 _gl, EGLConfig _config)
    {
    	players.add(new GfxObject(_gl, context, R.drawable.icon));
    	players.get(0)._xf = 240.0f;
    	players.get(0)._yf = 160.0f;
    	
        // Otetaan k‰yttˆˆn 2D-tekstuurit ja shademalli
        _gl.glEnable(GL10.GL_TEXTURE_2D);
        _gl.glShadeModel(GL10.GL_SMOOTH);

        // Piirret‰‰n tausta mustaksi
        _gl.glClearColor(0.0f, 0.0f, 0.0f, 0.5f);

        // Syvyyspuskurin oletusarvo
        _gl.glClearDepthf(1.0f);

        // Perspektiivilaskennat
        _gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_NICEST);

        // M‰‰ritet‰‰n l‰pin‰kyvyysasetukset
        _gl.glEnable(GL10.GL_ALPHA_TEST);
        _gl.glAlphaFunc(GL10.GL_GREATER, 0);
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
        
        //GLU.gluOrtho2D(_gl, -240, 240, -160, 160);
        GLU.gluOrtho2D(_gl, (-1)*(_width/2), (_width/2), (-1)*(_height/2), (_height/2));

        // Valitaan ja resetoidaan mallimatriisi
        _gl.glMatrixMode(GL10.GL_MODELVIEW);
        _gl.glLoadIdentity();
    }

    /** Kutsutaan, kun pinta tuhoutuu */
    public void onSurfaceDestroyed() {
        boolean retry = true;
    }

    /** Kutsutaan, kun pinta p‰ivitet‰‰n. */
    public void onDrawFrame(GL10 _gl)
    {
        // Tyhj‰t‰‰n ruutu ja syvyyspuskuri
        _gl.glClearColor(255, 255, 255, 0);
        _gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);

        // Piirret‰‰n objectit
        players.get(0).drawObject(_gl);
    }
}
