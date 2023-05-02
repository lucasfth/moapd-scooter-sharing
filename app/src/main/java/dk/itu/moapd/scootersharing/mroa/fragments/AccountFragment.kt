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
import dk.itu.moapd.scootersharing.mroa.R
import dk.itu.moapd.scootersharing.mroa.ScooterController
import dk.itu.moapd.scootersharing.mroa.activities.LoginActivity
import dk.itu.moapd.scootersharing.mroa.activities.MainActivity
import dk.itu.moapd.scootersharing.mroa.adapters.FirebaseAdapter
import dk.itu.moapd.scootersharing.mroa.databinding.FragmentAccountBinding
import dk.itu.moapd.scootersharing.mroa.databinding.FragmentMainBinding
import dk.itu.moapd.scootersharing.mroa.interfaces.ItemClickListener
import dk.itu.moapd.scootersharing.mroa.models.Scooter


/**
 * A simple [Fragment] subclass.
 * Use the [AccountFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AccountFragment : Fragment(), ItemClickListener {
    /**
     * _binding
     */
    private var _binding: FragmentAccountBinding? = null

    /**
     * Binding
     */
    private val binding
        get() = checkNotNull(_binding) {
            "Is the view visible?"
        }

    private lateinit var scooterController: ScooterController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        scooterController = ScooterController()
        MainActivity.auth.currentUser?.let {
            val query = MainActivity.database.child("scooters")
            val options = FirebaseRecyclerOptions.Builder<Scooter>()
                .setQuery(query, Scooter::class.java)
                .setLifecycleOwner(this)
                .build()
            adapter = FirebaseAdapter(this, options)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentAccountBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val navHostFragment = activity?.supportFragmentManager
            ?.findFragmentById(R.id.fragment_container_view) as NavHostFragment
        val navController = navHostFragment.navController

        with(binding) {
            mainList.layoutManager = LinearLayoutManager(activity)
            mainList.adapter = adapter

            clickButtonSignOut.setOnClickListener{
                MainActivity.auth.signOut()
                startLoginActivity()
            }

            clickButtonAdmin.setOnClickListener{
                navController.navigate(R.id.show_admin_page)
            }
        }
    }

    companion object {
        private lateinit var adapter: FirebaseAdapter
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

    /**
     * todo
     */
    private fun startLoginActivity() {
        val intent = Intent(activity, LoginActivity::class.java)

        startActivity(intent)
        activity?.finish()
    }
}