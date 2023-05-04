package dk.itu.moapd.scootersharing.mroa.fragments

import android.content.*
import android.graphics.Bitmap
import android.os.Bundle
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import com.google.android.material.snackbar.Snackbar
import dk.itu.moapd.scootersharing.mroa.PrefSingleton
import dk.itu.moapd.scootersharing.mroa.R
import dk.itu.moapd.scootersharing.mroa.ScooterController
import dk.itu.moapd.scootersharing.mroa.activities.MainActivity
import dk.itu.moapd.scootersharing.mroa.databinding.FragmentRideBinding
import dk.itu.moapd.scootersharing.mroa.models.Receipt
import dk.itu.moapd.scootersharing.mroa.services.LocationService
import java.io.ByteArrayOutputStream
import java.util.*

class RideFragment : Fragment() {

    var locationService : LocationService? = null

    var maxSpeed = 0f

    var mBound = false

    private val REQUEST_IMAGE_CAPTURE = 1

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

    /**
     * _binding
     */
    private lateinit var _binding: FragmentRideBinding

    /**
     * Binding
     */
    private val binding
        get() = checkNotNull(_binding) {
            "Is the view visible?"
        }

    private var seconds = 0

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putInt("seconds", seconds)
        outState.putFloat("maxSpeed", maxSpeed)
        super.onSaveInstanceState(outState)
    }

    override fun onResume() {
        Intent(requireContext(), LocationService::class.java).also {
            requireActivity().bindService(it, mServiceConnection, Context.BIND_AUTO_CREATE)
        }
        super.onResume()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRideBinding.inflate(inflater, container, false)
        runTimer()
        if(savedInstanceState != null) {
            seconds = savedInstanceState.getInt("seconds")
            maxSpeed = savedInstanceState.getFloat("maxSpeed")
        }


        binding.endRide.setOnClickListener {
            val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            try {
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
            } catch (e: ActivityNotFoundException) {
                // display error state to the user
            }
        }
        val rentingScooter = "Currently renting scooter ${MainFragment.selectedScooter.name}"
        binding.rentInfo.text = rentingScooter
        return binding.root
    }

    private fun runTimer() {
        val timeTextView = binding.rentTimer

        val handler = Handler(Looper.getMainLooper())

        handler.post(object : Runnable {
            override fun run() {
                val hours = seconds / 3600
                val minutes = (seconds % 3600) / 60
                val secs = seconds % 60
                val time: String = java.lang.String
                    .format(
                        Locale.getDefault(),
                        "%d:%02d:%02d", hours,
                        minutes, secs
                    )

                timeTextView.text = time

                val newPrice = minutes*5
                val priceString = "$newPrice DKK"

                binding.rentPrice.text = priceString
                val currentSpeed = PrefSingleton.getSpeed()

                if(currentSpeed > maxSpeed) {
                    maxSpeed = currentSpeed
                }

                val speedString = "${PrefSingleton.getSpeed()} km/h"
                val maxSpeedString = "Max speed: $maxSpeed km/t"

                binding.currentSpeed.text = speedString
                binding.maxSpeed.text = maxSpeedString

                seconds++
                handler.postDelayed(this ,1000)
            }
        })
    }



    override fun onDestroyView() {
        seconds = 0
        requireActivity().unbindService(mServiceConnection)
        mBound = false
        super.onDestroyView()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_CAPTURE) {
            val photo = data?.extras?.get("data") as Bitmap
            val storage = MainActivity.storage.child("scooters").child("${MainFragment.selectedScooter.name}.png")
            val baos = ByteArrayOutputStream()
            photo.compress(Bitmap.CompressFormat.PNG, 100, baos)
            val newData = baos.toByteArray()

            storage.putBytes(newData)
                .addOnSuccessListener {
                    val receipt = Receipt(MainFragment.selectedScooter.name!!, System.currentTimeMillis(), ((seconds % 3600) / 60)*5, maxSpeed)

                    val auth = MainActivity.auth
                    val database = MainActivity.database
                    auth.currentUser?.let {user ->
                        val uid = database.child("receipts")
                            .child(user.uid)
                            .push()
                            .key

                        uid?.let {
                            database.child("receipts")
                                .child(user.uid)
                                .child(it)
                                .setValue(receipt)
                        }
                    }
                    val controller = ScooterController()
                    val scooter = controller.createScooter(MainFragment.selectedScooter.name!!)

                    scooter.name?.let {
                        database.child("scooters")
                            .child(it)
                            .setValue(scooter)
                    }


                    val navHostFragment = activity?.supportFragmentManager
                        ?.findFragmentById(R.id.fragment_container_view) as NavHostFragment

                    val navController = navHostFragment.navController
                    navController.popBackStack(R.id.mainFragment, false)
                }
                .addOnFailureListener {
                    Snackbar.make(binding.root, "Try again", Snackbar.LENGTH_SHORT).show()
                }
        }
    }
}