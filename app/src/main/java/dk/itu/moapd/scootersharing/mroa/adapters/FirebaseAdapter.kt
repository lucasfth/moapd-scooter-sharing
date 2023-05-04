package dk.itu.moapd.scootersharing.mroa.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import dk.itu.moapd.scootersharing.mroa.databinding.ListRidesBinding
import dk.itu.moapd.scootersharing.mroa.interfaces.ItemClickListener
import dk.itu.moapd.scootersharing.mroa.models.Receipt
import java.util.*

class FirebaseAdapter (private val itemClickListener: ItemClickListener, options: FirebaseRecyclerOptions<Receipt>) :
    FirebaseRecyclerAdapter<Receipt, FirebaseAdapter.ViewHolder>(options) {

        class ViewHolder(private val binding: ListRidesBinding) : RecyclerView.ViewHolder(binding.root) {
            fun bind (receipt: Receipt) {
                with (binding) {
                    receiptName.text = receipt.name
                    receiptPrice.text = receipt.price.toString()
                    receiptTimestamp.text = Date(receipt.timestamp!!).toString()
                    receiptMaxSpeed.text = receipt.maxSpeed.toString()
                }
            }
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ListRidesBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int, receipt: Receipt) {
        holder.apply {
            bind(receipt)
            itemView.setOnClickListener {
                itemClickListener.onItemClickListener(receipt, position)
                true
            }
        }
    }
}