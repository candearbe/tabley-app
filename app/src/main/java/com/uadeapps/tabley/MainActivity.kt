package com.uadeapps.tabley

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.uadeapps.tabley.core.navigation.TableyNavigation
import com.uadeapps.tabley.ui.theme.TableyTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            TableyTheme {
                TableyNavigation()
            }
        }
    }
}
