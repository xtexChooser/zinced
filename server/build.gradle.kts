plugins {
    kotlin("jvm")
    kotlin("plugin.serialization")
    id("application")
}

//evaluationDependsOn(":common")
dependencies {
    api(project(":common"))

    // Logging
    implementation("io.github.microutils:kotlin-logging:3.0.0")
    implementation("org.slf4j:slf4j-api:2.0.2")
    implementation("org.apache.logging.log4j:log4j-core:2.19.0")
    implementation("org.apache.logging.log4j:log4j-slf4j2-impl:2.19.0")

    // Config
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-hocon:1.4.0")
    implementation("com.typesafe:config:1.4.2")

    // Web
    implementation("io.javalin:javalin:4.6.4")

    // MediaWiki
    implementation("org.sweble.wikitext:swc-parser-lazy:3.1.9")
    //implementation("io.github.java-diff-utils:java-diff-utils:4.12")
}

application {
    applicationName = "zinced-server"
    mainClass.set("zinced.server.main.MainKt")
    executableDir = ""
    applicationDefaultJvmArgs = listOf("-Dlog4j.skipJansi=false")
}

tasks.getByName<JavaExec>("run") {
    workingDir = file("run")
}

tasks.getByName<Jar>("jar") {
    manifest {
        attributes["Implementation-Version"] = project.version
    }
}

tasks.getByName<Copy>("processResources") {
    val jsDist = evaluationDependsOn(":frontend").tasks.getByName("browserDistribution")
    dependsOn(jsDist)
    from(jsDist.outputs) {
        into("zinced/frontend/dist")
        exclude("META-INF")
    }
}
