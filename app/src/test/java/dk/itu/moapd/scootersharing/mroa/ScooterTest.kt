package dk.itu.moapd.scootersharing.mroa

import dk.itu.moapd.scootersharing.mroa.models.Scooter
import org.junit.Test

import org.junit.Assert.*
import java.util.*

internal class ScooterTest {

    @Test
    fun testToString() {
        val scooter = Scooter("ScooterOfFun", 1683232076469, 55.660000, 12.590923)
        val expected = "[Scooter] ScooterOfFun is placed at 55.66, 12.590923 at timestamp ${Date(1683232076469)}"

        val actual = scooter.toString()
        assertEquals(expected, actual)
    }
}