package fi.tamk.anpro;

import javax.microedition.khronos.opengles.GL10;

import android.content.Context;

public class GameObject extends GfxObject {
	public int collisionType;
	public int collisionRadius;

	private int direction; // 0 is upwards
	
	private int movementSpeed;
	private int movementAcceleration;
	
	private int turningSpeed;
	private int turningAcceleration;
	private int turningDirection; // 0 none, 1 left, 2 right
	
	
	public GameObject(GL10 _gl, Context _context, int _id) {
		super(_gl, _context, _id);
	}
}
