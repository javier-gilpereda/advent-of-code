plugins {
    alias(libs.plugins.jvm)
    application
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(platform("io.arrow-kt:arrow-stack:1.0.1"))

    implementation(libs.bundles.aoc.implementation)
    implementation("io.arrow-kt:arrow-core")
    implementation("org.jetbrains.kotlinx:multik-api:0.1.1")
    implementation("org.jetbrains.kotlinx:multik-default:0.1.1")

    testImplementation(kotlin("test"))
    testImplementation(libs.bundles.aoc.test)
    testImplementation("org.junit.jupiter:junit-jupiter-params")
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

tasks.named<Test>("test") {
    useJUnitPlatform()
}
