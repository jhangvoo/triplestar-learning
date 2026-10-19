package com.triplestar.learn.data.model

import com.triplestar.learn.BuildConfig

enum class AdPlacementLocation {
    DASHBOARD_BOTTOM,
    PRACTICE_SUMMARY_BOTTOM
}

data class AdPlacementConfig(
    val placementId: String,
    val location: AdPlacementLocation,
    val productionAdUnitId: String = "ca-app-pub-6342160594726984/4436976945",
    val testAdUnitId: String = "ca-app-pub-3940256099942544/9214589741",
    val isChildDirected: Boolean = true, // Mandatory COPPA/Play Families flag
    val maxAdRating: String = "G",       // Families Policy G rating
    val isLiveConfigured: Boolean = true,
    val description: String = ""
) {
    /**
     * Resolves the safe ad unit ID:
     * In development/testing (DEBUG builds), returns Google's official banner test ID ("ca-app-pub-3940256099942544/9214589741")
     * In release/production, returns the TripleStar Learn production banner ad unit ID ("ca-app-pub-6342160594726984/4436976945")
     */
    fun resolveAdUnitId(isDebug: Boolean = BuildConfig.DEBUG): String {
        return if (isDebug) testAdUnitId else productionAdUnitId
    }
}
