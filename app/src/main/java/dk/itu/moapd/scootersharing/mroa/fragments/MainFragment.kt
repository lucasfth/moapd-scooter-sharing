package dk.itu.moapd.scootersharing.mroa.fragments

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.DrawableTransformation
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import dk.itu.moapd.scootersharing.mroa.R
import dk.itu.moapd.scootersharing.mroa.models.Scooter
import dk.itu.moapd.scootersharing.mroa.ScooterController
import dk.itu.moapd.scootersharing.mroa.activities.LoginActivity
import dk.itu.moapd.scootersharing.mroa.activities.MainActivity
import dk.itu.moapd.scootersharing.mroa.adapters.FirebaseAdapter
import dk.itu.moapd.scootersharing.mroa.databinding.FragmentMainBinding
import dk.itu.moapd.scootersharing.mroa.interfaces.ItemClickListener
import java.util.*
import kotlin.collections.ArrayList
import com.google.android.gms.location.*
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment

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

    companion object {
        private val TAG = MainFragment::class.qualifiedName
        private const val ALL_PERMISSIONS_RESULT = 1011
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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
        // Inflate the layout for this fragment
        _binding = FragmentMainBinding.inflate(inflater, container, false)
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


        val permissions: ArrayList<String> = ArrayList()
        permissions.add(Manifest.permission.ACCESS_FINE_LOCATION)
        permissions.add(Manifest.permission.ACCESS_COARSE_LOCATION)

        val permissionsToRequest = permissionsToRequest(permissions)

        if (permissionsToRequest.size > 0) {
            requestPermissions(permissionsToRequest.toTypedArray(), ALL_PERMISSIONS_RESULT)
        }



        with (binding) {
            clickButtonSettings.setOnClickListener {
                navController.navigate(R.id.show_settings)
            }
        }
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
            requireContext(), Manifest.permission.ACCESS_FINE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(
                    requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED

    override fun onMapReady(googleMap: GoogleMap) {
        if (checkPermission())
            return

        val mapFragment = requireActivity().supportFragmentManager
            .findFragmentById(R.id.map_fragment) as SupportMapFragment
        mapFragment.getMapAsync(this)
        // Show the current device's location as a blue dot.
        googleMap.isMyLocationEnabled = true
    }

    /**
     * On destroy view
     *
     */
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

