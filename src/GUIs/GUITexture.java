package GUIs;

import org.lwjgl.util.vector.Vector2f;

import renderEngine.DisplayManager;

public class GUITexture {
	
	public static int UPPER_LEFT = 0;
	public static int UPPER_RIGHT = 1;
	public static int LOWER_RIGHT = 2;
	public static int LOWER_LEFT = 3;
	
	private int texture;
	private Vector2f position;
	private Vector2f scale;
	
	public int getTexture() {
		return texture;
	}
	public Vector2f getPosition() {
		return position;
	}
	public Vector2f getScale() {
		return scale;
	}
	
	public GUITexture(int texture, Vector2f position, Vector2f scale) {
		super();
		this.texture = texture;
		this.position = position;
		Vector2f newscale = new Vector2f(scale.x/DisplayManager.WIDTH,scale.y/DisplayManager.HEIGHT);
		this.scale = newscale;
	}
	
	public GUITexture(int texture, Vector2f position, Vector2f scale,int Mode) {
		super();
		this.texture = texture;
		
		Vector2f newscale = new Vector2f(scale.x/DisplayManager.WIDTH,scale.y/DisplayManager.HEIGHT);
		this.scale = newscale;
		
		int factorx = 1,factory = 1;
		
		if(Mode == UPPER_RIGHT || Mode == LOWER_RIGHT) {
			factorx = -1;
		}
		if(Mode == UPPER_LEFT || Mode == UPPER_RIGHT) {
			factory = -1;
		}
		
		Vector2f newposition = new Vector2f();
		newposition.x = position.x + (newscale.x*factorx);
		newposition.y = position.y + (newscale.y*factory);
		
		this.position = newposition;
		System.out.println("position :" + newposition);
	}	
	
}
