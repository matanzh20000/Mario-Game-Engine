#type vertex
#version 330 core

layout (location = 0) in vec3 aPos;
layout (location = 1) in vec4 aColor;
layout (location = 2) in vec2 aTexCoord;

uniform mat4 uProjection;
uniform mat4 uView;


out vec4 fColor;
out vec2 fTexCoord;

void main() {
    fTexCoord = aTexCoord;
    fColor = aColor;
    gl_Position = uProjection * uView * vec4(aPos, 1.0);
}

#type fragment
#version 330 core

uniform float uTime;
uniform sampler2D TEX_SAMPLER;

in vec4 fColor;
in vec2 fTexCoord;

out vec4 color;

void main() {
    color = texture(TEX_SAMPLER, fTexCoord);
}