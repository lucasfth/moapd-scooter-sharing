package dk.itu.moapd.scootersharing.mroa.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import dk.itu.moapd.scootersharing.mroa.R
import dk.itu.moapd.scootersharing.mroa.models.Scooter
import dk.itu.moapd.scootersharing.mroa.ScooterController
import dk.itu.moapd.scootersharing.mroa.activities.ListRidesActivity
import dk.itu.moapd.scootersharing.mroa.activities.LoginActivity
import dk.itu.moapd.scootersharing.mroa.adapters.FirebaseAdapter
import dk.itu.moapd.scootersharing.mroa.databinding.FragmentMainBinding
import dk.itu.moapd.scootersharing.mroa.interfaces.ItemClickListener

/**
 * Main fragment
 *
 * @constructor Create empty Main fragment
 */
class MainFragment : Fragment(), ItemClickListener {

    /**
     * todo
     */
    private lateinit var auth: FirebaseAuth

    private var DATABASE_URL = "https://console.firebase.google.com/u/0/project/moapd-scooter-sharing/database/moapd-scooter-sharing-default-rtdb/data/~2F"

    private lateinit var database: DatabaseReference

    private lateinit var scooterController: ScooterController

    /**
     * _binding
     */
    private var _binding: FragmentMainBinding? = null

    /**
     * Binding
     */
    private val binding
        get() = checkNotNull(_binding) {
            "Is the view visible?"
        }

    companion object {
        private val TAG = MainFragment::class.qualifiedName
        private lateinit var adapter: FirebaseAdapter
    }

    /**
     * On create
     *
     * @param savedInstanceState
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        scooterController = ScooterController()
        auth = FirebaseAuth.getInstance()
        database = Firebase.database(DATABASE_URL).reference
        auth.currentUser?.let {
            val query = database.child("scooters")
                .child(it.uid)
                .orderByChild("timestamp")
            val options = FirebaseRecyclerOptions.Builder<Scooter>()
                .setQuery(query, Scooter::class.java)
                .setLifecycleOwner(this)
                .build()
            adapter = FirebaseAdapter(this, options)
        }
    }

    /**
     * On create view
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    /**
     * On view created
     *
     * @param view
     * @param savedInstanceState
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val navHostFragment = activity?.supportFragmentManager
            ?.findFragmentById(R.id.fragment_container_view) as NavHostFragment
        val navController = navHostFragment.navController
        with (binding) {
            mainList.layoutManager = LinearLayoutManager(activity)
            mainList.adapter = adapter

            clickButtonStartRide.setOnClickListener {
                navController.navigate(R.id.show_start_ride)
            }

            clickButtonUpdateRide.setOnClickListener{
                navController.navigate(R.id.show_update_ride)
            }

            clickButtonListRides.setOnClickListener{
                val intent = Intent(activity, ListRidesActivity::class.java)
                startActivity(intent)
            }

            clickButtonSignOut.setOnClickListener{
                auth.signOut()
                startLoginActivity()
            }
        }
    }

    /**
     * todo
     */
    override fun onStart() {
        super.onStart()
        if (auth.currentUser == null)
            startLoginActivity()
        val user = auth.currentUser
        // mainBinding.description
    }

    /**
     * todo
     */
    private fun startLoginActivity() {
        val intent = Intent(activity, LoginActivity::class.java)

        startActivity(intent)
        activity?.finish()
    }

    /**
     * On destroy view
     *
     */
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onItemClickListener(scooter: Scooter, position: Int) {
        MaterialAlertDialogBuilder(requireActivity())
            .setTitle("Deletion Confirmation Alertion!!")
            .setMessage("Do you really want to delete ${scooter.name}???")
            .setNeutralButton("Cancelado") { dialog, which -> }
            .setPositiveButton("Yesh please") {dialog, which ->
                adapter.getRef(position).removeValue()
                scooterController.showSnackMessage(binding.root,
                    "Deleted ${scooter.name} placed at ${scooter.location}")
            }.show()
    }
}

