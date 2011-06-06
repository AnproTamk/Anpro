package fi.tamk.anpro;

import javax.microedition.khronos.opengles.GL10;

import android.content.Context;

public class GameObject extends GfxObject {
	// Vakioita t�rm�yksentunnistukseen
	public static final int NO_COLLISION = 0;
	public static final int CIRCLE_COLLISION = 1;
	public static final int COLLISION_WITH_PROJECTILE = 10;
	public static final int COLLISION_WITH_PLAYER = 11;
	
	// Vakioita valintaan (ampumisessa)
	public static final int NO_SELECTION = 0;
	public static final int SELECTABLE = 1;
	
	// T�rm�yksentunnistuksen muuttujat
	public int collisionType = 0;
	public int collisionRadius = 0;
	
	// Valinnan muuttujat
	public int selectionRadius = 0;
	
	// Liikkeen muuttujat
	private int movementSpeed = 0;
	private int movementAcceleration = 0;
	
	// Suunnan ja k��ntymisen muuttujat
	private int direction = 0; // 0 on suoraan yl�sp�in, 90 oikealle
	private int turningSpeed = 0;
	private int turningAcceleration = 0;
	private int turningDirection = 0; // 0 ei k��nny, 1 vasen, 2 oikea
	
	
	public GameObject(GL10 _gl, Context _context, int _id) {
		super(_gl, _context, _id);
	}
	
	// Tekee osumalaskennat r�j�hdyksiss� (ei kahden objektin osumisessa toisiinsa)
	public void triggerImpact(int _damage, int _armorPiercing) {
		// VIRTUAALINEN
	}
	
	// Tekee osumalaskennat suorassa osumassa toiseen objektiin
	public void handleCollision(int _eventType, int _damage, int _armorPiercing) {
		// VIRTUAALINEN
	}
	
	public void updateMovement() {
		
	}
}
