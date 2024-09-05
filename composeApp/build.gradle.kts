import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsCompose)
    alias(libs.plugins.compose.compiler)

    alias(libs.plugins.ksp)
    alias(libs.plugins.room)

}

kotlin {
    androidTarget {
        @OptIn(ExperimentalKotlinGradlePluginApi::class)
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_17)
        }
    }
    
    jvm("desktop")
    
    sourceSets {
        val commonMain by getting
        val desktopMain by getting
        val androidMain by getting

        androidMain.dependencies {
            implementation(compose.preview)
            implementation(libs.androidx.activity.compose)
            implementation ("com.google.accompanist:accompanist-systemuicontroller:0.35.2-beta")
            implementation ("com.google.accompanist:accompanist-permissions:0.35.2-beta")
            implementation("androidx.work:work-runtime:2.9.1")
            implementation(libs.androidx.appcompat)


        }
        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
            implementation(libs.androidx.lifecycle.viewmodel)
            implementation(libs.androidx.lifecycle.runtime.compose)
            implementation("network.chaintech:kmp-date-time-picker:1.0.3")
            implementation(libs.androidx.room.runtime)
            implementation(libs.sqlite.bundled)

        }
        desktopMain.dependencies {
            implementation(compose.desktop.currentOs)
            implementation(libs.kotlinx.coroutines.swing)

        }
    }
}

android {
    namespace = "com.aslansoft.myactivities"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    sourceSets["main"].res.srcDirs("src/androidMain/res")
    sourceSets["main"].resources.srcDirs("src/commonMain/composeResources")

    defaultConfig {
        applicationId = "com.aslansoft.myactivities"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    buildFeatures {
        compose = true
    }
    dependencies {
        debugImplementation(compose.uiTooling)
    }
}
dependencies {
    implementation(libs.androidx.material3.android)
    implementation(libs.androidx.ui.android)
    implementation(libs.androidx.ui.android)
    implementation(libs.androidx.ui.text.android)
    implementation(libs.androidx.work.runtime.ktx)

}

compose.desktop {
    application {
        mainClass = "com.aslansoft.myactivities.MainKt"
        nativeDistributions {
            outputBaseDir.set(project.buildDir.resolve("NativeOutput"))
            includeAllModules = true
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb,TargetFormat.Exe)
            packageName = "com.aslansoft.myactivities"
            description = "My Activities"
            packageVersion = "1.0.0"
            vendor = "Aslan SOFTWARE STUDIO"
            copyright = "© 2024 Aslan SOFTWARE STUDIO. All rights reserved."
            windows{
                iconFile.set(project.file("my_activity_logo_win.ico"))
            }
            linux{
                iconFile.set(project.file("my_activity_logo.png"))
            }
            macOS{
                iconFile.set(project.file("my_activity_logo_mac.icns"))
            }
        }
    }
}

room{
    schemaDirectory("$projectDir/schemas")
}
dependencies{
    ksp(libs.androidx.room.compiler)
}