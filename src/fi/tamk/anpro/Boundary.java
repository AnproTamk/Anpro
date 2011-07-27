package fi.tamk.anpro;

import javax.microedition.khronos.opengles.GL10;

public class Boundary extends GfxObject 
{
	
	
	public Boundary(int _x, int _y, int _type)
	{
		x = _x;
		y = _y;
		
		if (_type == 0) {
			direction = 0;
		}
		else if (_type == 1) {
			direction = 90;
		}
		
		Wrapper.getInstance().addToDrawables(this);
	}
	
	
	@Override
	public void draw(GL10 _gl) 
	{
		// Tarkistaa onko animaatio p‰‰ll‰ ja kutsuu oikeaa animaatiota tai tekstuuria
        GLRenderer.boundaryTexture.draw(_gl, x, y, direction, currentFrame);
	}

	@Override
	protected void triggerEndOfAction()
	{
		// TODO Auto-generated method stub

	}
}
