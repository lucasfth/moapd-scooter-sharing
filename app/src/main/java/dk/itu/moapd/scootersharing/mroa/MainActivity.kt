package dk.itu.moapd.scootersharing.mroa

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.core.view.WindowCompat

class MainActivity : AppCompatActivity() {

    companion object {
        private val TAG = MainActivity::class.qualifiedName
    }

    private lateinit var scooterName: EditText
    private lateinit var scooterLocation: EditText
    private lateinit var startRide: Button

    private val scooter: Scooter = Scooter("", "")
    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        scooterName = findViewById(R.id.edit_text_name)
        scooterLocation = findViewById(R.id.edit_text_location)

        startRide = findViewById(R.id.click_button_start_ride)
        startRide.setOnClickListener {
            if (scooterName.text.isNotEmpty() &&
                    scooterLocation.text.isNotEmpty()) {
                val name = scooterName.text.toString().trim()
                val location = scooterLocation.text.toString().trim()

                scooter.setName(name)
                scooter.setLocation(location)

                scooterName.setText("")
                scooterLocation.setText("")

                showMessage()
            }
        }
    }

    private fun showMessage() {
        Log.d(TAG, scooter.toString())
    }
}