# Architecture Template App

# Introduction

This project is meant to be a starting point. 
It provides a rough template framework for a state-of-the-art Android Compose Project. 
Start here and build your new project from this base.

This readme file is a work in progress. Eventually it will contain instructions for setting up a new project from this template.

# Architecture Notes
The project is an example of State of the Art Android Architecture circa 2023. It uses:
* Kotlin 100%
* Koin for Dependency Injection
    * For now, the only classes that are injected are the ViewModel and Repository
* Compose for building UI
    * Also uses Compose NavGraph navigation
* Retrofit for Network calls
* Coil for loading images from network URLs
* Accompanist for permissions
* FusedLocationProvider from GooglePlayServices for location management
* MVVM / MVI to
    * persist UI local cache across orientation changes
    * UI state variables governing UI compose
        * Enable state hoisting
    * business logic to calculate values for UI display (i.e. state)
    * business logic for actions in response to UI events
* Repository to separate business logic from Data Source
    * Examples of both localDataSource and remoteDataSource
    * localDataSource is ROOM DB
    * remoteDataSource is RetroFit2
* Use Flows to move UI state data back to UI compose (via the ViewModel) from Repository
* Coroutines for both serial and parallel structured concurrency
* Unit Testing
    * Retrofit unit tests with test/java/com.huhn.jmpcexample/
    * DB instrumented test with androidTest/java/com.huhn.jmpcexample/DBInstrumentedTest
        * These need to run on a real device
        * best resource for room DB testing https://medium.com/@wambuinjumbi/unit-testing-in-android-room-361bf56b69c5
    * Compose UI testing
        * reference https://developer.android.com/codelabs/jetpack-compose-testing#0

# How to use the template

## Create a new project from the template
* Goto [GotHub](https://github.com/ElisabethHuhn/ArchitectureTemplate/tree/master) 
* **Needs more here**
* Enter a name for your new project
* Click on the green "Create repository from template" button

##  Update the package name

* In Android Studio, right click on the package name in the Project pane
* Select Refactor -> Rename
* Enter the new package name
* Click on the Refactor button

## Update the app name
* Manually:
* In Android Studio, open the AndroidManifest.xml file
* Change to the new name the:
  * android:label attribute of the application tag 
  * activity tag
  * activity-alias tag
  * meta-data tag
  * provider tag
  * receiver tag
  * service tag
  * uses-library tag
  * uses-permission tag
  * uses-permission-sdk-23 tag
  * uses-static-library tag
  * uses-feature tag
  * uses-sdk tag
  * uses-configuration tag
  * uses-gl-es tag
* Use the plugin: Android Package Renamer
  * https://plugins.jetbrains.com/plugin/10009-android-package-renamer
  *
*

## Update secrets
* add secret to local.properties
* Load the secret from BuildConfig

## Update Dependency Injection i.e. Koin

## Update Permissions

## Change UI
* Add new UI ScreenRoute files under UI package
  * Replace WeatherRoute with your new screen
* Update NavGraph to add new screens
* Call the appropriate UI compose function from MainActivity
* Update MainContract

## Update ViewModel
* Add new ViewModel files under ViewModel package
  * Replace WeatherViewModel with your new ViewModel

## Update Repository

* Add new Repository files under Repository package
  * Replace WeatherRepository with your new Repository
  * Replace WeatherRepositoryImpl with your new RepositoryImpl
  * Replace WeatherRepositoryImplTest with your new RepositoryImplTest
  * Replace WeatherRepositoryImplInstrumentedTest with your new RepositoryImplInstrumentedTest

## Update Data Source

## Update Data Model

## Update Network i.e. Retrofit

## Update Local Storage i.e. Room

## Update Unit Tests