plugins {
    id("java")
}

group = "jp.yamad.hakoniwa"
version = "0.0.1"

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":commons"))
    implementation("org.ow2.asm:asm:9.10.1")
    implementation("org.ow2.asm:asm-tree:9.10.1")
}
