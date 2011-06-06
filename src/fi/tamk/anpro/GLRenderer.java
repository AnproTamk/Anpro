package fi.tamk.anpro;

import java.util.ArrayList;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.opengl.GLSurfaceView.Renderer;
import android.opengl.GLU;

public class GLRenderer implements Renderer {

    // Piirrettävät objektit
    public ArrayList <Player>players;
    public ArrayList <GfxObject>enemies;
    public ArrayList <GfxObject>gui;

    private Context context;
    
    private Wrapper wrapper;

    /** Rakentaja */
    public GLRenderer(Context _context)
    {
        context = _context;
        
        wrapper = Wrapper.getInstance();

        // Alustetaan dynaamiset taulukot
        players    = new ArrayList<Player>();
        enemies    = new ArrayList<GfxObject>();
        gui        = new ArrayList<GfxObject>();
    }

    /** Kutsutaan, kun pinta luodaan. */
    public void onSurfaceCreated(GL10 _gl, EGLConfig _config)
    {
    	XmlReader reader = new XmlReader(context, this, _gl);
    	reader.readLevel(1);
    	
        // Otetaan käyttöön 2D-tekstuurit ja shademalli
        _gl.glEnable(GL10.GL_TEXTURE_2D);
        _gl.glShadeModel(GL10.GL_SMOOTH);

        // Piirretään tausta mustaksi
        _gl.glClearColor(0.0f, 0.0f, 0.0f, 0.5f);

        // Syvyyspuskurin oletusarvo
        _gl.glClearDepthf(1.0f);

        // Perspektiivilaskennat
        _gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_NICEST);

        // Määritetään läpinäkyvyysasetukset
        _gl.glEnable(GL10.GL_ALPHA_TEST);
        _gl.glAlphaFunc(GL10.GL_GREATER, 0);
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

    /** Kutsutaan, kun pinta päivitetään. */
    public void onDrawFrame(GL10 _gl)
    {
        // Tyhjätään ruutu ja syvyyspuskuri
        _gl.glClearColor(255, 255, 255, 0);
        _gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);

        // Käydään läpi piirtolistat
        for (int i = wrapper.playersToDraw.size()-1; i >= 0; --i) {
        	wrapper.playersToDraw.get(i).drawObject(_gl);
        }
        /*for (int i = wrapper.enemiesToDraw.size()-1; i >= 0; --i) {
        	
        }*/
        for (int i = wrapper.projectileLasersToDraw.size()-1; i >= 0; --i) {
        	wrapper.projectileLasersToDraw.get(i).drawObject(_gl);
        }
    }
}
