plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.lalaecomerce"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.lalaecomerce"
        minSdk = 24
        targetSdk = 34
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
    // Firebase Authentication
    implementation("com.google.firebase:firebase-auth:21.0.3")

    // Firebase Firestore
    implementation("com.google.firebase:firebase-firestore:24.0.1")

    // Material Design
    implementation("androidx.constraintlayout:constraintlayout:2.1.3")
    implementation("androidx.cardview:cardview:1.0.0")
    implementation ("com.google.android.material:material:1.9.0")
    implementation ("com.github.bumptech.glide:glide:4.16.0")
    annotationProcessor ("com.github.bumptech.glide:compiler:4.16.0")

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}