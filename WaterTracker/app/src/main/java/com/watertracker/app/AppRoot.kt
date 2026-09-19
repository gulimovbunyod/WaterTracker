package com.watertracker.app

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.watertracker.app.ui.home.HomeScreen
import com.watertracker.app.ui.home.HomeViewModel
import com.watertracker.app.ui.stats.StatsScreen
import com.watertracker.app.ui.stats.StatsViewModel

private enum class Tab { HOME, STATS }

@Composable
fun AppRoot(application: WaterTrackerApplication) {
    var currentTab by remember { mutableStateOf(Tab.HOME) }
    val homeViewModel: HomeViewModel = viewModel(factory = HomeViewModel.factory(application.repository))
    val statsViewModel: StatsViewModel = viewModel(factory = StatsViewModel.factory(application.repository))

    Scaffold(
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    selected = currentTab == Tab.HOME,
                    onClick = { currentTab = Tab.HOME },
                    icon = { Icon(Icons.Default.Home, contentDescription = null) },
                    label = { Text("Bugun") }
                )
                NavigationBarItem(
                    selected = currentTab == Tab.STATS,
                    onClick = { currentTab = Tab.STATS },
                    icon = { Icon(Icons.Default.BarChart, contentDescription = null) },
                    label = { Text("Statistika") }
                )
            }
        }
    ) { padding ->
        Box(modifier = Modifier.padding(padding)) {
            when (currentTab) {
                Tab.HOME -> HomeScreen(homeViewModel)
                Tab.STATS -> StatsScreen(statsViewModel)
            }
        }
    }
}
