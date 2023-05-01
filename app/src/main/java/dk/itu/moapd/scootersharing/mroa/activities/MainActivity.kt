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

import PrefSingleton
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import dk.itu.moapd.scootersharing.mroa.R
import dk.itu.moapd.scootersharing.mroa.databinding.ActivityMainBinding
import com.google.android.gms.maps.SupportMapFragment

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

    private var BUCKET_URL = "gs://moapd-scooter-sharing.appspot.com"


    companion object {
        private val TAG = MainActivity::class.qualifiedName
        lateinit var auth: FirebaseAuth
        lateinit var database: DatabaseReference
        lateinit var storage: StorageReference
        lateinit var prefSingleton: PrefSingleton
    }


    /**
     * On create
     *
     * @param savedInstanceState
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()
        database = Firebase.database(DATABASE_URL).reference
        storage = Firebase.storage(BUCKET_URL).reference
        mainBinding = ActivityMainBinding.inflate(layoutInflater)

        prefSingleton = PrefSingleton.getInstance(this)

        setContentView(mainBinding.root)

        if (auth.currentUser == null)
            startLoginActivity()
    }

    override fun onStart() {
        super.onStart()

        with (mainBinding) {

        }
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