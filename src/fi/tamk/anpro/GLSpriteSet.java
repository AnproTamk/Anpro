package fi.tamk.anpro;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLUtils;
import android.util.Log;

/**
 * Sis�lt�� yhden tekstuurin tai animaation tiedot ja toiminnot.
 */
public class GLSpriteSet
{
    /* Kuvan tiedot */
	protected int[] sprites;	 	 // Tekstuuri (sis�lt�� itseasiassa vain OpenGL:n sille antaman tunnuksen
	private   float realWidth;
    protected float imageWidth  = 0;
    protected float imageHeight = 0;
    
    public boolean isClamped;
    
    /* Animaation tiedot */
    public int length = 1;
    
    /* Puskurit ja vektorit objektille ja tekstuurille */
    protected FloatBuffer vertexBuffer;
    protected float[]     vertices;
    protected FloatBuffer textureBuffer;
    protected float       texture[] = {0.0f, 1.0f,
    			  					   0.0f, 0.0f,
    								   1.0f, 1.0f,
    								   1.0f, 0.0f};
    
    /* Viimeksi k�ytetty tekstuuri ja ruutu */
    private static int cachedTexture; // Jaettu kaikkien tekstuurien kesken
    private        int cachedFrame   = -1;
    
    /**
     * Alustaa luokan muuttujat.
     * 
     * @param _gl        OpenGL-konteksti
     * @param _context   Ohjelman konteksti
     * @param _resources Ohjelman resurssit
     * @param _id        Tekstuurin tunnus
     * @param _length    Animaation pituus
     */
    public GLSpriteSet(GL10 _gl, Context _context, int _id, int _length, boolean _isClamped)
    {
    	// Alustetaan tekstuuritaulukko
        sprites = new int[1];
        
        isClamped = _isClamped;
        
        // Generoidaan OpenGL-tunnukset
        _gl.glGenTextures(1, sprites, 0);
    	
    	if (!GLRenderer.loadingFailed) {
    		
	    	// Tallennetaan pituus
	        length = _length;
	
	        // Ladataan animaatio
	        if (!loadBitmap(_context, _id, _gl)) {
                GLRenderer.loadingFailed = true;
	        }
	        else {
	            // M��ritet��n tekstuurin vektorit
	            createVertices(_isClamped);
	            
	            // Luodaan vektoribufferit
	            createBuffers();
	        }
    	}
    }
    
    /* =======================================================
     * Uudet funktiot
     * ======================================================= */
    /**
     * Lataa tekstuurin muistiin ja muuntaa sen OpenGL-tekstuuriksi.
     * 
     * @param _context Ohjelman konteksti
     * @param _id      Resurssin tunnus
     * @param _gl      OpenGL-konteksti
     * 
     * @return Onnistuiko tekstuurin lataaminen?
     */
    protected boolean loadBitmap(Context _context, int _id, GL10 _gl)
    {
        // Luodaan tyhj� bitmap ja ladataan siihen tekstuuri resursseista
        Bitmap bitmap = null;
        try {
            bitmap = BitmapFactory.decodeResource(_context.getResources(), _id);
        }
        catch (Exception e) {
        	// Ladataan vakiokuva, jos latauksessa tapahtui virhe
        	// bitmap = BitmapFactory.decodeResource(_context.getResources(), R.drawable.notexture);
        }
        
        // Tallennetaan tekstuurin mitat (pelk�st��n leveys, sill� tekstuurin korkeuden
        // on oltava sama kuin leveyden)
    	if (imageWidth == 0) {
    		imageWidth  = (float)bitmap.getWidth();
    		imageHeight = (float)bitmap.getHeight();
    	}

        // Ladataan bitmap OpenGL-tekstuuriksi
        _gl.glBindTexture(GL10.GL_TEXTURE_2D, sprites[0]);
        _gl.glActiveTexture(sprites[0]);
        
        
        if (isClamped) {
	        // TODO: T�H�N TULEE SAMA KUIN ALLA, HIEMAN MUUTETTUNA (KATSO NETTITUTORIAALI!)
        	//_gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_T, GL10.GL_CLAMP_TO_EDGE);
	        _gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_S, GL10.GL_REPEAT);
	        _gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_NEAREST);
	        _gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_NEAREST);
        }
        else {
        	_gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_NEAREST);
	        _gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_NEAREST);
        }

        GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmap, 0);

        // Poistetaan bitmap muistista
        bitmap.recycle();
        
        return true;
    }
    
    /**
     * Luo vektoritaulukon kuvan koon mukaisesti.
     */
    protected void createVertices(boolean _isClamped)
	{
		if (isClamped) {
			vertices = new float[12];
	        
	        realWidth = imageHeight * length;
	        
	        vertices[0] = (float)((-1)*(int)(realWidth/length * 106));
	        vertices[1] = (-1)*imageHeight;
	        vertices[2] = 0.0f;
	        
	        vertices[3] = (float)((-1)*(int)(realWidth/length * 106));
	        vertices[4] = imageHeight;
	        vertices[5] = 0.0f;
	        
	        vertices[6] = (float)((int)(realWidth/length * 106));
	        vertices[7] = (-1)*imageHeight;
	        vertices[8] = 0.0f;
	        
	        vertices[9] = (float)(int)(realWidth/length * 106);
	        vertices[10] = imageHeight;
	        vertices[11] = 0.0f;
		}
		else {
	        vertices = new float[12];
	        
	        realWidth = imageHeight * length;
	        
	        vertices[0] = (float)((-1)*(int)(realWidth/length));
	        vertices[1] = (-1)*imageHeight;
	        vertices[2] = 0.0f;
	        
	        vertices[3] = (float)((-1)*(int)(realWidth/length));
	        vertices[4] = imageHeight;
	        vertices[5] = 0.0f;
	        
	        vertices[6] = (float)((int)(realWidth/length));
	        vertices[7] = (-1)*imageHeight;
	        vertices[8] = 0.0f;
	        
	        vertices[9] = (float)(int)(realWidth/length);
	        vertices[10] = imageHeight;
	        vertices[11] = 0.0f;
		}
	}
	
	/**
	 * Luo vektoripuskurit.
	 */
	protected void createBuffers()
	{
		// Varataan muistia objektin vektoreille ja lis�t��n ne puskuriin
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(vertices.length * 4);
        byteBuffer.order(ByteOrder.nativeOrder());
        vertexBuffer = byteBuffer.asFloatBuffer();
        vertexBuffer.put(vertices);
        vertexBuffer.position(0);
        
        // Varataan muistia tekstuurin vektoreille ja lis�t��n ne puskuriin
        byteBuffer = ByteBuffer.allocateDirect(vertices.length * 4);
        byteBuffer.order(ByteOrder.nativeOrder());
        textureBuffer = byteBuffer.asFloatBuffer();
        textureBuffer.put(texture);
        textureBuffer.position(0);
        textureBuffer.clear();
	}
	
	private final void generateTextureVectors(int _frame)
	{
    	float tempLeft;
    	float tempRight;
    	
    	if (length > 0) {
        	float temp = ((realWidth / (float)length) / imageWidth);
        	
    		tempLeft  = temp * _frame;
    		tempRight = temp * (_frame + 1);
    	}
    	else {
    		tempLeft  = 0.0f;
    		tempRight = 1.0f;
    	}
    	
    	if (isClamped) {
    		texture[4] = 53.0f; // oikea yl�, x-koordinaatti
	        texture[6] = 53.0f; // oikea ala, x-koordinaatti
    	}
    	else {
    		texture[0] = tempLeft; texture[1] = 1.0f;  // vasen yl�
			texture[2] = tempLeft; texture[3] = 0.0f;  // vasen ala
			texture[4] = tempRight; texture[5] = 1.0f; // oikea yl�
			texture[6] = tempRight; texture[7] = 0.0f; // oikea ala
    	}
        
        // Varataan muistia tekstuurin vektoreille ja lis�t��n ne puskuriin
        textureBuffer.put(texture);
        textureBuffer.position(0);
        textureBuffer.clear();
	}
    
    /**
     * Piirt�� tekstuurin ruudulle.
     * 
     * @param _gl		 OpenGL-konteksti
     * @param _x         Tekstuurin X-koordinaatti
     * @param _y         Tekstuurin Y-koordinaatti
     * @param _direction Tekstuurin suunta (0 = oikealle)
     * @param _frame     Tekstuurin j�rjestysnumero (animaatioille, tekstuureilla aina 0)
     */
    public final void draw(GL10 _gl, float _x, float _y, int _direction, int _frame)
    {
        // Resetoidaan mallimatriisi
        _gl.glLoadIdentity();
        
        // Siirret��n ja k��nnet��n mallimatriisia
        _gl.glTranslatef(_x - CameraManager.xTranslate, _y - CameraManager.yTranslate, 0);
        _gl.glRotatef((float)_direction-90.0f, 0.0f, 0.0f, 1.0f);
        _gl.glScalef(0.5f, 0.5f, 0.0f); // TODO: Miksi jaetaan kahdella? Originaali: _gl.glScalef(Options.scale/2, Options.scale/2, 0.0f);
        
        // Valitaan piirrett�v� tekstuuri
        if (cachedTexture != sprites[0]) {
        	cachedTexture = sprites[0];
            cachedFrame = _frame;
            
        	_gl.glBindTexture(GL10.GL_TEXTURE_2D, sprites[0]);
        	
        	generateTextureVectors(_frame);
        }
        else {
        	if (cachedFrame != _frame) {
        		generateTextureVectors(_frame);
                
                cachedFrame = _frame;
        	}
        }
        
        // Valitaan neli�n n�ytett�v� puoli
        _gl.glFrontFace(GL10.GL_CW);
        
        // Otetaan vektori- ja tekstuuripuskurit k�ytt��n
        _gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertexBuffer);
        _gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, textureBuffer);
    
        // Piirret��n neli�
        _gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, vertices.length/3);
    }
    
    /**
     * Piirt�� tekstuurin ruudulle ottaen huomioon suunnat my�s syvyyssuunnassa.
     * 
     * @param _gl		     OpenGL-konteksti
     * @param _x             Tekstuurin X-koordinaatti
     * @param _y             Tekstuurin Y-koordinaatti
     * @param _direction     Tekstuurin suunta (0 = oikealle)
     * @param _frame         Tekstuurin j�rjestysnumero (animaatioille, tekstuureilla aina 0)
     * @param _xAxisRotation K��nt� X-akselilla
     * @param _yAxisRotation K��nt� Y-akselilla
     * @param _currentFrame 
     */
    public final void drawIn3D(GL10 _gl, float _x, float _y, int _direction, int _frame,
    						   float _xAxisRotation, float _yAxisRotation)
    {
        // Resetoidaan mallimatriisi
        _gl.glLoadIdentity();
        
        // Siirret��n ja k��nnet��n mallimatriisia
        _gl.glTranslatef(_x - CameraManager.xTranslate, _y - CameraManager.yTranslate, 0);
        _gl.glRotatef(_xAxisRotation, 1.0f, 0.0f, 0.0f);
        _gl.glRotatef(_yAxisRotation, 0.0f, 1.0f, 0.0f);
        //_gl.glRotatef((float)_direction-90.0f, 0.0f, 0.0f, 0.0f);
        _gl.glScalef(0.5f, 0.5f, 0.0f); // TODO: Miksi jaetaan kahdella? Originaali: _gl.glScalef(Options.scale/2, Options.scale/2, 0.0f);
        
        // Valitaan piirrett�v� tekstuuri
        _gl.glBindTexture(GL10.GL_TEXTURE_2D, sprites[_frame]);
        
        // Valitaan neli�n n�ytett�v� puoli
        _gl.glFrontFace(GL10.GL_CW);
        
        // Otetaan vektori- ja tekstuuripuskurit k�ytt��n
        _gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertexBuffer);
        _gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, textureBuffer);
    
        // Piirret��n neli�
        _gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, vertices.length/3);
    }
}
