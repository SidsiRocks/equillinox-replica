package toolBox;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import entities.Camera;

public class Maths {

	public static Matrix4f createTransformationMatrix(Vector3f translation, float rx, float ry,
			float rz, float scale) {
		Matrix4f matrix = new Matrix4f();
		matrix.setIdentity();
		Matrix4f.translate(translation, matrix, matrix);
		Matrix4f.rotate((float) Math.toRadians(rx), new Vector3f(1,0,0), matrix, matrix);
		Matrix4f.rotate((float) Math.toRadians(ry), new Vector3f(0,1,0), matrix, matrix);
		Matrix4f.rotate((float) Math.toRadians(rz), new Vector3f(0,0,1), matrix, matrix);
		Matrix4f.scale(new Vector3f(scale,scale,scale), matrix, matrix);
		return matrix;
	}
	
	public static Matrix4f createViewMatrix(Camera camera) {
		Matrix4f viewMatrix = new Matrix4f();
		viewMatrix.setIdentity();
		Matrix4f.rotate((float) Math.toRadians(camera.getPitch()), new Vector3f(1, 0, 0), viewMatrix,
				viewMatrix);
		Matrix4f.rotate((float) Math.toRadians(camera.getYaw()), new Vector3f(0, 1, 0), viewMatrix, viewMatrix);
		Vector3f cameraPos = camera.getPosition();
		Vector3f negativeCameraPos = new Vector3f(-cameraPos.x,-cameraPos.y,-cameraPos.z);
		Matrix4f.translate(negativeCameraPos, viewMatrix, viewMatrix);
		return viewMatrix;
	}
	
	public static Vector3f getAngle(Vector3f direction) {
		Vector3f angle;
		
		float x = (float) Math.toDegrees(Math.acos(direction.x/Maths.getMagnitude(direction)));
		float y = -(float) Math.toDegrees(Math.acos(direction.y/Maths.getMagnitude(direction)));
		float z = (float) Math.toDegrees(Math.acos(direction.z/Maths.getMagnitude(direction)));
		
		angle = new Vector3f(x,y,z);
		
		return angle;
	}
	
	public static float getMagnitude(Vector3f vector) {
		return (float) Math.pow(vector.x*vector.x + vector.y*vector.y + vector.z*vector.z,0.5);
	}
	
	public static float clamp(float val,float min,float max) {
		float val2;
		val2 = Math.min(val,max);
		val2 = Math.max(val2,min);
		return val2;
	}
	public static Matrix4f createTransformationMatrix(Vector2f translation, Vector2f scale) {
		Matrix4f matrix = new Matrix4f();
		matrix.setIdentity();
		Matrix4f.translate(translation, matrix, matrix);
		Matrix4f.scale(new Vector3f(scale.x, scale.y, 1f), matrix, matrix);
		return matrix;
	}
	
	public static Vector3f getAnglewithAxis(Vector3f vector) {
		Vector3f normalVector = (Vector3f) vector.normalise();
		
		float dotx = Vector3f.dot(normalVector,new Vector3f(1,0,0));
		float doty = Vector3f.dot(normalVector,new Vector3f(0,1,0));
		float dotz = Vector3f.dot(normalVector,new Vector3f(0,0,1));
		
		System.out.println("dotx : " + Math.acos(dotx));
		System.out.println("doty : " + Math.acos(doty));
		System.out.println("dotz : " + Math.acos(dotz));
		
		float anglex = (float) ((Math.acos(dotx)*180)/Math.PI);
		float angley = (float) ((Math.acos(doty)*180)/Math.PI);
		float anglez = (float) ((Math.acos(dotz)*180)/Math.PI);
		
		return new Vector3f(anglex,angley,anglez);
	}
	
	public static Vector4f multiply(Vector4f vector,float x) {
		Vector4f new_vector = new Vector4f();
		new_vector.x = vector.x * x;
		new_vector.y = vector.y * x;
		new_vector.z = vector.z * x;
		new_vector.w = vector.w * x;
		return new_vector;
	}
	
	public static Vector4f add(Vector4f v1,Vector4f v2) {
		Vector4f v3 = new Vector4f(v1.x + v2.x,v1.y + v2.y,v1.z + v2.z,v1.w + v2.w);
		return v3;
	}
	
	
}
