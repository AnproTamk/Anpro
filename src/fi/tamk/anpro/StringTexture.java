package fi.tamk.anpro;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.opengl.GLUtils;

public class StringTexture {
    // Kuva
    private int[] sprite;
    
    // Tekstuurin mitat
    private float imageSize = 0;
    
    // Puskuri ja taulukko vektoreita varten
    public FloatBuffer vertexBuffer;
    public float[]     vertices;

    // Puskuri ja taulukko tekstuuria varten
    private FloatBuffer textureBuffer;
    private float texture[] = {
        0.0f, 1.0f, // ylävasen
        0.0f, 0.0f, // alavasen
        1.0f, 1.0f, // yläoikea
        1.0f, 0.0f  // alaoikea
    };

    /*
     * Rakentaja
     */
    public StringTexture(GL10 _gl, Context _context, String _text) {
        sprite = new int[1];
        
        // Luodaan tyhjä bitmappi ja lisätään se canvasiin
        Bitmap bitmap = Bitmap.createBitmap(64, 64, Bitmap.Config.ARGB_4444);
        bitmap.eraseColor(120);
        
        Canvas canvas = new Canvas(bitmap);
        
        int a = bitmap.getWidth();
        int b = bitmap.getHeight();
        
        // Piirretään teksti
        Paint textPaint = new Paint();
        textPaint.setTextSize(16);
        textPaint.setAntiAlias(false);
        textPaint.setARGB(255, 255, 255, 255);
        
        //canvas.drawText("tes", 0, 0, textPaint);
        canvas.drawARGB(255, 255, 255, 255);
        
        // Asetetaan mitat
        imageSize = (float)bitmap.getWidth();

        _gl.glGenTextures(1, sprite, 0);
        _gl.glBindTexture(GL10.GL_TEXTURE_2D, sprite[0]);

        _gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_NEAREST);
        _gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_NEAREST);

        GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmap, 0);

        bitmap.recycle();
        b= a; a = b;
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
    }
    
    /*
     * Piirtää tekstuurin ruudulle
     */
    public void draw(GL10 _gl, float _x, float _y) {
        // Resetoidaan mallimatriisi
    	_gl.glLoadIdentity();
        
        // Siirretään mallimatriisia (X, Y, Z)
        _gl.glTranslatef(_x, _y, 0);
        
        // Valitaan piirrettävä tekstuuri
        _gl.glBindTexture(GL10.GL_TEXTURE_2D, sprite[0]);
        
        // Avataan tekstuuri- ja vektoritaulukot käyttöön
        _gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
        _gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
        
        // Valitaan neliön näytettävä puoli
        _gl.glFrontFace(GL10.GL_CW);
        
        // Otetaan vektori- ja tekstuuripuskurit käyttöön
        _gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertexBuffer);
        _gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, textureBuffer);
    
        // Piirretään neliö (parametreissa on sana "TRIANGLE", sillä neliö muodostetaan oikeasti
        // kahdesta kolmiosta)   
        _gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, vertices.length/3);
    
        // Lukitaan tekstuuri- ja vektoritaulukot pois käytöstä
        _gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
        _gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
    }
}
