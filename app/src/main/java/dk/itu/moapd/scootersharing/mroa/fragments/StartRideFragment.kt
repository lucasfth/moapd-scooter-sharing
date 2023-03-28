package dk.itu.moapd.scootersharing.mroa.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import dk.itu.moapd.scootersharing.mroa.ScooterController
import dk.itu.moapd.scootersharing.mroa.activities.MainActivity
import dk.itu.moapd.scootersharing.mroa.databinding.FragmentStartRideBinding
import dk.itu.moapd.scootersharing.mroa.databinding.InputBoxBinding
import dk.itu.moapd.scootersharing.mroa.models.Scooter

/**
 * Start ride fragment
 *
 * @constructor Create empty Start ride fragment
 */
class StartRideFragment : Fragment() {


    /**
     * _binding
     */
    private var _binding: FragmentStartRideBinding? = null

    /**
     * Binding
     */
    private val binding
        get() = checkNotNull(_binding) {
            "Is the view visible?"
        }

    /**
     * Login input box binding
     */
    private lateinit var loginInputBoxBinding: InputBoxBinding

    /**
     * Controller
     */
    private lateinit var controller: ScooterController
    companion object {
        private val TAG = FragmentStartRideBinding::class.qualifiedName
    }

    /**
     * Scooter name
     */
    private lateinit var scooterName: EditText

    /**
     * Scooter location
     */
    private lateinit var scooterLocation: EditText

    /**
     * Scooter
     */
    private lateinit var scooter: Scooter

    /**
     * On create
     *
     * @param savedInstanceState
     */
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
        _binding = FragmentStartRideBinding.inflate(inflater, container, false)
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
        loginInputBoxBinding = InputBoxBinding.bind(binding.root)

        scooterName = loginInputBoxBinding.editTextName
        scooterLocation = loginInputBoxBinding.editTextLocation
        controller = ScooterController()

        with (binding) {
            clickButtonStartRide.setOnClickListener { checkInputValidity() }
        }
    }

    /**
     * Check input validity
     *
     * Used to ensure that what the user has typed in is correct
     * If not the it will check what error the user has done
     * in the ScooterController::checkInputError
     */
    private fun checkInputValidity() {
        val view: View? = activity?.currentFocus
        with(controller) {
            if (view != null) {
                activity?.hideKeyboard(view)
            }
            if (scooterName.text.isNotEmpty() &&
                scooterLocation.text.isNotEmpty()) {

                scooter = createScooter(scooterName, scooterLocation)
                val auth = MainActivity.auth
                val database = MainActivity.database
                auth.currentUser?.let { user ->
                    val uid = database.child("scooters")
                        .child(user.uid)
                        .push()
                        .key

                    uid?.let {
                        database.child("scooters")
                            .child(user.uid)
                            .child(it)
                            .setValue(scooter)
                    }
                }
                clearInput(scooterName, scooterLocation)

                printMessage(TAG, scooter)
                showSnackMessage(binding.root, scooter.toString())
            } else {
                checkInputError(scooterName, scooterLocation, binding)
            }
        }
    }
}