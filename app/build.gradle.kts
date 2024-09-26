plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.google.gms.google.services)
}

android {
    namespace = "com.example.island_spotter"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.island_spotter"
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        viewBinding = true
    }
}

dependencies {

    // Firebase Authentication
    implementation ("com.google.firebase:firebase-auth:22.1.1")

    // Firebase Realtime Database
    implementation ("com.google.firebase:firebase-database:20.3.0")

    // ViewBinding (if you are using it for layout binding)
    implementation ("androidx.databinding:viewbinding:7.0.0")

    // Other dependencies
    implementation("androidx.core:core-ktx:1.10.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.9.0")
    implementation("androidx.activity:activity-ktx:1.8.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.firebase.database)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    implementation ("org.jetbrains.kotlin:kotlin-script-runtime:1.9.0")

    implementation ("com.google.android.gms:play-services-maps:18.0.2")

    implementation("androidx.navigation:navigation-fragment-ktx:2.5.3")
    implementation("androidx.navigation:navigation-ui-ktx:2.5.3")

    implementation ("androidx.recyclerview:recyclerview:1.2.1")

    // KTX for the Maps SDK for Android library
    implementation("com.google.maps.android:maps-ktx:5.1.1")

//    implementation ("com.google.firebase:firebase-database-ktx:latest_version")
//    implementation ("androidx.recyclerview:recyclerview:latest_version")

}