package com.kelompok3.profileapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import com.kelompok3.profileapp.ui.components.PaperPlaneIntroScreen
import com.kelompok3.profileapp.ui.screens.HomeScreen
import com.kelompok3.profileapp.ui.theme.Kelompok3Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Kelompok3Theme {
                var showIntro by rememberSaveable { mutableStateOf(true) }

                Crossfade(
                    targetState = showIntro,
                    animationSpec = tween(350),
                    label = "introTransition"
                ) { isIntroActive ->
                    if (isIntroActive) {
                        PaperPlaneIntroScreen(
                            onFinish = { showIntro = false }
                        )
                    } else {
                        HomeScreen(
                            onReplayIntro = { showIntro = true }
                        )
                    }
                }
            }
        }
    }
}
