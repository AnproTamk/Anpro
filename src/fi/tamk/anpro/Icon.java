package fi.tamk.anpro;

/**
 * Sis‰lt‰‰ aseiden cooldowneihin tarvittavat toiminnot.
 * 
 * @extends GuiObject
 */
public class Icon extends GuiObject
{
	private int cooldown;
	private final int DEVIDER = 1000;
	public Icon(int _x, int _y)
	{
		super(_x, _y);
		
		usedTexture = GLRenderer.TEXTURE_COOLDOWN;
		int a = 0;
	}
	
	public void updateCooldownIcon(int _cooldown)
	{
		cooldown = _cooldown;
		
		//							14-23
		usedTexture = GLRenderer.TEXTURE_COOLDOWN + (int)((((float)cooldown / (float)DEVIDER) * 10) - 1);
		int a = 0;
	}

}
