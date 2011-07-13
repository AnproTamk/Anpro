package fi.tamk.anpro;

import android.util.Log;

public class GuideArrow extends GuiObject
{
	/* Vakiot kohteille */
	public static final byte TARGET_COLLECTABLE = 0;
	public static final byte TARGET_MOTHERSHIP  = 1;
		
	/* Kohteet */
	private Collectable collectableTarget = null;
	private Mothership  mothershipTarget  = null;
	
	/* Nuolen tyyppi */
	private byte targetType; 
	
	public GuideArrow(int _x, int _y, byte _target)
	{
		super(_x, _y);
		
		targetType = _target;
		
		usedTexture = GLRenderer.TEXTURE_GUIDEARROW + _target;
	}

	public final void updateArrow()
	{
		if (targetType == TARGET_COLLECTABLE) {
			// Haetaan uusi kohde
			if (collectableTarget == null || wrapper.collectableStates.get(collectableTarget.listId) != Wrapper.FULL_ACTIVITY) {
	
				int distanceToClosest = -1;
				int indexOfClosest    = -1;
				int distance;
	
				for (int i = wrapper.collectables.size() - 1; i >= 0; --i) {
	
					if (wrapper.collectableStates.get(i) == Wrapper.FULL_ACTIVITY) {
						distance = Utility.getDistance(x, y, wrapper.collectables.get(i).x, wrapper.collectables.get(i).y);
	
						if (distanceToClosest == -1 || distance < distanceToClosest) {
							distanceToClosest = distance;
							indexOfClosest    = i;
						}
					}
				}
	
				if (indexOfClosest != -1) {
					collectableTarget = wrapper.collectables.get(indexOfClosest);
				}
			}
	
			// Päivitetään suunta kohteeseen
			if (collectableTarget != null) {
				wrapper.guiObjectStates.set(listId, Wrapper.FULL_ACTIVITY);
				direction = Utility.getAngle(wrapper.player.x, wrapper.player.y, collectableTarget.x, collectableTarget.y);
			}
			else {
				wrapper.guiObjectStates.set(listId, Wrapper.INACTIVE);
			}
		}
		else if (targetType == TARGET_MOTHERSHIP) {
			if (mothershipTarget == null) {
				mothershipTarget = wrapper.mothership;
			}
			direction = Utility.getAngle(wrapper.player.x, wrapper.player.y, mothershipTarget.x, mothershipTarget.y);
		}
		
	}
}
