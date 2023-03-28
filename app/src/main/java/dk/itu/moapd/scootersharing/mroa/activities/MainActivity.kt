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

package dk.itu.moapd.scootersharing.mroa.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import dk.itu.moapd.scootersharing.mroa.databinding.ActivityMainBinding

/**
 * Main activity
 *
 * @constructor Create empty Main activity
 */

class MainActivity : AppCompatActivity() {

    /**
     * Main binding
     */
    private lateinit var mainBinding: ActivityMainBinding

    private var DATABASE_URL = "https://moapd-scooter-sharing-default-rtdb.europe-west1.firebasedatabase.app"


    companion object {
        private val TAG = MainActivity::class.qualifiedName
        lateinit var auth: FirebaseAuth
        lateinit var database: DatabaseReference
    }


    /**
     * On create
     *
     * @param savedInstanceState
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //WindowCompat.setDecorFitsSystemWindows(window, false)
        // Singleton to share an object between the app activities .
        auth = FirebaseAuth.getInstance()
        database = Firebase.database(DATABASE_URL).reference
        mainBinding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(mainBinding.root)
    }

    override fun onStart() {
        super.onStart()
        if (auth.currentUser == null)
            startLoginActivity()
    }

    /**
     * todo
     */
    private fun startLoginActivity() {
        val intent = Intent(this, LoginActivity::class.java)

        startActivity(intent)
        finish()
    }
}