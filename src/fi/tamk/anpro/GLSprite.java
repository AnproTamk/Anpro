package fi.tamk.anpro;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLUtils;

/**
 * Sisältää yhden tekstuurin tai animaation tiedot ja toiminnot.
 */
abstract public class GLSprite
{
    /* Kuvan tiedot */
	protected int[] sprites;	 	 // Tekstuuri (sisältää itseasiassa vain OpenGL:n sille antaman tunnuksen
    protected float imageWidth  = 0;
    protected float imageHeight = 0;
    
    /* Puskurit ja vektorit objektille ja tekstuurille */
    protected FloatBuffer vertexBuffer;
    protected float[]     vertices;
    protected FloatBuffer textureBuffer;
    protected float       texture[] = {0.0f, 1.0f,
    			  					   0.0f, 0.0f,
    								   1.0f, 1.0f,
    								   1.0f, 0.0f};
    
    /* Osoitin CameraManageriin */
    private CameraManager cameraManager;
    
    public GLSprite()
    {
    	// Otetaan CameraManager käyttöön kameran simulointia varten
    	cameraManager = CameraManager.getInstance();
    }
    
    /**
     * Lataa tekstuurin muistiin ja muuntaa sen OpenGL-tekstuuriksi.
     * 
     * @param _context Ohjelman konteksti
     * @param _id      Resurssin tunnus
     * @param _gl      OpenGL-konteksti
     * @param _index   Tekstuurin järjestysnumero (sprites-taulukossa!)
     * 
     * @return Onnistuiko tekstuurin lataaminen?
     */
    protected boolean loadBitmap(Context _context, int _id, GL10 _gl, int _index)
    {
        // Luodaan tyhjä bitmap ja ladataan siihen tekstuuri resursseista
        Bitmap bitmap = null;
        try {
            bitmap = BitmapFactory.decodeResource(_context.getResources(), _id);
        }
        catch (Exception e) {
        	// Ladataan vakiokuva, jos latauksessa tapahtui virhe
        	bitmap = BitmapFactory.decodeResource(_context.getResources(), R.drawable.notexture);
        }
        
        // Tallennetaan tekstuurin mitat (pelkästään leveys, sillä tekstuurin korkeuden
        // on oltava sama kuin leveyden)
        if (bitmap != null) {
        	if (imageWidth == 0) {
        		imageWidth  = (float)bitmap.getWidth();
        		imageHeight = (float)bitmap.getHeight();
        	}

            // Ladataan bitmap OpenGL-tekstuuriksi
            _gl.glBindTexture(GL10.GL_TEXTURE_2D, sprites[_index]);

            _gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_LINEAR);
            _gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR);

            GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmap, 0);

            // Poistetaan bitmap muistista
            bitmap.recycle();
            
            return true;
        }
        else {
        	return false;
        }
    }
    
    /**
     * Luo vektoritaulukon kuvan koon mukaisesti.
     */
	protected void createVertices()
	{
        vertices = new float[12];
        
        vertices[0] = (-1)*imageWidth;
        vertices[1] = (-1)*imageHeight;
        vertices[2] = 0.0f;
        
        vertices[3] = (-1)*imageWidth;
        vertices[4] = imageHeight;
        vertices[5] = 0.0f;
        
        vertices[6] = imageWidth;
        vertices[7] = (-1)*imageHeight;
        vertices[8] = 0.0f;
        
        vertices[9] = imageWidth;
        vertices[10] = imageHeight;
        vertices[11] = 0.0f;
	}
	
	/**
	 * Luo vektoripuskurit.
	 */
	protected void createBuffers()
	{
		// Varataan muistia objektin vektoreille ja lisätään ne puskuriin
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(vertices.length * 4);
        byteBuffer.order(ByteOrder.nativeOrder());
        vertexBuffer = byteBuffer.asFloatBuffer();
        vertexBuffer.put(vertices);
        vertexBuffer.position(0);
        
        // Varataan muistia tekstuurin vektoreille ja lisätään ne puskuriin
        byteBuffer = ByteBuffer.allocateDirect(vertices.length * 4);
        byteBuffer.order(ByteOrder.nativeOrder());
        textureBuffer = byteBuffer.asFloatBuffer();
        textureBuffer.put(texture);
        textureBuffer.position(0);
	}
    
    /**
     * Piirtää tekstuurin ruudulle.
     * 
     * @param _gl		 OpenGL-konteksti
     * @param _x         Tekstuurin X-koordinaatti
     * @param _y         Tekstuurin Y-koordinaatti
     * @param _direction Tekstuurin suunta (0 = oikealle)
     * @param _frame     Tekstuurin järjestysnumero (animaatioille, tekstuureilla aina 0)
     */
    public final void draw(GL10 _gl, float _x, float _y, int _direction, int _frame)
    {
        // Resetoidaan mallimatriisi
        _gl.glLoadIdentity();
        
        // Siirretään ja käännetään mallimatriisia
    	// TODO: Alukset tökkii
        _gl.glTranslatef(_x - cameraManager.xTranslate, _y - cameraManager.yTranslate, 0);
        _gl.glRotatef((float)_direction-90.0f, 0.0f, 0.0f, 1.0f);
        _gl.glScalef(Options.scale/2, Options.scale/2, 0.0f); // TODO: Miksi jaetaan kahdella?
        
        // Valitaan piirrettävä tekstuuri
        _gl.glBindTexture(GL10.GL_TEXTURE_2D, sprites[_frame]);
        
        // Avataan tekstuuri- ja vektoritaulukot käyttöön
        _gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
        _gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
        
        // Valitaan neliön näytettävä puoli
        _gl.glFrontFace(GL10.GL_CW);
        
        // Otetaan vektori- ja tekstuuripuskurit käyttöön
        _gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertexBuffer);
        _gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, textureBuffer);
    
        // Piirretään neliö
        _gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, vertices.length/3);
    
        // Lukitaan tekstuuri- ja vektoritaulukot pois käytöstä
        _gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
        _gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
    }
    
    /**
     * Piirtää tekstuurin ruudulle ottaen huomioon suunnat myös syvyyssuunnassa.
     * 
     * @param _gl		     OpenGL-konteksti
     * @param _x             Tekstuurin X-koordinaatti
     * @param _y             Tekstuurin Y-koordinaatti
     * @param _direction     Tekstuurin suunta (0 = oikealle)
     * @param _frame         Tekstuurin järjestysnumero (animaatioille, tekstuureilla aina 0)
     * @param _xAxisRotation Kääntö X-akselilla
     * @param _yAxisRotation Kääntö Y-akselilla
     */
    public final void drawIn3D(GL10 _gl, float _x, float _y, int _direction, int _frame,
    						   float _xAxisRotation, float _yAxisRotation)
    {
        // Resetoidaan mallimatriisi
        _gl.glLoadIdentity();
        
        // Siirretään ja käännetään mallimatriisia
    	// TODO: Alukset tökkii
        _gl.glTranslatef(_x - cameraManager.xTranslate, _y - cameraManager.yTranslate, 0);
        _gl.glRotatef((float)_direction-90.0f, _xAxisRotation, _yAxisRotation, 1.0f);
        _gl.glScalef(Options.scale/2, Options.scale/2, 0.0f); // TODO: Miksi jaetaan kahdella?
        
        // Valitaan piirrettävä tekstuuri
        _gl.glBindTexture(GL10.GL_TEXTURE_2D, sprites[_frame]);
        
        // Avataan tekstuuri- ja vektoritaulukot käyttöön
        _gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
        _gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
        
        // Valitaan neliön näytettävä puoli
        _gl.glFrontFace(GL10.GL_CW);
        
        // Otetaan vektori- ja tekstuuripuskurit käyttöön
        _gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertexBuffer);
        _gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, textureBuffer);
    
        // Piirretään neliö
        _gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, vertices.length/3);
    
        // Lukitaan tekstuuri- ja vektoritaulukot pois käytöstä
        _gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
        _gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
    }
}
