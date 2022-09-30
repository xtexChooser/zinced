import org.jetbrains.kotlin.gradle.tasks.KotlinJvmCompile

/*buildscript {
    dependencies {
        classpath("org.jetbrains.kotlinx:atomicfu-gradle-plugin:0.18.3")
    }
}
apply(plugin: "kotlinx-atomicfu")
*/

plugins {
    kotlin("multiplatform") version "1.7.10" apply false
    kotlin("jvm") version "1.7.10" apply false
    kotlin("plugin.serialization") version "1.7.10" apply false
    id("org.jetbrains.compose") version "1.2.0-beta02-dev798" apply false
    id("net.kyori.indra.licenser.spotless") version "2.2.0"
    id("net.kyori.indra.git") version "2.2.0"
}

allprojects {
    group = "zinced"
    version = "1.0-SNAPSHOT"

    apply(plugin = "net.kyori.indra.licenser.spotless")
    apply(plugin = "net.kyori.indra.git")

    repositories {
        mavenCentral()
    }

    afterEvaluate {
        tasks.withType<Jar> {
            indraGit.applyVcsInformationToManifest(manifest)
        }
        tasks.withType<KotlinJvmCompile> {
            kotlinOptions {
                jvmTarget = "17"
            }
        }
        tasks.withType<org.jetbrains.kotlin.gradle.dsl.KotlinJsCompile> {
            kotlinOptions {
                moduleKind = "umd"
                sourceMap = true
                metaInfo = true
            }
        }
    }
}
