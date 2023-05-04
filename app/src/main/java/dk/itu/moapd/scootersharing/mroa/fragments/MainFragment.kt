package dk.itu.moapd.scootersharing.mroa.fragments

import android.Manifest
import android.content.*
import android.content.Context.BIND_AUTO_CREATE
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.Polyline
import com.google.android.gms.maps.model.PolylineOptions
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.maps.GeoApiContext
import com.google.maps.PendingResult
import com.google.maps.model.DirectionsResult
import com.google.maps.model.TravelMode
import dk.itu.moapd.scootersharing.mroa.PrefSingleton
import dk.itu.moapd.scootersharing.mroa.R
import dk.itu.moapd.scootersharing.mroa.activities.MainActivity
import dk.itu.moapd.scootersharing.mroa.databinding.FragmentMainBinding
import dk.itu.moapd.scootersharing.mroa.models.Scooter
import dk.itu.moapd.scootersharing.mroa.services.LocationService
import java.util.*
import com.google.maps.DirectionsApiRequest as DirectionsApiRequest


/**
 * Main fragment
 *
 * @constructor Create empty Main fragment
 */
class MainFragment : Fragment(), OnMapReadyCallback {

    /**
     * _binding
     */
    private var _binding: FragmentMainBinding? = null

    /**
     * Binding
     */
    private val binding
        get() = checkNotNull(_binding) {
            "Is the view visible?"
        }

    var locationService : LocationService? = null

    var mBound = false

    private var scooterRef = MainActivity.database.child("scooters")

    private var polyline: Polyline? = null

    private var selectedMarker: Marker? = null


    private val mServiceConnection: ServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName, service: IBinder) {
            val binder: LocationService.LocalBinder = service as LocationService.LocalBinder
            locationService = binder.getService()
            mBound = true
        }

        override fun onServiceDisconnected(name: ComponentName) {
            locationService = null
            mBound = false
        }
    }
    companion object {
        private val TAG = MainFragment::class.qualifiedName
        var selectedScooter = Scooter()
    }

    override fun onResume() {
        super.onResume()
        Intent(requireContext(), LocationService::class.java).also {
            requireActivity().bindService(it, mServiceConnection, BIND_AUTO_CREATE)
        }
    }

    /**
     * On create view
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentMainBinding.inflate(inflater, container, false)

        val mapFragment = childFragmentManager
            .findFragmentById(R.id.map_fragment) as SupportMapFragment
        mapFragment.getMapAsync(this)

        requestUserPermissions()

        return binding.root
    }

    /**
     * On view created
     *
     * @param view
     * @param savedInstanceState
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val navHostFragment = activity?.supportFragmentManager
            ?.findFragmentById(R.id.fragment_container_view) as NavHostFragment

        val navController = navHostFragment.navController

        print("\n It is from the motherfucking fragment ----------- ${PrefSingleton.getLat()} ${PrefSingleton.getLng()} -----------\n")

        with (binding) {
            clickButtonSettings.setOnClickListener {
                navController.navigate(R.id.show_settings)
            }

            clickButtonSelectRide.setOnClickListener {
                if (selectedScooter.name != null) {
                    navController.navigate(R.id.show_selected_ride)
                } else {
                    Snackbar.make(root, "No scooter selected", Snackbar.LENGTH_SHORT).show()
                }
            }
        }


    }

    private fun requestUserPermissions() {

        // An array with location-aware permissions.
        val permissions: ArrayList<String> = ArrayList()
        permissions.add(Manifest.permission.ACCESS_FINE_LOCATION)
        permissions.add(Manifest.permission.ACCESS_COARSE_LOCATION)
        permissions.add(Manifest.permission.CAMERA)

        // Check which permissions is needed to ask to the user.
        val permissionsToRequest = permissionsToRequest(permissions)

        // Show the permissions dialogs to the user.
        if (permissionsToRequest.size > 0)
            requestPermissions(
                permissionsToRequest.toTypedArray(),
                0
            )
    }

    private fun permissionsToRequest(permissions: ArrayList<String>) : ArrayList<String> {
        val result: ArrayList<String> = ArrayList()
        for (permission in permissions) {
            if (requireActivity().checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED)
                result.add(permission)
        }
        return result
    }

    private fun checkPermission() =
        ActivityCompat.checkSelfPermission(
            requireActivity(), Manifest.permission.ACCESS_FINE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(
                    requireActivity(), Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED

    override fun onMapReady(map: GoogleMap) {
        if (checkPermission())
            return

        // Show the current device's location as a blue dot.
        map.isMyLocationEnabled = true

        map.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(
            PrefSingleton.getLat(),
            PrefSingleton.getLng()
        ), 13f))

        scooterRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot){
                for (ds in dataSnapshot.children) {
                    val name = ds.child("name").value
                    val lat = ds.child("lat").value
                    val lng = ds.child("lng").value
                    val position = LatLng(lat as Double, lng as Double)
                    map.addMarker(
                        MarkerOptions()
                            .title(name as String?)
                            .position(position)
                    )
                }
            }
            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException())
            }
        })

        val geoApiContext = GeoApiContext.Builder()
            .apiKey(getString(R.string.google_api_key))
            .build()



        map.setOnMarkerClickListener { marker ->
            val markerName = marker.title
            if (markerName != null && marker != selectedMarker) {
                polyline?.remove()
                selectedMarker = marker
                scooterSelected(markerName, geoApiContext, map)
            }
            false
        }


    }



    private fun scooterSelected(scooterName: String, geoApiContext: GeoApiContext, map: GoogleMap) {
        scooterRef.child(scooterName).get().addOnSuccessListener { ds ->
            println(ds.child("name"))
            with(selectedScooter) {
                name = ds.child("name").value as String?
                lat = ds.child("lat").value as Double?
                lng = ds.child("lng").value as Double?
                timestamp = ds.child("timestamp").value as Long?
                val directionsApi = DirectionsApiRequest(geoApiContext)
                    .mode(TravelMode.WALKING)
                    .origin(com.google.maps.model.LatLng(PrefSingleton.getLat(), PrefSingleton.getLng()))
                    .destination(com.google.maps.model.LatLng(lat!!, lng!!))

                directionsApi.setCallback(object : PendingResult.Callback<DirectionsResult> {
                    override fun onResult(result: DirectionsResult?) {
                        val handler = Handler(Looper.getMainLooper())
                        handler.post {
                            val route = result?.routes?.get(0)
                            val polylineOptions = PolylineOptions()
                            println("Beginning to load polyline")
                            for (position in route?.overviewPolyline?.decodePath()!!) {
                                polylineOptions.add(LatLng(position.lat, position.lng))
                            }
                            polylineOptions.color(Color.BLUE).width(10f)
                            polyline = map.addPolyline(polylineOptions)
                        }
                    }

                    override fun onFailure(e: Throwable?) {
                        if (e != null) {
                            Log.e(e.message, "Failed to draw route")
                        }
                    }
                })
            }
        }
    }

    /**
     * On destroy view
     *
     */
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        requireActivity().unbindService(mServiceConnection)
        mBound = false
    }
}

