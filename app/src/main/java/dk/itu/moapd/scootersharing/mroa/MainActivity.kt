/*
 * MIT License
 * Copyright (c) 2023 Lucas Hanson
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package dk.itu.moapd.scootersharing.mroa

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import androidx.core.view.WindowCompat
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import dk.itu.moapd.scootersharing.mroa.databinding.ActivityMainBinding
import dk.itu.moapd.scootersharing.mroa.databinding.InputBoxBinding

/**
 * MainActivity is a class that starts the functionality of the Scooter Sharing app.
 *
 * @author Mads Roager (mroa) and Lucas Hanson (luha)
 */

class MainActivity : AppCompatActivity() {

    private lateinit var mainBinding: ActivityMainBinding
    private lateinit var loginInputBoxBinding: InputBoxBinding
    companion object {
        private val TAG = MainActivity::class.qualifiedName
    }

    private lateinit var scooterName: EditText
    private lateinit var scooterLocation: EditText
    private lateinit var startRide: Button

    private val scooter: Scooter = Scooter("", "")

    /**
     * Called upon app start-up.
     * Instantiates needed classes.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mainBinding = ActivityMainBinding.inflate(layoutInflater)
        loginInputBoxBinding = InputBoxBinding.bind(mainBinding.root)

        scooterName = loginInputBoxBinding.editTextName
        scooterLocation = loginInputBoxBinding.editTextLocation
        startRide = mainBinding.clickButtonStartRide

        startRide.setOnClickListener { checkInputValidity() }
        setContentView(mainBinding.root)
    }

    private fun checkInputValidity() {
        hideKeyboard()
        if (scooterName.text.isNotEmpty() &&
            scooterLocation.text.isNotEmpty()) {
            val name = scooterName.text.toString().trim()
            val location = scooterLocation.text.toString().trim()

            updateScooter(name, location)

            clearInput()

            printMessage()
            showMessage(mainBinding.root, scooter.toString())
        } else {
            checkInputError()
        }
    }

    private fun checkInputError() {
        if (scooterName.text.isEmpty() && scooterLocation.text.isEmpty())
            showMessage(mainBinding.root, "Fields need to be filled out")
        else if (scooterName.text.isEmpty())
            showMessage(mainBinding.root, "Scooter name is needed")
        else
            showMessage(mainBinding.root, "Location is needed")
    }

    private fun updateScooter(name : String, location : String) {
        scooter.name = name
        scooter.location = location
    }

    private fun clearInput() {
        scooterName.setText("")
        scooterLocation.setText("")
    }

    private fun hideKeyboard() {
        val inputMethodManager =
            getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager

        val view: View? = this.currentFocus

        inputMethodManager.hideSoftInputFromWindow(view?.windowToken, 0)
    }

    private fun showMessage(root : View, message : String) {
        Snackbar.make(root, message, Snackbar.LENGTH_SHORT).show()
    }
    /**
     * Prints Scooter that has been inputted into the app
     */
    private fun printMessage() {
        Log.d(TAG, scooter.toString())
    }
}