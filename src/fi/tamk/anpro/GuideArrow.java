package fi.tamk.anpro;

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
			for (int i = wrapper.collectables.size() - 1; i >= 0; --i) {
				if (wrapper.collectableStates.get(i) == Wrapper.FULL_ACTIVITY) {
					target = wrapper.collectables.get(i);
				}
			}
		}
		
		// P‰ivitet‰‰n suunta kohteeseen
		direction = Utility.getAngle(x, y, target.x, target.y);
	}
}
