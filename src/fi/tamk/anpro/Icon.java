package fi.tamk.anpro;

public class Icon extends GuiObject {

	public Icon(int _x, int _y) {
		
		super(_x, _y);
		// TODO Auto-generated constructor stub
		usedTexture = GLRenderer.TEXTURE_BUTTON_NOTSELECTED;
	}

	public void setState(boolean _selected) {
		if(_selected) {
			usedTexture = GLRenderer.TEXTURE_BUTTON_NOTSELECTED;
		}
		
		else {
			usedTexture = GLRenderer.TEXTURE_BUTTON_NOTSELECTED;
		}
	}
}
	

