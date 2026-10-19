package com.triplestar.learn.data.repository

import com.triplestar.learn.data.model.AdPlacementConfig
import com.triplestar.learn.data.model.AdPlacementLocation

class AdRepository {

    /**
     * Supplies fixed ad slot definitions adhering strictly to Google Play Families Policy:
     * - Only fixed bottom banner slots (never popups, interstitials interrupting active exercises, or stacked banners)
     * - Strict COPPA child-directed treatment enabled
     * - Maximum content rating 'G' (General audience / children)
     * - Non-personalized context only
     * - Production Ad Unit ID: ca-app-pub-6342160594726984/4436976945
     * - Development/Test Ad Unit ID: ca-app-pub-3940256099942544/9214589741
     */
    fun getFixedPlacement(location: AdPlacementLocation): AdPlacementConfig {
        return when (location) {
            AdPlacementLocation.DASHBOARD_BOTTOM -> AdPlacementConfig(
                placementId = "fixed_slot_dashboard_footer",
                location = AdPlacementLocation.DASHBOARD_BOTTOM,
                productionAdUnitId = "ca-app-pub-6342160594726984/4436976945",
                testAdUnitId = "ca-app-pub-3940256099942544/9214589741",
                isChildDirected = true,
                maxAdRating = "G",
                isLiveConfigured = true,
                description = "Non-intrusive fixed banner placement at the base of the dashboard."
            )
            AdPlacementLocation.PRACTICE_SUMMARY_BOTTOM -> AdPlacementConfig(
                placementId = "fixed_slot_practice_footer",
                location = AdPlacementLocation.PRACTICE_SUMMARY_BOTTOM,
                productionAdUnitId = "ca-app-pub-6342160594726984/4436976945",
                testAdUnitId = "ca-app-pub-3940256099942544/9214589741",
                isChildDirected = true,
                maxAdRating = "G",
                isLiveConfigured = true,
                description = "Fixed banner placement on practice completion screen."
            )
        }
    }
}
