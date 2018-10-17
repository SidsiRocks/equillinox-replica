package TerrainSampling;

import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import toolBox.Maths;

public class BasicTerrainSampler implements TerrainSampler{

	
	
	@Override
	public Vector4f getColor(float height) {
		
		
		if(height > 75) {
			return new Vector4f(1,1,1,1);
		}else if(height > 35) {
			return new Vector4f((196f/255f),(141f/255f),(113f/255f),(1f));
		}else if (height> -5){
			return new Vector4f(111f/255f,158f/255f,75/255f,1f);
		}else {
			return new Vector4f(234f/255f,219f/255f,145f/255f,1f);
		}
		
		
	}

}
