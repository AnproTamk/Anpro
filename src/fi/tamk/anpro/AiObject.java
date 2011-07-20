package fi.tamk.anpro;

import javax.microedition.khronos.opengles.GL10;

public class AiObject extends GameObject
{
	/* Tekoälyn nopeus */
    public byte aiPriority = 3;
    
    /* Haluttu liikkumissuunta (PELAAJALLE!) */
    public int movementTargetDirection;
    
    /* Tekoäly */
    public AbstractAi ai;
    
    public AiObject(int _speed)
    {
		super(_speed);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void draw(GL10 _gl) { }

	@Override
	protected void triggerEndOfAction() { }
}
