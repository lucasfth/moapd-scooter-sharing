package dk.itu.moapd.scootersharing.mroa

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.core.view.WindowCompat
import com.google.android.material.snackbar.Snackbar
import dk.itu.moapd.scootersharing.mroa.databinding.ActivityMainBinding
import dk.itu.moapd.scootersharing.mroa.databinding.ActivityStartRideBinding
import dk.itu.moapd.scootersharing.mroa.databinding.InputBoxBinding

class StartRideActivity : AppCompatActivity() {
    private lateinit var mainBinding: ActivityStartRideBinding
    private lateinit var loginInputBoxBinding: InputBoxBinding
    private lateinit var controller: ScooterController
    companion object {
        private val TAG = StartRideActivity::class.qualifiedName
    }

    private lateinit var scooterName: EditText
    private lateinit var scooterLocation: EditText

    private val scooter: Scooter = Scooter("", "", 0)

    /**
     * Called upon app start-up.
     * Instantiates needed classes.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        super.onCreate(savedInstanceState)
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

                updateScooter(scooter, scooterName, scooterLocation)

                clearInput(scooterName, scooterLocation)

                printMessage(TAG, scooter)
                showSnackMessage(mainBinding.root, scooter.toString())
            } else {
                checkInputError(scooterName, scooterLocation, mainBinding)
            }
        }
    }
}