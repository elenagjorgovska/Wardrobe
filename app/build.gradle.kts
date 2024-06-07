plugins {
    id("com.android.application")
    id ("org.jetbrains.kotlin.android")
    id("com.google.gms.google-services")
    id("kotlin-kapt")

}

android {
    namespace = "com.elena.mywardrobe"
    compileSdk = 34


    kapt {
        arguments {
            arg("room.schemaLocation", "$projectDir/schemas")
        }
    }

    defaultConfig {
        applicationId = "com.elena.mywardrobe"
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

    viewBinding {
        enable = true
    }
    buildFeatures {
        dataBinding = true
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
    implementation ("androidx.core:core-ktx:1.13.1")
    implementation ("androidx.appcompat:appcompat:1.7.0")
    implementation ("com.google.android.material:material:1.9.0")
    implementation ("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation ("androidx.lifecycle:lifecycle-livedata-ktx:2.8.1")
    implementation ("androidx.lifecycle:lifecycle-viewmodel-ktx:2.8.1")
    implementation ("androidx.navigation:navigation-fragment-ktx:2.7.7")
    implementation ("androidx.navigation:navigation-ui-ktx:2.7.7")
    implementation ("com.google.android.material:material:1.9.0")
    implementation ("com.google.android.flexbox:flexbox:3.0.0")
    implementation ("com.google.firebase:firebase-analytics:22.0.1")
    implementation ("com.google.firebase:firebase-auth:23.0.0")
    implementation ("com.google.firebase:firebase-auth-ktx:23.0.0")
    implementation ("com.google.firebase:firebase-firestore-ktx:25.0.0")
    implementation ("com.google.firebase:firebase-messaging:23.1.2")
    implementation ("com.google.firebase:firebase-messaging-ktx:24.0.0")
    implementation ("com.google.firebase:firebase-firestore:25.0.0")
    implementation ("com.google.firebase:firebase-storage:20.2.0")
    implementation(platform("com.google.firebase:firebase-bom:33.1.0"))
    implementation ("com.google.firebase:firebase-firestore-ktx")
    implementation("androidx.work:work-runtime-ktx:2.9.0")

    testImplementation ("junit:junit:4.13.2")
    androidTestImplementation ("androidx.test.ext:junit:1.1.5")
    androidTestImplementation ("androidx.test.espresso:espresso-core:3.5.1")
    kapt ("androidx.room:room-compiler:2.6.1")
    implementation ("androidx.room:room-runtime:2.6.1")
    implementation ("androidx.room:room-ktx:2.6.1")
    annotationProcessor ("androidx.room:room-compiler:2.6.1")


    // When using the BoM, you don"t specify versions in Firebase library dependencies

    // Add the dependency for the Firebase SDK for Google Analytics
    implementation ("com.google.firebase:firebase-analytics-ktx")

    // Add the dependency for the Firebase Authentication library
    // When using the BoM, you don"t specify versions in Firebase library dependencies
    implementation ("com.google.firebase:firebase-auth-ktx")

    // Also add the dependency for the Google Play services library and specify its version
    implementation ("com.google.android.gms:play-services-auth:20.5.0")

    implementation ("com.facebook.android:facebook-android-sdk:[8,9)")



    implementation ("androidx.lifecycle:lifecycle-runtime-ktx:2.6.1")


    // optional - RxJava2 support for Room
    implementation("androidx.room:room-rxjava2:2.6.1")

    // optional - RxJava3 support for Room
    implementation("androidx.room:room-rxjava3:2.6.1")

    // optional - Guava support for Room, including Optional and ListenableFuture
    implementation("androidx.room:room-guava:2.6.1")

    // optional - Test helpers
    testImplementation("androidx.room:room-testing:2.6.1")

    // optional - Paging 3 Integration
    implementation("androidx.room:room-paging:2.6.1")

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}