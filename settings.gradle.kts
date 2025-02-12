pluginManagement {
    /*
     * The pluginManagement.repositories block configures the repositories
     * Gradle uses to search or download the Gradle plugins and
     * their transitive dependencies.
     *
     * Gradle pre-configures support for remote repositories such as:
     *      JCenter,
     *      Maven Central, and
     *      Ivy.
     *
     * You can also use local repositories or define your own remote repositories.
     *
     * The code below defines the repositories Gradle should use to look for its dependencies as:
     *      the Gradle Plugin Portal,
     *      Google's Maven repository, and
     *      the Maven Central Repository
     */
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
}
plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"
}
dependencyResolutionManagement {
    /*
     * The dependencyResolutionManagement.repositories block is where you configure
     * the repositories and dependencies used by all modules in your project,
     *
     * For example: libraries that you are using to create your application.
     *
     * Module-specific dependencies should be configured in each module-level
     * build.gradle file.
     *
     * For new projects by default, Android Studio includes repositories b
     * ut it does not configure any dependencies. (unless you select a template that requires some).
     *
     * Default repositories are:
     *      Google's Maven repository and
     *      the Maven Central Repository
     */
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "FetchRewards"
include(":app")
 