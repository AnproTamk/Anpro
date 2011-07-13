package fi.tamk.anpro;

import android.util.Log;

public class GuideArrow extends GuiObject
{
	private Collectable target;
	//private Mothership target;
	
	public GuideArrow(int _x, int _y)
	{
		super(_x, _y);
		
		usedTexture = GLRenderer.TEXTURE_GUIDEARROW;
	}

	public final void updateArrow()
	{
		// Haetaan uusi kohde
		if (target == null || wrapper.collectableStates.get(target.listId) != Wrapper.FULL_ACTIVITY) {

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
				target = wrapper.collectables.get(indexOfClosest);
			}
		}

		// Päivitetään suunta kohteeseen
		if (target != null) {
			wrapper.guiObjectStates.set(listId, Wrapper.FULL_ACTIVITY);
			direction = Utility.getAngle(x, y, target.x, target.y);
			Log.e("TESTI", String.valueOf(direction));
		}
		else {
			wrapper.guiObjectStates.set(listId, Wrapper.INACTIVE);
		}
	}
}
