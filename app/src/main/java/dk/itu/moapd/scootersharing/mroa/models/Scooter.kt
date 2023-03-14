package dk.itu.moapd.scootersharing.mroa.models

import java.util.*

/**
 * Scooter is a data class that represents a Scooter.
 */
data class Scooter (val name : String, var location : String, var timestamp: Long) {

    /**
     * Formats how the Scooter should be made into a string
     */
    override fun toString(): String {
        return "[Scooter] $name is placed at $location at timestamp ${Date(timestamp)}"
    }
}