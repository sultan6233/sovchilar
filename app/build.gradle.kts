plugins {
    kotlin("kapt")
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-parcelize")
    id("com.google.dagger.hilt.android")
}

android {
    namespace = "com.sovchilar.made"
    compileSdk = 34

    defaultConfig {
        applicationId = Versions.appliccationId
        minSdk = 27
        targetSdk = 34
        versionCode = 2
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    implementation(project(":domain"))
    implementation(project(":comm"))
    implementation(project(":featureRemoteApi"))

    Depend.kotlinDependency.forEach { implementation(it) }
    Depend.supportAndroidLibs.forEach { implementation(it) }

    Depend.others.forEach { implementation(it) }
    implementation(Depend.hilt)
    Depend.okHttpLibraries.forEach { implementation(it) }
    implementation(Depend.retrofitConvert)
    kapt(Depend.hiltKapt)

    //leakcanary
    debugImplementation("com.squareup.leakcanary:leakcanary-android:2.12")
    //tests
    testImplementation("junit:junit:4.13.2")
    implementation("androidx.test.ext:junit-ktx:1.1.5")
    androidTestImplementation("androidx.test:rules:1.5.0")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    debugImplementation("androidx.fragment:fragment-testing:1.6.2")
}
kapt {
    correctErrorTypes = true
}