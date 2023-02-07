package com.mystegy.tracker.feature_tracker.presentation.main.main.main_screen.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Backup
import androidx.compose.material.icons.filled.StackedLineChart
import androidx.compose.material.icons.filled.TrackChanges
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import com.mystegy.tracker.feature_tracker.domain.models.Nav
import com.mystegy.tracker.feature_tracker.presentation.main.NavGraphs
import com.mystegy.tracker.feature_tracker.presentation.main.appCurrentDestinationAsState
import com.mystegy.tracker.feature_tracker.presentation.main.destinations.*
import com.mystegy.tracker.feature_tracker.presentation.main.startAppDestination
import com.ramcosta.composedestinations.navigation.navigate
import com.ramcosta.composedestinations.navigation.popBackStack
import com.ramcosta.composedestinations.navigation.popUpTo
import com.ramcosta.composedestinations.utils.isRouteOnBackStack
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun NavDrawer(
    navController: NavController,
    drawerState: DrawerState,
    scope: CoroutineScope
) {
    val currentDestination: Destination = navController.appCurrentDestinationAsState().value
        ?: NavGraphs.root.startAppDestination

    ModalDrawerSheet {
        Text(
            text = "Tracker",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(16.dp)
        )
        Divider()
        Spacer(modifier = Modifier.height(16.dp))
        NavDrawerItem.values().forEach { destination ->
            val isCurrentDestOnBackStack = navController.isRouteOnBackStack(destination.direction)
            NavigationDrawerItem(
                icon = {
                    Icon(
                        destination.icon,
                        contentDescription = destination.label
                    )
                },
                label = {
                    Text(
                        destination.label,
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 1
                    )
                },
                selected = currentDestination == destination.direction,
                onClick = {
                    scope.launch { drawerState.close() }
                    if (isCurrentDestOnBackStack) {
                        navController.popBackStack(destination.direction, false)
                        return@NavigationDrawerItem
                    }
                    navController.navigate(destination.direction) {
                        popUpTo(TrackerScreenDestination) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
            )
        }
    }
}

enum class NavDrawerItem(
    val direction: DirectionDestination,
    val icon: ImageVector,
    val label: String
) {
    Tracker(TrackerScreenDestination, Icons.Default.TrackChanges, "Tracker"),
    Graphs(GraphsScreenDestination, Icons.Default.StackedLineChart, "Graphs"),
    Database(BackupActivityDestination, Icons.Default.Backup, "Import/Export Database")
}

fun updateBarState(
    navBackStackEntry: NavBackStackEntry?
) = when (navBackStackEntry?.destination?.route) {
    TrackerScreenDestination.route -> {
        true to Nav.Tracker
    }
    AddEditItemScreenDestination.route -> {
        true to Nav.Tracker
    }
    GraphsScreenDestination.route -> {
        true to Nav.Graph
    }
    AddEditGraphScreenDestination.route -> {
        true to Nav.Graph
    }
    else -> {
        false to Nav.Else
    }
}
