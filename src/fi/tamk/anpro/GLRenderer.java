package fi.tamk.anpro;

import java.util.ArrayList;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.opengl.GLSurfaceView.Renderer;
import android.opengl.GLU;

public class GLRenderer implements Renderer {

    // Piirrett‰v‰t objektit
    ArrayList <GfxObject>_players;
    ArrayList <GfxObject>_enemies;
    ArrayList <GfxObject>_obstacles;
    ArrayList <GfxObject>_background;
    ArrayList <GfxObject>_foreground;
    ArrayList <GfxObject>_gui;

    private Context  _context;

    // Oletusarvo piirtoet‰isyydelle
    public final static float CAMERA_DISTANCE = -8.0f;

    /** Rakentaja */
    public GLRenderer(Context context)
    {
        _context = context;

        // Alustetaan dynaamiset taulukot
        _players    = new ArrayList<GfxObject>();
        _enemies    = new ArrayList<GfxObject>();
        _obstacles  = new ArrayList<GfxObject>();
        _background = new ArrayList<GfxObject>();
        _foreground = new ArrayList<GfxObject>();
        _gui        = new ArrayList<GfxObject>();
    }

    /** Kutsutaan, kun pinta luodaan. */
    public void onSurfaceCreated(GL10 gl, EGLConfig config)
    {
        // Otetaan k‰yttˆˆn 2D-tekstuurit ja shademalli
        gl.glEnable(GL10.GL_TEXTURE_2D);
        gl.glShadeModel(GL10.GL_SMOOTH);

        // Piirret‰‰n tausta mustaksi
        gl.glClearColor(0.0f, 0.0f, 0.0f, 0.5f);

        // Syvyyspuskurin oletusarvo
        gl.glClearDepthf(1.0f);

        // Perspektiivilaskennat
        gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_NICEST);

        // M‰‰ritet‰‰n l‰pin‰kyvyysasetukset
        gl.glEnable(GL10.GL_ALPHA_TEST);
        gl.glAlphaFunc(GL10.GL_GREATER, 0);
    }

    /** Kutsutaan, kun pinta muuttuu (k‰nnykk‰‰ k‰‰nnet‰‰n tai muuten vain koko muuttuu) */
    public void onSurfaceChanged(GL10 gl, int width, int height)
    {
        // Estet‰‰n nollalla jakaminen
        if (height == 0) {
            height = 1;
        }

        // Resetoidaan viewport
        gl.glViewport(0, 0, width, height);

        // Valitaan ja resetoidaan projektiomatriisi
        gl.glMatrixMode(GL10.GL_PROJECTION);
        gl.glLoadIdentity();

        // Lasketaan kuvasuhde kuvaruudun nykyisten mittojen mukaan
        GLU.gluOrtho2D(gl, -5, 5, -3, 3);

        // Valitaan ja resetoidaan mallimatriisi
        gl.glMatrixMode(GL10.GL_MODELVIEW);
        gl.glLoadIdentity();
    }

    /** Kutsutaan, kun pinta tuhoutuu */
    public void onSurfaceDestroyed() {
        boolean retry = true;
    }

    /** Kutsutaan, kun pinta p‰ivitet‰‰n. */
    public void onDrawFrame(GL10 gl)
    {
        // Tyhj‰t‰‰n ruutu ja syvyyspuskuri
        gl.glClearColor(255, 255, 255, 0);
        gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);

        // Piirret‰‰n objectit
        // ...
    }
}
