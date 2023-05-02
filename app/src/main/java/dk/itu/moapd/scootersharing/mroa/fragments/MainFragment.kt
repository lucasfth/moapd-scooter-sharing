package dk.itu.moapd.scootersharing.mroa.fragments

import android.Manifest
import android.content.*
import android.content.Context.BIND_AUTO_CREATE
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import dk.itu.moapd.scootersharing.mroa.PrefSingleton
import dk.itu.moapd.scootersharing.mroa.R
import dk.itu.moapd.scootersharing.mroa.activities.MainActivity
import dk.itu.moapd.scootersharing.mroa.databinding.FragmentMainBinding
import dk.itu.moapd.scootersharing.mroa.services.LocationService
import java.util.*


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

    var mLocationService : LocationService? = null

    var mBound = false

    var scooterRef = MainActivity.database.child("scooters")


    private val mServiceConnection: ServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName, service: IBinder) {
            val binder: LocationService.LocalBinder = service as LocationService.LocalBinder
            mLocationService = binder.getService()
            mBound = true
        }

        override fun onServiceDisconnected(name: ComponentName) {
            mLocationService = null
            mBound = false
        }
    }
    companion object {
        private val TAG = MainFragment::class.qualifiedName
        private const val ALL_PERMISSIONS_RESULT = 1011
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
    ): View? {

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
        }


    }

    private fun requestUserPermissions() {

        // An array with location-aware permissions.
        val permissions: ArrayList<String> = ArrayList()
        permissions.add(Manifest.permission.ACCESS_FINE_LOCATION)
        permissions.add(Manifest.permission.ACCESS_COARSE_LOCATION)

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

        map.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(PrefSingleton.getLat().toDouble(),
            PrefSingleton.getLng().toDouble()
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

        map.setOnMarkerClickListener { marker ->
            val markerName = marker.title
            println("$markerName") // This print the marker that has been clicked
            false
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

