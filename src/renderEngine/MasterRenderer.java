package renderEngine;

import java.util.ArrayList;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import models.TexturedModel;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Matrix4f;

import Physics.Time;
import Water.Water;
import shaders.BetterTerrainShader;
import shaders.StaticShader;
import shaders.WaterShader;
import terrains.Terrain;
import entities.Camera;
import entities.Entity;
import entities.Light;

public class MasterRenderer {
	
	public static final float FOV = 70;
	public static final float NEAR_PLANE = 0.1f;
	private static final float FAR_PLANE = 5000;
	
	
	private Matrix4f projectionMatrix;
	
	private StaticShader shader = new StaticShader();
	private EntityRenderer renderer;
	
	public TerrainRenderer terrainRenderer;
	private BetterTerrainShader terrainShader = new BetterTerrainShader();
	
	public WaterShader watershader = new WaterShader();
	public WaterRenderer waterRenderer;
	
	private Map<TexturedModel,List<Entity>> entities = new HashMap<TexturedModel,List<Entity>>();
	private List<Terrain> terrains = new ArrayList<Terrain>();
	private List<Water> waterList = new ArrayList<Water>();
	
	private long previous_time = 0;
	
	private float time = 20;
	
	private static float WAVE_SPEED = 18f;
	private static float WAVE_LENGTH = 12;
	
	boolean b = true;
	public MasterRenderer(){
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glCullFace(GL11.GL_BACK);
		createProjectionMatrix();
		renderer = new EntityRenderer(shader,projectionMatrix);
		terrainRenderer = new TerrainRenderer(terrainShader,projectionMatrix);
		waterRenderer = new WaterRenderer(watershader,projectionMatrix);
	}
	
	public void render(Light sun,Camera camera){
		long currentTime = System.currentTimeMillis();
		prepare();
		shader.start();
		shader.loadLight(sun);
		shader.loadViewMatrix(camera);
		renderer.render(entities);
		shader.stop();
		
		terrainShader.start();
		terrainShader.loadLight(sun);
		terrainShader.loadViewMatrix(camera);
		terrainRenderer.render(terrains);
		terrainShader.stop();
		
		if(Time.deltaTime < 1) {
			time += Time.deltaTime/WAVE_SPEED;
		}
		
		watershader.start();
		watershader.loadLight(sun);
		watershader.loadViewMatrix(camera);
		watershader.loadWavelength(WAVE_LENGTH);
		watershader.loadWaveTime(time);
		waterRenderer.render(waterList);
		watershader.stop();
		
		terrains.clear();
		entities.clear();
		waterList.clear();
		
		long deltaTime = currentTime - previous_time;
		float delta = deltaTime/1000f;
		Time.deltaTime = delta;
		
		previous_time = currentTime;
	}
	
	public void processTerrain(Terrain terrain){
		terrains.add(terrain);
	}
	
	public void processWater(Water water) {
		waterList.add(water);
	}
	
	public void processEntity(Entity entity){
		TexturedModel entityModel = entity.getModel();
		List<Entity> batch = entities.get(entityModel);
		if(batch!=null){
			batch.add(entity);
		}else{
			List<Entity> newBatch = new ArrayList<Entity>();
			newBatch.add(entity);
			entities.put(entityModel, newBatch);		
		}
	}
		
	public void cleanUp(){
		shader.cleanUp();
		terrainShader.cleanUp();
		watershader.cleanUp();
	}
	
	public void prepare() {
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
		GL11.glClearColor(0.49f, 89f, 0.98f, 1);
	}
	
	private void createProjectionMatrix() {
		float aspectRatio = (float) Display.getWidth() / (float) Display.getHeight();
		float y_scale = (float) ((1f / Math.tan(Math.toRadians(FOV / 2f))) * aspectRatio);
		float x_scale = y_scale / aspectRatio;
		float frustum_length = FAR_PLANE - NEAR_PLANE;

		projectionMatrix = new Matrix4f();
		projectionMatrix.m00 = x_scale;
		projectionMatrix.m11 = y_scale;
		projectionMatrix.m22 = -((FAR_PLANE + NEAR_PLANE) / frustum_length);
		projectionMatrix.m23 = -1;
		projectionMatrix.m32 = -((2 * NEAR_PLANE * FAR_PLANE) / frustum_length);
		projectionMatrix.m33 = 0;
	}
	

}

