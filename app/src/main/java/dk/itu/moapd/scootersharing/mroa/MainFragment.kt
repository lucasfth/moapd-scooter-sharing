package dk.itu.moapd.scootersharing.mroa

import android.content.Intent
import android.location.Location
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.WindowCompat
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dk.itu.moapd.scootersharing.mroa.databinding.ListRidesBinding
import dk.itu.moapd.scootersharing.mroa.databinding.ActivityMainBinding
import dk.itu.moapd.scootersharing.mroa.databinding.FragmentMainBinding

/**
 * Main fragment
 *
 * @constructor Create empty Main fragment
 */
class MainFragment : Fragment() {

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
                val intent = Intent(activity, dk.itu.moapd.scootersharing.mroa.ListRidesActivity::class.java)
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
        override fun onBindViewHolder(holder: ViewHolder,
                                      position: Int) {
            val dummy = data[position]
            holder.bind(dummy)
        }
    }

    inner class ViewHolder(private val binding: ListRidesBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(scooter: Scooter) {
            with (binding) {
                scooterName.text = root.context.getString(
                    R.string.scooter_name)
                scooterLocation.text = root.context.getString(
                    R.string.scooter_location)
                scooterTimestamp.text = root.context.getString(
                    R.string.scooter_timestamp)
            }
        }
    }
}

