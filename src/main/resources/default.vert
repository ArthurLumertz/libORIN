#version 330 core

layout(location = 0) in vec3 a_pos;
layout(location = 1) in vec2 a_texCoord;
layout(location = 2) in vec3 a_normal;

uniform mat4 u_combMatrix;
uniform mat4 u_transMatrix;

out vec3 v_normal;
out vec2 v_texCoord;
out vec3 v_fragPos;

void main() {
    vec4 worldPosition = u_transMatrix * vec4(a_pos, 1.0);
    v_fragPos = worldPosition.xyz;
    v_normal = mat3(transpose(inverse(u_transMatrix))) * a_normal;
    v_texCoord = a_texCoord;
	
    gl_Position = u_combMatrix * worldPosition;
}
