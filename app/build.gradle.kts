plugins {
    kotlin("kapt")
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-parcelize")
    id("com.google.dagger.hilt.android")
    id("com.google.gms.google-services")
    id("com.google.firebase.crashlytics")
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

        testInstrumentationRunner = "com.sovchilar.made.CustomAppTestRunner"
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

    implementation(platform(Depend.firebaseBom))
    Depend.firebase.forEach {
        implementation(it)
    }
    Depend.google.forEach {
        implementation(it)
    }

    Depend.kotlinDependency.forEach { implementation(it) }
    Depend.supportAndroidLibs.forEach { implementation(it) }


    Depend.others.forEach { implementation(it) }
    implementation(Depend.hilt)
    Depend.okHttpLibraries.forEach { implementation(it) }
    implementation(Depend.retrofitConvert)
    implementation(Depend.paging)
    implementation("com.appodeal.ads:sdk:3.2.1.+") {
        exclude(group = "com.appodeal.ads.sdk.services", module = "adjust")
        exclude(group = "com.appodeal.ads.sdk.services", module = "appsflyer")
        exclude(group = "com.appodeal.ads.sdk.services", module = "firebase")
        exclude(group = "com.appodeal.ads.sdk.services", module = "facebook_analytics")
        exclude(group = "com.appodeal.ads.sdk.services", module = "sentry_analytics")
    }

    kapt(Depend.hiltKapt)
    androidTestImplementation(Depend.hiltInstrumentedTest)
    kaptAndroidTest(Depend.hiltKaptTest)
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