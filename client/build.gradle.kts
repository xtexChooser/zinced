plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization")
}

kotlin {
    jvm {

    }
    js(IR) {
        browser {}
        nodejs()
        binaries.executable()
    }

    sourceSets {
        commonMain {
            dependencies {
                api(project(":common"))
            }
        }
    }
}
