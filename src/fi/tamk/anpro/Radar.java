package fi.tamk.anpro;

public class Radar extends GuiObject {

	private int target;
	private double distance;
	private double angle;
	
	public Radar(int _x, int _y, int _target) {
		
		super(_x, _y);
		
		target = _target;
		
		usedTexture = GLRenderer.TEXTURE_RADAR;
	}
	
	public final void updateRadar() {
		
		for(int i = 0; i <= wrapper.enemies.size()-1; ++i) {
			
			distance = Utility.getDistance(wrapper.player.x, wrapper.player.y, wrapper.enemies.get(i).x, wrapper.enemies.get(i).y);
			
			angle = Utility.getAngle(wrapper.player.x, wrapper.player.y, wrapper.enemies.get(i).x, wrapper.enemies.get(i).y);
			
			if(angle > 315 || angle <= 45) {
				target = 12;
			}
			
			else if(angle > 45 && angle <= 135) {
				target = 3;
			}
			
			else if(angle > 135 && angle <= 225) {
				target = 6;
			}
			
			else if(angle > 225 && angle <= 315) {
				target = 9;
			}

			if(distance < 400) {
				target = 0;
			}
			
			usedTexture = GLRenderer.TEXTURE_RADAR + target;
		}
	}
}
