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

    id("com.android.application") version "8.7.3" apply false
    id("org.jetbrains.kotlin.android") version "2.0.0" apply false

    id("com.android.library") version "8.1.4" apply false
    id("com.google.devtools.ksp") version "2.1.10-1.0.29" apply false

    alias(libs.plugins.compose.compiler) apply false
}

buildscript {
    dependencies {
        classpath(libs.secrets.gradle.plugin)
    }
}