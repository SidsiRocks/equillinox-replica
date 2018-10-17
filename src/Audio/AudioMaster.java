package Audio;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.LWJGLException;
import org.lwjgl.openal.AL;
import org.lwjgl.openal.AL10;
import org.lwjgl.util.WaveData;

public class AudioMaster {
	
	public static List<Integer> buffers = new ArrayList<Integer>();
	
	public static void init() {
		try {
			AL.create();
		} catch (LWJGLException e) {
			System.err.println("error initialising audio");
			e.printStackTrace();
		}
	}
	
	public static void setListenerData() {
		AL10.alListener3f(AL10.AL_POSITION,0,0,0);
		AL10.alListener3f(AL10.AL_VELOCITY,0,0,0);
		
	}
	
	public static int loadSound(String name) {
		int buffer = AL10.alGenBuffers();
		buffers.add(buffer);
		WaveData wave = WaveData.create(name);
		AL10.alBufferData(buffer,wave.format,wave.data,wave.samplerate);
		wave.dispose();
		return buffer;
	}
	
	public static void cleanUp() {
		AL.destroy();
		for(int buffer:buffers) {
			AL10.alDeleteBuffers(buffer);
		}
	}
	
	public static int generateSource() {
		return AL10.alGenSources();
	}
}
