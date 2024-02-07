// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    /* These plugins are searched for in the repositories mentioned in settings.gradle
     *
     * Use `apply false` in the top-level build.gradle file to add a Gradle
     * plugin as a build dependency but not apply it to the current (root)
     * project.
     *
     * Don't use `apply false` in sub-projects. For more information,
     * see Applying external plugins with same version to subprojects.
     */

    id("com.android.application") version "8.2.1" apply false
    id("org.jetbrains.kotlin.android") version "1.9.22" apply false

    id("com.android.library") version "8.1.4" apply false
    id("com.google.devtools.ksp") version "1.9.22-1.0.17" apply false
}

buildscript {
    dependencies {
        classpath("com.google.android.libraries.mapsplatform.secrets-gradle-plugin:secrets-gradle-plugin:2.0.1")
    }
}