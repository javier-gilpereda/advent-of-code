plugins {
    alias(libs.plugins.jvm)
    alias(libs.plugins.compose)
    alias(libs.plugins.composeCompiler)
}

dependencies {
    implementation(libs.bundles.aoc.implementation)
    implementation(project(":common:commons"))
    implementation(libs.kotlinx.coroutines)
    implementation(compose.desktop.currentOs)
    implementation(compose.material)
    implementation(compose.material3)
    implementation(compose.foundation)
    implementation(compose.runtime)

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
    minHeapSize = "2048m"
    maxHeapSize = "6144m"
}
