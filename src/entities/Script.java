package entities;

public abstract class Script {
	public Entity entity;
	//Before the game Starts
	public abstract void Start();
	
	//Every Frame
	public abstract void Update();
	
	public Script(Entity entity) {
		this.entity = entity;
	}
	//Every physicsUpdate(Not Implemented yet)
}
