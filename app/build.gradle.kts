plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    id("com.google.devtools.ksp")
    id("org.jetbrains.kotlin.plugin.serialization")
}

android {
    namespace = "com.example.fletes"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.fletes"
        minSdk = 29
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
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
    sourceSets {
        getByName("androidTest").java.srcDirs("src/androidTest/java")
    }
}

dependencies {

    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.room.ktx)
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.koin.android)
    implementation(libs.kotlinx.coroutines.android)
    testImplementation(libs.junit)
    testImplementation(libs.androidx.core)
    testImplementation(libs.koin.test) // Use the same koin version
    testImplementation(libs.androidx.room.testing)
    testImplementation(libs.kotlinx.coroutines.test) // Use the same version
    androidTestImplementation(libs.androidx.core)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
    //room
    ksp(libs.androidx.room.compiler)
    testFixturesImplementation(libs.koin.test.junit4)



    // optional - Test helpers
    // optional - Paging 3 Integration
    implementation(libs.androidx.room.paging)


    //serializable
    implementation(libs.kotlinx.serialization.json)
    // Optional: Material 3 Window Size Class (useful for adaptive layouts)
    implementation(libs.androidx.material3.window.size.class1)

    // Optional: Material 3 Adaptive Navigation Suite
    implementation(libs.androidx.material3.adaptive.navigation.suite)

    implementation(libs.material3)


}
