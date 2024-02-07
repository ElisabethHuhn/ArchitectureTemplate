/*
 * The first section in the build configuration applies the Android Gradle plugin
 * to this build and makes the android block available to specify
 * Android-specific build options.
 */
plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    //secrets gradle plugin
    id("com.google.android.libraries.mapsplatform.secrets-gradle-plugin")
    id("com.google.devtools.ksp")

}

//val weatherApiKey: String = gradleLocalProperties(rootDir).getProperty("weatherApiKey")
/*
 * Locate (and possibly download) a JDK used to build your kotlin source code.
 * This also acts as a default for sourceCompatibility, targetCompatibility and jvmTarget.
 *
 * Note that this does not affect which JDK is used to run the Gradle build itself,
 * and does not need to take into account the JDK version required by Gradle plugins
 * (such as the Android Gradle Plugin)
 *
 * See compileOptions and kotlinOptions below, as both are related
 */
//kotlin {
//    jvmToolchain(17)
//}

/*
 * The android block is where you configure all your Android-specific build options.
 */
android {
    /*
     * The app's namespace. Used as the package name for generated R and BuildConfig classes
     */
    namespace = "com.huhn.architecturetemplate"
    /*
     * compileSdk
     * determines which Android and Java APIs are available when compiling the source code.
     * can use the API features included in this API level and lower.
     */
    compileSdk = 34

    /*
     * The defaultConfig block encapsulates default settings and entries for all build variants
     * and can override some attributes in main/AndroidManifest.xml dynamically from the build system.
     *
     * You can override these values by configuring product flavors for different versions of your app.
     */
    defaultConfig {
        // applicationId Uniquely identifies the package for publishing.
        applicationId = "com.huhn.architecturetemplate"
        // testApplicationId "com.huhn.architecturetemplate.test"

        minSdk = 24 //specifies the lowest version of Android that the app will run on
        /*
         * targetSdk attests which version of Android you've tested against
         * must be <= the value of compileSdk.
         * if runtime is on a higher API version, the app app runs in compatibility mode
         * that matches the targetSdk
         */
        targetSdk = 34
        versionCode = 1 // Defines the version number of your app.
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    /*
    * The buildTypes block is where you can configure multiple build types.
    * By default, the build system defines two build types: debug and release.
    *
    * The debug build type is not explicitly shown in the default build configuration,
    * but it includes debugging tools and is signed with the debug key.
    *
    * The release build type applies ProGuard settings and is not signed by default.
    */
    buildTypes {
        /*
         * By default, Android Studio configures the release build type to enable code
         * shrinking, using minifyEnabled, and specifies the default ProGuard rules file.
         *
         * By default, Android Studio configures the release build type to enable code
         * shrinking, using minifyEnabled, and specifies the default ProGuard rules file.
         */
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    /*
     * Product flavors are optional, and the build system does not create them by default.
     *
     * The productFlavors block is declares and configures multiple product flavors,
     * Each flavor overrides the defaultConfig block with their own settings.
     *
     * This example creates a free and paid product flavor.
     * Each product flavor then specifies its own application ID,
     * so that they can simultaneously exist on the Google Play Store, or an Android device.
     *
     * If you declare product flavors, you must also declare flavor dimensions
     * and assign each flavor to a flavor dimension.
     */
//    flavorDimensions += "tier"
//    productFlavors {
//        create("free") {
//            dimension = "tier"
//            applicationId = "com.example.myapp.free"
//        }
//
//        create("paid") {
//            dimension = "tier"
//            applicationId = "com.example.myapp.paid"
//        }
//    }



    /*
     * use options to override source and target compatibility
     * (if different from the toolchain JDK version),
     * All of these default to the same value as kotlin.jvmToolchain.
     *
     * If you're using the same version for these values and kotlin.jvmToolchain,
     *  you can remove these blocks.
     */
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    /*
     * need more on Build features
     */
    buildFeatures {
        compose = true
        buildConfig = true
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

/*
 * specifies dependencies (external binaries or other library modules)
 * required to build only the module itself.
 *
 * dependencies can be local or remote
 * remote online repositories are specified in the settings.gradle file in the
 * dependencyResolutionManagement { repositories {...} } block
 * The order in which you list each repository determines the order in which Gradle searches the repositories
 *
 * any transitive dependencies they declare are automatically included as well.
 *  use the exclude keyword to exclude transitive dependencies
 * The Android plugin for Gradle provides a task that displays
 * a list of the dependencies Gradle resolves for a given module.
 */
dependencies {

    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.7.0")
    // Lifecycle components
    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.7.0")
    //navigation
    implementation ("androidx.navigation:navigation-compose:2.7.6")
    implementation("androidx.compose.material:material:1.6.0")

    implementation("androidx.activity:activity-compose:1.8.2")
    implementation(platform("androidx.compose:compose-bom:2023.08.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")

    //coil for image loading
    implementation("io.coil-kt:coil-compose:2.4.0")


    //koin
    implementation ("io.insert-koin:koin-androidx-compose:3.4.6")

    //ROOM
    implementation("androidx.room:room-runtime:2.6.1")
    ksp("androidx.room:room-compiler:2.6.1")
    implementation ("androidx.room:room-ktx:2.6.1")

    //retrofit
    implementation ("com.google.code.gson:gson:2.10.1")
    implementation ("com.squareup.retrofit2:retrofit:2.9.0")
    implementation ("com.squareup.retrofit2:converter-gson:2.9.0")


    //fused location provider
    implementation ("com.google.android.gms:play-services-location:21.0.1")

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation(platform("androidx.compose:compose-bom:2023.08.00"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")
}
