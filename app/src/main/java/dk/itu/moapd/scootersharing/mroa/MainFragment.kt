package dk.itu.moapd.scootersharing.mroa

import android.content.Intent
import android.location.Location
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.WindowCompat
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

        with (binding) {
            clickButtonStartRide.setOnClickListener {
                activity?.supportFragmentManager?.beginTransaction()?.add(R.id.fragment_container_view, StartRideFragment())?.commit()
            }

            clickButtonUpdateRide.setOnClickListener{
                val intent = Intent(activity, dk.itu.moapd.scootersharing.mroa.UpdateRideActivity::class.java)
                startActivity(intent)
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