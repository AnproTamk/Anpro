package fi.tamk.anpro;

public class Radar extends GuiObject {

	private int type;
	private double distance;
	private double angle;
	
	/**
	 * Tutkan rakentaja
	 * 
	 * @param _x
	 * @param _y
	 * @param _type
	 */
	public Radar(int _x, int _y, int _type) {
		
		super(_x, _y);
		
		type = _type;
		
		usedTexture = GLRenderer.TEXTURE_RADAR;
		
		// Tutka ylös
		if(type == 0) {
			direction = 90;
		}
		
		// Tutka vasen
		if(type == 1) {
			direction = 180;
		}
		
		// Tutka oikea
		if(type == 2) {
			direction = 0;
		}
		
		// Tutka alas
		if(type == 3) {
			direction = -90;
		}
	}
	
	public final void updateRadar() {

		if (usedAnimation == -1) {
			for(int i = wrapper.enemies.size()-1; i >= 0; --i) {
				
				if(wrapper.enemyStates.get(i) == Wrapper.FULL_ACTIVITY) {
					distance = Utility.getDistance(wrapper.player.x, wrapper.player.y, wrapper.enemies.get(i).x, wrapper.enemies.get(i).y);
					
					angle = Utility.getAngle(wrapper.player.x, wrapper.player.y, wrapper.enemies.get(i).x, wrapper.enemies.get(i).y);
					
					if(distance <= 800 && distance >= 350) {
						
						if((angle > 315 || angle <= 45) && type == 2) {
								startAnimation(GLRenderer.ANIMATION_RADAR_WARNING, 1, 1, GLRenderer.TEXTURE_RADAR, 0);
								break;
						}
						
						else if((angle > 45 && angle <= 135) && type == 1) {
								startAnimation(GLRenderer.ANIMATION_RADAR_WARNING, 1, 1, GLRenderer.TEXTURE_RADAR, 0);
								break;
						}
						
						else if((angle > 135 && angle <= 225) && type == 3) {
								startAnimation(GLRenderer.ANIMATION_RADAR_WARNING, 1, 1, GLRenderer.TEXTURE_RADAR, 0);
								break;
						}
						
						else if((angle > 225 || angle <= 315) && type == 0) {
								startAnimation(GLRenderer.ANIMATION_RADAR_WARNING, 1, 1, GLRenderer.TEXTURE_RADAR, 0);
								break;
						}
					}
				}
			}
		}
	}
}
