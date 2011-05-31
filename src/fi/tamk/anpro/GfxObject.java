package fi.tamk.anpro;

import java.lang.reflect.Array;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLUtils;

public class GfxObject {
/*    // Vakioita helpottamaan luokan k�ytt��
    public static final int DRAW_FROM_CENTER = 1;
    public static final int DRAW_FROM_CORNER = 2;
    
    public static final int STATIC_TEXTURE   = 0;
    public static final int STATIC_ANIMATION = 1;
    public static final int MOVE_ANIMATION   = 2;
    
    // Vertailupiste, mist� n�hden objektin sijainti m��ritet��n
    public int _referencePoint = 1;
    
    // Objektin sijainti joko keskipisteeseen tai vasempaan yl�reunaan n�hden
    // (float-arvot ovat pelk�st��n OpenGL:�� varten)
    public int   _x  = 0;
    public float _xf = 0.0f;
    public int   _y = 0;
    public float _yf = 0.0f;
    public int   _z = 0;
    public float _zf = 0.0f;
    
    // K�yt�ss� oleva animaatio ja sen kuva
    private int _usedAnimation = 0;
    
    // Tekstuurin mitat
    private int   _imageSize;
    private float _imageSizef;
    
    // Puskuri ja taulukko vektoreita varten (huomaa vektorien j�rjestys).
    // N�it� ei ole tarpeen muuttaa.
    private FloatBuffer vertexBuffer;
    private float[] vertices;
    /*private float vertices[] = {
        -1.0f, -1.0f, 0.0f, // alavasen
        -1.0f, 1.0f, 0.0f,  // yl�vasen
        1.0f, -1.0f, 0.0f,	// alaoikea
        1.0f, 1.0f, 0.0f    // yl�oikea
    };*/

/*    // Puskuri ja taulukko tekstuuria varten (huomaa, ett� kulmien j�rjestys
    // on eri kuin vektoreilla!). N�it� ei ole tarpeen muuttaa.
    private FloatBuffer textureBuffer;
    //private float texture[];
    private float texture[] = {
        0.0f, 1.0f,		// yl�vasen
        0.0f, 0.0f,		// alavasen
        1.0f, 1.0f,		// yl�oikea
        1.0f, 0.0f		// alaoikea
    };
    
    // Staattinen tekstuuri
    private int[] _staticTexture = new int[1];
    
    // Animaatiot
    //private Animation _staticAnimation;
    //private Animation _moveAnimation;
    
    // Konteksti ja OpenGL-rajapinta
    private Context _context;
    private GL10    _gl;
    
    /** Rakentaja */
 /*   public GfxObject(GL10 gl, Context context, int id) {
        // Ladataan pakollinen staattinen tekstuuri
        loadGLTexture(gl, context, _staticTexture, 0, id);

        vertices = new float[12];
        vertices[0] = (-1)*_imageSizef;
        vertices[1] = vertices[0];
        vertices[2] = 0.0f;
        vertices[3] = vertices[0];
        vertices[4] = _imageSizef;
        vertices[5] = 0.0f;
        vertices[6] = _imageSizef;
        vertices[7] = vertices[0];
        vertices[8] = 0.0f;
        vertices[9] = _imageSizef;
        vertices[10] = _imageSizef;
        vertices[11] = 0.0f;
        
        /*texture = new float[8];
        texture[0] = 0.0f;
        texture[1] = _imageSizef;
        texture[2] = 0.0f;
        texture[3] = 0.0f;
        texture[4] = _imageSizef;
        texture[5] = _imageSizef;
        texture[6] = _imageSizef;
        texture[7] = 0.0f;*/
        
        // Varataan muistia koordinaateille (4 tavua jokaiselle) ja allokoidaan
        // ne vektoripuskuriin. T�ytet��n vektoripuskuri vektoreilla ja asetetaan
        // osoitin puskurin alkuun (t�t� ei tarvitse ymm�rt��... P��asia, ett�
        // se toimii).
  /*      ByteBuffer byteBuffer = ByteBuffer.allocateDirect(vertices.length * 4);
        byteBuffer.order(ByteOrder.nativeOrder());
        vertexBuffer = byteBuffer.asFloatBuffer();
        vertexBuffer.put(vertices);
        vertexBuffer.position(0);
        
        // Sama kuin ylh��ll�, mutta tekstuurille vektoreiden sijaan.
        byteBuffer = ByteBuffer.allocateDirect(vertices.length * 4);
        byteBuffer.order(ByteOrder.nativeOrder());
        textureBuffer = byteBuffer.asFloatBuffer();
        textureBuffer.put(texture);
        textureBuffer.position(0);
        
        _gl      = gl;
        _context = context;
        
        _staticAnimation = null;
        _moveAnimation   = null;
    }
    
    /** Lataa OpenGL-tekstuurin */
/*    public void loadGLTexture(GL10 gl, Context context, int[] var, int offset, int id)
    {
        // Ladataan tekstuuri resursseista
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), id);
        
        // Asetetaan mitat
        _imageSize  = bitmap.getWidth();
        _imageSizef = ((float)_imageSize / 800) * 10.0f;
        
        // Generoidaan tekstuuriosoitin ja liitet��n se tekstuuritaulukkoon
        gl.glGenTextures(1, var, 0);
        gl.glBindTexture(GL10.GL_TEXTURE_2D, var[offset]);
        
        // Asetetaan tekstuurin asetukset. Ensimm�inen parametri kertoo tekstuurityypin,
        // jonka asetuksia muutetaan (2D-tekstuurit). Toinen parametri kertoo muutettavan
        // asetuksen ja t�ss� tapauksessa sek� MIN_FILTER ett� MAG_FILTER liittyv�t tekstuurin
        // skaalaamiseen, mik�li ne eiv�t sovi k�ytettyyn neli��n. Kolmas parametri kertoo
        // halutun asetusarvon, joista en osaa erityisemmin kertoa...
        gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_NEAREST);
        gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR);
        
        // Muunnetaan bitmap-tekstuuri 2-ulotteiseksi OpenGL-tekstuuriksi
        GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmap, 0);
        
        // Vapautetaan bitmapin viem� muisti
        bitmap.recycle();
    }
    
    /** Lataa animaation kuvat */
 /*   public void loadAnimation(int anim, int length) {
        if (anim == STATIC_ANIMATION && _staticAnimation == null) {
            _staticAnimation = new Animation(_gl, _context, "bird", 0);
        }
        else if (anim == MOVE_ANIMATION && _staticAnimation == null) {
            _moveAnimation = new Animation(_gl, _context, "bird", 0);
        }
    }

    /** M��ritt�� X- ja Y-koordinaatit. */
 /*   public void setLocation(int x, int y) {
        _x = x;
        _y = y;
        
        convertCoordinates();
    }

    /** M��ritt�� X-, Y- ja Z-koordinaatit. */
   /* public void setLocation(int x, int y, int z) {
        _x = x;
        _y = y;
        _z = z;
        
        convertCoordinates();
    }
    
    /** Muuntaa pikselikoordinaatit OpenGL-yksik�iksi. */
 /*   private void convertCoordinates() {
        // Muunnetaan pikseliarvot OpenGL-yksik�iksi
        if (_referencePoint == DRAW_FROM_CENTER) {
            _xf = (float)(400 - _x) / 400 * -5.0f;
            _yf = (float)(240 - _y) / 240 * 3.0f;
        }
        else if (_referencePoint == DRAW_FROM_CORNER) {
            //_xf = -0.1f + (float)_x / 800 * 1.0f;
            //_yf = 0.1f + (float)_x / 480 * 1.0f;
        }
    }
    
    /** M��ritt�� vertailupisteen */
  /*  public void setReferencePoint(int ref) {
        if (ref == 1 || ref == 2) {
            _referencePoint = ref;
        }
    }
    
    /** Piirtofunktio */
 /*   public void drawObject(GL10 gl) {
        // Resetoidaan mallimatriisi
        gl.glLoadIdentity();
        
        // Siirret��n mallimatriisia (X, Y, Z)
        gl.glTranslatef(_xf, _yf, 0);
        
        // Valitaan piirrett�v� tekstuuri
        gl.glBindTexture(GL10.GL_TEXTURE_2D, _staticTexture[0]);
        
        // Avataan tekstuuri- ja vektoritaulukot k�ytt��n
        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
        
        // Valitaan neli�n n�ytett�v� puoli. Huom! Vaikka k�yt�mme 2D-tekstuureja, ovat
        // objektit silti 3-ulotteisia. T�st� syyst� on valittava objekteista puoli, joka
        // n�ytet��n.
        gl.glFrontFace(GL10.GL_CW);
        
        // Otetaan vektori- ja tekstuuripuskurit k�ytt��n (OpenGL-ymm�rt�� nyt, miten objektit
        // on tarkoitus piirt��)
        gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertexBuffer);
        gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, textureBuffer);

        // Piirret��n neli� (parametreissa on sana "TRIANGLE", sill� neli� muodostetaan oikeasti
        // kahdesta kolmiosta)
        gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, vertices.length/3);

        // Lukitaan tekstuuri- ja vektoritaulukot pois k�yt�st�
        gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
    }
    */
}
