import org.jetbrains.kotlin.gradle.plugin.KotlinJsCompilerType
import org.jetbrains.kotlin.gradle.targets.js.nodejs.NodeJsRootExtension
import org.jetbrains.kotlin.gradle.targets.js.nodejs.NodeJsRootPlugin

plugins {
    kotlin("multiplatform") version Version.kotlin
    kotlin("plugin.serialization") version Version.kotlin
}

// region Kotlin Multiplatform (Common)

kotlin.sourceSets {
    commonMain.kotlin.srcDir("commonMain")
}

// endregion
// region Kotlin JVM

kotlin.jvm {
    compilations.all {
        kotlinOptions.jvmTarget = BuildConfiguration.jvmTarget
    }

    withJava()
}

kotlin.sourceSets {
    jvmMain.kotlin.srcDir("jvmMain")

    jvmMain.dependencies {
        implementation("org.junit.jupiter:junit-jupiter-api:${Version.junit}")
    }
}

// endregion
// region Kotlin JS

rootProject.plugins.withType<NodeJsRootPlugin> {
    rootProject.the<NodeJsRootExtension>().download = BuildConfiguration.downloadNodeJs
}

kotlin.js(KotlinJsCompilerType.BOTH).browser {
}

kotlin.js(KotlinJsCompilerType.BOTH).nodejs {
}

kotlin.sourceSets {
    jsMain.kotlin.srcDir("jsMain")
}

// endregion

kotlin {
    val nativeTarget = when(BuildConfiguration.hostOs) {
        HostOs.Windows -> mingwX64("native")
        HostOs.Linux -> linuxX64("native")
        HostOs.MacOsX -> macosX64("native")
        else -> throw GradleException("Host OS is not supported in Kotlin/Native.")
    }

    sourceSets {
        nativeMain.kotlin.srcDir("nativeMain")
    }
}
