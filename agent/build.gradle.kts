plugins {
    id("java")
    id("com.gradleup.shadow") version "9.2.0"
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

    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.test {
    useJUnitPlatform()
}

tasks.jar {
    manifest {
        attributes(
            "Premain-Class" to "jp.yamad.hakoniwa.agent.Hakoniwa",
            "Agent-Class" to "jp.yamad.hakoniwa.agent.Hakoniwa",
            "Can-Redefine-Classes" to "true",
            "Can-Retransform-Classes" to "true"
        )
    }
}

tasks.shadowJar {
    archiveBaseName.set("hakoniwa-agent")
    archiveClassifier.set("")
}

tasks.named("assemble") {
    dependsOn(tasks.named("shadowJar"))
}

tasks.build {
    dependsOn(tasks.shadowJar)
}
