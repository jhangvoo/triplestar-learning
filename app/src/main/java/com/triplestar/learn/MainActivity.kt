package com.triplestar.learn

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import com.triplestar.learn.navigation.AppNavigation
import com.triplestar.learn.ui.theme.TripleStarTheme
import com.triplestar.learn.util.LocaleManager
import java.util.Locale

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            val currentLang by LocaleManager.currentLanguage.collectAsState()
            val layoutDirection = LocaleManager.getLayoutDirection(currentLang)

            val locale = remember(currentLang) { Locale(currentLang) }
            val localizedConfiguration = remember(currentLang) {
                Configuration(resources.configuration).apply {
                    setLocale(locale)
                    setLayoutDirection(locale)
                }
            }
            val localizedContext = remember(currentLang) {
                object : android.content.ContextWrapper(this@MainActivity) {
                    override fun getResources(): android.content.res.Resources {
                        return baseContext.createConfigurationContext(localizedConfiguration).resources
                    }
                }
            }

            CompositionLocalProvider(
                LocalConfiguration provides localizedConfiguration,
                LocalContext provides localizedContext,
                LocalLayoutDirection provides layoutDirection
            ) {
                TripleStarTheme {
                    Surface(modifier = Modifier.fillMaxSize()) {
                        AppNavigation()
                    }
                }
            }
        }
    }
}
