import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt)
}

// La API key vive en local.properties (que no se sube a git), en una linea:
//   API_NINJAS_KEY=tu_key
// Se lee con providers.fileContents para que Gradle detecte si el archivo cambia.
val apiNinjasKey: String = providers
    .fileContents(rootProject.layout.projectDirectory.file("local.properties"))
    .asText
    .map { text -> Properties().apply { load(text.reader()) }.getProperty("API_NINJAS_KEY", "") }
    .getOrElse("")

// google-services.json no se sube a git (ver README, seccion Firebase).
// Mientras no este en app/, el proyecto compila igual y la app usa el acceso de demostracion.
if (file("google-services.json").exists()) {
    apply(plugin = "com.google.gms.google-services")
}

android {
    namespace = "com.example.parcialtp3"
    compileSdk {
        version = release(37)
    }

    defaultConfig {
        applicationId = "com.example.parcialtp3"
        minSdk = 24
        targetSdk = 37
        versionCode = 1
        versionName = "1.0"

        // Genera BuildConfig.API_NINJAS_KEY para usarla desde el codigo.
        buildConfigField("String", "API_NINJAS_KEY", "\"$apiNinjasKey\"")

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            // R8: reduce y ofusca el codigo y quita recursos sin uso (APK mas chico y rapido).
            // Las reglas para no romper Gson/Retrofit estan en app/src/main/keepRules/app-rules.keep
            optimization {
                enable = true
            }
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    buildFeatures {
        compose = true
        buildConfig = true // necesario para que se genere la clase BuildConfig
    }
}

dependencies {
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(libs.androidx.lifecycle.viewmodel.compose)

    // Navigation Compose
    implementation(libs.androidx.navigation.compose)

    // Hilt
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)
    implementation(libs.androidx.hilt.navigation.compose)

    // Room
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    ksp(libs.androidx.room.compiler)

    // Retrofit
    implementation(libs.retrofit)
    implementation(libs.retrofit.converter.gson)

    // Firebase (ampliacion opcional): Authentication para el login y Firestore para el perfil.
    // platform(...) aplica el BoM: fija versiones compatibles de auth y firestore.
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.auth)
    implementation(libs.firebase.firestore)
    implementation(libs.kotlinx.coroutines.play.services)

    testImplementation(libs.junit)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.junit)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
    debugImplementation(libs.androidx.compose.ui.tooling)
}