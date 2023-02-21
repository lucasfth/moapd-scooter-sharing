package dk.itu.moapd.scootersharing.mroa

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.core.view.WindowCompat
import dk.itu.moapd.scootersharing.mroa.databinding.ActivityStartRideBinding
import dk.itu.moapd.scootersharing.mroa.databinding.InputBoxBinding

/**
 * Start ride activity
 *
 * Activity for starting a new ride.
 *
 * @constructor Create empty Start ride activity
 */
class StartRideActivity : AppCompatActivity() {

    /**
     * Main binding
     */
    private lateinit var mainBinding: ActivityStartRideBinding

    /**
     * Login input box binding
     */
    private lateinit var loginInputBoxBinding: InputBoxBinding

    /**
     * Controller
     */
    private lateinit var controller: ScooterController
    companion object {
        private val TAG = StartRideActivity::class.qualifiedName
        lateinit var ridesDB : RidesDB
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
        WindowCompat.setDecorFitsSystemWindows(window, false)
        super.onCreate(savedInstanceState)
        // Singleton to share an object between the app activities .
        ridesDB = RidesDB.get (this)
        setContentView(R.layout.activity_main)

        mainBinding = ActivityStartRideBinding.inflate(layoutInflater)
        loginInputBoxBinding = InputBoxBinding.bind(mainBinding.root)

        scooterName = loginInputBoxBinding.editTextName
        scooterLocation = loginInputBoxBinding.editTextLocation
        controller = ScooterController()

        with (mainBinding) {
            clickButtonStartRide.setOnClickListener { checkInputValidity() }
        }

        setContentView(mainBinding.root)
    }

    /**
     * Check input validity
     *
     * Checks if input is valid or not.
     * If valid then start ride else figure out what error occured.
     */
    private fun checkInputValidity() {
        val view: View? = this.currentFocus
        with(controller) {
            if (view != null) {
                hideKeyboard(view)
            }
            if (scooterName.text.isNotEmpty() &&
                scooterLocation.text.isNotEmpty()) {

                scooter = createScooter(scooterName, scooterLocation)

                clearInput(scooterName, scooterLocation)

                printMessage(TAG, scooter)
                showSnackMessage(mainBinding.root, scooter.toString())
            } else {
                checkInputError(scooterName, scooterLocation, mainBinding)
            }
        }
    }
}