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
}
