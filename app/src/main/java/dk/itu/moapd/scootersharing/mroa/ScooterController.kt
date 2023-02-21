package dk.itu.moapd.scootersharing.mroa

import android.content.Context
import android.content.Context.INPUT_METHOD_SERVICE
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.getSystemService
import androidx.viewbinding.ViewBinding
import com.google.android.material.snackbar.Snackbar

/**
 * Scooter controller
 *
 * Common methods that has to be used to interact
 * with a scooter and more.
 * Made to minimize code duplication.
 *
 * @constructor Create empty Scooter controller
 */
class ScooterController {


    /**
     * Check input error
     *
     * Made to find out what error has occurred with the
     * user intractable input fields.
     *
     * @param scooterName
     * @param scooterLocation
     * @param binding
     */
    fun checkInputError(scooterName: EditText, scooterLocation: EditText, binding: ViewBinding) {
        if (scooterName.text.isEmpty() && scooterLocation.text.isEmpty())
            showSnackMessage(binding.root,
                "Fields need to be filled out")
        else if (scooterName.text.isEmpty())
            showSnackMessage(binding.root,
                "Scooter name is needed")
        else
            showSnackMessage(binding.root,
                "Location is needed")
    }


    /**
     * Update scooter
     *
     * Updated scooter information
     *
     * @param scooter
     * @param scooterName
     * @param scooterLocation
     */
    fun updateScooter(scooter: Scooter, scooterName: EditText, scooterLocation: EditText) {
        scooter.name = scooterName.text.toString().trim()
        scooter.location = scooterLocation.text.toString().trim()
        scooter.timestamp = System.currentTimeMillis()
    }


    /**
     * Clear input
     *
     * Clear user accessible input fields.
     *
     * @param scooterName
     * @param scooterLocation
     */
    fun clearInput(scooterName: EditText, scooterLocation: EditText) {
        scooterName.setText("")
        scooterLocation.setText("")
    }


    /**
     * Hide keyboard
     *
     * @param view
     */
    fun Context.hideKeyboard(view: View) {
        val inputMethodManager =
            getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }

    /**
     * Show snack message
     *
     * Shows a snack-bar with the given information and scooter info.
     *
     * @param root
     * @param message
     */
    fun showSnackMessage(root : View, message : String) {
        Snackbar.make(root, message, Snackbar.LENGTH_SHORT).show()
    }

    /**
     * Print message
     *
     * Prints the given information and scooter info in the terminal.
     *
     * @param TAG
     * @param scooter
     */
    fun printMessage(TAG: String?, scooter: Scooter) {
        Log.d(TAG, scooter.toString())
    }
}