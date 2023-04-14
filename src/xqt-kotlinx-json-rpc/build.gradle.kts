import org.jetbrains.kotlin.gradle.plugin.KotlinJsCompilerType
import org.jetbrains.kotlin.gradle.targets.js.nodejs.NodeJsRootExtension
import org.jetbrains.kotlin.gradle.targets.js.nodejs.NodeJsRootPlugin

plugins {
    kotlin("multiplatform") version Version.kotlin
    kotlin("plugin.serialization") version Version.kotlin
    id("maven-publish")
}

group = ProjectMetadata.groupId
version = ProjectMetadata.version

// region Kotlin JVM

kotlin.jvm {
    compilations.all {
        kotlinOptions.jvmTarget = BuildConfiguration.jvmTarget
    }

    withJava()

    testRuns["test"].executionTask.configure {
        useJUnitPlatform() // JUnit 5
    }
}

kotlin.sourceSets {
    jvmMain.kotlin.srcDir("jvmMain")
    jvmTest.kotlin.srcDir("jvmTest")
}

// endregion
// region Kotlin JS

rootProject.plugins.withType<NodeJsRootPlugin> {
    rootProject.the<NodeJsRootExtension>().download = BuildConfiguration.downloadNodeJs
}

kotlin.js(KotlinJsCompilerType.BOTH).browser {
    testTask {
        useKarma {
            when (BuildConfiguration.jsBrowser) {
                JsBrowser.Chrome -> useChromeHeadless()
                JsBrowser.ChromeCanary -> useChromeCanaryHeadless()
                JsBrowser.Chromium -> useChromiumHeadless()
                JsBrowser.Firefox -> useFirefoxHeadless()
                JsBrowser.FirefoxAurora -> useFirefoxAuroraHeadless()
                JsBrowser.FirefoxDeveloper -> useFirefoxDeveloperHeadless()
                JsBrowser.FirefoxNightly -> useFirefoxNightlyHeadless()
                JsBrowser.PhantomJs -> usePhantomJS()
                JsBrowser.Safari -> useSafari()
            }
        }
    }
}

kotlin.js(KotlinJsCompilerType.BOTH).nodejs {
}

kotlin.sourceSets {
    jsMain.kotlin.srcDir("jsMain")
    jsTest.kotlin.srcDir("jsTest")
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
        commonMain.kotlin.srcDir("commonMain")
        commonTest.kotlin.srcDir("commonTest")

        commonMain.dependencies {
            implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:${Version.kotlinSerialization}")
        }

        commonTest.dependencies {
            implementation(kotlin("test"))
            implementation(project(":src:xqt-kotlinx-test"))
        }

        nativeMain.kotlin.srcDir("nativeMain")
        nativeTest.kotlin.srcDir("nativeTest")

        when (BuildConfiguration.hostOs) {
            HostOs.Windows -> nativeMain.kotlin.srcDir("windowsMain")
            else -> nativeMain.kotlin.srcDir("posixMain")
        }
    }
}
