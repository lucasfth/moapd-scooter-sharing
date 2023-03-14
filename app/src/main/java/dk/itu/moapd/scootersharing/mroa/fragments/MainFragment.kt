package dk.itu.moapd.scootersharing.mroa.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dk.itu.moapd.scootersharing.mroa.R
import dk.itu.moapd.scootersharing.mroa.RidesDB
import dk.itu.moapd.scootersharing.mroa.models.Scooter
import dk.itu.moapd.scootersharing.mroa.ScooterController
import dk.itu.moapd.scootersharing.mroa.activities.ListRidesActivity
import dk.itu.moapd.scootersharing.mroa.databinding.ListRidesBinding
import dk.itu.moapd.scootersharing.mroa.databinding.FragmentMainBinding

/**
 * Main fragment
 *
 * @constructor Create empty Main fragment
 */
class MainFragment : Fragment() {

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
        lateinit var ridesDB : RidesDB
        private lateinit var adapter: CustomAdapter
    }

    /**
     * On create
     *
     * @param savedInstanceState
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ridesDB = activity?.let { RidesDB.get(it) }!!
        scooterController = ScooterController()
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
        adapter = CustomAdapter(ridesDB.getRidesList())
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
        }
    }

    /**
     * On destroy view
     *
     */
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private inner class CustomAdapter(
        private val data: List<Scooter>) :
        RecyclerView.Adapter<ViewHolder>() {

        //ArrayAdapter<Scooter>(context, R.layout.list_rides, data) {
        override fun onCreateViewHolder(parent: ViewGroup,
                                        viewType: Int): ViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = ListRidesBinding.inflate(
                inflater, parent, false)
            return ViewHolder(binding)
        }
        override fun getItemCount() = data.size
        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val item = data[position]
            holder.bind(item)
            holder.itemView.setOnClickListener {
                MaterialAlertDialogBuilder(activity!!)
                    .setTitle("Deletion Confirmation Alertion!!")
                    .setMessage("Do you really want to delete ${item.name}???")
                    .setNeutralButton("Cancelado") { dialog, which -> }
                    .setPositiveButton("Yesh please") {dialog, which ->
                        ridesDB.deleteScooter(item.name)
                        adapter.notifyDataSetChanged()
                        scooterController.showSnackMessage(binding.root, "Deleted ${item.name} placed at ${item.location}")
                    }.show()
            }
        }
    }

    inner class ViewHolder(private val listBinding: ListRidesBinding) :
        RecyclerView.ViewHolder(listBinding.root) {
        fun bind(scooter: Scooter) {
            with (listBinding) {
                scooterName.text = scooter.name
                scooterLocation.text = scooter.location
                scooterTimestamp.text = scooter.timestamp.toString()
            }
        }
    }
}

