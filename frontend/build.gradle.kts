import org.jetbrains.kotlin.gradle.dsl.KotlinJsCompile

plugins {
    kotlin("js")
    kotlin("plugin.serialization")
    id("org.jetbrains.compose")
}

repositories.maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")

dependencies {
    api(project(":client"))
    implementation(compose.web.core)
    implementation(compose.runtime)
    implementation("app.softwork:routing-compose:0.2.8")
}

kotlin {
    js(IR) {
        browser {}
        binaries.executable()
    }
}

tasks.withType<KotlinJsCompile> {
    kotlinOptions {
        freeCompilerArgs += listOf(
            "-P",
            "plugin:androidx.compose.compiler.plugins.kotlin:suppressKotlinVersionCompatibilityCheck=true"
        )
    }
}
