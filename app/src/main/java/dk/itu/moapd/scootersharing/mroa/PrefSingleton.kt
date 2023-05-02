package dk.itu.moapd.scootersharing.mroa

import android.content.Context
import android.content.SharedPreferences

object PrefSingleton {
    private const val PREFS_NAME = "MyPrefsFile"

    private lateinit var prefs: SharedPreferences

    fun init(context: Context) {
        prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    fun getLat(): Double {
        return prefs.getFloat("lat", 0f).toDouble()
    }

    fun setLat(lat: Double) {
        prefs.edit().putFloat("lat", lat.toFloat()).apply()
    }

    fun getLng(): Double {
        return prefs.getFloat("lng", 0f).toDouble()
    }

    fun setLng(lng: Double) {
        prefs.edit().putFloat("lng", lng.toFloat()).apply()
    }
}