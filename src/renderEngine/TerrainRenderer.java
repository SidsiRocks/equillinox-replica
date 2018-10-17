package renderEngine;

import java.util.List;

import models.RawModel;
import models.TexturedModel;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import shaders.BetterTerrainShader;
import terrains.Terrain;
import textures.ModelTexture;
import toolBox.Maths;

public class TerrainRenderer {

	private BetterTerrainShader shader;

	public boolean wireframe = false;
	
	public TerrainRenderer(BetterTerrainShader shader, Matrix4f projectionMatrix) {
		this.shader = shader;
		shader.loadShineVariables(0.1f, 0.3f);
		shader.start();
		shader.loadProjectionMatrix(projectionMatrix);
		shader.stop();
	}

	public void render(List<Terrain> terrains) {
		for (Terrain terrain : terrains) {
			prepareTerrain(terrain);
			loadModelMatrix(terrain);
			if(wireframe) {	
				GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK,GL11.GL_LINE);
				GL11.glDrawElements(GL11.GL_TRIANGLES, terrain.getModel().getVertexCount(),
						GL11.GL_UNSIGNED_INT, 0);
				GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK,GL11.GL_FILL);
			}else {
				GL11.glDrawElements(GL11.GL_TRIANGLES, terrain.getModel().getVertexCount(),
						GL11.GL_UNSIGNED_INT, 0);				
			}
			unbindTexturedModel();
		}
	}

	private void prepareTerrain(Terrain terrain) {
		RawModel rawModel = terrain.getModel();
		GL30.glBindVertexArray(rawModel.getVaoID());
		GL20.glEnableVertexAttribArray(0);
		GL20.glEnableVertexAttribArray(1);
		GL20.glEnableVertexAttribArray(2);
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
	}

	private void unbindTexturedModel() {
		GL20.glDisableVertexAttribArray(0);
		GL20.glDisableVertexAttribArray(1);
		GL20.glDisableVertexAttribArray(2);
		GL30.glBindVertexArray(0);
	}

	private void loadModelMatrix(Terrain terrain) {
		Matrix4f transformationMatrix = Maths.createTransformationMatrix(
				new Vector3f(terrain.getX(), 0, terrain.getZ()), 0, 0, 0, 1);
		shader.loadTransformationMatrix(transformationMatrix);
	}

}

