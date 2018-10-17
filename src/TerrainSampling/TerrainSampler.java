package TerrainSampling;

import org.lwjgl.util.vector.Vector4f;

public interface TerrainSampler {
	
	public Vector4f getColor(float height);
	
}
