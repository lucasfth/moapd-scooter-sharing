package dk.itu.moapd.scootersharing.mroa.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.NavHostFragment
import dk.itu.moapd.scootersharing.mroa.R
import dk.itu.moapd.scootersharing.mroa.databinding.FragmentAdminBinding
import dk.itu.moapd.scootersharing.mroa.databinding.FragmentMainBinding

/**
 * A simple [Fragment] subclass.
 * Use the [AdminFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AdminFragment : Fragment() {

    /**
     * _binding
     */
    private var _binding: FragmentAdminBinding? = null

    /**
     * Binding
     */
    private val binding
        get() = checkNotNull(_binding) {
            "Is the view visible?"
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentAdminBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val navHostFragment = activity?.supportFragmentManager
            ?.findFragmentById(R.id.fragment_container_view) as NavHostFragment
        val navController = navHostFragment.navController

        with(binding) {
            clickButtonStartRide.setOnClickListener {
                navController.navigate(R.id.show_start_ride)
            }
        }
    }

    companion object {
    }
}