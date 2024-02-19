object Depend {
    val supportAndroidLibs = arrayOf(
        "androidx.annotation:annotation:${Versions.SupportAndroidLibs.supportLibraryAnnotation}",
        "androidx.appcompat:appcompat:${Versions.SupportAndroidLibs.supportLibraryAppCompat}",
        "com.google.android.material:material:${Versions.SupportAndroidLibs.materialDesign}",
        "androidx.constraintlayout:constraintlayout:${Versions.SupportAndroidLibs.constraintLayout}",
        "androidx.navigation:navigation-fragment-ktx:${Versions.SupportAndroidLibs.navigationComponent}",
        "androidx.navigation:navigation-ui-ktx:${Versions.SupportAndroidLibs.navigationComponent}",
        "androidx.lifecycle:lifecycle-livedata-ktx:${Versions.SupportAndroidLibs.lifecycle}",
        "androidx.lifecycle:lifecycle-viewmodel-ktx:${Versions.SupportAndroidLibs.lifecycle}",
        "androidx.core:core-splashscreen:${Versions.SupportAndroidLibs.splashScreen}",
        "androidx.swiperefreshlayout:swiperefreshlayout:1.1.0"
    )

    val kotlinDependency = arrayOf(
        "androidx.core:core-ktx:${Versions.KotlinDependency.std}"
    )

//    const val kotlinKTX = "androidx.core:core-ktx:${Versions.KotlinDependency.std}"

    // The same here in Google Libraries
    val google = arrayOf(
        "com.google.android.gms:play-services-analytics:${Versions.Google.playServices}",
        "com.google.firebase:firebase-core:${Versions.Google.firebase}",
        "com.android.installreferrer:installreferrer:${Versions.Google.installReferrer}"
    )

    //Dagger
    val dagger = arrayOf(
        "com.google.dagger:dagger:${Versions.Google.dagger}",
        "com.google.dagger:dagger-android:${Versions.Google.dagger}",
        "com.google.dagger:dagger-android-support:${Versions.Google.dagger}"
    )

    const val daggerKapt = "com.google.dagger:dagger-compiler:${Versions.Google.dagger}"

    const val hilt = "com.google.dagger:hilt-android:${Versions.Google.hilt}"
    const val hiltKapt = "com.google.dagger:hilt-android-compiler:${Versions.Google.hilt}"

    //Dagger AnnotationProcessor
    val daggerAnnotationProcessor = arrayOf(
        "com.google.dagger:dagger-compiler:${Versions.Google.dagger}",
        "com.google.dagger:dagger-android-processor:${Versions.Google.dagger}"
    )

    //OkHttp and Retrofit
    val okHttpLibraries = arrayOf(
        "com.squareup.okhttp3:okhttp:${Versions.Libraries.okhttp}",
        "com.squareup.okhttp3:logging-interceptor:${Versions.Libraries.okhttp}",
        "com.squareup.retrofit2:retrofit:${Versions.Libraries.retrofit}",

    )

    val retrofitConvert = "com.squareup.retrofit2:converter-gson:${Versions.Libraries.retrofit}"

    //Retrofit Mock
    val retrofitMock =
        arrayOf("com.squareup.retrofit2:retrofit-mock:${Versions.Libraries.retrofit}")

    //    //Gson
//    const val gson = "com.google.code.gson:gson:${Versions.Libraries.gson}"
    //other dependencies
    val others = arrayOf(
        "com.airbnb.android:lottie:${Versions.Libraries.lottie}",
        "com.onesignal:OneSignal:${Versions.Libraries.onesignal}",
        "com.github.terrakok:cicerone:${Versions.Libraries.cicerone}",)

    const val paging = "androidx.paging:paging-runtime:${Versions.paging_version}"

    // Test unit testImplementation
    const val testUnit = "junit:junit:${Versions.TestLibraries.junit}"

    //androidTestImplementation
    val testRunner = arrayOf(
        "androidx.test.ext:junit:${Versions.TestLibraries.ext}"
    )

    //Test Espresso
    const val testEspresso =
        "androidx.test.espresso:espresso-core:${Versions.TestLibraries.espresso}"
}