package dk.itu.moapd.scootersharing.mroa.interfaces

import dk.itu.moapd.scootersharing.mroa.models.Scooter

interface ItemClickListener {
    fun onItemClickListener (scooter: Scooter, position: Int)
}