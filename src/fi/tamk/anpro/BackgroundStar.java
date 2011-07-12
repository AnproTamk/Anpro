package fi.tamk.anpro;

import javax.microedition.khronos.opengles.GL10;

public class BackgroundStar extends GfxObject
{
	// Osoitin Wrapperiin
	private Wrapper       wrapper;
	private CameraManager cameraManager;
	
	// Osoitin CameraManageriin
	
	public BackgroundStar(int _x, int _y)
	{
		x = _x;
		y = _y;
		
		wrapper       = Wrapper.getInstance();
		cameraManager = CameraManager.getInstance();
		
		wrapper.addToList(this, Wrapper.CLASS_TYPE_BACKGROUNDSTAR, 0);
	}

	public void checkPosition()
	{
		if (x < cameraManager.xTranslate - 600) {
			x = Utility.getRandom((int)cameraManager.xTranslate + 420, (int)cameraManager.xTranslate + 590);
			y = Utility.getRandom((int)cameraManager.yTranslate - 235, (int)cameraManager.yTranslate + 235);
		}
		else if (x > cameraManager.xTranslate + 600) {
			x = Utility.getRandom((int)cameraManager.xTranslate - 590, (int)cameraManager.xTranslate - 420);
			y = Utility.getRandom((int)cameraManager.yTranslate - 235, (int)cameraManager.yTranslate + 235);
		}
		else if (y < cameraManager.yTranslate - 400) {
			x = Utility.getRandom((int)cameraManager.xTranslate - 395, (int)cameraManager.xTranslate + 395);
			y = Utility.getRandom((int)cameraManager.yTranslate + 245, (int)cameraManager.yTranslate + 390);
		}
		else if (y > cameraManager.yTranslate + 400) {
			x = Utility.getRandom((int)cameraManager.xTranslate - 395, (int)cameraManager.xTranslate + 395);
			y = Utility.getRandom((int)cameraManager.yTranslate - 390, (int)cameraManager.yTranslate - 245);
		}
		
		/*if (x < -20 - cameraManager.xTranslate + (float)(Options.screenWidth)/2) {
			x = Utility.getRandom((int)(-100 - cameraManager.xTranslate - Options.screenWidth), (int)(-20 - cameraManager.xTranslate - Options.screenWidth));
		}
		else if (x > 20 + cameraManager.xTranslate - (float)(Options.screenWidth)/2) {
			x = Utility.getRandom((int)(20 + cameraManager.xTranslate + Options.screenWidth), (int)(100 + cameraManager.xTranslate + Options.screenWidth));
		}
		else if (y < -20 - cameraManager.yTranslate + (float)(Options.screenHeight)/2) {
			y = Utility.getRandom((int)(-100 - cameraManager.yTranslate - Options.screenHeight), (int)(100 - cameraManager.yTranslate - Options.screenHeight));
		}
		else if (y > 20 + cameraManager.yTranslate - (float)(Options.screenHeight)/2) {
			y = Utility.getRandom((int)(20 + cameraManager.yTranslate + Options.screenHeight), (int)(100 + cameraManager.yTranslate + Options.screenHeight));
		}*/
	}
	
	@Override
	public void draw(GL10 _gl)
	{
		GLRenderer.starBackgroundTexture.draw(_gl, x, y, 0, 0);
	}

	@Override
	protected void triggerEndOfAction()
	{
		// ...
	}
}
