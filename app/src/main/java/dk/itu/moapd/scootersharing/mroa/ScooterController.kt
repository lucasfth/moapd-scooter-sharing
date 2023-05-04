package dk.itu.moapd.scootersharing.mroa

import android.content.Context
import android.content.Context.INPUT_METHOD_SERVICE
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.viewbinding.ViewBinding
import com.google.android.material.snackbar.Snackbar
import dk.itu.moapd.scootersharing.mroa.models.Scooter

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
    fun checkInputError(scooterName: EditText, binding: ViewBinding) {
        if (scooterName.text.isEmpty())
            showSnackMessage(binding.root,
                "Fields need to be filled out")
        else if (scooterName.text.isEmpty())
            showSnackMessage(binding.root,
                "Scooter name is needed")
        else
            showSnackMessage(binding.root,
                "Location is needed")
    }


    fun createScooter(scooterName: EditText): Scooter {
        return Scooter(
            scooterName.text.toString().trim(),
            System.currentTimeMillis(),
            PrefSingleton.getLat(),
            PrefSingleton.getLng()
        )
    }

    fun createScooter(scooterName: String): Scooter {
        return Scooter(
            scooterName,
            System.currentTimeMillis(),
            PrefSingleton.getLat(),
            PrefSingleton.getLng()
        )
    }


    /**
     * Clear input
     *
     * Clear user accessible input fields.
     *
     * @param scooterName
     * @param scooterLocation
     */
    fun clearInput(scooterName: EditText) {
        scooterName.setText("")
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