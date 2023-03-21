package dk.itu.moapd.scootersharing.mroa.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import dk.itu.moapd.scootersharing.mroa.R
import dk.itu.moapd.scootersharing.mroa.RidesDB
import dk.itu.moapd.scootersharing.mroa.adapters.CustomArrayAdapter
import dk.itu.moapd.scootersharing.mroa.databinding.ActivityListRidesBinding


/**
 * List rides activity
 *
 * @constructor Create empty List rides activity
 */
class ListRidesActivity : AppCompatActivity() {
    /**
     * Main binding
     */
    private lateinit var mainBinding: ActivityListRidesBinding
    companion object {
        private val TAG = ListRidesActivity::class.qualifiedName
        lateinit var ridesDB : RidesDB
        private lateinit var adapter: CustomArrayAdapter
    }

    /**
     * On create
     *
     * @param savedInstanceState
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        //WindowCompat.setDecorFitsSystemWindows(window, false)
        super.onCreate(savedInstanceState)
        // Singleton to share an object between the app activities .
        ridesDB = RidesDB.get (this)
        setContentView(R.layout.activity_main)
        var data = ridesDB.getRidesList()
        adapter = CustomArrayAdapter(this, R.layout.list_rides, data)
        mainBinding = ActivityListRidesBinding.inflate(layoutInflater)
        mainBinding.listView.adapter = adapter
        setContentView(mainBinding.root)
    }
}