import nu.studer.gradle.jooq.JooqEdition
import org.jooq.meta.jaxb.Logging
import org.jooq.meta.jaxb.Property
import org.jooq.meta.jaxb.ForcedType

plugins {
    // Apply the application plugin to add support for building a CLI application in Java.
    application
    java
    id("nu.studer.jooq") version "8.2"
}

group = "com.github.soarnir"
version = "0.1.0"

repositories {
    // Use Maven Central for resolving dependencies.
    mavenCentral()
}

tasks.test {
    useJUnitPlatform()
}

jooq {
    version.set("3.18.4")
    edition.set(JooqEdition.OSS)

    configurations {
        create("main") {
            jooqConfiguration.apply {
                logging = Logging.WARN
                jdbc.apply {
                    driver = "org.postgresql.Driver"
                    url = "jdbc:postgresql://localhost:5432/bot-dev"
                    user = "bot"
                    password = "M57\"pKL*es>77\$7"
                    properties.add(Property().apply {
                        key = "ssl"
                        value = "false"
                    })
                }
                generator.apply {
                    name = "org.jooq.codegen.DefaultGenerator"
                    database.apply {
                        name = "org.jooq.meta.postgres.PostgresDatabase"
                        inputSchema = "public"
                        forcedTypes.addAll(listOf(
                                /*ForcedType().apply {
                                    name = "varchar"
                                    includeExpression = ".*"
                                    includeTypes = "JSONB?"
                                },
                                ForcedType().apply {
                                    name = "varchar"
                                    includeExpression = ".*"
                                    includeTypes = "INET"
                                }*/
                        ))
                    }
                    generate.apply {
                        isDeprecated = false
                        isRecords = true
                        isImmutablePojos = true
                        isFluentSetters = true
                    }
                    target.apply {
                        packageName = "pollorb.database"
                        directory = "src/generated"
                    }
                    strategy.name = "org.jooq.codegen.DefaultGeneratorStrategy"
                }
            }
        }
    }
}

dependencies {
    // Tests
    // Use JUnit test framework.
    testImplementation("org.junit.jupiter:junit-jupiter:5.10.0")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

    // --------------
    // Implementation
    // --------------

    // DBUtils
    implementation("commons-dbutils:commons-dbutils:1.8.1")

    // JOOQ
    implementation("org.jooq:jooq:3.18.7")

    // Persistence
    implementation("javax.persistence:javax.persistence-api:2.2")

    // Guava
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
    // JOOQ
    // -----------

    jooqGenerator("org.postgresql:postgresql:42.6.0")

    // -----------
    // RuntimeOnly
    // -----------

    // PostgreSQL
    runtimeOnly("com.h2database:h2:2.2.224")
    runtimeOnly("org.postgresql:postgresql:42.6.0")
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
