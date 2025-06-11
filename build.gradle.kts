import org.gradle.internal.os.OperatingSystem

val lwjglVersion = "3.3.6"
val jomlVersion = "1.10.7"

val lwjglNativesList = listOf(
    "natives-windows",
    "natives-windows-arm64",
    "natives-macos",
    "natives-macos-arm64",
    "natives-linux",
    "natives-linux-arm64",
    "natives-linux-arm32"
)

plugins {
    java
    application
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(platform("org.lwjgl:lwjgl-bom:$lwjglVersion"))

    implementation("org.lwjgl", "lwjgl")
    implementation("org.lwjgl", "lwjgl-glfw")
    implementation("org.lwjgl", "lwjgl-openal")
    implementation("org.lwjgl", "lwjgl-opengl")
    implementation("org.lwjgl", "lwjgl-stb")


    lwjglNativesList.forEach { classifier ->
        runtimeOnly("org.lwjgl", "lwjgl", classifier = classifier)
        runtimeOnly("org.lwjgl", "lwjgl-glfw", classifier = classifier)
        runtimeOnly("org.lwjgl", "lwjgl-openal", classifier = classifier)
        runtimeOnly("org.lwjgl", "lwjgl-opengl", classifier = classifier)
        runtimeOnly("org.lwjgl", "lwjgl-stb", classifier = classifier)
    }

    implementation("org.joml", "joml", jomlVersion)
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

tasks.compileJava {
    options.release.set(8)
}

tasks.jar {
    manifest {
        attributes(
            "Main-Class" to "net.orin.EngineTester"
        )
    }
}

application {
    mainClass.set("net.orin.EngineTester")
}

tasks.named<com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar>("shadowJar") {
    archiveClassifier.set("")
    mergeServiceFiles()

    exclude("kotlin/**")
    exclude("META-INF/kotlin*")
}

tasks.named("build") {
    dependsOn(tasks.named("shadowJar"))
}

tasks.named("distZip") {
    dependsOn(tasks.named("shadowJar"))
}

tasks.named("distTar") {
    dependsOn(tasks.named("shadowJar"))
}

tasks.named("startScripts") {
    dependsOn(tasks.named("shadowJar"))
}