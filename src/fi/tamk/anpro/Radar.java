package fi.tamk.anpro;

public class Radar extends GuiObject {

	private int enemy;
	
	public Radar(int _x, int _y, int _enemy) {
		
		super(_x, _y);
		
		enemy = _enemy;
		
		usedTexture = GLRenderer.TEXTURE_RADAR;
	}

}
