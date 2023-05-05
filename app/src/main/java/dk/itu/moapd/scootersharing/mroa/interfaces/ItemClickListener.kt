package dk.itu.moapd.scootersharing.mroa.interfaces

import dk.itu.moapd.scootersharing.mroa.models.Receipt

interface ItemClickListener {
    fun onItemClickListener (scooter: Receipt, position: Int)
}