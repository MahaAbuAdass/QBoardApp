package com.example.slaughterhousescreen

import android.app.Activity
import android.util.DisplayMetrics
import android.view.WindowManager

object DisplayMetricsHelper {

    // Target screen width and height in pixels (e.g., 1920x1080 for Full HD)
    private const val TARGET_WIDTH_PX = 960
    private const val TARGET_HEIGHT_PX = 540

    // Target screen density (e.g., 320 for xhdpi)
    private const val TARGET_DENSITY_DPI = 216

    // Adjust the display metrics to enforce the target size and density
    fun enforceDisplayMetrics(activity: Activity) {

        val displayMetrics = DisplayMetrics()
        activity.windowManager.defaultDisplay.getMetrics(displayMetrics)

        // Calculate scaling factors
        val widthScale = TARGET_WIDTH_PX.toFloat() / displayMetrics.widthPixels
        val heightScale = TARGET_HEIGHT_PX.toFloat() / displayMetrics.heightPixels
        val densityScale = TARGET_DENSITY_DPI.toFloat() / displayMetrics.densityDpi

        // Apply scaling factors to the display metrics
        displayMetrics.density = displayMetrics.density * densityScale
        displayMetrics.scaledDensity = displayMetrics.scaledDensity * densityScale
        displayMetrics.densityDpi = TARGET_DENSITY_DPI
        displayMetrics.widthPixels = TARGET_WIDTH_PX
        displayMetrics.heightPixels = TARGET_HEIGHT_PX

        // Update the activity's resources with the new display metrics
        val resources = activity.resources
        resources.displayMetrics.setTo(displayMetrics)
    }


    // Reset the display metrics to the device's default values for Smart TV
    fun resetDisplayMetrics(activity: Activity) {
        val displayMetrics = DisplayMetrics()
        activity.windowManager.defaultDisplay.getMetrics(displayMetrics)

        // Update the activity's resources with the default display metrics
        val resources = activity.resources
        resources.displayMetrics.setTo(displayMetrics)
    }
}