plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    id("com.google.devtools.ksp") version "1.9.0-1.0.13"
    id("kotlin-parcelize")
}

android {
    buildFeatures {
        viewBinding = true
        buildConfig = true
    }
    namespace = "com.cindy.githubuser"
    compileSdk = 34

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }

    defaultConfig {
        applicationId = "com.cindy.githubuser"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        buildConfigField("String", "API_KEY", "\"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\"")
        buildConfigField("String", "BASE_URL", "\"https://api.github.com/\"")

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
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    implementation(libs.androidx.ui.graphics.android)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    // retrofit
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    // GSON
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    // okhttp
    implementation("com.squareup.okhttp3:logging-interceptor:4.11.0")
    // Circle Image View
    implementation ("de.hdodenhof:circleimageview:3.1.0")
    // Gridle
    implementation ("com.github.bumptech.glide:glide:4.16.0")
    // Material Design
    implementation("com.google.android.material:material:1.11.0")
    // ViewPager2
    implementation("androidx.viewpager2:viewpager2:1.0.0")
    // Splash Screen API
    implementation ("androidx.core:core-splashscreen:1.0.1")

    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.2")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.6.2")
    implementation("androidx.room:room-runtime:2.5.2")
    ksp("androidx.room:room-compiler:2.5.2")

    //coroutine support
    implementation("androidx.room:room-ktx:2.4.0")

    implementation("androidx.datastore:datastore-preferences:1.0.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.5.2")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.5.2")
}