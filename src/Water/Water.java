package Water;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import models.RawModel;
import renderEngine.Loader;

public class Water {
	public static int SIZE = 3072;
	public static int VERTEX_COUNT = 60;
	public float x,z;
	
	public static RawModel waterModel;
	
	public Water(float gridX,float gridZ,Loader loader) {
		this.x = gridX;
		this.z = gridZ;
		if(waterModel == null) {
			waterModel = generateRawModel(loader);
		}
	}
	
	public RawModel generateRawModel(Loader loader) {

		int count = VERTEX_COUNT * VERTEX_COUNT;
		float[] vertices = new float[count * 3];		
		int vertexPointer = 0;
		
		for(int i=0;i<VERTEX_COUNT;i++){
			for(int j=0;j<VERTEX_COUNT;j++){
				vertices[vertexPointer*3] = (float)j/((float)VERTEX_COUNT - 1) * SIZE;
				vertices[vertexPointer*3+1] = -40;
				vertices[vertexPointer*3+2] = (float)i/((float)VERTEX_COUNT - 1) * SIZE;
				
				vertexPointer++;
			}
		}
		
		int[] indices = new int[6*(VERTEX_COUNT - 1)*(VERTEX_COUNT - 1)];
		int pointer = 0;
		
		for(int gz=0;gz<VERTEX_COUNT-1;gz++){
			for(int gx=0;gx<VERTEX_COUNT-1;gx++){
				int topLeft = (gz*VERTEX_COUNT)+gx;
				int topRight = topLeft + 1;
				int bottomLeft = ((gz+1)*VERTEX_COUNT)+gx;
				int bottomRight = bottomLeft + 1;
				if((gx+gz)%2 == 0) {
					indices[pointer++] = topLeft;
					indices[pointer++] = bottomLeft;
					indices[pointer++] = topRight;
					indices[pointer++] = bottomRight;
					indices[pointer++] = topRight;
					indices[pointer++] = bottomLeft;
				}else {
					indices[pointer++] = topLeft;
					indices[pointer++] = bottomLeft;
					indices[pointer++] = topRight;
					indices[pointer++] = bottomRight;
					indices[pointer++] = topRight;
					indices[pointer++] = bottomLeft;				
				}
			}
		}
		File file = new File("C:\\Users\\Destop\\Desktop\\Water.txt");
		OutputStream stream = null;
		try {
			stream = new FileOutputStream(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		String data2 = "";
		for(int i = 0;i < indices.length;i+=3) {
			data2 += (indices[i] + " " + indices[i+1] + " "  + indices[i+2]);
			data2 += "\n";
		}
		for(int i = 0;i < data2.length();i++) {
			try {
				stream.write((int) data2.charAt(i));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return loader.loadToVAO(vertices,indices);
	}

	public RawModel getModel() {
		return waterModel;
	}
	
	public float getX() {
		return x;
	}
	public float getZ() {
		return z;
	}
}
