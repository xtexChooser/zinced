plugins {
    kotlin("jvm") version "1.7.10"
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
    implementation("org.slf4j:slf4j-api:2.0.2")
    implementation("org.apache.logging.log4j:log4j-core:2.19.0")
    implementation("org.apache.logging.log4j:log4j-slf4j2-impl:2.19.0")
    implementation("io.github.microutils:kotlin-logging:3.0.0")

    implementation("org.kohsuke:github-api:1.308")

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
