package dk.itu.moapd.scootersharing.mroa.services

import android.Manifest
import android.app.Service
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Binder
import android.os.IBinder
import android.os.Looper
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.*
import dk.itu.moapd.scootersharing.mroa.PrefSingleton


class LocationService : Service() {

    private val binder: IBinder = LocalBinder()

    /**
     * Provides access to the Fused Location Provider API.
     */
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    /**
     * Callback for changes in location.
     */
    private lateinit var locationCallback: LocationCallback


    override fun onCreate() {
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)



        startLocationAware()
    }

    override fun onBind(intent: Intent?): IBinder {
        subscribeToLocationUpdates()
        return binder
    }

    override fun onUnbind(intent: Intent?): Boolean {
        unsubscribeToLocationUpdates()
        return super.onUnbind(intent)
    }

    private fun startLocationAware() {
        // Initialize the `LocationCallback`.
        locationCallback = object : LocationCallback() {

            /**
             * This method will be executed when `FusedLocationProviderClient` has a new location.
             *
             * @param locationResult The last known location.
             */
            override fun onLocationResult(locationResult: LocationResult) {
                super.onLocationResult(locationResult)

                // Updates the user interface components with GPS data location.
                locationResult.lastLocation?.let { location ->
                    sendLocationBroadcast(location)
                }
            }
        }
    }

    private fun sendLocationBroadcast(location: Location) {
        PrefSingleton.setLat(location.latitude)
        PrefSingleton.setLng(location.longitude)
        PrefSingleton.setSpeed(location.speed*3.6f)
    }

    /**
     * Subscribes this application to get the location changes via the `locationCallback()`.
     */
    private fun subscribeToLocationUpdates() {

        // Check if the user allows the application to access the location-aware resources.
        if (checkPermission())
            return

        // Sets the accuracy and desired interval for active location updates.
        val locationRequest = LocationRequest
            .Builder(Priority.PRIORITY_HIGH_ACCURACY, 5)
            .build()

        // Subscribe to location changes.
        fusedLocationProviderClient.requestLocationUpdates(
            locationRequest, locationCallback, Looper.getMainLooper()
        )
    }

    /**
     * Unsubscribes this application of getting the location changes from  the `locationCallback()`.
     */
    private fun unsubscribeToLocationUpdates() {
        // Unsubscribe to location changes.
        fusedLocationProviderClient
            .removeLocationUpdates(locationCallback)
    }

    private fun checkPermission() =
        ActivityCompat.checkSelfPermission(
            this, Manifest.permission.ACCESS_FINE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(
                    this, Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED


    /**
     * Class used for the client Binder.  Since this service runs in the same process as its
     * clients, we don't need to deal with IPC.
     */
    inner class LocalBinder : Binder() {
        fun getService(): LocationService {
            return this@LocationService
        }
    }
}
