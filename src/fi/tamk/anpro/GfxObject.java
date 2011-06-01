package fi.tamk.anpro;

import java.lang.reflect.Array;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;

import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLUtils;

public class GfxObject {
    public static final int STATIC_TEXTURE   = 0;
    public static final int STATIC_ANIMATION = 1;
    public static final int MOVE_ANIMATION   = 2;
    
    // Objektin sijainti
    public float _xf = 0.0f;
    public float _yf = 0.0f;
    public float _zf = 0.0f;
    
    // Käytössä oleva animaatio ja sen kuva
    private int usedAnimation = 0;
    
    // Tekstuurin mitat
    private float imageSize;
    
    // Puskuri ja taulukko vektoreita varten
    private FloatBuffer vertexBuffer;
    private float[] vertices;

    // Puskuri ja taulukko tekstuuria varten
    private FloatBuffer textureBuffer;
    private float texture[] = {
        0.0f, 1.0f,		// ylävasen
        0.0f, 0.0f,		// alavasen
        1.0f, 1.0f,		// yläoikea
        1.0f, 0.0f		// alaoikea
    };
    
    // Staattinen tekstuuri
    private int[] _staticTexture = new int[1];
    
    // Animaatiot
    private ArrayList <Animation>animations;
    private Animation staticAnimation;
    private Animation moveAnimation;
    
    // Konteksti ja OpenGL-rajapinta
    private Context context;
    private GL10    gl;
    
    /** Rakentaja */
    public GfxObject(GL10 _gl, Context _context, int _id) {
        // Ladataan pakollinen staattinen tekstuuri
        loadGLTexture(_gl, _context, _staticTexture, 0, _id);
        
        vertices = new float[12];
        vertices[0] = (-1)*imageSize;
        vertices[1] = vertices[0];
        vertices[2] = 0.0f;
        vertices[3] = vertices[0];
        vertices[4] = imageSize;
        vertices[5] = 0.0f;
        vertices[6] = imageSize;
        vertices[7] = vertices[0];
        vertices[8] = 0.0f;
        vertices[9] = imageSize;
        vertices[10] = imageSize;
        vertices[11] = 0.0f;
        
        // Varataan muistia koordinaateille
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(vertices.length * 4);
        byteBuffer.order(ByteOrder.nativeOrder());
        vertexBuffer = byteBuffer.asFloatBuffer();
        vertexBuffer.put(vertices);
        vertexBuffer.position(0);
        
        // Sama kuin ylhäällä, mutta tekstuurille vektoreiden sijaan.
        byteBuffer = ByteBuffer.allocateDirect(vertices.length * 4);
        byteBuffer.order(ByteOrder.nativeOrder());
        textureBuffer = byteBuffer.asFloatBuffer();
        textureBuffer.put(texture);
        textureBuffer.position(0);
        
        gl      = _gl;
        context = _context;
        
        staticAnimation = null;
        moveAnimation   = null;
    }
    
    /** Lataa OpenGL-tekstuurin */
    public void loadGLTexture(GL10 gl, Context context, int[] var, int offset, int id)
    {
        // Ladataan tekstuuri resursseista
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), id);
        
        // Asetetaan mitat
        imageSize = (float)bitmap.getWidth();
        
        // Generoidaan tekstuuriosoitin ja liitetään se tekstuuritaulukkoon
        gl.glGenTextures(1, var, 0);
        gl.glBindTexture(GL10.GL_TEXTURE_2D, var[offset]);
        
        // Asetetaan tekstuurin asetukset
        gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_NEAREST);
        gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_NEAREST);
        
        // Muunnetaan bitmap-tekstuuri 2-ulotteiseksi OpenGL-tekstuuriksi
        GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmap, 0);
        
        // Vapautetaan bitmapin viemä muisti
        bitmap.recycle();
    }
    
    /** Lataa animaation kuvat */
    public void loadAnimation(int _anim, int _length, String _id) {
    	animations.add(_anim, new Animation(gl, context, _id, _length));
    }
    
    /** Piirtofunktio */
    public void drawObject(GL10 gl) {
        // Resetoidaan mallimatriisi
        gl.glLoadIdentity();
        
        // Siirretään mallimatriisia (X, Y, Z)
        gl.glTranslatef(_xf, _yf, 0);
        
        // Valitaan piirrettävä tekstuuri
        if (usedAnimation != 0) {
        	gl.glBindTexture(GL10.GL_TEXTURE_2D, animations.get(usedAnimation).frames[staticAnimation.currentFrame]);
        }
        else {
        	gl.glBindTexture(GL10.GL_TEXTURE_2D, _staticTexture[0]);
        }
        
        // Avataan tekstuuri- ja vektoritaulukot käyttöön
        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
        
        // Valitaan neliön näytettävä puoli. Huom! Vaikka käytämme 2D-tekstuureja, ovat
        // objektit silti 3-ulotteisia. Tästä syystä on valittava objekteista puoli, joka
        // näytetään.
        gl.glFrontFace(GL10.GL_CW);
        
        // Otetaan vektori- ja tekstuuripuskurit käyttöön (OpenGL-ymmärtää nyt, miten objektit
        // on tarkoitus piirtää)
        gl.glVertexPointer(3, GL10.GL_FLOAT, 0, animations.get(usedAnimation).vertexBuffer);
        
        gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, textureBuffer);

        // Piirretään neliö (parametreissa on sana "TRIANGLE", sillä neliö muodostetaan oikeasti
        // kahdesta kolmiosta)   
        gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, animations.get(usedAnimation).vertices.length/3);


        // Lukitaan tekstuuri- ja vektoritaulukot pois käytöstä
        gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
    }
}
