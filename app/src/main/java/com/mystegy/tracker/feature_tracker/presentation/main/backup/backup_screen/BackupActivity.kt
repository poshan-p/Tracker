package com.mystegy.tracker.feature_tracker.presentation.main.backup.backup_screen

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mystegy.tracker.feature_tracker.data.local.TrackerDatabase
import com.mystegy.tracker.feature_tracker.presentation.MainActivity
import com.mystegy.tracker.feature_tracker.presentation.ui.theme.TrackerTheme
import com.ramcosta.composedestinations.annotation.ActivityDestination
import dagger.hilt.android.AndroidEntryPoint
import de.raphaelebner.roomdatabasebackup.core.RoomBackup
import javax.inject.Inject

@AndroidEntryPoint
@ActivityDestination
class BackupActivity : ComponentActivity()  {
    @Inject
    lateinit var database: TrackerDatabase
    val backup = RoomBackup(this)

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TrackerTheme {
                Scaffold(
                    topBar = {
                        TopAppBar(
                            title = {
                                Text(text = "Import/Export Database")
                            },
                            navigationIcon = {
                                IconButton(onClick = {finish()}) {
                                    Icon(
                                        imageVector = Icons.Default.ArrowBack,
                                        contentDescription = null
                                    )
                                }
                            }
                        )
                    }
                ) { paddingValues ->
                    Column(modifier = Modifier
                        .padding(paddingValues)
                        .fillMaxSize()
                        .verticalScroll(
                            rememberScrollState()
                        ), verticalArrangement = Arrangement.SpaceEvenly) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = "" +
                                        "Backup saves all of your " +
                                        "app data including all your tracked data." +
                                        " You should back up your data to an " +
                                        "external device or cloud storage regularly in case your device " +
                                        "gets lost or broken."
                            )
                            Button(onClick = {
                                backup.database(database)
                                    .backupLocation(RoomBackup.BACKUP_FILE_LOCATION_CUSTOM_DIALOG)
                                    .onCompleteListener { success, message, exitCode ->
                                        if (success) {
                                            Toast.makeText(
                                                this@BackupActivity,
                                                "Data backed up",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        } else {
                                            Toast.makeText(
                                                this@BackupActivity,
                                                message,
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                    }
                                    .backup()
                            },
                             modifier = Modifier
                                 .fillMaxWidth()
                                 .padding(vertical = 32.dp)) {
                                Text(text = "Export")
                            }
                        }
                        Divider(color = MaterialTheme.colorScheme.primary)
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = "WARNING: Restoring from a backup will " +
                                        "erase all data and setup currently in the app. " +
                                        "Tracker will restart after the restore is c" +
                                        "omplete."
                            )
                            Button(onClick = {
                                backup.database(database)
                                    .backupLocation(RoomBackup.BACKUP_FILE_LOCATION_CUSTOM_DIALOG)
                                    .onCompleteListener { success, message, exitCode ->
                                        if (success) {
                                            Toast.makeText(
                                                this@BackupActivity,
                                                "Data restored",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                            backup.restartApp(Intent(this@BackupActivity, MainActivity::class.java))
                                        } else {
                                            Toast.makeText(
                                                this@BackupActivity,
                                                message,
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                    }
                                    .restore()
                            },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 32.dp)) {
                                Text(text = "Import")
                            }
                        }


                    }
                }
            }
        }
    }
}