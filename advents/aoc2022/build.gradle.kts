plugins {
    alias(libs.plugins.jvm)
    application
}

dependencies {
    implementation(libs.bundles.aoc.implementation)

    testImplementation(kotlin("test"))
    testImplementation(libs.bundles.aoc.test)
    testImplementation("org.junit.jupiter:junit-jupiter-params")
}

tasks.test {
    useJUnitPlatform()
    testLogging.showStandardStreams = true
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

application {
    mainClass.set("MainKt")
}
