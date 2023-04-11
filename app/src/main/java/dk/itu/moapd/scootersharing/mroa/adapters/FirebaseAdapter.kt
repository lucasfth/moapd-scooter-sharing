package dk.itu.moapd.scootersharing.mroa.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import dk.itu.moapd.scootersharing.mroa.databinding.ListRidesBinding
import dk.itu.moapd.scootersharing.mroa.interfaces.ItemClickListener
import dk.itu.moapd.scootersharing.mroa.models.Scooter
import java.util.*

class FirebaseAdapter (private val itemClickListener: ItemClickListener, options: FirebaseRecyclerOptions<Scooter>) :
    FirebaseRecyclerAdapter<Scooter, FirebaseAdapter.ViewHolder>(options) {

        class ViewHolder(private val binding: ListRidesBinding) : RecyclerView.ViewHolder(binding.root) {
            fun bind (scooter: Scooter) {
                with (binding) {
                    scooterName.text = scooter.name
                    scooterLocation.text = scooter.location
                    scooterTimestamp.text = Date(scooter.timestamp!!).toString()
                }
            }
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ListRidesBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int, scooter: Scooter) {
        holder.apply {
            bind(scooter)
            itemView.setOnClickListener {
                itemClickListener.onItemClickListener(scooter, position)
                true
            }
        }
    }
}