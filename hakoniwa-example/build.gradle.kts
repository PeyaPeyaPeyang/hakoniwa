plugins {
    id("java")
}

group = "jp.yamad.hakoniwa"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":commons"))
}
