package dk.itu.moapd.scootersharing.mroa.models

data class Receipt(var name: String, var timestamp: Long, val price: Int, val maxSpeed: Int)
