package dk.itu.moapd.scootersharing.mroa.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import dk.itu.moapd.scootersharing.mroa.R
import dk.itu.moapd.scootersharing.mroa.databinding.FragmentAdminBinding

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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
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
}