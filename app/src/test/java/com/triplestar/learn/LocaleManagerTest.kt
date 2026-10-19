package com.triplestar.learn

import androidx.compose.ui.unit.LayoutDirection
import com.triplestar.learn.util.LocaleManager
import org.junit.Assert.assertEquals
import org.junit.Test

class LocaleManagerTest {

    @Test
    fun `urdu layout direction is RTL`() {
        val direction = LocaleManager.getLayoutDirection(LocaleManager.LANG_URDU)
        assertEquals(LayoutDirection.Rtl, direction)
    }

    @Test
    fun `english layout direction is LTR`() {
        val direction = LocaleManager.getLayoutDirection(LocaleManager.LANG_ENGLISH)
        assertEquals(LayoutDirection.Ltr, direction)
    }
}
