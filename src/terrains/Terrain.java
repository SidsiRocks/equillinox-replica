package terrains;

import java.io.File;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Random;

import org.lwjgl.util.vector.ReadableVector3f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import noise.Perlin;
import noise.PerlinNoise;
import TerrainSampling.BasicTerrainSampler;
import models.RawModel;
import renderEngine.Loader;
import textures.ModelTexture;

public class Terrain {
	
	public static float SIZE = 1024;
	public static int VERTEX_COUNT = 32;
	
	private float x;
	private float z;

	private float gridX;
	private float gridZ;
	
	private RawModel model;
	public float[][] heightMap;
	public float[] normals;
	
	public Terrain(int gridX, int gridZ, Loader loader){
		this.x = gridX * SIZE;
		this.z = gridZ * SIZE;
		this.gridX = gridX;
		this.gridZ = gridZ;		
		this.model = generateTerrain(loader);
	}
	
	public Terrain(int gridX, int gridZ, Loader loader, ModelTexture texture,float[][] heightMap){
		this.x = gridX * SIZE;
		this.z = gridZ * SIZE;
		this.gridX = gridX;
		this.gridZ = gridZ;	
		this.heightMap = heightMap;
		this.model = generateTerrain(loader);
	}	
	
	public float getX() {
		return x;
	}



	public float getZ() {
		return z;
	}



	public RawModel getModel() {
		return model;
	}

	private RawModel generateTerrain(Loader loader){
		int count = VERTEX_COUNT * VERTEX_COUNT;
		float[] vertices = new float[count * 3];
		float[] normals = new float[count * 3];
		float[] textureCoords = new float[count*2];
		float[] colors = new float[count * 4];
		
		int[] indices = new int[6*(VERTEX_COUNT-1)*(VERTEX_COUNT-1)];
		int vertexPointer = 0;
		
		float[][] noise = generateNoiseMap(16,120,0.9f,0.1f);
		this.heightMap = noise;
		
		BasicTerrainSampler basic= new BasicTerrainSampler();
		
		for(int i=0;i<VERTEX_COUNT;i++){
			for(int j=0;j<VERTEX_COUNT;j++){
					vertices[vertexPointer*3] = (float)j/((float)VERTEX_COUNT - 1) * SIZE;
				if(heightMap == null) {
					vertices[vertexPointer*3+1] = noise[i][j];
				}else {
					vertices[vertexPointer*3+1] = heightMap[i][j];
				}
				vertices[vertexPointer*3+2] = (float)i/((float)VERTEX_COUNT - 1) * SIZE;
				
				Vector3f normal = calculateNormal(i,j,noise);
				
				normals[vertexPointer*3] = normal.x;
				normals[vertexPointer*3+1] = normal.y;
				normals[vertexPointer*3+2] = normal.z;
				
				
				textureCoords[vertexPointer*2] = (float)j/((float)VERTEX_COUNT - 1);
				textureCoords[vertexPointer*2+1] = (float)i/((float)VERTEX_COUNT - 1);
				
				
				
				Vector4f color = basic.getColor(noise[i][j]);
				
				colors[vertexPointer * 4 + 0] = color.x;
				colors[vertexPointer * 4 + 1] = color.y;
				colors[vertexPointer * 4 + 2] = color.z;
				colors[vertexPointer * 4 + 3] = 1;
				
				vertexPointer++;
			}
		}
		
		int pointer = 0;
		
		for(int gz=0;gz<VERTEX_COUNT-1;gz++){
			for(int gx=0;gx<VERTEX_COUNT-1;gx++){
					int topLeft = (gz*VERTEX_COUNT)+gx;
					int topRight = topLeft + 1;
					int bottomLeft = ((gz+1)*VERTEX_COUNT)+gx;
					int bottomRight = bottomLeft + 1;
					indices[pointer++] = topLeft;
					indices[pointer++] = bottomLeft;
					indices[pointer++] = topRight;
					indices[pointer++] = bottomRight;
					indices[pointer++] = topRight;
					indices[pointer++] = bottomLeft;
			}
		}
		
		this.normals = normals; 
		
		return loader.loadToVAO(vertices, colors, normals, indices,true);
	}
	
	public float[][] generateHeightMap(float scale,float heightFactor){
		int seed = 1;
		int octaves = 3;
		float amplitude = 0.1f;
		float roughness = 0.5f;
		float heightMap[][] = new float[VERTEX_COUNT][VERTEX_COUNT];
		PerlinNoise perlin = new PerlinNoise(seed,octaves,amplitude,roughness);
		for(int i =0;i <VERTEX_COUNT;i++) {
			for(int j =0;j < VERTEX_COUNT;j++) {
				float x1= (i+ (VERTEX_COUNT-1)*x)/scale ;
				float y1 = (j+ (VERTEX_COUNT-1)*z)/scale;
				heightMap[i][j] =(perlin.getPerlinNoise((int)x1,(int) y1)*heightFactor);
			}
		}
		
 		return heightMap;
		
	}
	
	public float[][] generateNoiseMap(int scale,float heightFactor,float minimumamplitude,float maxamplitude){
		float x1 = (gridX*VERTEX_COUNT);
		float y1 = (gridZ*VERTEX_COUNT);
		float[][] noiseMap = new float[VERTEX_COUNT][VERTEX_COUNT];
		String data = "";
		for(int i =0;i <VERTEX_COUNT;i++) {
			data += "\n";
			for(int j =0;j < VERTEX_COUNT;j++) {
				float x2 = 0,y2 = 0;
				if(gridX == 0) {
					x2 = x1 + j;
				}else if(gridX > 0){
					x2 = x1 + j - 1;
				}else if(gridX < 0) {
					x2 = x1 + j + 1;
				}
				if(gridZ == 0) {
					y2 = y1 + i;
				}else if(gridZ > 0){
					y2 = y1 + i - 1;
				}else if(gridZ < 0) {
					y2 = y1 + i + 1;
				}
				float perlin = (float) Perlin.Loadednoise(5,minimumamplitude,maxamplitude,scale,(int)x2,(int)y2);
				noiseMap[i][j] = (int)(perlin*heightFactor);
				data += "("+ (x2) + " , " + (y2) + ")";
			}
		}
		File file = new File("C:\\Users\\Destop\\Desktop\\TestFile" + gridX + gridZ + ".txt");
		OutputStream stream = null;
		try {
			stream = new FileOutputStream(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		for(int i = 0;i < data.length();i++) {
			int j = (int) data.charAt(i);
			try {
				stream.write(j);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return noiseMap;
	}
	
	private Vector3f calculateNormal(int x,int z,float[][] heightmap) {
		if(x != 0 && z != 0 && x != VERTEX_COUNT - 1 && z != VERTEX_COUNT - 1) {
			float heightL = heightmap[x - 1][z];
			float heightR = heightmap[x + 1][z];
			float heightD = heightmap[x][z - 1];
			float heightU = heightmap[x][z + 1];
			
			Vector3f normal = new Vector3f(heightL-heightR,2f,heightD-heightU);
			normal.normalise();
			
			return normal;
		}else {
			return new Vector3f(0,1,0);
		}
	}
}
