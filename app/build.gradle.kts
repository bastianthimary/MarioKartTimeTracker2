plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
    id("io.objectbox")
}

android {
    compileSdk = 34

    defaultConfig {
        applicationId = "com.buffe.mariokarttimetracker"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }

    buildFeatures {
        dataBinding = true
        viewBinding = true
    }

    namespace = "com.buffe.mariokarttimetracker"
}

dependencies {
    // Standard-Android-Abhängigkeiten
    implementation("androidx.core:core-ktx:1.10.1")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.9.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    //Object Box - Datenbank
    implementation("io.objectbox:objectbox-kotlin:4.2.0")
    implementation("io.objectbox:objectbox-android:4.2.0")
    implementation("io.objectbox:objectbox-gradle-plugin:4.2.0")
    kapt("io.objectbox:objectbox-processor:4.2.0")

    // CameraX (Core, Lifecycle und View)
    implementation("androidx.camera:camera-core:1.4.2")
    implementation("androidx.camera:camera-camera2:1.4.2")
    implementation("androidx.camera:camera-lifecycle:1.4.2")
    implementation("androidx.camera:camera-view:1.4.2")
    implementation("com.google.code.gson:gson:2.11.0")

    // ML Kit Text Recognition
    implementation("com.google.mlkit:text-recognition:16.0.1")
    implementation("org.opencv:opencv:4.9.0")

    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")
    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")
    implementation("org.jetbrains.kotlinx:kotlinx-metadata-jvm:0.9.0")

    // Test-Abhängigkeiten
    testImplementation ("io.mockk:mockk:1.13.5")
    testImplementation("junit:junit:4.13.2")
    testImplementation(libs.junit.jupiter)
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}
