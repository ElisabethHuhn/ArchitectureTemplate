package com.huhn.architecturetemplate.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import coil.compose.AsyncImage
import com.huhn.architecturetemplate.R
import com.huhn.architecturetemplate.navigation.ForecastDestination
import com.huhn.architecturetemplate.navigation.LandingDestination
import com.huhn.architecturetemplate.utils.getActivity
import com.huhn.architecturetemplate.viewmodel.MainViewModelImpl
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LandingRoute(
    screenTitle: Int,
    navigateToForecast: () -> Unit,
    navController: NavController
) {
    val viewModel : MainViewModelImpl = koinViewModel()
    val state by viewModel.weatherState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    //preload weather from last time the app ran
    LaunchedEffect(key1 = "firsttime" ){
        viewModel.onInitialization()
        viewModel.getCurrentLocation(context)
        viewModel.onWeatherUserEvent(WeatherUserEvent.OnDisplayWeatherEvent(isByLoc = true, isForecast = true))
   }

    LandingScreen (
        screenTitle = screenTitle,
        state = state,
        onUserEvent = viewModel::onWeatherUserEvent,
        navController = navController,
        navigateToForecast = navigateToForecast
    )
}

@OptIn(ExperimentalComposeUiApi::class)
@ExperimentalMaterial3Api
@Composable
fun LandingScreen(
    screenTitle : Int,
    state: WeatherUIState,
    onUserEvent: (WeatherUserEvent) -> Unit,
    navigateToForecast: () -> Unit,
    navController: NavController
) {
    // Navigation bar list
    val navItems = listOf(
        LandingDestination,
        ForecastDestination,
//    WeatherDestination
    )

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Box(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            text = stringResource(screenTitle),
                            fontSize = 30.sp,
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }
                },
            )
        },
        bottomBar = {
            BottomNavigation {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination
                navItems.forEach { screen ->
                    BottomNavigationItem(
                        icon = { Icon(Icons.Filled.Favorite, contentDescription = null) },
                        label = { Text(stringResource(screen.navLabel)) },
                        selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                        onClick = {
                            navController.navigate(screen.route) {
                                // Pop up to the start destination of the graph to
                                // avoid building up a large stack of destinations
                                // on the back stack as users select items
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                // Avoid multiple copies of the same destination when
                                // reselecting the same item
                                launchSingleTop = true
                                // Restore state when reselecting a previously selected item
                                restoreState = true
                            }
                        }
                    )
                }
            }
        },
//        floatingActionButton = {
//            FloatingActionButton(
//                content = { Text(text = "FAB")},
//                onClick = {
//                    /*TODO Do something here if have FAB */
//
//                },
//                shape = RectangleShape,
//            )
//        }

    ) {it
        val context = LocalContext.current
        val activity : MainActivity = context.getActivity() as MainActivity
        var locPermissionRequested: Boolean by remember { mutableStateOf(false) }
        val locPermissionGranted: Boolean by remember { mutableStateOf(activity.isLocationPermissionGranted) }

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth(1f)
                .testTag(tag = "landing_screen"),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                Spacer(modifier = Modifier.height(95.0.dp))
            }
            item {
                Text(text = "*************************************", fontWeight = FontWeight.Bold)
            }


//***************************************************************************************************
            if (locPermissionGranted) {
                item {
                    Row(
                        Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(text = state.city, fontWeight = FontWeight.Bold, fontSize = 25.sp)
                    }
                }
                item {
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .padding(start = 20.dp, end = 20.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = "Wind:", fontWeight = FontWeight.Bold)
                        Text(text = state.temp)
                    }
                }
                item {
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .padding(start = 20.dp, end = 20.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = state.windDir)
                        Spacer(modifier = Modifier.width(5.0.dp))
                        Text(text = "Feels Like ${state.feelsLike}")
                    }
                }

                item {
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .padding(start = 20.dp, end = 20.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = "${state.windSpeed} mph")
                        Spacer(modifier = Modifier.width(5.0.dp))
                        Text(text = "${state.tempMax} / ${state.tempMin}")
                    }
                }

                items(state.forecastState.size) { position ->
                    val forecast = state.forecastState[position]
                    Spacer(modifier = Modifier.height(5.0.dp))
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .padding(start = 20.dp, end = 20.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = forecast.dt_txt)
                        Spacer(modifier = Modifier.width(5.0.dp))
                        Text(text = forecast.temp)
                        Spacer(modifier = Modifier.width(5.0.dp))
                        AsyncImage(
                            model = "https://openweathermap.org/img/wn/${forecast.icon}@2x.png",
                            placeholder = painterResource(id = R.drawable.ic_android_black_24dp),
                            error = painterResource(id = R.drawable.ic_android_black_24dp),
                            contentDescription = "An icon suggesting weather description",
                            contentScale = ContentScale.Fit,
                            modifier = Modifier
                                .size(50.dp)
                                .border(
                                    BorderStroke(5.dp, Color.Gray),
                                    RectangleShape
                                )
                        )
                    }
                }
            } else {
                item {
                    Spacer(modifier = Modifier.height(95.0.dp))
                    Text(text = "Location permission must be granted to run app")
                }
                item {
                    if (locPermissionRequested) {
                        Text(text = "Please uninstall/reinstall app")
                    } else {
                        Button(
                            onClick = {
                                locPermissionRequested = true
                                activity.askLocationPermissions()
                            })
                        {
                            Text(text = "Grant Location Permissions")
                        }
                    }
                }
            }
        }
// ****************************************************************************************
    }
}
