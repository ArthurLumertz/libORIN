#version 330 core

in vec3 v_normal;
in vec2 v_texCoord;
in vec3 v_fragPos;

out vec4 FragColor;

uniform sampler2D u_texture;
uniform bool useTexture;       
uniform vec3 materialColor = vec3(1.0); 

const int MAX_SPOT_LIGHTS = 8;
const int MAX_POINT_LIGHTS = 8;
const int MAX_DIR_LIGHTS = 4;

struct SpotLight {
    vec3 position;
    vec3 direction;
    vec3 color;
    float intensity;
    float cutoffAngle;
    float exponent;
    float constant;
    float linear;
    float quadratic;
};

struct PointLight {
    vec3 position;
    vec3 color;
    float intensity;
};

struct DirectionalLight {
    vec3 direction;
    vec3 color;
};

uniform int numSpotLights;
uniform SpotLight spotLights[MAX_SPOT_LIGHTS];

uniform int numPointLights;
uniform PointLight pointLights[MAX_POINT_LIGHTS];

uniform int numDirLights;
uniform DirectionalLight dirLights[MAX_DIR_LIGHTS];

uniform vec3 viewPos;

uniform vec3 fogColor;
uniform float fogStart;
uniform float fogEnd;

vec3 calculateDirectionalLight(DirectionalLight light, vec3 normal) {
    vec3 lightDir = normalize(-light.direction);
    float diff = max(dot(normal, lightDir), 0.0);
    return light.color * diff;
}

vec3 calculatePointLight(PointLight light, vec3 normal, vec3 fragPos) {
    vec3 lightDir = light.position - fragPos;
    float distance = length(lightDir);
    lightDir = normalize(lightDir);

    float diff = max(dot(normal, lightDir), 0.0);

    float constant = 1.0;
    float linear = 0.09;
    float quadratic = 0.032;

    float attenuation = 1.0 / (constant + linear * distance + quadratic * distance * distance);
    return light.color * diff * light.intensity * attenuation;
}

vec3 calculateSpotLight(SpotLight light, vec3 normal, vec3 fragPos) {
    vec3 lightDir = normalize(light.position - fragPos);
    float distance = length(light.position - fragPos);
    float theta = dot(lightDir, normalize(-light.direction));

    if (theta > cos(light.cutoffAngle)) {
        float diff = max(dot(normal, lightDir), 0.0);
        float angleFalloff = pow(theta, light.exponent);
        float attenuation = 1.0 / (light.constant + light.linear * distance + light.quadratic * distance * distance);
        return light.color * diff * light.intensity * angleFalloff * attenuation;
    }

    return vec3(0.0);
}

float computeFogFactor(float distance, float start, float end) {
    float fogDensity = 1.0 / (end - start);
    float fogAmount = 1.0 - exp(-pow(distance * fogDensity, 1.5));
    return clamp(1.0 - fogAmount, 0.0, 1.0);
}

void main() {
    vec3 norm = normalize(v_normal);
    vec3 fragPos = v_fragPos;

    vec3 baseColor = useTexture ? texture(u_texture, v_texCoord).rgb : materialColor;

    vec3 lighting = vec3(0.1); // ambient light

    for (int i = 0; i < numDirLights; ++i)
        lighting += calculateDirectionalLight(dirLights[i], norm);

    for (int i = 0; i < numPointLights; ++i)
        lighting += calculatePointLight(pointLights[i], norm, fragPos);

    for (int i = 0; i < numSpotLights; ++i)
        lighting += calculateSpotLight(spotLights[i], norm, fragPos);

    vec3 litColor = baseColor * lighting;

    float distance = length(viewPos - fragPos);
    float fogFactor = computeFogFactor(distance, fogStart, fogEnd);

    vec3 finalColor = mix(fogColor, litColor, fogFactor);

    FragColor = vec4(finalColor, 1.0);
}
