import android.content.Context
import android.content.SharedPreferences

object PrefSingleton {
    private const val PREFS_NAME = "MyPrefsFile"

    private lateinit var prefs: SharedPreferences

    fun getInstance(context: Context): PrefSingleton {
        prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return this
    }

    fun getLat(): Float {
        return prefs.getFloat("lat", 0f)
    }

    fun setLat(lat: Double) {
        prefs.edit().putFloat("lat", lat.toFloat()).apply()
    }

    fun getLng(): Float {
        return prefs.getFloat("lng", 0f)
    }

    fun setLng(lng: Double) {
        prefs.edit().putFloat("lng", lng.toFloat()).apply()
    }
}