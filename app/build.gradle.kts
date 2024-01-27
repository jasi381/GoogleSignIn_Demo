plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.gms.service)
    alias(libs.plugins.kotlinKsp)
    alias(libs.plugins.hiltAndroid)
}

android {
    signingConfigs {
        create("release") {
            storeFile =
                file("/home/jasmeet_singh/AndroidStudioProjects/GoogleSignInDemo/keystore.jks")
            storePassword = "jasmeet34"
            keyAlias = "release"
            keyPassword = "jasmeet34"
        }
    }
    namespace = "com.jasmeet.googlesignindemo"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.jasmeet.googlesignindemo"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("release")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_19
        targetCompatibility = JavaVersion.VERSION_19
    }
    kotlinOptions {
        jvmTarget = "19"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.8"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    //hilt
    implementation(libs.hiltAndroid)
    ksp(libs.hiltCompiler)
    ksp(libs.hiltCompilerKapt)
    implementation(libs.hiltNavigation)

    //firebase
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.auth)
    implementation(libs.firebase.firestore.ktx)
    implementation(libs.play.services.auth)

    //navigation compose
    implementation(libs.androidx.navigation.compose)

    //liveData
    implementation(libs.liveData)

    //Coil
    implementation(libs.coil.compose)

}
