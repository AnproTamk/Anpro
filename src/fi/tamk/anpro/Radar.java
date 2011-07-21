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
		
		// Tallennetaan tutkan tyyppi (suunta, jota tutka tarkkailee)
		type = _type;
		
		// Määritetään tekstuuri ja suunta
		usedTexture = GLRenderer.TEXTURE_RADAR;
		
		if (type == 0) {
			direction = 90;
		}
		else if (type == 1) {
			direction = 180;
		}
		else if (type == 2) {
			direction = 0;
		}
		else if (type == 3) {
			direction = -90;
		}
	}

	/* =======================================================
	 * Uudet funktiot
	 * ======================================================= */
	public final void updateRadar() {

		if (usedAnimation == -1) {
			for(int i = wrapper.enemies.size()-1; i >= 0; --i) {
				
				if(wrapper.enemies.get(i).state == Wrapper.FULL_ACTIVITY) {
					distance = Utility.getDistance(wrapper.player.x, wrapper.player.y, wrapper.enemies.get(i).x, wrapper.enemies.get(i).y);
					
					angle = Utility.getAngle(wrapper.player.x, wrapper.player.y, wrapper.enemies.get(i).x, wrapper.enemies.get(i).y);
					
					if(distance <= 800 && distance >= 350) {
						
						if((angle > 315 || angle <= 45) && type == 2) {
								startAnimation(GLRenderer.ANIMATION_RADAR_WARNING, 1, 1, GLRenderer.TEXTURE_RADAR, 0);
								break;
						}
						
						else if((angle > 45 && angle <= 135) && type == 0) {
								startAnimation(GLRenderer.ANIMATION_RADAR_WARNING, 1, 1, GLRenderer.TEXTURE_RADAR, 0);
								break;
						}
						
						else if((angle > 135 && angle <= 225) && type == 1) {
								startAnimation(GLRenderer.ANIMATION_RADAR_WARNING, 1, 1, GLRenderer.TEXTURE_RADAR, 0);
								break;
						}
						
						else if((angle > 225 && angle <= 315) && type == 3) {
								startAnimation(GLRenderer.ANIMATION_RADAR_WARNING, 1, 1, GLRenderer.TEXTURE_RADAR, 0);
								break;
						}
					}
				}
			}
		}
	}
}
