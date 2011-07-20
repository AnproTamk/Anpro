package fi.tamk.anpro;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.opengl.GLSurfaceView.Renderer;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class CustomSurfaceView extends GLSurfaceView implements SurfaceHolder.Callback
{
	private GLThread glThread;
	
	public CustomSurfaceView(Context context)
	{
		super(context);
		
		SurfaceHolder holder = getHolder();
        holder.addCallback(this);
        holder.setType(SurfaceHolder.SURFACE_TYPE_GPU);
	}
	
	public void setRenderer(Renderer renderer)
	{
        glThread = new GLThread(renderer);
        glThread.start();
    }
	
	public void killThread()
	{
		glThread = null;
	}
	
	private class GLThread extends Thread
	{
		public GLThread(Renderer renderer)
		{
			super();
		}
		
		@Override
		public void run()
		{
			setName("CustomGLThread " + getId());
		}
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
	}
}
