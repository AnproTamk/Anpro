package fi.tamk.anpro;

import javax.microedition.khronos.opengles.GL10;

public class GuiObject extends GfxObject {
	private String text;
	
	public StringTexture stringTexture = null;
	
	private Wrapper wrapper;
	
	private int listId;
	
	public GuiObject(String _text) {
		text = _text;
        
        wrapper = Wrapper.getInstance();
        
        listId = wrapper.addToList(this);
	}

    public void draw(GL10 _gl)
    {
    	try {
    		stringTexture.draw(_gl, x, y);
    	}
    	catch (Exception e) {
    		// juu...
    	}
    }
	
	public void setDrawables(StringTexture _texture)
	{
		stringTexture = _texture;
	}
}
