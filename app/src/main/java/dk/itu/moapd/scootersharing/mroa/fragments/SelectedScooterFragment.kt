package dk.itu.moapd.scootersharing.mroa.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import dk.itu.moapd.scootersharing.mroa.activities.MainActivity
import dk.itu.moapd.scootersharing.mroa.databinding.FragmentSelectedScooterBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [SelectedScooterFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SelectedScooterFragment : Fragment() {

    /**
     * _binding
     */
    private var _binding: FragmentSelectedScooterBinding? = null

    /**
     * Binding
     */
    private val binding
        get() = checkNotNull(_binding) {
            "Is the view visible?"
        }

    private val selectedScooter = MainFragment.selectedScooter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSelectedScooterBinding.inflate(inflater, container, false)

        MainActivity.storage.child("scooters").child("${selectedScooter.name}.png").downloadUrl.addOnSuccessListener {
            Glide.with(binding.scooterImage.context)
                .load(it)
                .centerCrop()
                .into(binding.scooterImage)
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        with(binding) {
            scooterInfoPanel.text = selectedScooter.name
        }
        super.onViewCreated(view, savedInstanceState)
    }
}