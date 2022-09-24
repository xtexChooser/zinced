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
}

tasks.test {
    useJUnitPlatform()
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
