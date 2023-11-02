plugins {
    // Apply the application plugin to add support for building a CLI application in Java.
    application
    java
    id("org.springframework.boot") version "3.1.4"
    id("io.spring.dependency-management") version "1.1.3"
}

group = "com.github.soarnir"
version = "0.1.0"

repositories {
    // Use Maven Central for resolving dependencies.
    mavenCentral()
}

dependencies {
    // Tests
    // Use JUnit test framework.
    testImplementation("junit:junit:4.13.2")

    // --------------
    // Implementation
    // --------------

    // This dependency is used by the application.
    implementation("com.google.guava:guava:31.1-jre")

    // Gson
    implementation("com.google.code.gson:gson:2.10.1")

    // JDA
    implementation("net.dv8tion:JDA:5.0.0-beta.15")

    // Logback
    implementation("ch.qos.logback:logback-classic:1.4.6")

    // Reflections
    implementation("org.reflections:reflections:0.10.2")

    // -----------
    // RuntimeOnly
    // --------------

    // PostgreSQL
    runtimeOnly("com.h2database:h2")
    runtimeOnly("org.postgresql:postgresql")
}

// Apply a specific Java toolchain to ease working on different environments.
java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

application {
    // Define the main class for the application.
    mainClass.set("pollorb.launcher.Launcher")
}
