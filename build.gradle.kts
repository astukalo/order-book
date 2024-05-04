plugins {
    id("java")
    application
}

group = "xyz.a5s7"
version = "1.0-SNAPSHOT"

java {
    sourceCompatibility = JavaVersion.VERSION_17
}

repositories {
    mavenCentral()
}

dependencies {
    testImplementation("io.github.java-diff-utils:java-diff-utils:4.12")
    testImplementation(platform("org.junit:junit-bom:5.9.2"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation("org.mockito:mockito-junit-jupiter:5.11.0")
    testImplementation("org.assertj:assertj-core:3.25.3")
}

tasks.jar {
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    manifest {
        attributes["Main-Class"] = "xyz.a5s7.Application"
    }

    from(configurations.runtimeClasspath.get().map { if (it.isDirectory) it else zipTree(it) })
}

tasks.test {
    useJUnitPlatform()

    reports {
        junitXml.required.set(true)
        html.required.set(true)
    }
}