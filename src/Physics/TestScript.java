package Physics;

import org.lwjgl.util.vector.Vector3f;

import entities.Entity;
import entities.Script;

public class TestScript extends Script{
	
	public Vector3f velocity = new Vector3f(0,0,100);
	
	public TestScript(Entity entity) {
		super(entity);
	}
	
	@Override
	public void Start() {
		
	}

	@Override
	public void Update() {
		if(Time.deltaTime < 100) {
			Vector3f displacement = Multiply(velocity,Time.deltaTime);
			Vector3f oldposition = entity.getPosition();
			Vector3f newposition = new Vector3f();
			newposition = Vector3f.add(displacement,oldposition,newposition);
			entity.setPosition(newposition);
		}
	}
	
	private Vector3f Multiply(Vector3f velocity,float time) {
		Vector3f displacementVector = new Vector3f();
		displacementVector.x = velocity.x * time;
		displacementVector.y = velocity.y * time;
		displacementVector.z = velocity.z * time;
		return displacementVector;
	}
}
