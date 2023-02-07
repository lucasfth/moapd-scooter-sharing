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

/**
 * MainActivity is a class that starts the functionality of the Scooter Sharing app.
 *
 * @author Mads Roager (mroa) and Lucas Hanson (luha)
 */

class MainActivity : AppCompatActivity() {

    private lateinit var mainBinding: ActivityMainBinding
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

        scooterName = mainBinding.editTextName
        scooterLocation = mainBinding.editTextLocation
        startRide = mainBinding.clickButtonStartRide

        startRide.setOnClickListener {
            if (scooterName.text.isNotEmpty() &&
                    scooterLocation.text.isNotEmpty()) {
                val name = scooterName.text.toString().trim()
                val location = scooterLocation.text.toString().trim()

                scooter.name = name
                scooter.location = location

                hideKeyboard()

                scooterName.setText("")
                scooterLocation.setText("")

                printMessage()
                showMessage()
            }
        }

        setContentView(mainBinding.root)
    }

    private fun hideKeyboard() {
        val inputMethodManager =
            getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager

        val view: View? = this.currentFocus

        inputMethodManager.hideSoftInputFromWindow(view?.windowToken, 0)
    }

    private fun showMessage() {
        Snackbar.make(mainBinding.root, scooter.toString(), Snackbar.LENGTH_SHORT).show()
    }

    /**
     * Prints Scooter that has been inputted into the app
     */
    private fun printMessage() {
        Log.d(TAG, scooter.toString())
    }
}