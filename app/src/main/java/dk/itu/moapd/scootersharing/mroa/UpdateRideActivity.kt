package dk.itu.moapd.scootersharing.mroa

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.core.view.WindowCompat
import dk.itu.moapd.scootersharing.mroa.databinding.ActivityUpdateRideBinding
import dk.itu.moapd.scootersharing.mroa.databinding.InputBoxBinding

class UpdateRideActivity : AppCompatActivity() {
    private lateinit var mainBinding: ActivityUpdateRideBinding
    private lateinit var loginInputBoxBinding: InputBoxBinding
    companion object {
        private val TAG = UpdateRideActivity::class.qualifiedName
        lateinit var ridesDB : RidesDB
    }

    private lateinit var scooterName: EditText
    private lateinit var scooterLocation: EditText
    private lateinit var controller: ScooterController

    private lateinit var scooter: Scooter

    /**
     * Called upon app start-up.
     * Instantiates needed classes.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        super.onCreate(savedInstanceState)
        // Singleton to share an object between the app activities .
        ridesDB = RidesDB.get (this)
        setContentView(R.layout.activity_main)

        mainBinding = ActivityUpdateRideBinding.inflate(layoutInflater)
        loginInputBoxBinding = InputBoxBinding.bind(mainBinding.root)

        scooterName = loginInputBoxBinding.editTextName
        scooterLocation = loginInputBoxBinding.editTextLocation

        controller = ScooterController()

        scooterName.isEnabled = false

        with (mainBinding) {
            clickButtonUpdateRide.setOnClickListener { checkInputValidity() }
        }

        setContentView(mainBinding.root)
    }

    /**
     * Checks if user given info is valid
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