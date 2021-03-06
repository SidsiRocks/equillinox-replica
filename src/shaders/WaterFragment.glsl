#version 400 core

flat in vec4 color;

out vec4 out_Color;

vec4 invert(vec4 color){
	vec4 new_color = vec4(1,1,1,1);
	new_color.x = 1 - color.x;
	new_color.y = 1 - color.y;
	new_color.z = 1 - color.z;
	new_color.w = color.w;
	return new_color;
}

void main(void){
	out_Color = color;
}
