plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android") version "1.8.10" apply false
}

android {
    namespace = "com.example.watch_step"
    compileSdk = 33

    defaultConfig {
        applicationId = "com.example.watch_step"
        minSdk = 33
        targetSdk = 30
        versionCode = 1
        versionName = "1.0"

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
}

dependencies {
    implementation("androidx.core:core-ktx:1.10.0")
    implementation(platform("org.jetbrains.kotlin:kotlin-bom:1.8.0"))
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.9.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")

    // Wear OS specific dependencies
    implementation("androidx.wear:wear:1.3.0")
    implementation("androidx.wear.tiles:tiles-proto:1.0.0")

    // For RecyclerView
    implementation("androidx.recyclerview:recyclerview:1.3.0")

    // SQLite
    implementation("androidx.sqlite:sqlite:2.3.0")

    // Permissions
    implementation("androidx.activity:activity-ktx:1.7.2")
    implementation("org.jetbrains.kotlin:kotlin-stdlib:1.8.22") // explicitly include kotlin-stdlib

    // Testing
}