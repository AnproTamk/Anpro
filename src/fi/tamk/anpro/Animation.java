package fi.tamk.anpro;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

import fi.tamk.anpro.R;
import fi.tamk.anpro.R.drawable;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLUtils;

public class Animation {
    // Frames
    public int[] frames;
    public int   length;
    
    // Tekstuurin mitat
    private float imageSize = 0;
    
    // Puskuri ja taulukko vektoreita varten
    public FloatBuffer vertexBuffer;
    public float[] vertices;

    // Rakentaja
    public Animation(GL10 _gl, Context context, String _id, int _length) {
        frames = new int[_length];
        length = _length;

        try {
            String idField = _id + "_anim_";
            for (int i = 0; i < frames.length; ++i) {
                loadFrame(_gl, context, frames, i, R.drawable.class.getField(idField+"_"+i).getInt(getClass()));
            }
        } catch (Exception e) {
            // Vituiks meni
        }
        
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
    }

    public void loadFrame(GL10 gl, Context context, int[] var, int offset, int id)
    {
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), id);
        
        // Asetetaan mitat
        if (imageSize == 0) {
        	imageSize = (float)bitmap.getWidth();
        }

        gl.glGenTextures(1, var, 0);
        gl.glBindTexture(GL10.GL_TEXTURE_2D, var[offset]);

        gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_NEAREST);
        gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_NEAREST);

        GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmap, 0);

        bitmap.recycle();
    }
}
