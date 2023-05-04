package dk.itu.moapd.scootersharing.mroa.services

import dk.itu.moapd.scootersharing.mroa.PrefSingleton
import android.Manifest
import android.app.*
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.*
import android.provider.ContactsContract.Directory.PACKAGE_NAME
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.*
import android.os.IBinder


class LocationService : Service() {
    private val PACKAGE_NAME =
        "dk.itu.moapd.scootersharing.mroa.services"

    private val binder: IBinder = LocalBinder()

    /**
     * Contains parameters used by [com.google.android.gms.location.FusedLocationProviderApi].
     */
    private lateinit var locationRequest: LocationRequest

    /**
     * Provides access to the Fused Location Provider API.
     */
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    /**
     * Callback for changes in location.
     */
    private lateinit var locationCallback: LocationCallback

    private lateinit var serviceHandler: Handler

    /**
     * The current location.
     */
    private lateinit var location: Location

    companion object {
        val EXTRA_LOCATION = "$PACKAGE_NAME.location"
    }

    override fun onCreate() {
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)



        startLocationAware()
    }

    override fun onBind(intent: Intent?): IBinder? {
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
        println(location.toString())
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
