package renderEngine;

import java.util.List;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import Water.Water;
import models.RawModel;
import shaders.WaterShader;
import toolBox.Maths;

public class WaterRenderer {
	private WaterShader shader;
	public boolean wireframe = false;
	
	public WaterRenderer(WaterShader shader, Matrix4f projectionMatrix) {
		this.shader = shader;
		shader.start();
		shader.loadProjectionMatrix(projectionMatrix);
		shader.loadShineDamper(22);
		shader.stop();
	}
	
	public void render(List<Water> waterList) {
		for(Water water:waterList)	{
			prepareTerrain(water);
			loadModelMatrix(water);
			shader.loadLocation(water.x,water.z);
			//GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK,GL11.GL_LINE);
			GL11.glDrawElements(GL11.GL_TRIANGLES, water.getModel().getVertexCount(),
						GL11.GL_UNSIGNED_INT, 0);
			//GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK,GL11.GL_FILL);
			unbindTexturedModel();
		}
	}
	
	private void prepareTerrain(Water terrain) {
		RawModel rawModel = terrain.getModel();
		GL30.glBindVertexArray(rawModel.getVaoID());
		GL20.glEnableVertexAttribArray(0);
	}

	private void unbindTexturedModel() {
		GL20.glDisableVertexAttribArray(0);
		GL30.glBindVertexArray(0);
	}

	private void loadModelMatrix(Water terrain) {
		Matrix4f transformationMatrix = Maths.createTransformationMatrix(
				new Vector3f(terrain.getX()*Water.SIZE, 0, terrain.getZ()*Water.SIZE), 0, 0, 0, 1);
		shader.loadTransformationMatrix(transformationMatrix);
	}
}
