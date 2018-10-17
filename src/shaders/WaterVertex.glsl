#version 400 core

in vec3 position;

const float PI = 3.14159f;
const float waveAmplitude = 9f;
const float TimePeriod = 1.5f;
const int VERTEX_COUNT = 60;

uniform mat4 transformationMatrix;
uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;
uniform vec3 lightDirection;
uniform vec3 lightColour;
uniform float waveLength;
uniform float waveTime;
uniform float shineDamper;
uniform vec2 location;

flat out vec4 color;

float generateOffset(float x, float z,float val1,float val2){
	float x1 = location.x*VERTEX_COUNT;
	float z1 = location.y*VERTEX_COUNT;

	float radiansX = ((mod(x+z*x*val1, waveLength)/waveLength) + waveTime * mod(x * 0.8 + z, 1.5)) * 2.0 * PI;
	float radiansZ = ((mod(val2 * (z*x +x*z), waveLength)/waveLength) + waveTime * 2.0 * mod(x , 2.0) ) * 2.0 * PI;

	return waveAmplitude * 0.5 * (sin(radiansZ) + cos(radiansX));
}

vec3 getOffset(vec3 vertex){
	float yDistortion = generateOffset(vertex.x, vertex.z,0.1f,0.3f);
	return vertex + vec3(0, yDistortion,0);
}

vec3 calculateNormal(vec3 vertex){
	float x = vertex.x;
	float z = vertex.z;

	float heightL = (getOffset(vec3(x-1,0,z))).y;
	float heightR = (getOffset(vec3(x+1,0,z))).y;
	float heightD = (getOffset(vec3(x,0,z-1))).y;
	float heightU = (getOffset(vec3(x,0,z+1))).y;

	vec3 normal = vec3(heightL-heightR,2f,heightD-heightU);
	normal = normalize(normal);
	return normal;
}

float calculateLighting(vec3 surfaceNormal,vec3 LightVector,float ambient){
	vec3 unitNormal = normalize(surfaceNormal);
	vec3 unitLight = -normalize(LightVector);
	float brightness = dot(unitNormal,LightVector);
	float finalBrightness = max(brightness,1);
	finalBrightness = max(brightness,ambient);
	return finalBrightness;
}

float specularLighting(vec3 surfaceNormal,vec3 LightDirection,vec3 CameraVector,float shinedamper){
	vec3 unitNormal = normalize(surfaceNormal);
	vec3 LightVector = normalize(LightDirection);
	vec3 newLightDirection = reflect(LightVector,unitNormal);
	float brightness = dot(newLightDirection,normalize(CameraVector));
	brightness = min(max(brightness,0),1);
	brightness = pow(brightness,shinedamper);
	return brightness;
}

void main(void){
	vec3 new_position = getOffset(position);
	vec4 worldPosition = transformationMatrix * vec4(new_position,1.0);
	gl_Position = projectionMatrix * viewMatrix * worldPosition;

	vec3 normal = calculateNormal(position);
	vec3 surfaceNormal = (transformationMatrix * vec4(normal,0.0)).xyz;
	vec3 toLightVector = lightDirection;
	vec3 toCameraVector = (inverse(viewMatrix) * vec4(0.0,0.0,0.0,1.0)).xyz - worldPosition.xyz;

	float brightness = calculateLighting(surfaceNormal,toLightVector,0.8f);
	vec3 bluecolor = vec3(154f/256,219f/256,217f/256);
	vec3 diffuse = brightness * bluecolor;
	vec4 diffusenew = vec4(diffuse,0.8f);

	float specularBrightness = specularLighting(normal,toLightVector,toCameraVector,shineDamper);
	vec3 specular = lightColour*specularBrightness;

	color =  diffusenew + vec4(specular,0);
}

