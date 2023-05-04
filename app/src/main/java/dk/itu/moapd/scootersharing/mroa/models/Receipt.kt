package dk.itu.moapd.scootersharing.mroa.models

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class Receipt(var name: String? = null, var timestamp: Long? = null, val price: Int? = null, val maxSpeed: Float? = null)
