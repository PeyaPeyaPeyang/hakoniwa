plugins {
    id("java")
}

group = "jp.yamad.hakoniwa"
version = "0.0.1"

repositories {
    mavenCentral()
}

dependencies {
    compileOnly(project(":commons"))

    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.test {
    useJUnitPlatform()
}
