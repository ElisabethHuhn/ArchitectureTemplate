# Architecture Template App

# Introduction

This project is a specific example of using the template as a starting point.

In partucular, it is for an interview with Fetch

# Discussion

I was actually not terribly happy with the results of using this template. 
The architecture ended up being convoluted with a lot of tech debt that needs to be cleaned up.

Explicitly, there are bits and pieces left over from the template that are not needed in the app
being built. Some of these I left in (Permissions, ROOM) and some I removed. 

# Requirements
Please write a native Android app in Kotlin or Java that retrieves the data from https://fetch-hiring.s3.amazonaws.com/hiring.json.

Display this list of items to the user based on the following requirements:

Display all the items grouped by "listId"
Sort the results first by "listId" then by "name" when displaying.
Filter out any items where "name" is blank or null.
The final result should be displayed to the user in an easy-to-read list.

Please make the project buildable on the latest (non-pre release) tools and supporting the current release mobile OS

# Architecture Notes
The project is an example of State of the Art Android Architecture circa 2024. It uses:
* Kotlin 100%
* Koin for Dependency Injection
    * For now, the only classes that are injected are the ViewModel and Repository
* Compose for building UI
    * Also uses Compose NavGraph navigation
* Retrofit for Network calls
* ~~Coil for loading images from network URLs~~
* ~~Accompanist for permissions~~
* ~~FusedLocationProvider from GooglePlayServices for location management~~
* Version Catalog for Gradle dependencies
* MVVM / MVI to
    * persist UI local cache across orientation changes
    * UI state variables governing UI compose
        * Enable state hoisting
    * business logic to calculate values for UI display (i.e. state)
    * business logic for actions in response to UI events
* Repository to separate business logic from Data Source
    * Examples of both localDataSource and remoteDataSource
    * ~~localDataSource is ROOM DB~~
    * remoteDataSource is RetroFit2
* Use Flows to move UI state data back to UI compose (via the ViewModel) from Repository
* Coroutines for both serial and parallel structured concurrency
* Unit Testing
    * The problem statement did not call for unit tests, and the 2-4 hour time box seemed to obviate tests. I did make use of @PREVIEW to test the UI
    * ~~Retrofit unit tests with test/java/com.huhn.jmpcexample/
    * DB instrumented test with androidTest/java/com.huhn.jmpcexample/DBInstrumentedTest
        * These need to run on a real device
        * best resource for room DB testing https://medium.com/@wambuinjumbi/unit-testing-in-android-room-361bf56b69c5
    * Compose UI testing
        * reference https://developer.android.com/codelabs/jetpack-compose-testing#0~~



# Architecture and its Effects on Testing
I initially wrote this app with an MVVM architecture,
as that was the direct requirement. But as I learned more about MVI I rewrote the app.
And the experience has taught me that MVI is clearly superior.
One file, ScreenContract.kt, tells you everything you need to know to unit test the corresponding Screen.
Testing the UI, ViewModel, Repository and local/remote data sources is straightforward, and obvious from the Architecture.

# Future Work
* Two screens: one with list of ListIds with # of users, and one with list of users for a ListId
* Automated Testing
    * Unit Testing
    * UI Testing