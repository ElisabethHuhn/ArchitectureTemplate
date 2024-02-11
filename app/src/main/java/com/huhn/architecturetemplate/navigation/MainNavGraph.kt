package com.huhn.architecturetemplate.navigation

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.huhn.architecturetemplate.R
import com.huhn.architecturetemplate.ui.ForecastRoute
import com.huhn.architecturetemplate.ui.LandingRoute

/*
 * A ScreenDestination keeps together all the information needed
 * for navigation to/from the screen
 * Every screen has one of these ScreenDestinations defined for it
 * Best reference https://bignerdranch.com/blog/using-the-navigation-component-in-jetpack-compose/
 */
interface ScreenDestination {
    val route: String
    val title: Int
    val navLabel: Int
}

object WeatherDestination : ScreenDestination {
    override val route: String
        get() = "weather_screen"
    override val title: Int
        get() = R.string.weather_title
    override val navLabel: Int
        get() = R.string.weather
    //If one needs to pass arguments to a screen, it is possible. E.g.:
//    const val driverIdArg = "driverId"
//    val routeWithArg: String = "$route/{$driverIdArg}"
//    val arguments = listOf(navArgument(driverIdArg) {type = NavType.StringType})
//    fun getNavigationDriverToRoute(driverId: String) = "$route/$driverId"
}
object LandingDestination : ScreenDestination {
    override val route: String
        get() = "landing_screen"
    override val title: Int
        get() = R.string.landing_title

    override val navLabel: Int
        get() = R.string.home
}
object ForecastDestination : ScreenDestination {
    override val route: String
        get() = "forecast_screen"
    override val title: Int
        get() = R.string.forecast_title
    override val navLabel: Int
        get() = R.string.forecast
}



/*
 * The NavHost is single source of truth for all screen navigation in the app
 */
@ExperimentalMaterial3Api
@Composable
fun MainNavGraph(){
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination =  LandingDestination.route
    ){
        composable(
            route = LandingDestination.route,
        ){
            LandingRoute(
                screenTitle = LandingDestination.title,
                navigateToForecast = { navController.navigateToForecast() },
                navController = navController
            )
        }
        composable(
            route = ForecastDestination.route,
        ){
            ForecastRoute(
                screenTitle = ForecastDestination.title,
                navigateToLanding = { navController.navigateToLanding() },
                navController = navController
            )
        }
    }
}

fun NavController.navigateToLanding(navOptions: NavOptions? = null) {
    this.navigate(LandingDestination.route, navOptions = navOptions)
}
fun NavController.navigateToForecast(navOptions: NavOptions? = null) {
    this.navigate(ForecastDestination.route, navOptions = navOptions)
}
fun NavController.navigateToWeather(navOptions: NavOptions? = null) {
    this.navigate(WeatherDestination.route, navOptions = navOptions)
}



