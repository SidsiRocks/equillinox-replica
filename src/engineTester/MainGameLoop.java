package engineTester;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import java.net.URL;

import models.RawModel;
import models.TexturedModel;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import GUIs.GUITexture;
import GUIs.GuiRenderer;
import noise.PerlinNoise;
import Physics.TestScript;
import Physics.Time;
import Txt.TextMaster;
import Water.Water;
import renderEngine.DisplayManager;
import renderEngine.Loader;
import renderEngine.MasterRenderer;
import renderEngine.OBJLoader;
import terrains.Terrain;
import textures.ModelTexture;
import toolBox.Maths;
import entities.Camera;
import entities.Entity;
import entities.Light;
import fontMeshCreator.FontType;
import fontMeshCreator.GUIText;

public class MainGameLoop {

	public static void main(String[] args) {
		long time1 = System.currentTimeMillis();
		DisplayManager.createDisplay();
		Loader loader = new Loader();
		//255 242 0
		TextMaster.init(loader);
		Light light = new Light(new Vector3f(-1f, -1f, -1f), new Vector3f(1,1f,1f));
		
		Camera camera = new Camera();	
		camera.setPosition(new Vector3f(5.800659f, 118.599976f, 150.60645f));
		
		RawModel sheep = OBJLoader.loadObjModel("sheep_8", loader);
		ModelTexture sheepTexture = new ModelTexture(loader.loadTexture("sheep_2"));
		TexturedModel texturedSheep = new TexturedModel(sheep, sheepTexture);
		
		InputStream stream = Class.class.getResourceAsStream("/sans.fnt");
		
		FontType font = new FontType(loader.loadTexture("sans"),stream);
		
		GUIText text = new GUIText("0",3,font,new Vector2f(0f,0f),0.5f,false);
		text.setColor(new Vector3f(255/255f,242/255f,0));
		
		Vector3f position = new Vector3f(512f,100,512);
		
		Entity Sheep = new Entity(texturedSheep,position, 0, 15, 0, 5);
		camera.player = Sheep;
		
		ModelTexture terrain = new ModelTexture(loader.loadTexture("grass"));
		
		Terrain t = new Terrain(0,0, loader, terrain,null);	
		Terrain t1 = new Terrain(1,0, loader, terrain,null);	
		Terrain t2 = new Terrain(0,1, loader, terrain,null);	
		Terrain t3 = new Terrain(1,1, loader, terrain,null);	
		Terrain t4 = new Terrain(0,-1, loader, terrain,null);
		Terrain t5 = new Terrain(-1,0, loader, terrain,null);
		Terrain t6 = new Terrain(-1,-1, loader, terrain,null);
		Terrain t7 = new Terrain(1,1, loader, terrain,null);
		Terrain t8 = new Terrain(-1,1, loader, terrain,null);
		Terrain t9 = new Terrain(1,-1, loader, terrain,null);

		Water w = new Water(-0.35f,-0.35f, loader);

		
		MasterRenderer renderer = new MasterRenderer();
		
		GuiRenderer guiRender = new GuiRenderer(loader);
		
		List<GUITexture> guis = new ArrayList<GUITexture>();
		GUITexture texture = new GUITexture(loader.loadTexture("black"),new Vector2f(-1f,1f),new Vector2f(160,80),GUITexture.UPPER_LEFT);
		guis.add(texture);
		
		long i = 0;
		
		while(!Display.isCloseRequested()){
			text.load();
			camera.move();
			renderer.processEntity(Sheep);
			renderer.processWater(w);
			
			renderer.processTerrain(t);
			renderer.processTerrain(t1);
			renderer.processTerrain(t2);
			renderer.processTerrain(t3);
			renderer.processTerrain(t4);
			renderer.processTerrain(t5);
			renderer.processTerrain(t6);
			renderer.processTerrain(t7);
			renderer.processTerrain(t8);
			renderer.processTerrain(t9);
			
			
			
			if(Keyboard.isKeyDown(Keyboard.KEY_SPACE)) {
				System.out.println(camera.getPosition());
			}
			if(Keyboard.isKeyDown(Keyboard.KEY_1)) {
				System.out.println(1/Time.deltaTime);
			}
			renderer.render(light, camera);
			guiRender.render(guis);
			
			TextMaster.render();
			
			DisplayManager.updateDisplay();
			float fps = 1f/Time.deltaTime;
			int fps2 = (int) fps;
			if(i%10 == 0) {
				text.setText(Integer.toString(fps2));
			}
			text.remove();
			i++;
		}
		
		TextMaster.cleanUp();
		guiRender.cleanUp();
		renderer.cleanUp();
		loader.cleanUp();
		
		DisplayManager.closeDisplay();

	}

	
	
}
