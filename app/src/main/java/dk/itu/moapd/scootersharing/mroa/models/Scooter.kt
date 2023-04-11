package dk.itu.moapd.scootersharing.mroa.models

import com.google.firebase.database.IgnoreExtraProperties
import java.util.*

/**
 * Scooter is a data class that represents a Scooter.
 */
@IgnoreExtraProperties
data class Scooter (val name : String? = null, var location : String? = null, var timestamp: Long? = null) {

    /**
     * Formats how the Scooter should be made into a string
     */
    override fun toString(): String {
        return "[Scooter] $name is placed at $location at timestamp ${Date(timestamp!!)}"
    }
}