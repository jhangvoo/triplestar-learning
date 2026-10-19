package com.triplestar.learn

import android.content.Context
import android.content.res.Configuration
import androidx.test.core.app.ApplicationProvider
import com.triplestar.learn.util.LocaleManager
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import java.util.Locale

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class TripleStarAppRobolectricTest {

    @Test
    fun `verify app identity string in English`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val appName = context.getString(R.string.app_name)
        assertEquals("TripleStar Learn", appName)
    }

    @Test
    fun `verify app identity string in Urdu`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val config = Configuration(context.resources.configuration)
        config.setLocale(Locale("ur"))
        val localizedContext = context.createConfigurationContext(config)
        val urduAppName = localizedContext.getString(R.string.app_name)
        assertEquals("ٹرپل اسٹار لرن", urduAppName)
    }

    @Test
    fun `verify LocaleManager persistence`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        LocaleManager.init(context)
        assertEquals("en", LocaleManager.currentLanguage.value)

        LocaleManager.setLanguage(context, LocaleManager.LANG_URDU)
        assertEquals("ur", LocaleManager.currentLanguage.value)

        val prefs = context.getSharedPreferences("triplestar_learn_prefs", Context.MODE_PRIVATE)
        assertEquals("ur", prefs.getString("selected_language", null))

        // Reset to English
        LocaleManager.setLanguage(context, LocaleManager.LANG_ENGLISH)
        assertEquals("en", LocaleManager.currentLanguage.value)
    }

    @Test
    fun `verify application class instance is initialized`() {
        val app = ApplicationProvider.getApplicationContext<TripleStarApplication>()
        assertNotNull(app)
    }
}
