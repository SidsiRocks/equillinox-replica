package shaders;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;

import entities.Camera;
import entities.Light;
import toolBox.Maths;

public class WaterShader extends ShaderProgram{
	public static String vertexFile = "/shaders/WaterVertex.glsl";
	public static String fragmentFile = "/shaders/WaterFragment.glsl";
	
	private int location_transformationMatrix;
	private int location_projectionMatrix;
	private int location_viewMatrix;
	private int location_lightDirection;
	private int location_lightColour;
	private int location_waveLength;
	private int location_waveTime;
	private int location_location;
	private int location_shineDamper;
	
	public WaterShader() {
		super(vertexFile,fragmentFile);
	}

	@Override
	protected void getAllUniformLocations() {
		location_transformationMatrix = super.getUniformLocation("transformationMatrix");
		location_projectionMatrix = super.getUniformLocation("projectionMatrix");
		location_viewMatrix = super.getUniformLocation("viewMatrix");
		location_lightDirection = super.getUniformLocation("lightDirection");
		location_lightColour = super.getUniformLocation("lightColour");
		location_waveLength = super.getUniformLocation("waveLength");
		location_waveTime = super.getUniformLocation("waveTime");
		location_location = super.getUniformLocation("location");
		location_shineDamper = super.getUniformLocation("shineDamper");
	}

	@Override
	protected void bindAttributes() {
		super.bindAttribute(0,"position");
	}
	
	
	public void loadTransformationMatrix(Matrix4f matrix){
		super.loadMatrix(location_transformationMatrix, matrix);
	}
	
	public void loadLight(Light light){
		super.loadVector(location_lightDirection, light.getPosition());
		super.loadVector(location_lightColour, light.getColour());
	}
	
	public void loadLocation(float x, float z) {
		super.load2DVector(location_location,new Vector2f(x,z));
	}
	public void loadViewMatrix(Camera camera){
		Matrix4f viewMatrix = Maths.createViewMatrix(camera);
		super.loadMatrix(location_viewMatrix, viewMatrix);
	}
	
	public void loadProjectionMatrix(Matrix4f projection){
		super.loadMatrix(location_projectionMatrix, projection);
	}
	
	public void loadWavelength(float wavelength) {
		super.loadFloat(location_waveLength,wavelength);
	}
	public void loadWaveTime(float waveTime) {
		super.loadFloat(location_waveTime,waveTime);
	}
	public void loadShineDamper(float shineDamper) {
		super.loadFloat(location_shineDamper,shineDamper);
	}
}
