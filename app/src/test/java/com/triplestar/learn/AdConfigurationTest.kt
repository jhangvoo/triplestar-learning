package com.triplestar.learn

import com.triplestar.learn.data.model.AdPlacementLocation
import com.triplestar.learn.data.repository.AdRepository
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class AdConfigurationTest {

    private val adRepository = AdRepository()

    @Test
    fun `dashboard bottom placement complies with Families Policy`() {
        val config = adRepository.getFixedPlacement(AdPlacementLocation.DASHBOARD_BOTTOM)
        assertEquals("fixed_slot_dashboard_footer", config.placementId)
        assertEquals(AdPlacementLocation.DASHBOARD_BOTTOM, config.location)
        assertTrue("Must be child directed", config.isChildDirected)
        assertEquals("G", config.maxAdRating)
        assertEquals("ca-app-pub-6342160594726984/4436976945", config.productionAdUnitId)
        assertEquals("ca-app-pub-3940256099942544/9214589741", config.testAdUnitId)
    }

    @Test
    fun `practice summary bottom placement complies with Families Policy`() {
        val config = adRepository.getFixedPlacement(AdPlacementLocation.PRACTICE_SUMMARY_BOTTOM)
        assertEquals("fixed_slot_practice_footer", config.placementId)
        assertEquals(AdPlacementLocation.PRACTICE_SUMMARY_BOTTOM, config.location)
        assertTrue("Must be child directed", config.isChildDirected)
        assertEquals("G", config.maxAdRating)
        assertEquals("ca-app-pub-6342160594726984/4436976945", config.productionAdUnitId)
        assertEquals("ca-app-pub-3940256099942544/9214589741", config.testAdUnitId)
    }

    @Test
    fun `ad unit id resolves to official test id in debug and production id in release`() {
        val config = adRepository.getFixedPlacement(AdPlacementLocation.DASHBOARD_BOTTOM)
        assertEquals(
            "ca-app-pub-3940256099942544/9214589741",
            config.resolveAdUnitId(isDebug = true)
        )
        assertEquals(
            "ca-app-pub-6342160594726984/4436976945",
            config.resolveAdUnitId(isDebug = false)
        )
    }
}
