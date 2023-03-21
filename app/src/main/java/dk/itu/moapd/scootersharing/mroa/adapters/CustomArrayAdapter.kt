package dk.itu.moapd.scootersharing.mroa.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import dk.itu.moapd.scootersharing.mroa.R
import dk.itu.moapd.scootersharing.mroa.models.Scooter

/**
 * Custom array adapter
 *
 * @property resource
 * @constructor
 *
 * @param context
 * @param data
 */
class CustomArrayAdapter(context: Context, private var resource: Int,
                         data: List<Scooter>) :
    ArrayAdapter<Scooter>(context, R.layout.list_rides, data) {
    /**
     * View holder
     *
     * @constructor
     *
     * @param view
     */
    private class ViewHolder(view: View) {
        /**
         * Name
         */
        val name: TextView = view.findViewById(R.id.scooter_name)

        /**
         * Location
         */
        val location: TextView = view.findViewById(R.id.scooter_location)

        /**
         * Timestamp
         */
        val timestamp: TextView = view.findViewById(R.id.scooter_timestamp)
    }

    /**
     * Get view
     *
     * @param position
     * @param convertView
     * @param parent
     * @return
     */
    override fun getView(position: Int, convertView: View?,
                         parent: ViewGroup): View {
        var view = convertView
        val viewHolder: ViewHolder
        if (view == null) {
            val inflater = LayoutInflater.from(context)
            view = inflater.inflate(resource, parent, false)
            viewHolder = ViewHolder(view)
        } else
            viewHolder = view.tag as ViewHolder
        val scooter = getItem(position)
        viewHolder.name.text = scooter?.name
        viewHolder.location.text = scooter?.location
        viewHolder.timestamp.text = scooter?.timestamp.toString()
        view?.tag = viewHolder
        return view!!
    }
}