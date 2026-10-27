package com.triplestar.learn

import android.app.Application
import android.util.Log
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.RequestConfiguration
import com.google.firebase.FirebaseApp
import com.triplestar.learn.util.LocaleManager
import com.triplestar.learn.util.TtsManager

class TripleStarApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        Log.i("TripleStarApplication", "TripleStar Application initialized.")

        // Initialize Firebase and add a log statement to verify successful initialization in Logcat
        try {
            if (FirebaseApp.getApps(this).isEmpty()) {
                FirebaseApp.initializeApp(this)
            }
            Log.i("TripleStarApplication", "Firebase initialized successfully.")
        } catch (e: Exception) {
            Log.e("TripleStarApplication", "Failed to initialize Firebase", e)
        }

        // Ensure language preference, math progress, and computer progress are applied at startup
        LocaleManager.init(this)
        TtsManager.init(this)
        com.triplestar.learn.util.MathProgressManager.init(this)
        com.triplestar.learn.util.ComputerProgressManager.init(this)

        // Configure Google Mobile Ads SDK strictly for Child / Families safety (COPPA & Play Families Policy)
        try {
            val requestConfig = RequestConfiguration.Builder()
                .setTagForChildDirectedTreatment(RequestConfiguration.TAG_FOR_CHILD_DIRECTED_TREATMENT_TRUE)
                .setMaxAdContentRating(RequestConfiguration.MAX_AD_CONTENT_RATING_G)
                .build()
            MobileAds.setRequestConfiguration(requestConfig)
            MobileAds.initialize(this) { initStatus ->
                Log.i("TripleStarApplication", "Google Mobile Ads initialized successfully.")
            }
        } catch (e: Exception) {
            Log.w("TripleStarApplication", "Google Mobile Ads initialization deferred: ${e.message}")
        }
    }
}
