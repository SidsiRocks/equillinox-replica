package entities;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector3f;

public class Camera {
	
	private float distance = 50f;
	private float angleAroundPlayer = 0f;
	
	private Vector3f position = new Vector3f(0,5,0);
	private float pitch = 10;
	private float yaw = 0;
	private float roll = 0;
	
	public Entity player;
	
	public boolean change = false;
	
	public Camera(){}
	
	public void move() {
//		if(Keyboard.isKeyDown(Keyboard.KEY_UP)) {
//			position.y += 0.5;
//		}
//		if(Keyboard.isKeyDown(Keyboard.KEY_DOWN)) {
//			position.y -= 0.5;
//		}
//		if(Keyboard.isKeyDown(Keyboard.KEY_RIGHT)) {
//			position.x += 0.5;
//		}
//		if(Keyboard.isKeyDown(Keyboard.KEY_LEFT)) {
//			position.x -= 0.5;
//		}
//		if(Keyboard.isKeyDown(Keyboard.KEY_ADD)) {
//			position.z -= 0.5;
//		}
//		if(Keyboard.isKeyDown(Keyboard.KEY_SUBTRACT)) {
//			position.z += 0.5;
//		}
		
		calculateZoom();
		calculatePitch();
		angleAroundPlayer();
		
		float horizontal = calculateHorizontalDistance();
		float vertical = caluclteVerticalDistance();
		
		moveCamera(horizontal,vertical);
		
		this.yaw = 180 - (player.getRotY() + angleAroundPlayer);
	}

	private void moveCamera(float horizontal,float vertical) {
		float theta = player.getRotY() + angleAroundPlayer;
		float offsetX = (float) (horizontal*Math.sin(Math.toRadians(theta)));
		float offsetZ = (float) (horizontal*Math.cos(Math.toRadians(theta)));
		Vector3f displacement = new Vector3f(-offsetX,vertical,-offsetZ);
		Vector3f finaldisplacment = new Vector3f(player.getPosition().x + displacement.x,player.getPosition().y + displacement.y,player.getPosition().z + displacement.z);
		setPosition(finaldisplacment);
	}
		
	public Vector3f getPosition() {
		return position;
	}

	public float getPitch() {
		return pitch;
	}

	public float getYaw() {
		return yaw;
	}

	public float getRoll() {
		return roll;
	}
	
	public void setPosition(Vector3f position) {
		this.position = position;
	}

	private void calculateZoom() {
		float zoomlevel = Mouse.getDWheel()*0.1f;
		distance -= zoomlevel;
	}
	
	private void calculatePitch() {
		if(Mouse.isButtonDown(0)) {
			float pitchchange = Mouse.getDY()*0.1f;
			pitch -= pitchchange;
		}
	}
	
	private void angleAroundPlayer() {
		if(Mouse.isButtonDown(0)) {
			float angleChange = Mouse.getDX() * 0.3f;
			angleAroundPlayer -= angleChange;
		}
	}
	
	private float calculateHorizontalDistance() {
		return (float)(distance * Math.cos(Math.toRadians(pitch)));
	}
	
	private float caluclteVerticalDistance() {
		return (float)(distance * Math.sin(Math.toRadians(pitch)));		
	}
}
