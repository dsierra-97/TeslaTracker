plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.hilt)
}

android {
    namespace = "com.sierrapor.teslatracker"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.sierrapor.teslatracker"
        minSdk = 26
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    //Hilt
    implementation(libs.hilt.android)
    implementation(libs.lifecycle.viewmodel.android)
    annotationProcessor(libs.hilt.compiler)
    // Dependencias comunes de Android
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.constraintlayout)
    implementation(libs.navigation.fragment)
    implementation(libs.navigation.ui)
    implementation(libs.preference)
    implementation(libs.activity)
    implementation(libs.recyclerview)
    //testing
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    //Room
    implementation(libs.room.runtime) // Biblioteca principal de Room
    annotationProcessor(libs.room.compiler) // Procesador de anotaciones para Java
}