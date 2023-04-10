package com.mystegy.tracker.feature_tracker.presentation.main.main.main_screen

import android.Manifest
import android.annotation.SuppressLint
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Upload
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.currentBackStackEntryAsState
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi
import com.mystegy.tracker.core.utils.ERROR_MESSAGE
import com.mystegy.tracker.core.utils.csvNameGenerator
import com.mystegy.tracker.feature_tracker.domain.models.AddEditGraphScreenArgs
import com.mystegy.tracker.feature_tracker.domain.models.AddEditGraphScreenArgsParcelable
import com.mystegy.tracker.feature_tracker.domain.models.AddEditTrackerScreenArgs
import com.mystegy.tracker.feature_tracker.domain.models.Nav
import com.mystegy.tracker.feature_tracker.presentation.main.NavGraphs
import com.mystegy.tracker.feature_tracker.presentation.main.destinations.*
import com.mystegy.tracker.feature_tracker.presentation.main.main.main_screen.components.NavDrawer
import com.mystegy.tracker.feature_tracker.presentation.main.main.main_screen.components.updateBarState
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.animations.defaults.RootNavGraphDefaultAnimations
import com.ramcosta.composedestinations.animations.rememberAnimatedNavHostEngine
import com.ramcosta.composedestinations.navigation.navigate
import com.ramcosta.composedestinations.utils.isRouteOnBackStack
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@SuppressLint("Recycle")
@OptIn(
    ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class,
    ExperimentalMaterialNavigationApi::class
)
@Composable
fun MainScreen(
    viewModel: MainScreenViewModel = hiltViewModel()
) {
    val navController = rememberAnimatedNavController()
    val context = LocalContext.current
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val state = updateBarState(navBackStackEntry)
    var import = rememberSaveable { false }
    val uiState = viewModel.uiState.collectAsState()

    val snackbarHostState = remember { SnackbarHostState() }
    LaunchedEffect(Unit) {
        viewModel.isSnackBarShown.collectLatest {
            if (it) {
                val result = snackbarHostState.showSnackbar(
                    message = uiState.value.errorMessage
                )
                when (result) {
                    SnackbarResult.Dismissed -> {
                        viewModel.showSnackBar(false, null)
                    }

                    SnackbarResult.ActionPerformed -> {
                        viewModel.showSnackBar(false, null)
                    }
                }
            }
        }
    }


    val exportCSV = rememberLauncherForActivityResult(
        ActivityResultContracts.CreateDocument("text/csv")
    ) { result ->
        if (result != null) {
            try {
                viewModel.writeToCSV(context.contentResolver.openOutputStream(result)!!)
            } catch (e: Exception) {
                viewModel.showSnackBar(true, e.message ?: ERROR_MESSAGE)
            }
            return@rememberLauncherForActivityResult
        } else {
            viewModel.showSnackBar(true, ERROR_MESSAGE)
        }
    }

    val importCSV =
        rememberLauncherForActivityResult(ActivityResultContracts.OpenDocument()) { result ->
            if (result != null) {
                try {
                    viewModel.readFromCSV(context.contentResolver.openInputStream(result)!!)
                } catch (e: Exception) {
                    viewModel.showSnackBar(true, e.message ?: ERROR_MESSAGE)
                }
                return@rememberLauncherForActivityResult
            } else {
                viewModel.showSnackBar(true, ERROR_MESSAGE)
            }
        }

    val permissionRequestLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            permissions.entries.forEach { _ ->
                if (!import) {
                    exportCSV.launch(csvNameGenerator())
                    return@rememberLauncherForActivityResult
                } else {
                    importCSV.launch(arrayOf("text/csv", "text/*"))
                    return@rememberLauncherForActivityResult
                }
            }
        }

    if (uiState.value.exportDialogVisible) {
        AlertDialog(
            onDismissRequest = {
                viewModel.onEvent(MainUIEvent.ExportDialogVisibility)
                viewModel.onEvent(MainUIEvent.ClearAllSelectedTracker)
            },
            title = { Text(text = "Export") },
            confirmButton = {
                TextButton(onClick = {
                    if (uiState.value.selectedTrackers.isNotEmpty()) {
                        viewModel.onEvent(MainUIEvent.ExportDialogVisibility)
                        import = false
                        permissionRequestLauncher.launch(
                            arrayOf(
                                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                Manifest.permission.READ_EXTERNAL_STORAGE
                            )
                        )
                    } else {
                        viewModel.showSnackBar(true, "Select at least one tracker to continue")
                    }
                }) {
                    Text(text = "Export")
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    viewModel.onEvent(MainUIEvent.ExportDialogVisibility)
                    viewModel.onEvent(MainUIEvent.ClearAllSelectedTracker)
                }) {
                    Text(text = "Cancel")
                }
            },
            icon = {
                Icon(imageVector = Icons.Default.Upload, contentDescription = null)
            },
            text = {
                LazyColumn {
                    item {
                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(text = "Select tracker to be exported")
                                if (uiState.value.trackers.isNotEmpty()) {
                                    Spacer(modifier = Modifier.weight(1f))
                                    TextButton(onClick = {
                                        viewModel.onEvent(MainUIEvent.SelectAllTracker)
                                    }) {
                                        Text(text = "Select All")
                                    }
                                }

                            }

                            Spacer(modifier = Modifier.padding(8.dp))
                            if (uiState.value.trackers.isEmpty()) {
                                Text(text = "No trackers available")
                            }
                        }

                    }
                    items(uiState.value.trackers) { tracker ->
                        val checked = uiState.value.selectedTrackers.contains(tracker)
                        Row(verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp),
                            modifier = Modifier
                                .clickable {
                                    viewModel.onEvent(MainUIEvent.Tracker(tracker))
                                }
                                .fillMaxWidth()) {
                            Checkbox(checked = checked, onCheckedChange = {
                                viewModel.onEvent(MainUIEvent.Tracker(tracker))
                            })
                            Text(text = tracker.title)
                        }
                    }
                }


            }
        )
    }
    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            NavDrawer(navController = navController, drawerState = drawerState, scope = scope)
        },
        gesturesEnabled = state.first && !navController.isRouteOnBackStack(
            TrackerDetailScreenDestination
        )
    ) {
        Scaffold(
            topBar = {
                if (state.first && !navController.isRouteOnBackStack(TrackerDetailScreenDestination) && !navController.isRouteOnBackStack(
                        GroupTrackersScreenDestination
                    )
                ) {
                    TopAppBar(
                        title = { Text(text = state.second.label) },
                        navigationIcon = {
                            IconButton(onClick = { scope.launch { drawerState.open() } }) {
                                Icon(
                                    imageVector = Icons.Default.Menu,
                                    contentDescription = null
                                )
                            }
                        },
                        actions = {
                            when (state.second) {
                                Nav.Tracker -> {
                                    IconButton(onClick = {
                                        import = true
                                        permissionRequestLauncher.launch(
                                            arrayOf(
                                                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                                Manifest.permission.READ_EXTERNAL_STORAGE
                                            )
                                        )
                                    }) {
                                        Icon(
                                            imageVector = Icons.Default.Download,
                                            contentDescription = null
                                        )
                                    }
                                    IconButton(onClick = {
                                        viewModel.onEvent(MainUIEvent.ExportDialogVisibility)
                                    }) {
                                        Icon(
                                            imageVector = Icons.Default.Upload,
                                            contentDescription = null
                                        )
                                    }
                                    IconButton(onClick = {
                                        navController.navigate(
                                            AddEditTrackerScreenDestination(
                                                AddEditTrackerScreenArgs(
                                                    group = ""
                                                )
                                            )
                                        )
                                    }) {
                                        Icon(
                                            imageVector = Icons.Default.Add,
                                            contentDescription = null
                                        )
                                    }
                                }

                                Nav.Graph -> {
                                    IconButton(onClick = {
                                        navController.navigate(
                                            AddEditGraphScreenDestination(
                                                AddEditGraphScreenArgsParcelable(
                                                    AddEditGraphScreenArgs(
                                                        graphID = "",
                                                        trackers = listOf(),
                                                        edit = false,
                                                        title = ""
                                                    )
                                                )
                                            )
                                        )
                                    }) {
                                        Icon(
                                            imageVector = Icons.Default.Add,
                                            contentDescription = null
                                        )
                                    }
                                }

                                Nav.Else -> {

                                }
                            }
                        }
                    )
                }
            },
            snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
        ) { innerPadding ->
            DestinationsNavHost(
                navGraph = NavGraphs.root,
                engine = rememberAnimatedNavHostEngine(rootDefaultAnimations =
                RootNavGraphDefaultAnimations(
                    enterTransition = {
                        scaleIn(
                            tween(300),
                            initialScale = 0.75f
                        ) + fadeIn(tween(300))
                    },
                    exitTransition = {
                        scaleOut(tween(300), targetScale = 0.5f) + fadeOut(
                            tween(
                                300
                            )
                        )
                    },
                    popEnterTransition = {
                        scaleIn(tween(300), initialScale = 0.75f) + fadeIn(
                            tween(300)
                        )
                    },
                    popExitTransition = {
                        scaleOut(tween(300), targetScale = 0.5f) + fadeOut(
                            tween(300)
                        )
                    }
                )
                ),
                modifier = Modifier.padding(innerPadding),
                navController = navController,
                startRoute = TrackerScreenDestination
            )
        }
    }
}