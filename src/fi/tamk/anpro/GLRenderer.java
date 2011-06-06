package fi.tamk.anpro;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLSurfaceView.Renderer;
import android.opengl.GLU;
import android.opengl.GLUtils;

public class GLRenderer implements Renderer {
    // Piirrettävät objektit
	private ArrayList<Animation> playerAnimations;     // 3
	private ArrayList<Texture>   playerTextures;       // 2
	private ArrayList<Animation> enemyAnimations;      // 3 per rank
	private ArrayList<Texture>   enemyTextures;        // 2 per rank
	private ArrayList<Animation> projectileAnimations; // 3 per projectile
	private ArrayList<Texture>   projectileTextures;   // 1 per projectile

    private Context context;
    
    private Wrapper wrapper;

    /** Rakentaja */
    public GLRenderer(Context _context)
    {
        context = _context;
        
        wrapper = Wrapper.getInstance();
        
        // Määritetään taulukoiden koot
        //...
    }

    /** Kutsutaan, kun pinta luodaan. */
    public void onSurfaceCreated(GL10 _gl, EGLConfig _config)
    {
    	//XmlReader reader = new XmlReader(context, this, _gl);
    	//reader.readLevel(1);
    	
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
    }

    /** Kutsutaan, kun pinta päivitetään. */
    public void onDrawFrame(GL10 _gl)
    {
        // Tyhjätään ruutu ja syvyyspuskuri
        _gl.glClearColor(255, 255, 255, 0);
        _gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
        
        Player[] playersTemp = wrapper.players;
        Enemy[] enemiesTemp  = wrapper.enemies;

        // Käydään läpi piirtolistat
        for (int i = playersTemp.size()-1; i >= 0; --i) {
        	playersTemp.get(i).draw();
        }
        for (int i = enemiesTemp.size()-1; i >= 0; --i) {
        	enemiesTemp.get(i).draw();
        }
    }
}
