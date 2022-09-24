import de.undercouch.gradle.tasks.download.Download

plugins {
    kotlin("jvm") version "1.7.10"
    kotlin("plugin.serialization") version "1.7.10"
    id("application")
    id("net.kyori.indra") version "2.2.0"
    id("net.kyori.indra.licenser.spotless") version "2.2.0"
    id("net.kyori.indra.git") version "2.2.0"
}

group = "zinced"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))

    // Logging
    implementation("io.github.microutils:kotlin-logging:3.0.0")
    implementation("org.slf4j:slf4j-api:2.0.2")
    implementation("org.apache.logging.log4j:log4j-core:2.19.0")
    implementation("org.apache.logging.log4j:log4j-slf4j2-impl:2.19.0")

    // Config
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-core:1.4.0")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-hocon:1.4.0")
    implementation("com.typesafe:config:1.4.2")

    // Gitea
    implementation("com.github.zeripath:java-gitea-api:1.16.8")
    implementation("io.javalin:javalin:4.6.4")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.4.0")

    implementation("com.squareup.okhttp3:okhttp:5.0.0-alpha.10")

}

/*tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}*/

indra {
    javaVersions {
        target(17)
    }
    apache2License()
    github("xtexChooser", "zinced") {
        ci(true)
        issues(true)
        scm(true)
    }
}

tasks.withType<Jar> {
    indraGit.applyVcsInformationToManifest(manifest)
}

application {
    mainClass.set("zinced.main.MainKt")
    executableDir = ""
    applicationDefaultJvmArgs = listOf("-Dlog4j.skipJansi=false")
}

tasks.getByName<JavaExec>("run") {
    workingDir = file("run")
}
