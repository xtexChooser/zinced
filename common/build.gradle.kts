plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization")
}

kotlin {
    jvm {
    }
    js(IR) {
        browser()
        nodejs()
        binaries.library()
    }
    sourceSets {
        commonMain {
            dependencies {
                api(kotlin("reflect"))
                api("org.jetbrains.kotlinx:kotlinx-serialization-core:1.4.0")
                api("org.jetbrains.kotlinx:kotlinx-serialization-json:1.4.0")
                api("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")
                api("org.jetbrains.kotlinx:atomicfu:0.18.3")
                api("org.jetbrains.kotlinx:kotlinx-datetime:0.4.0")
            }
        }
        val jvmMain by getting {
            dependencies {
                api("com.squareup.okhttp3:okhttp:5.0.0-alpha.10")
                api("com.squareup.okhttp3:okhttp-coroutines:5.0.0-alpha.10")
            }
        }
    }
}
