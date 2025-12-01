plugins {
    alias(libs.plugins.jvm)
    `java-library`
}

dependencies {
    implementation(libs.kotlinx.coroutines)
    testImplementation(kotlin("test"))
    testImplementation(libs.bundles.aoc.test)
    testImplementation("org.junit.jupiter:junit-jupiter-params")
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

tasks.test {
    useJUnitPlatform()
    testLogging.showStandardStreams = true
}
