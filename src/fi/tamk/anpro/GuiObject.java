package fi.tamk.anpro;

import javax.microedition.khronos.opengles.GL10;

public class GuiObject extends GfxObject {
	public StringTexture stringTexture = null;
	
	private Wrapper wrapper;
	
	private int listId;
	
	public GuiObject() {
        wrapper = Wrapper.getInstance();
        
        listId = wrapper.addToList(this);
	}

    public void draw(GL10 _gl)
    {
    	GLRenderer.hudTextures.get(usedTexture).draw(_gl, x, y, 0);
    }
	
	public void setDrawables(StringTexture _texture)
	{
		stringTexture = _texture;
	}
}
