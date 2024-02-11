# Architecture Template App

# Introduction

This project is a specific example of using the template as a starting point.

# Discussion

I was actually not terribly happy with the results of using this template. 
The architecture ended up being convoluted with a lot of tech debt that needs to be cleaned up.

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



# Architecture and its Effects on Testing
I initially wrote this app with an MVVM architecture,
as that was the direct requirement. But as I learned more about MVI I rewrote the app.
And the experience has taught me that MVI is clearly superior.
One file, ScreenContract.kt, tells you everything you need to know to unit test the corresponding Screen.
Testing the UI, ViewModel, Repository and local/remote data sources is straightforward, and obvious from the Architecture.

# Justification for odd screen layout
Unfortunately, Compose does odd things with the UI component tree if there are if statements in the LazyColumn.
For example, all items following the if statement are duplicated.
For now, I've just punted and put all the if statements at the end of the column.

# Automated Testing
Writing and maintaining automated testing is clearly costly.
But the potential for payback ROI is obvious.
Regression testing is performed consistently and repeatedly.
The earlier you find a bug, the cheaper it is to fix.
Thus, a bug found by a developer is cheaper than that same bug found in QA.
Automated testing finds bugs earlier in the process,
and this savings more than covers the cost of creating and maintaining automated testing.
However even more than that, an architecture that is designed to be testable is also less likely to have bugs in the first place.  
With a good architecture you can avoid a whole class of bugs entirely.
A testable architecture is easier to maintain, thus justifying the cost of creating and maintaining automated testing.

MVI allows for a greater separation of concerns over MVVM, and thus an easier isolation of code, leading to higher quality testing.
MVI allows for the tracking of user event occurrence resulting in UI state change.

## To run automated tests:
I ran the automated tests from Android Studio:

* ViewModel, run the tests in DriverViewModelTest
* Repository, run the tests in DriverRepositoryTest
* Smoke test the UI, run the tests in TestDriverComposeUI and TestRouteComposeUI

## ViewModel Testing Strategy
The only things publicly visible from the ViewModel are:
* Screen State
* onUserEvent() function, which takes a UserEvent and updates the ScreenState accordingly
* Any user action functions that are exposed on the UI.
  * These include lambdas that must be passed on,
  * such as actions passed to GoogleMaps when a marker is selected, etc.

Thus the ViewModel can be tested by initializing a state value, simulating a user event, and making assertions about the final state

### ViewModel testing Notes
* Arrange
  * Initialize the ViewModel with a fake repository class and a known state
* Act
  * Call the onUserEvent() function with the known UserEvent
* Assert
  * Make assertions about the final state

## Repository Testing Strategy
The only things publicly visible from the Repository are:
* Fetch the forecast and update the UI state depending upon whether the Landing or 
the Forecast screen is being displayed
  * Initial fetch is from the local DB
  * If the local DB is empty, then fetch from the remote API
* Delete all Drivers and Routes from the local DB

So the Repository can be tested by initializing the local DB (both empty and with known values),
then fetching the lists of Drivers and Routes, and making assertions about the final state.

## UI Testing Strategy
Once the ViewModel and Repository are tested, the UI testing can be to simply automate the smoke test of the App.
Compose gives us the ability to test that the UI is properly displaying the expected state.
The easiest way to test that an element is on the UI is to assign testTags to the UI elements,
then find those testTags in the UI test. Thus, all of the compose elements have testTags assigned to them.

However, it is never easy. Compose seems to duplicate nodes when there is an if statement governing the items of a LazyColumn.
So relying on the testTag to find the element in the UI test is not a bullet-proof strategy.
I got around this by putting all the if statements at the end of the LazyColumn, but it makes for a stilted UI.


### Smoke Test Script:

The smoke test is a quick test that assures the app is displaying the expected UI state.

# Near Exhaustive list of Testing Types
This app only illustrates a few of the many types of testing that can be performed on an app.
* Functional - Does it do what the requirements say it should do
* Non-functional - Performance, Capacity, Throughput, Power consumption, network reception
* Android UI
  * component visibility
  * user interaction (event detection and response actions conform to requirements)
* Device Compatibility
  * OS version
  * Device Model
  * Screen Size
  * Screen Resolution
  * Network Connectivity
* Integration Testing
* Network Testing
  * Connectivity
  * Intermittent reception
  * Field testing with Mobile Data Network
* Installation Testing
* Security Testing
* Bucket or A/B testing








