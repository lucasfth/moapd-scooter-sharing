package dk.itu.moapd.scootersharing.mroa

import android.content.Intent
import android.location.Location
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.WindowCompat
import androidx.navigation.fragment.NavHostFragment
import dk.itu.moapd.scootersharing.mroa.databinding.ActivityMainBinding
import dk.itu.moapd.scootersharing.mroa.databinding.FragmentMainBinding

class MainFragment : Fragment() {

    private var _binding: FragmentMainBinding? = null
    private val binding
        get() = checkNotNull(_binding) {
            "Is the view visible?"
        }

    companion object {
        private val TAG = MainFragment::class.qualifiedName
        lateinit var ridesDB : RidesDB
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val navHostFragment = activity?.supportFragmentManager
            ?.findFragmentById(R.id.fragment_container_view) as NavHostFragment
        val navController = navHostFragment.navController
        with (binding) {
            clickButtonStartRide.setOnClickListener {
                navController.navigate(R.id.show_start_ride)
            }

            clickButtonUpdateRide.setOnClickListener{
                navController.navigate(R.id.show_update_ride)
            }

            clickButtonListRides.setOnClickListener{
                val intent = Intent(activity, dk.itu.moapd.scootersharing.mroa.ListRidesActivity::class.java)
                startActivity(intent)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}