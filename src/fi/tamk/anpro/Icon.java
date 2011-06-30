package fi.tamk.anpro;

import android.util.Log;

/**
 * Sis‰lt‰‰ aseiden cooldowneihin tarvittavat toiminnot.
 * 
 * @extends GuiObject
 */
public class Icon extends GuiObject
{
	private final int DEVIDER = 1000;
	public Icon(int _x, int _y)
	{
		super(_x, _y);
		
		usedTexture = GLRenderer.TEXTURE_COOLDOWN;
	}
	
	public void updateCooldownIcon(int _cooldown)
	{
		//							13-23
		usedTexture = GLRenderer.TEXTURE_COOLDOWN + (int)((((float)_cooldown / (float)DEVIDER) * 20) + 1);
		
		if (_cooldown <= 0)
			usedTexture = GLRenderer.TEXTURE_COOLDOWN;
	}

}
