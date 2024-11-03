plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.serialization)
}

android {
    namespace = "com.example.myapplication"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.myapplication"
        minSdk = 28
        targetSdk = 34
        versionCode = 103
        versionName = "1.0.3"

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
    implementation(libs.androidx.leanback)
    implementation(libs.glide)
    //网络请求模块
    implementation("com.squareup.okhttp3:okhttp:4.9.3")
    implementation(libs.androidx.cardview)
    //json及序列化 解析模块
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.5.1")
    implementation("androidx.core:core-ktx:1.9.0")
    implementation("org.jetbrains.kotlin:kotlin-parcelize-runtime:1.8.22")
    //协程模块
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.6.0")
    //播放器模块
    implementation("com.github.CarGuo.GSYVideoPlayer:gsyvideoplayer-java:v10.0.0")
    //是否需要ExoPlayer模式
    implementation("com.github.CarGuo.GSYVideoPlayer:gsyvideoplayer-exo2:v10.0.0")
    //是否需要AliPlayer模式
    implementation("com.github.CarGuo.GSYVideoPlayer:gsyvideoplayer-aliplay:v10.0.0")
    //更多ijk的编码支持
    implementation("com.github.CarGuo.GSYVideoPlayer:gsyvideoplayer-ex_so:v10.0.0")

}