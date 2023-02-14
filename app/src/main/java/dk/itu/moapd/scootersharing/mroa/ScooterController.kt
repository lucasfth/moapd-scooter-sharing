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

class ScooterController {


    /**
     * Checks what is wrong with input
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
     * Updates scooter information
     */
    fun updateScooter(scooter: Scooter, scooterName: EditText, scooterLocation: EditText) {
        scooter.name = scooterName.text.toString().trim()
        scooter.location = scooterLocation.text.toString().trim()
        scooter.timestamp = System.currentTimeMillis()
    }

    /**
     * Clears input text
     */
    fun clearInput(scooterName: EditText, scooterLocation: EditText) {
        scooterName.setText("")
        scooterLocation.setText("")
    }

    /**
     * Hides user keyboard
     */
    fun Context.hideKeyboard(view: View) {
        val inputMethodManager =
            getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }

    /**
     * Shows snackbar to user with given info
     */
    fun showSnackMessage(root : View, message : String) {
        Snackbar.make(root, message, Snackbar.LENGTH_SHORT).show()
    }
    /**
     * Prints Scooter that has been inputted into the app
     */
    fun printMessage(TAG: String?, scooter: Scooter) {
        Log.d(TAG, scooter.toString())
    }
}