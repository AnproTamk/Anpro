package fi.tamk.anpro;

public class Radar extends GuiObject {

	private int type;
	private int target;
	private double distance;
	private double angle;
	
	public Radar(int _x, int _y, int _type) {
		
		super(_x, _y);
		
		type = _type;
		
		usedTexture = GLRenderer.TEXTURE_RADAR;
		
		if(type == 0) {
			//usedTexture = GLRenderer.TEXTURE_RADAR;
		}
		
		if(type == 1) {
			//usedTexture = GLRenderer.TEXTURE_RADAR + 1;
		}
		
		if(type == 2) {
			//usedTexture = GLRenderer.TEXTURE_RADAR + 2;
		}
		
		if(type == 3) {
			//usedTexture = GLRenderer.TEXTURE_RADAR + 3;
		}
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
			
			usedTexture = GLRenderer.TEXTURE_RADAR;
			//usedTexture = GLRenderer.TEXTURE_RADAR + target;
		}
	}
}
