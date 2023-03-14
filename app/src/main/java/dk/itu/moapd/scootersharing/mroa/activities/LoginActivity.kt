package dk.itu.moapd.scootersharing.mroa.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult
import com.google.android.material.snackbar.Snackbar
import dk.itu.moapd.scootersharing.mroa.R
import dk.itu.moapd.scootersharing.mroa.ScooterController

class LoginActivity : AppCompatActivity() {

    private val signInLauncher =
        registerForActivityResult(
            FirebaseAuthUIActivityResultContract()
        ) { result ->
            onSignInResult(result)
        }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        createSignInIntent()
    }

    private fun onSignInResult(result: FirebaseAuthUIAuthenticationResult?) {
        if (result != null) {
            if (result.resultCode == RESULT_OK) {
                Snackbar.make(findViewById(android.R.id.content), "Weyy user got inside the app 🤤", Snackbar.LENGTH_SHORT).show()
                startMainActivity()
            } else {
                Snackbar.make(findViewById(android.R.id.content), "Errr auth failed gruelly 🫥", Snackbar.LENGTH_SHORT).show()
            }
        }
    }

    private fun startMainActivity() {
        val intent = Intent(this,
            MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun createSignInIntent() {
        // Choose authentication providers
        val providers = arrayListOf(
            AuthUI.IdpConfig.EmailBuilder().build(),
            // AuthUI.IdpConfig.PhoneBuilder().build(),
            AuthUI.IdpConfig.GoogleBuilder().build(),
        )

        // Create and launch sign-in intent
        val signInIntent = AuthUI.getInstance()
            .createSignInIntentBuilder()
            .setAvailableProviders(providers)
            .setLogo(R.drawable.baseline_emergency_share_24)
            .setTheme(R.style.Theme_FirebaseAuthentication)
            .build()
        signInLauncher.launch(signInIntent)
    }
}