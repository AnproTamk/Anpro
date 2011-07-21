package fi.tamk.anpro;

public class GuideArrow extends GuiObject
{
	/* Vakiot kohteille */
	public static final byte TARGET_COLLECTABLE = 0;
	public static final byte TARGET_MOTHERSHIP  = 1;
	public static final byte TARGET_BOSS	    = 2;
		
	/* Kohde ja tyyppi */
	private GameObject targetObject = null;
	private byte       targetType;
	
	/**
	 * Alustaa luokan muuttujat.
	 * 
	 * @param _x      Nuolen X-koordinaatti
	 * @param _y	  Nuolen Y-koordinaatti
	 * @param _target Kohteen tyyppi, johon nuolen pitäisi osoittaa
	 */
	public GuideArrow(int _x, int _y, byte _target)
	{
		super(_x, _y);
		
		// Tallennetaan kohteen tyyppi
		targetType = _target;
		
		// Määritetään aloitustekstuuri
		usedTexture = GLRenderer.TEXTURE_GUIDEARROW + _target;
	}

	/**
	 * Päivittää nuolen etsimällä uuden kohteen mikäli tarpeen ja tarkistamalla
	 * nuolen ja kohteen välisen kulman.
	 */
	public final void updateArrow()
	{
		// Haetaan uusi kohde
		if (targetObject == null || targetObject.state != Wrapper.FULL_ACTIVITY) {

			int distanceToClosest = -1;
			int indexOfClosest    = -1;
			int distance;

			if (targetType == TARGET_COLLECTABLE) {
				for (int i = wrapper.collectables.size() - 1; i >= 0; --i) {
	
					if (wrapper.collectables.get(i).state == Wrapper.FULL_ACTIVITY) {
						distance = Utility.getDistance(x, y, wrapper.collectables.get(i).x, wrapper.collectables.get(i).y);
	
						if (distanceToClosest == -1 || distance < distanceToClosest) {
							distanceToClosest = distance;
							indexOfClosest    = i;
						}
					}
				}
	
				if (indexOfClosest != -1) {
					targetObject = wrapper.collectables.get(indexOfClosest);
				}
			}
			else if (targetType == TARGET_MOTHERSHIP) {
				targetObject = wrapper.mothership;
			}
			else if (targetType == TARGET_BOSS) {
				// TODO: Tee toteutus
			}
		}
	
		// Päivitetään suunta kohteeseen, jos sellainen on
		if (targetObject != null) {
			state = Wrapper.FULL_ACTIVITY;
			direction = Utility.getAngle(wrapper.player.x, wrapper.player.y, targetObject.x, targetObject.y);
		}
		else {
			state = Wrapper.INACTIVE;
		}
	}
}
