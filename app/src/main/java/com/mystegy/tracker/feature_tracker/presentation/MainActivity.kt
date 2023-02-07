package com.mystegy.tracker.feature_tracker.presentation

import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.mystegy.tracker.feature_tracker.presentation.main.main.main_screen.MainScreen
import com.mystegy.tracker.feature_tracker.presentation.ui.theme.TrackerTheme
import dagger.hilt.android.AndroidEntryPoint

@Suppress("DEPRECATION")
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        setContent {
            TrackerTheme {
                MainScreen()
            }
        }
    }
}