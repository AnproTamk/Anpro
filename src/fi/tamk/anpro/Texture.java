package fi.tamk.anpro;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLUtils;

public class Texture {
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
        0.0f, 1.0f, // yl�vasen
        0.0f, 0.0f, // alavasen
        1.0f, 1.0f, // yl�oikea
        1.0f, 0.0f  // alaoikea
    };

    public Texture(GL10 _gl, Context context, int _id) {
        sprite = new int[1];
        
        Bitmap bitmap = null;
        
        try {
            bitmap = BitmapFactory.decodeResource(context.getResources(), _id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        // Asetetaan mitat
        imageSize = (float)bitmap.getWidth();

        _gl.glGenTextures(1, sprite, 0);
        _gl.glBindTexture(GL10.GL_TEXTURE_2D, sprite[0]);

        _gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_NEAREST);
        _gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_NEAREST);

        GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmap, 0);

        bitmap.recycle();
        
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
        
        // Sama kuin ylh��ll�, mutta tekstuurille vektoreiden sijaan.
        byteBuffer = ByteBuffer.allocateDirect(vertices.length * 4);
        byteBuffer.order(ByteOrder.nativeOrder());
        textureBuffer = byteBuffer.asFloatBuffer();
        textureBuffer.put(texture);
        textureBuffer.position(0);
    }
    
    public void draw(GL10 _gl, float _x, float _y, int _direction) {
        // Resetoidaan mallimatriisi
        _gl.glLoadIdentity();
        
        // Siirret��n mallimatriisia (X, Y, Z)
        _gl.glTranslatef(_x, _y, 0);
        _gl.glRotatef((float)_direction-90.0f, 0.0f, 0.0f, 1.0f);
        
        // Valitaan piirrett�v� tekstuuri
        _gl.glBindTexture(GL10.GL_TEXTURE_2D, sprite[0]);
        
        // Avataan tekstuuri- ja vektoritaulukot k�ytt��n
        _gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
        _gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
        
        // Valitaan neli�n n�ytett�v� puoli
        _gl.glFrontFace(GL10.GL_CW);
        
        // Otetaan vektori- ja tekstuuripuskurit k�ytt��n
        _gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertexBuffer);
        _gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, textureBuffer);
    
        // Piirret��n neli� (parametreissa on sana "TRIANGLE", sill� neli� muodostetaan oikeasti
        // kahdesta kolmiosta)   
        _gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, vertices.length/3);
    
        // Lukitaan tekstuuri- ja vektoritaulukot pois k�yt�st�
        _gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
        _gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
    }
}
