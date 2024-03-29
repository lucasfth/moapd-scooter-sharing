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
import dk.itu.moapd.scootersharing.mroa.interfaces.ItemClickListener
import dk.itu.moapd.scootersharing.mroa.models.Receipt

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
            val query = MainActivity.database.child("receipts").child(it.uid)
            val options = FirebaseRecyclerOptions.Builder<Receipt>()
                .setQuery(query, Receipt::class.java)
                .setLifecycleOwner(this)
                .build()
            adapter = FirebaseAdapter(this, options)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
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

    override fun onItemClickListener(receipt: Receipt, position: Int) {
        MaterialAlertDialogBuilder(requireActivity())
            .setTitle("Deletion Confirmation Alertion!!")
            .setMessage("Do you really want to delete receipt ${receipt.name}???")
            .setNeutralButton("Cancelado") { _, _ -> }
            .setPositiveButton("Yesh please") { _, _ ->
                adapter.getRef(position).removeValue()
                scooterController.showSnackMessage(binding.root,
                    "Deleted ${receipt.name} with max speed ${receipt.maxSpeed}")
            }.show()
    }

    private fun startLoginActivity() {
        val intent = Intent(activity, LoginActivity::class.java)

        startActivity(intent)
        activity?.finish()
    }
}